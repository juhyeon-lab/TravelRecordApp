package com.example.travelrecordapp

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TravelDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    private lateinit var ivDetailPhoto: ImageView
    private lateinit var tvDetailPhotoLabel: TextView
    private lateinit var tvDetailPlace: TextView
    private lateinit var tvDetailDate: TextView
    private lateinit var tvDetailMemo: TextView
    private lateinit var tvDetailLocation: TextView
    private lateinit var btnEditTravel: Button
    private lateinit var btnBackToList: Button

    private var recordNo: Int = -1
    private var currentRecord: TravelRecord? = null

    companion object {
        const val EXTRA_RECORD_NO = "record_no"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_detail)

        dbHelper = DBHelper(this)

        ivDetailPhoto = findViewById(R.id.ivDetailPhoto)
        tvDetailPhotoLabel = findViewById(R.id.tvDetailPhotoLabel)
        tvDetailPlace = findViewById(R.id.tvDetailPlace)
        tvDetailDate = findViewById(R.id.tvDetailDate)
        tvDetailMemo = findViewById(R.id.tvDetailMemo)
        tvDetailLocation = findViewById(R.id.tvDetailLocation)
        btnEditTravel = findViewById(R.id.btnEditTravel)
        btnBackToList = findViewById(R.id.btnBackToList)

        recordNo = intent.getIntExtra(EXTRA_RECORD_NO, -1)

        if (recordNo == -1) {
            Toast.makeText(this, "여행 기록 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadTravelRecord()

        btnEditTravel.setOnClickListener {
            Toast.makeText(
                this,
                "수정 기능은 다음 단계에서 구현합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnBackToList.setOnClickListener {
            finish()
        }
    }

    private fun loadTravelRecord() {
        val record = dbHelper.getTravelRecord(recordNo)

        if (record == null) {
            Toast.makeText(this, "해당 여행 기록이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        currentRecord = record

        tvDetailPlace.text = record.place
        tvDetailDate.text = record.visitDate
        tvDetailMemo.text = record.memo.ifBlank {
            "작성된 메모가 없습니다."
        }

        if (record.hasLocation()) {
            tvDetailLocation.text = "위도: ${record.latitude}\n경도: ${record.longitude}"
        } else {
            tvDetailLocation.text = "위치 정보가 아직 저장되지 않았습니다."
        }

        if (record.hasPhoto()) {
            ivDetailPhoto.visibility = android.view.View.VISIBLE
            tvDetailPhotoLabel.visibility = android.view.View.GONE
            ivDetailPhoto.setImageURI(Uri.parse(record.photoUri))
        } else {
            ivDetailPhoto.visibility = android.view.View.GONE
            tvDetailPhotoLabel.visibility = android.view.View.VISIBLE
            tvDetailPhotoLabel.text = "PHOTO"
        }
    }
}