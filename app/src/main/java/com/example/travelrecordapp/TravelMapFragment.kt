package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TravelMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var tvMapStatus: TextView
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvMapStatus = view.findViewById(R.id.tvMapStatus)

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

        map.addMarker(
            MarkerOptions()
                .position(korea)
                .title("대한민국")
                .snippet("여행 기록 위치를 표시할 기본 지도입니다.")
        )

        tvMapStatus.text =
            "Google Map API 연결이 완료되었습니다.\n" +
                    "내일은 저장된 여행 기록의 위치 정보를 지도 마커로 표시할 예정입니다."
    }
}