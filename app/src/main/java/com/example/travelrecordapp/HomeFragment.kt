package com.example.travelrecordapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnGoList = view.findViewById<Button>(R.id.btnGoList)

        btnGoList.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "여행 기록 목록 화면은 하단의 '기록' 탭에서 확인할 수 있습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }
}