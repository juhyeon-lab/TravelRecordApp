package com.example.travelrecordapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddTravelActivity : AppCompatActivity() {

    private lateinit var etPlace: EditText
    private lateinit var etMemo: EditText
    private lateinit var tvSelectedDateTime: TextView
    private lateinit var tvPhotoStatus: TextView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var btnPickPhoto: Button
    private lateinit var btnSaveTravel: Button
    private lateinit var btnCancel: Button

    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        etPlace = findViewById(R.id.etPlace)
        etMemo = findViewById(R.id.etMemo)
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime)
        tvPhotoStatus = findViewById(R.id.tvPhotoStatus)
        btnPickDate = findViewById(R.id.btnPickDate)
        btnPickTime = findViewById(R.id.btnPickTime)
        btnPickPhoto = findViewById(R.id.btnPickPhoto)
        btnSaveTravel = findViewById(R.id.btnSaveTravel)
        btnCancel = findViewById(R.id.btnCancel)

        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnPickTime.setOnClickListener {
            showTimePicker()
        }

        btnPickPhoto.setOnClickListener {
            Toast.makeText(
                this,
                "사진 선택 기능은 이후 단계에서 구현합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnSaveTravel.setOnClickListener {
            checkInputForNextStep()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                updateSelectedDateTimeText()
            },
            year,
            month,
            day
        )

        dialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format(
                    "%02d:%02d",
                    selectedHour,
                    selectedMinute
                )

                updateSelectedDateTimeText()
            },
            hour,
            minute,
            true
        )

        dialog.show()
    }

    private fun updateSelectedDateTimeText() {
        val dateText = if (selectedDate.isBlank()) "날짜 미선택" else selectedDate
        val timeText = if (selectedTime.isBlank()) "시간 미선택" else selectedTime

        tvSelectedDateTime.text = "$dateText $timeText"
    }

    private fun checkInputForNextStep() {
        val place = etPlace.text.toString().trim()
        val memo = etMemo.text.toString().trim()

        if (place.isEmpty()) {
            Toast.makeText(this, "여행지명을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDate.isBlank()) {
            Toast.makeText(this, "방문 날짜를 선택하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(
            this,
            "입력 확인 완료: $place / $selectedDate",
            Toast.LENGTH_SHORT
        ).show()

        // 실제 SQLite 저장은 다음 단계에서 구현합니다.
        // 현재는 추가 Activity 화면과 입력 검증까지만 확인합니다.
    }
}