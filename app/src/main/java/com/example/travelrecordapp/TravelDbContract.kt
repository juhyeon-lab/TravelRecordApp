package com.example.travelrecordapp

object TravelDbContract {
    const val DATABASE_NAME = "travel_record.db"
    const val DATABASE_VERSION = 1

    const val TABLE_NAME = "travel_records"

    const val COL_NO = "no"
    const val COL_PLACE = "place"
    const val COL_VISIT_DATE = "visit_date"
    const val COL_MEMO = "memo"
    const val COL_PHOTO_URI = "photo_uri"
    const val COL_LATITUDE = "latitude"
    const val COL_LONGITUDE = "longitude"

    const val CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COL_NO INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_PLACE TEXT NOT NULL,
            $COL_VISIT_DATE TEXT NOT NULL,
            $COL_MEMO TEXT,
            $COL_PHOTO_URI TEXT,
            $COL_LATITUDE REAL,
            $COL_LONGITUDE REAL
        )
    """

    const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    const val SELECT_ALL = "SELECT * FROM $TABLE_NAME ORDER BY $COL_NO DESC"
}