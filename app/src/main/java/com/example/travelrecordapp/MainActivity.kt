package com.example.travelrecordapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var btnHome: Button
    private lateinit var btnList: Button
    private lateinit var btnMap: Button
    private lateinit var btnInfo: Button

    private val tagHome = "HOME"
    private val tagList = "LIST"
    private val tagMap = "MAP"
    private val tagInfo = "INFO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHome = findViewById(R.id.btnHome)
        btnList = findViewById(R.id.btnList)
        btnMap = findViewById(R.id.btnMap)
        btnInfo = findViewById(R.id.btnInfo)

        btnHome.setOnClickListener {
            changeFragment(HomeFragment(), tagHome)
        }

        btnList.setOnClickListener {
            changeFragment(TravelListFragment(), tagList)
        }

        btnMap.setOnClickListener {
            changeFragment(TravelMapFragment(), tagMap)
        }

        btnInfo.setOnClickListener {
            changeFragment(InfoFragment(), tagInfo)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateSelectedButtonByCurrentFragment()
        }

        if (savedInstanceState == null) {
            changeFragment(HomeFragment(), tagHome, false)
        } else {
            updateSelectedButtonByCurrentFragment()
        }
    }

    fun openTravelListFragment() {
        changeFragment(TravelListFragment(), tagList)
    }

    fun openTravelMapFragment() {
        changeFragment(TravelMapFragment(), tagMap)
    }

    private fun changeFragment(fragment: Fragment, tag: String, addToBackStack: Boolean = true) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (currentFragment != null && currentFragment.tag == tag) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        transaction.commit()
        updateSelectedButton(tag)
    }

    private fun updateSelectedButtonByCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val currentTag = currentFragment?.tag ?: tagHome
        updateSelectedButton(currentTag)
    }

    private fun updateSelectedButton(selectedTag: String) {
        val selectedColor = ContextCompat.getColor(this, R.color.travel_blue)
        val unselectedColor = ContextCompat.getColor(this, R.color.nav_unselected)
        val selectedTextColor = ContextCompat.getColor(this, R.color.white)
        val unselectedTextColor = ContextCompat.getColor(this, R.color.travel_dark)

        val buttons = listOf(
            Pair(btnHome, tagHome),
            Pair(btnList, tagList),
            Pair(btnMap, tagMap),
            Pair(btnInfo, tagInfo)
        )

        for ((button, tag) in buttons) {
            if (tag == selectedTag) {
                button.backgroundTintList = ColorStateList.valueOf(selectedColor)
                button.setTextColor(selectedTextColor)
            } else {
                button.backgroundTintList = ColorStateList.valueOf(unselectedColor)
                button.setTextColor(unselectedTextColor)
            }
        }
    }
}