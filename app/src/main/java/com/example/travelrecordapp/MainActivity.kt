package com.example.travelrecordapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var btnHome: Button
    private lateinit var btnTravelList: Button
    private lateinit var btnMap: Button
    private lateinit var btnInfo: Button
    private lateinit var btnSchedule: Button

    private val tagHome = "home"
    private val tagList = "list"
    private val tagMap = "map"
    private val tagInfo = "info"
    private val tagSchedule = "schedule"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHome = findViewById(R.id.btnHome)
        btnTravelList = findViewById(R.id.btnTravelList)
        btnMap = findViewById(R.id.btnMap)
        btnInfo = findViewById(R.id.btnInfo)
        btnSchedule = findViewById(R.id.btnSchedule)

        btnHome.setOnClickListener {
            changeFragment(HomeFragment(), tagHome)
        }

        btnTravelList.setOnClickListener {
            changeFragment(TravelListFragment(), tagList)
        }

        btnMap.setOnClickListener {
            changeFragment(TravelMapFragment(), tagMap)
        }

        btnInfo.setOnClickListener {
            changeFragment(InfoFragment(), tagInfo)
        }

        btnSchedule.setOnClickListener {
            changeFragment(ScheduleFragment(), tagSchedule)
        }

        if (savedInstanceState == null) {
            changeFragment(HomeFragment(), tagHome)
        }
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commit()

        updateTabButton(tag)
    }

    private fun updateTabButton(selectedTag: String) {
        val selectedColor = getColor(R.color.travel_blue)
        val unselectedColor = getColor(R.color.nav_unselected)

        btnHome.setBackgroundColor(unselectedColor)
        btnTravelList.setBackgroundColor(unselectedColor)
        btnMap.setBackgroundColor(unselectedColor)
        btnInfo.setBackgroundColor(unselectedColor)
        btnSchedule.setBackgroundColor(unselectedColor)

        btnHome.setTextColor(getColor(R.color.travel_dark))
        btnTravelList.setTextColor(getColor(R.color.travel_dark))
        btnMap.setTextColor(getColor(R.color.travel_dark))
        btnInfo.setTextColor(getColor(R.color.travel_dark))
        btnSchedule.setTextColor(getColor(R.color.travel_dark))

        when (selectedTag) {
            tagHome -> {
                btnHome.setBackgroundColor(selectedColor)
                btnHome.setTextColor(getColor(R.color.white))
            }

            tagList -> {
                btnTravelList.setBackgroundColor(selectedColor)
                btnTravelList.setTextColor(getColor(R.color.white))
            }

            tagMap -> {
                btnMap.setBackgroundColor(selectedColor)
                btnMap.setTextColor(getColor(R.color.white))
            }

            tagInfo -> {
                btnInfo.setBackgroundColor(selectedColor)
                btnInfo.setTextColor(getColor(R.color.white))
            }

            tagSchedule -> {
                btnSchedule.setBackgroundColor(selectedColor)
                btnSchedule.setTextColor(getColor(R.color.white))
            }
        }
    }

    fun openTravelListFragment() {
        changeFragment(TravelListFragment(), tagList)
    }

    fun openTravelMapFragment() {
        changeFragment(TravelMapFragment(), tagMap)
    }
}