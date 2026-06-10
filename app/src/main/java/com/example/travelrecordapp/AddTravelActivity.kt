package com.example.travelrecordapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddTravelActivity : AppCompatActivity() {

    private lateinit var tvAddTravelTitle: TextView
    private lateinit var tvAddTravelDesc: TextView
    private lateinit var etPlace: EditText
    private lateinit var etMemo: EditText
    private lateinit var tvSelectedDateTime: TextView
    private lateinit var tvPhotoStatus: TextView
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var btnPickPhoto: Button
    private lateinit var btnSaveTravel: Button
    private lateinit var btnCancel: Button

    private lateinit var dbHelper: DBHelper

    private var selectedDate = ""
    private var selectedTime = ""
    private var selectedPhotoUri = ""

    private var editRecordNo = -1
    private var currentEditRecord: TravelRecord? = null

    companion object {
        private const val REQUEST_PICK_IMAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        dbHelper = DBHelper(this)

        tvAddTravelTitle = findViewById(R.id.tvAddTravelTitle)
        tvAddTravelDesc = findViewById(R.id.tvAddTravelDesc)
        etPlace = findViewById(R.id.etPlace)
        etMemo = findViewById(R.id.etMemo)
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime)
        tvPhotoStatus = findViewById(R.id.tvPhotoStatus)
        btnPickDate = findViewById(R.id.btnPickDate)
        btnPickTime = findViewById(R.id.btnPickTime)
        btnPickPhoto = findViewById(R.id.btnPickPhoto)
        btnSaveTravel = findViewById(R.id.btnSaveTravel)
        btnCancel = findViewById(R.id.btnCancel)

        editRecordNo = intent.getIntExtra(TravelDetailActivity.EXTRA_RECORD_NO, -1)

        if (editRecordNo != -1) {
            loadRecordForEdit(editRecordNo)
        }

        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnPickTime.setOnClickListener {
            showTimePicker()
        }

        btnPickPhoto.setOnClickListener {
            openGallery()
        }

        btnSaveTravel.setOnClickListener {
            saveTravelRecord()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadRecordForEdit(recordNo: Int) {
        val record = dbHelper.getTravelRecord(recordNo)

        if (record == null) {
            Toast.makeText(this, "수정할 여행 기록을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        currentEditRecord = record

        tvAddTravelTitle.text = "여행 기록 수정"
        tvAddTravelDesc.text = "저장된 여행 기록을 수정합니다."
        btnSaveTravel.text = "수정 완료"

        etPlace.setText(record.place)
        etMemo.setText(record.memo)

        selectedPhotoUri = record.photoUri

        if (record.visitDate.contains(" ")) {
            selectedDate = record.visitDate.substringBefore(" ")
            selectedTime = record.visitDate.substringAfter(" ")
        } else {
            selectedDate = record.visitDate
            selectedTime = ""
        }

        updateSelectedDateTimeText()

        if (record.hasPhoto()) {
            tvPhotoStatus.text = "기존 사진이 저장되어 있습니다."
        } else {
            tvPhotoStatus.text = "선택된 사진이 없습니다"
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val year = if (selectedDate.isNotBlank()) {
            selectedDate.substringBefore("-").toIntOrNull() ?: calendar.get(Calendar.YEAR)
        } else {
            calendar.get(Calendar.YEAR)
        }

        val month = if (selectedDate.isNotBlank()) {
            selectedDate.split("-").getOrNull(1)?.toIntOrNull()?.minus(1)
                ?: calendar.get(Calendar.MONTH)
        } else {
            calendar.get(Calendar.MONTH)
        }

        val day = if (selectedDate.isNotBlank()) {
            selectedDate.split("-").getOrNull(2)?.toIntOrNull()
                ?: calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            calendar.get(Calendar.DAY_OF_MONTH)
        }

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

        val hour = if (selectedTime.isNotBlank()) {
            selectedTime.substringBefore(":").toIntOrNull()
                ?: calendar.get(Calendar.HOUR_OF_DAY)
        } else {
            calendar.get(Calendar.HOUR_OF_DAY)
        }

        val minute = if (selectedTime.isNotBlank()) {
            selectedTime.substringAfter(":").toIntOrNull()
                ?: calendar.get(Calendar.MINUTE)
        } else {
            calendar.get(Calendar.MINUTE)
        }

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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data

            if (imageUri != null) {
                selectedPhotoUri = imageUri.toString()

                try {
                    contentResolver.takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }

                tvPhotoStatus.text = "사진이 선택되었습니다."
                Toast.makeText(this, "사진이 선택되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "사진을 선택하지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTravelRecord() {
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

        val visitDate = if (selectedTime.isBlank()) {
            selectedDate
        } else {
            "$selectedDate $selectedTime"
        }

        if (editRecordNo == -1) {
            insertTravelRecord(place, visitDate, memo)
        } else {
            updateTravelRecord(place, visitDate, memo)
        }
    }

    private fun insertTravelRecord(place: String, visitDate: String, memo: String) {
        val record = TravelRecord(
            place = place,
            visitDate = visitDate,
            memo = memo,
            photoUri = selectedPhotoUri,
            latitude = 0.0,
            longitude = 0.0
        )

        val result = dbHelper.insertTravelRecord(record)

        if (result != -1L) {
            Toast.makeText(this, "여행 기록이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "여행 기록 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTravelRecord(place: String, visitDate: String, memo: String) {
        val oldRecord = currentEditRecord

        if (oldRecord == null) {
            Toast.makeText(this, "수정할 기록 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedRecord = TravelRecord(
            no = editRecordNo,
            place = place,
            visitDate = visitDate,
            memo = memo,
            photoUri = selectedPhotoUri,
            latitude = oldRecord.latitude,
            longitude = oldRecord.longitude
        )

        val result = dbHelper.updateTravelRecord(updatedRecord)

        if (result > 0) {
            Toast.makeText(this, "여행 기록이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "여행 기록 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}