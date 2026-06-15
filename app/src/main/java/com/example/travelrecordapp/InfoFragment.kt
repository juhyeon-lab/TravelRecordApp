package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    private lateinit var tvStatTotalCount: TextView
    private lateinit var tvStatPhotoCount: TextView
    private lateinit var tvStatLocationCount: TextView
    private lateinit var tvStatRecentDate: TextView
    private lateinit var tvStatSummary: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        tvStatTotalCount = view.findViewById(R.id.tvStatTotalCount)
        tvStatPhotoCount = view.findViewById(R.id.tvStatPhotoCount)
        tvStatLocationCount = view.findViewById(R.id.tvStatLocationCount)
        tvStatRecentDate = view.findViewById(R.id.tvStatRecentDate)
        tvStatSummary = view.findViewById(R.id.tvStatSummary)

        loadStatistics()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized) {
            loadStatistics()
        }
    }

    private fun loadStatistics() {
        val records = dbHelper.getAllTravelRecords()

        val totalCount = records.size
        val photoCount = records.count { it.hasPhoto() }
        val locationCount = records.count { it.hasLocation() }

        tvStatTotalCount.text = "${totalCount}개"
        tvStatPhotoCount.text = "${photoCount}개"
        tvStatLocationCount.text = "${locationCount}개"

        if (records.isNotEmpty()) {
            val recentRecord = records[0]

            tvStatRecentDate.text = recentRecord.visitDate

            tvStatSummary.text =
                "현재 총 ${totalCount}개의 여행 기록이 저장되어 있습니다.\n" +
                        "이 중 사진이 포함된 기록은 ${photoCount}개입니다.\n" +
                        "위치 정보가 포함된 기록은 ${locationCount}개입니다.\n\n" +
                        "최근 기록은 ${recentRecord.place}입니다."
        } else {
            tvStatRecentDate.text = "없음"

            tvStatSummary.text =
                "아직 저장된 여행 기록이 없습니다.\n" +
                        "기록 탭에서 여행 기록을 추가하면 이곳에 통계가 표시됩니다."
        }
    }
}