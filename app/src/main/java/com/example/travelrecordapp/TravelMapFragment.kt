package com.example.travelrecordapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TravelMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var dbHelper: DBHelper
    private lateinit var tvMapStatus: TextView
    private lateinit var progressMapLoading: ProgressBar

    private var googleMap: GoogleMap? = null
    private var focusRecordNo: Int = -1

    companion object {
        private const val ARG_FOCUS_RECORD_NO = "focus_record_no"

        fun newInstance(recordNo: Int): TravelMapFragment {
            val fragment = TravelMapFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_FOCUS_RECORD_NO, recordNo)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())
        focusRecordNo = arguments?.getInt(ARG_FOCUS_RECORD_NO, -1) ?: -1

        tvMapStatus = view.findViewById(R.id.tvMapStatus)
        progressMapLoading = view.findViewById(R.id.progressMapLoading)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.googleMapFragment) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val korea = LatLng(36.5, 127.8)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 6.5f))

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        map.setOnMapLongClickListener { latLng ->
            showLocationSaveMenu(latLng)
        }

        loadMarkersWithThread()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized && googleMap != null) {
            loadMarkersWithThread()
        }
    }

    private fun loadMarkersWithThread() {
        progressMapLoading.visibility = View.VISIBLE
        tvMapStatus.text = "저장된 여행 기록 위치를 불러오는 중입니다."

        Thread {
            val records = dbHelper.getAllTravelRecords()
            val locationRecords = records.filter { it.hasLocation() }

            activity?.runOnUiThread {
                if (!isAdded) {
                    return@runOnUiThread
                }

                val map = googleMap ?: return@runOnUiThread

                map.clear()

                if (locationRecords.isNotEmpty()) {
                    for (record in locationRecords) {
                        val position = LatLng(record.latitude, record.longitude)

                        map.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(record.place)
                                .snippet(record.visitDate)
                        )
                    }

                    val focusRecord = locationRecords.find { it.no == focusRecordNo }
                    val targetRecord = focusRecord ?: locationRecords[0]
                    val targetPosition = LatLng(targetRecord.latitude, targetRecord.longitude)

                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(targetPosition, 13f)
                    )

                    if (focusRecord != null) {
                        tvMapStatus.text =
                            "${focusRecord.place} 위치를 지도에서 표시했습니다.\n" +
                                    "위도: ${String.format("%.5f", focusRecord.latitude)}\n" +
                                    "경도: ${String.format("%.5f", focusRecord.longitude)}"
                    } else {
                        tvMapStatus.text =
                            "총 ${records.size}개의 여행 기록 중 " +
                                    "${locationRecords.size}개의 위치 정보가 지도에 표시되었습니다.\n" +
                                    "위치 저장은 지도를 길게 눌러 진행합니다."
                    }
                } else {
                    val korea = LatLng(36.5, 127.8)

                    map.addMarker(
                        MarkerOptions()
                            .position(korea)
                            .title("대한민국")
                            .snippet("아직 위치가 저장된 여행 기록이 없습니다.")
                    )

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 6.5f))

                    tvMapStatus.text =
                        "Google Map API 연결이 완료되었습니다.\n" +
                                "아직 위치 정보가 저장된 여행 기록은 없습니다.\n" +
                                "지도를 길게 눌러 여행 위치를 저장해보세요."
                }

                progressMapLoading.visibility = View.GONE
            }
        }.start()
    }

    private fun showLocationSaveMenu(latLng: LatLng) {
        val records = dbHelper.getAllTravelRecords()

        val menuList = mutableListOf<String>()
        menuList.add("새 여행 기록으로 저장")

        for (record in records) {
            menuList.add("${record.place} (${record.visitDate})")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("여행 위치 저장")
            .setItems(menuList.toTypedArray()) { _, which ->
                if (which == 0) {
                    showCreateRecordDialog(latLng)
                } else {
                    val selectedRecord = records[which - 1]
                    showSaveLocationConfirmDialog(selectedRecord, latLng)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showCreateRecordDialog(latLng: LatLng) {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 20, 40, 0)

        val etPlace = EditText(requireContext())
        etPlace.hint = "여행지명 입력"
        etPlace.inputType = android.text.InputType.TYPE_CLASS_TEXT

        val etMemo = EditText(requireContext())
        etMemo.hint = "메모 입력"
        etMemo.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
        etMemo.minLines = 2

        layout.addView(etPlace)
        layout.addView(etMemo)

        AlertDialog.Builder(requireContext())
            .setTitle("새 여행 기록 저장")
            .setMessage("선택한 지도 위치로 새 여행 기록을 저장합니다.")
            .setView(layout)
            .setPositiveButton("저장") { _, _ ->
                val place = etPlace.text.toString().trim()
                val memo = etMemo.text.toString().trim()

                if (place.isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "여행지명을 입력해야 저장할 수 있습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    createNewRecordWithLocation(place, memo, latLng)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun createNewRecordWithLocation(place: String, memo: String, latLng: LatLng) {
        val currentDateTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.KOREA
        ).format(Date())

        val record = TravelRecord(
            place = place,
            visitDate = currentDateTime,
            memo = memo,
            photoUri = "",
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )

        val result = dbHelper.insertTravelRecord(record)

        if (result != -1L) {
            Toast.makeText(
                requireContext(),
                "새 여행 기록이 저장되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            loadMarkersWithThread()
        } else {
            Toast.makeText(
                requireContext(),
                "새 여행 기록 저장에 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSaveLocationConfirmDialog(record: TravelRecord, latLng: LatLng) {
        val latitudeText = String.format("%.5f", latLng.latitude)
        val longitudeText = String.format("%.5f", latLng.longitude)

        AlertDialog.Builder(requireContext())
            .setTitle("${record.place} 위치 저장")
            .setMessage(
                "선택한 위치를 이 여행 기록에 저장할까요?\n\n" +
                        "위도: $latitudeText\n" +
                        "경도: $longitudeText"
            )
            .setPositiveButton("저장") { _, _ ->
                saveRecordLocation(record, latLng)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun saveRecordLocation(record: TravelRecord, latLng: LatLng) {
        val updatedRecord = TravelRecord(
            no = record.no,
            place = record.place,
            visitDate = record.visitDate,
            memo = record.memo,
            photoUri = record.photoUri,
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )

        val result = dbHelper.updateTravelRecord(updatedRecord)

        if (result > 0) {
            Toast.makeText(
                requireContext(),
                "여행 위치가 저장되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            loadMarkersWithThread()
        } else {
            Toast.makeText(
                requireContext(),
                "여행 위치 저장에 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}