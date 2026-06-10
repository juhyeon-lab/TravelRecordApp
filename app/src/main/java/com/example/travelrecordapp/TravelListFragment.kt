package com.example.travelrecordapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TravelListFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var travelAdapter: TravelAdapter

    private lateinit var btnAddTravel: Button
    private lateinit var btnListOption: Button
    private lateinit var emptyListCard: View
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvListTotalCount: TextView
    private lateinit var tvRecentDate: TextView
    private lateinit var recyclerTravel: RecyclerView

    private var isLatestOrder = true

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
        btnListOption = view.findViewById(R.id.btnListOption)
        emptyListCard = view.findViewById(R.id.emptyListCard)
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle)
        tvListTotalCount = view.findViewById(R.id.tvListTotalCount)
        tvRecentDate = view.findViewById(R.id.tvRecentDate)
        recyclerTravel = view.findViewById(R.id.recyclerTravel)

        travelAdapter = TravelAdapter(
            mutableListOf(),
            onItemClick = { record ->
                val intent = Intent(requireContext(), TravelDetailActivity::class.java)
                intent.putExtra(TravelDetailActivity.EXTRA_RECORD_NO, record.no)
                startActivity(intent)
            },
            onEditClick = { record ->
                val intent = Intent(requireContext(), AddTravelActivity::class.java)
                intent.putExtra(TravelDetailActivity.EXTRA_RECORD_NO, record.no)
                startActivity(intent)
            },
            onDeleteClick = { record ->
                showDeleteConfirmDialog(record)
            }
        )

        recyclerTravel.layoutManager = LinearLayoutManager(requireContext())
        recyclerTravel.adapter = travelAdapter

        btnAddTravel.setOnClickListener {
            val intent = Intent(requireContext(), AddTravelActivity::class.java)
            startActivity(intent)
        }

        btnListOption.setOnClickListener {
            showListOptionMenu(it)
        }

        loadTravelRecords()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized && ::travelAdapter.isInitialized) {
            loadTravelRecords()
        }
    }

    private fun showListOptionMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)

        popupMenu.menu.add("최신순 정렬")
        popupMenu.menu.add("오래된순 정렬")
        popupMenu.menu.add("전체 삭제")

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "최신순 정렬" -> {
                    isLatestOrder = true
                    loadTravelRecords()
                    Toast.makeText(requireContext(), "최신순으로 정렬했습니다.", Toast.LENGTH_SHORT).show()
                    true
                }

                "오래된순 정렬" -> {
                    isLatestOrder = false
                    loadTravelRecords()
                    Toast.makeText(requireContext(), "오래된순으로 정렬했습니다.", Toast.LENGTH_SHORT).show()
                    true
                }

                "전체 삭제" -> {
                    showDeleteAllConfirmDialog()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun loadTravelRecords() {
        val records = dbHelper.getAllTravelRecords()
        val sortedRecords = if (isLatestOrder) {
            records
        } else {
            records.reversed()
        }

        val count = sortedRecords.size

        tvListTotalCount.text = "${count}개"

        if (sortedRecords.isNotEmpty()) {
            val recentRecord = records[0]

            tvRecentDate.text = recentRecord.visitDate
            emptyListCard.visibility = View.GONE

            tvSectionTitle.text = if (isLatestOrder) {
                "저장된 여행 기록"
            } else {
                "저장된 여행 기록 - 오래된순"
            }

            travelAdapter.updateList(sortedRecords)
        } else {
            tvRecentDate.text = "준비중"
            emptyListCard.visibility = View.VISIBLE
            tvSectionTitle.text = "저장된 여행 기록"

            travelAdapter.updateList(emptyList())
        }
    }

    private fun showDeleteConfirmDialog(record: TravelRecord) {
        AlertDialog.Builder(requireContext())
            .setTitle("여행 기록 삭제")
            .setMessage("${record.place} 기록을 삭제할까요?")
            .setPositiveButton("삭제") { _, _ ->
                deleteTravelRecord(record)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deleteTravelRecord(record: TravelRecord) {
        val result = dbHelper.deleteTravelRecord(record.no)

        if (result > 0) {
            Toast.makeText(
                requireContext(),
                "여행 기록이 삭제되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            loadTravelRecords()
        } else {
            Toast.makeText(
                requireContext(),
                "여행 기록 삭제에 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showDeleteAllConfirmDialog() {
        val count = dbHelper.getTravelRecordCount()

        if (count == 0) {
            Toast.makeText(requireContext(), "삭제할 여행 기록이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("전체 삭제")
            .setMessage("저장된 여행 기록 ${count}개를 모두 삭제할까요?")
            .setPositiveButton("전체 삭제") { _, _ ->
                val result = dbHelper.deleteAllTravelRecords()

                Toast.makeText(
                    requireContext(),
                    "여행 기록 ${result}개가 삭제되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                loadTravelRecords()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}