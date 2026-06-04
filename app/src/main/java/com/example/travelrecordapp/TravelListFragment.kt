package com.example.travelrecordapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TravelListFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var travelAdapter: TravelAdapter

    private lateinit var btnAddTravel: Button
    private lateinit var emptyListCard: View
    private lateinit var tvSectionTitle: TextView

    private lateinit var tvListTotalCount: TextView
    private lateinit var tvRecentDate: TextView
    private lateinit var recyclerTravel: RecyclerView

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
        emptyListCard = view.findViewById(R.id.emptyListCard)
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle)
        tvListTotalCount = view.findViewById(R.id.tvListTotalCount)
        tvRecentDate = view.findViewById(R.id.tvRecentDate)
        recyclerTravel = view.findViewById(R.id.recyclerTravel)

        travelAdapter = TravelAdapter(mutableListOf()) { record ->
            Toast.makeText(
                requireContext(),
                "${record.place} / ${record.visitDate}",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerTravel.layoutManager = LinearLayoutManager(requireContext())
        recyclerTravel.adapter = travelAdapter

        btnAddTravel.setOnClickListener {
            val intent = Intent(requireContext(), AddTravelActivity::class.java)
            startActivity(intent)
        }

        loadTravelRecords()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized && ::travelAdapter.isInitialized) {
            loadTravelRecords()
        }
    }

    private fun loadTravelRecords() {
        val records = dbHelper.getAllTravelRecords()
        val count = records.size

        tvListTotalCount.text = "${count}개"

        if (records.isNotEmpty()) {
            val recentRecord = records[0]

            tvRecentDate.text = recentRecord.visitDate
            emptyListCard.visibility = View.GONE
            tvSectionTitle.text = "저장된 여행 기록"

            travelAdapter.updateList(records)
        } else {
            tvRecentDate.text = "준비중"
            emptyListCard.visibility = View.VISIBLE
            tvSectionTitle.text = "저장된 여행 기록"

            travelAdapter.updateList(emptyList())
        }
    }
}