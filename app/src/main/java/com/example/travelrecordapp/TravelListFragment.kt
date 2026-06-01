package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class TravelListFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    private lateinit var btnAddTravel: Button
    private lateinit var layoutTravelItem: View
    private lateinit var emptyListCard: View
    private lateinit var tvSectionTitle: TextView

    private lateinit var tvListTotalCount: TextView
    private lateinit var tvRecentDate: TextView

    private lateinit var tvItemPlace: TextView
    private lateinit var tvItemDate: TextView
    private lateinit var tvItemMemo: TextView
    private lateinit var tvItemAction: TextView
    private lateinit var tvPhotoLabel: TextView

    private var currentRecord: TravelRecord? = null
    private val sampleRecord = TravelRecord.sample()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        btnAddTravel = view.findViewById(R.id.btnAddTravel)
        layoutTravelItem = view.findViewById(R.id.layoutTravelItem)
        emptyListCard = view.findViewById(R.id.emptyListCard)
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle)

        tvListTotalCount = view.findViewById(R.id.tvListTotalCount)
        tvRecentDate = view.findViewById(R.id.tvRecentDate)

        tvItemPlace = view.findViewById(R.id.tvItemPlace)
        tvItemDate = view.findViewById(R.id.tvItemDate)
        tvItemMemo = view.findViewById(R.id.tvItemMemo)
        tvItemAction = view.findViewById(R.id.tvItemAction)
        tvPhotoLabel = view.findViewById(R.id.tvPhotoLabel)

        btnAddTravel.setOnClickListener {
            insertSampleTravelRecord()
        }

        layoutTravelItem.setOnClickListener {
            val record = currentRecord ?: sampleRecord

            Toast.makeText(
                requireContext(),
                "${record.place} / ${record.visitDate}",
                Toast.LENGTH_SHORT
            ).show()
        }

        loadTravelRecords()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized) {
            loadTravelRecords()
        }
    }

    private fun insertSampleTravelRecord() {
        val nextNumber = dbHelper.getTravelRecordCount() + 1

        val record = TravelRecord(
            place = "부산 해운대 $nextNumber",
            visitDate = "2026-06-01",
            memo = "SQLite 데이터베이스에 저장된 여행 기록 예시입니다.",
            photoUri = "",
            latitude = 35.1587,
            longitude = 129.1603
        )

        val result = dbHelper.insertTravelRecord(record)

        if (result != -1L) {
            Toast.makeText(
                requireContext(),
                "여행 기록이 저장되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            loadTravelRecords()
        } else {
            Toast.makeText(
                requireContext(),
                "여행 기록 저장에 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadTravelRecords() {
        val records = dbHelper.getAllTravelRecords()
        val count = records.size

        tvListTotalCount.text = "${count}개"

        if (records.isNotEmpty()) {
            val recentRecord = records[0]
            currentRecord = recentRecord

            tvRecentDate.text = recentRecord.visitDate
            emptyListCard.visibility = View.GONE
            tvSectionTitle.text = "최근 저장된 여행 기록"

            bindTravelRecord(recentRecord, false)
        } else {
            currentRecord = null

            tvRecentDate.text = "준비중"
            emptyListCard.visibility = View.VISIBLE
            tvSectionTitle.text = "RecyclerView 아이템 미리보기"

            bindTravelRecord(sampleRecord, true)
        }
    }

    private fun bindTravelRecord(record: TravelRecord, isSample: Boolean) {
        tvItemPlace.text = record.place
        tvItemDate.text = record.visitDate
        tvItemMemo.text = record.memo
        tvPhotoLabel.text = "PHOTO"

        tvItemAction.text = if (isSample) {
            "상세 보기 준비중"
        } else {
            "저장된 기록입니다"
        }
    }
}