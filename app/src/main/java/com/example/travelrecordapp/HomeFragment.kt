package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    private lateinit var tvTodayMessage: TextView
    private lateinit var tvTodaySubMessage: TextView
    private lateinit var tvTotalCount: TextView
    private lateinit var tvMarkerStatus: TextView
    private lateinit var tvRecentPlace: TextView
    private lateinit var tvRecentMemo: TextView
    private lateinit var btnGoList: Button
    private lateinit var btnGoMap: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        tvTodayMessage = view.findViewById(R.id.tvTodayMessage)
        tvTodaySubMessage = view.findViewById(R.id.tvTodaySubMessage)
        tvTotalCount = view.findViewById(R.id.tvTotalCount)
        tvMarkerStatus = view.findViewById(R.id.tvMarkerStatus)
        tvRecentPlace = view.findViewById(R.id.tvRecentPlace)
        tvRecentMemo = view.findViewById(R.id.tvRecentMemo)
        btnGoList = view.findViewById(R.id.btnGoList)
        btnGoMap = view.findViewById(R.id.btnGoMap)

        btnGoList.setOnClickListener {
            (activity as? MainActivity)?.openTravelListFragment()
        }

        btnGoMap.setOnClickListener {
            (activity as? MainActivity)?.openTravelMapFragment()
        }

        loadHomeData()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized) {
            loadHomeData()
        }
    }

    private fun loadHomeData() {
        val records = dbHelper.getAllTravelRecords()
        val count = records.size
        val markerCount = records.count { it.hasLocation() }

        tvTotalCount.text = "${count}개"
        tvMarkerStatus.text = "${markerCount}개"

        if (records.isNotEmpty()) {
            val recentRecord = records[0]

            tvTodayMessage.text = "최근 여행: ${recentRecord.place}"
            tvTodaySubMessage.text = "${recentRecord.visitDate}에 저장된 여행 기록입니다."

            tvRecentPlace.text = recentRecord.place
            tvRecentMemo.text = recentRecord.memo.ifBlank {
                "작성된 메모가 없는 여행 기록입니다."
            }
        } else {
            tvTodayMessage.text = "아직 저장된 여행 기록이 없습니다"
            tvTodaySubMessage.text = "기록 탭에서 여행지명, 날짜, 사진, 메모를 저장할 수 있습니다."

            tvRecentPlace.text = "최근 기록이 없습니다"
            tvRecentMemo.text = "여행 기록을 추가하면 이곳에 최근 여행지가 표시됩니다."
        }
    }
}