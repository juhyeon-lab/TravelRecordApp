package com.example.travelrecordapp

data class TravelRecord(
    val no: Int = 0,
    val place: String,
    val visitDate: String,
    val memo: String = "",
    val photoUri: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun hasPhoto(): Boolean {
        return photoUri.isNotBlank()
    }

    fun hasLocation(): Boolean {
        return latitude != 0.0 && longitude != 0.0
    }

    companion object {
        fun sample(): TravelRecord {
            return TravelRecord(
                no = 1,
                place = "부산 해운대",
                visitDate = "2026-06-01",
                memo = "바다를 보러 간 여행 기록 예시입니다.",
                photoUri = "",
                latitude = 35.1587,
                longitude = 129.1603
            )
        }
    }
}