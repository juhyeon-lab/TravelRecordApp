package com.example.travelrecordapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment() {

    private lateinit var etSchedulePlace: EditText
    private lateinit var calendarSchedule: CalendarView
    private lateinit var timePickerSchedule: TimePicker
    private lateinit var tvSelectedSchedule: TextView
    private lateinit var btnSaveSchedule: Button
    private lateinit var btnClearSchedule: Button

    private var selectedDate = ""
    private var selectedTime = ""

    private val prefName = "travel_schedule_pref"
    private val keyPlace = "schedule_place"
    private val keyDate = "schedule_date"
    private val keyTime = "schedule_time"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSchedulePlace = view.findViewById(R.id.etSchedulePlace)
        calendarSchedule = view.findViewById(R.id.calendarSchedule)
        timePickerSchedule = view.findViewById(R.id.timePickerSchedule)
        tvSelectedSchedule = view.findViewById(R.id.tvSelectedSchedule)
        btnSaveSchedule = view.findViewById(R.id.btnSaveSchedule)
        btnClearSchedule = view.findViewById(R.id.btnClearSchedule)

        initDefaultDateTime()
        loadSavedSchedule()

        calendarSchedule.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format(
                "%04d-%02d-%02d",
                year,
                month + 1,
                dayOfMonth
            )

            updateSelectedScheduleText()
        }

        timePickerSchedule.setOnTimeChangedListener { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            updateSelectedScheduleText()
        }

        btnSaveSchedule.setOnClickListener {
            saveSchedule()
        }

        btnClearSchedule.setOnClickListener {
            clearSchedule()
        }
    }

    private fun initDefaultDateTime() {
        val calendar = Calendar.getInstance()

        selectedDate = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.KOREA
        ).format(calendar.time)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        timePickerSchedule.setIs24HourView(true)
        timePickerSchedule.hour = hour
        timePickerSchedule.minute = minute

        selectedTime = String.format("%02d:%02d", hour, minute)

        updateSelectedScheduleText()
    }

    private fun updateSelectedScheduleText() {
        val place = etSchedulePlace.text.toString().trim()

        val placeText = if (place.isBlank()) {
            "예정 여행지 미입력"
        } else {
            place
        }

        tvSelectedSchedule.text =
            "예정 여행지: $placeText\n" +
                    "선택 날짜: $selectedDate\n" +
                    "선택 시간: $selectedTime"
    }

    private fun saveSchedule() {
        val place = etSchedulePlace.text.toString().trim()

        if (place.isBlank()) {
            Toast.makeText(requireContext(), "예정 여행지를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val pref = requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)

        pref.edit()
            .putString(keyPlace, place)
            .putString(keyDate, selectedDate)
            .putString(keyTime, selectedTime)
            .apply()

        updateSelectedScheduleText()

        Toast.makeText(requireContext(), "여행 일정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun loadSavedSchedule() {
        val pref = requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)

        val savedPlace = pref.getString(keyPlace, "") ?: ""
        val savedDate = pref.getString(keyDate, "") ?: ""
        val savedTime = pref.getString(keyTime, "") ?: ""

        if (savedPlace.isNotBlank()) {
            etSchedulePlace.setText(savedPlace)
        }

        if (savedDate.isNotBlank()) {
            selectedDate = savedDate

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val date = dateFormat.parse(savedDate)

            if (date != null) {
                calendar.time = date
                calendarSchedule.date = calendar.timeInMillis
            }
        }

        if (savedTime.isNotBlank()) {
            selectedTime = savedTime

            val hour = savedTime.substringBefore(":").toIntOrNull() ?: 0
            val minute = savedTime.substringAfter(":").toIntOrNull() ?: 0

            timePickerSchedule.setIs24HourView(true)
            timePickerSchedule.hour = hour
            timePickerSchedule.minute = minute
        }

        updateSelectedScheduleText()
    }

    private fun clearSchedule() {
        val pref = requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)

        pref.edit().clear().apply()

        etSchedulePlace.setText("")
        initDefaultDateTime()

        Toast.makeText(requireContext(), "여행 일정이 초기화되었습니다.", Toast.LENGTH_SHORT).show()
    }
}