package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class TravelListFragment : Fragment() {

    private val sampleRecord = TravelRecord.sample()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_travel_list, container, false)

        val btnAddTravel = view.findViewById<Button>(R.id.btnAddTravel)
        val layoutTravelItem = view.findViewById<View>(R.id.layoutTravelItem)

        btnAddTravel.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "여행 기록 추가 화면은 다음 단계에서 구현할 예정입니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        layoutTravelItem.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "${sampleRecord.place} / ${sampleRecord.visitDate}",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }
}