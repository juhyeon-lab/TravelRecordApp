package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    private lateinit var tvStatsTotalCount: TextView
    private lateinit var tvStatsPhotoCount: TextView
    private lateinit var tvStatsLocationCount: TextView
    private lateinit var tvStatsRecentPlace: TextView
    private lateinit var tvStatsRecentDate: TextView
    private lateinit var tvStatsRecentMemo: TextView
    private lateinit var tvStatsSummary: TextView

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

        tvStatsTotalCount = view.findViewById(R.id.tvStatsTotalCount)
        tvStatsPhotoCount = view.findViewById(R.id.tvStatsPhotoCount)
        tvStatsLocationCount = view.findViewById(R.id.tvStatsLocationCount)
        tvStatsRecentPlace = view.findViewById(R.id.tvStatsRecentPlace)
        tvStatsRecentDate = view.findViewById(R.id.tvStatsRecentDate)
        tvStatsRecentMemo = view.findViewById(R.id.tvStatsRecentMemo)
        tvStatsSummary = view.findViewById(R.id.tvStatsSummary)

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

        tvStatsTotalCount.text = "${totalCount}개"
        tvStatsPhotoCount.text = "${photoCount}개"
        tvStatsLocationCount.text = "${locationCount}개"

        if (records.isNotEmpty()) {
            val recentRecord = records[0]

            tvStatsRecentPlace.text = recentRecord.place
            tvStatsRecentDate.text = recentRecord.visitDate
            tvStatsRecentMemo.text = recentRecord.memo.ifBlank {
                "작성된 메모가 없습니다."
            }

            tvStatsSummary.text =
                "현재 저장된 여행 기록은 총 ${totalCount}개입니다.\n" +
                        "이 중 사진이 포함된 기록은 ${photoCount}개이고, 위치 정보가 포함된 기록은 ${locationCount}개입니다.\n" +
                        "최근 여행 기록은 ${recentRecord.place}입니다."
        } else {
            tvStatsRecentPlace.text = "최근 기록이 없습니다"
            tvStatsRecentDate.text = "날짜 없음"
            tvStatsRecentMemo.text = "여행 기록을 추가하면 이곳에 최근 기록 메모가 표시됩니다."

            tvStatsSummary.text = "아직 분석할 여행 기록이 없습니다. 기록 탭에서 여행 기록을 추가해 주세요."
        }
    }
}