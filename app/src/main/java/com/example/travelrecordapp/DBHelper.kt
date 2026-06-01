package com.example.travelrecordapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(
    context,
    TravelDbContract.DATABASE_NAME,
    null,
    TravelDbContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TravelDbContract.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(TravelDbContract.DROP_TABLE)
        onCreate(db)
    }

    fun insertTravelRecord(record: TravelRecord): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(TravelDbContract.COL_PLACE, record.place)
            put(TravelDbContract.COL_VISIT_DATE, record.visitDate)
            put(TravelDbContract.COL_MEMO, record.memo)
            put(TravelDbContract.COL_PHOTO_URI, record.photoUri)
            put(TravelDbContract.COL_LATITUDE, record.latitude)
            put(TravelDbContract.COL_LONGITUDE, record.longitude)
        }

        val result = db.insert(TravelDbContract.TABLE_NAME, null, values)
        db.close()

        return result
    }

    fun getAllTravelRecords(): MutableList<TravelRecord> {
        val travelList = mutableListOf<TravelRecord>()
        val db = readableDatabase
        val cursor = db.rawQuery(TravelDbContract.SELECT_ALL, null)

        if (cursor.moveToFirst()) {
            do {
                val no = cursor.getInt(cursor.getColumnIndexOrThrow(TravelDbContract.COL_NO))
                val place = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_PLACE))
                val visitDate = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_VISIT_DATE))
                val memo = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_MEMO)) ?: ""
                val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_PHOTO_URI)) ?: ""
                val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(TravelDbContract.COL_LATITUDE))
                val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(TravelDbContract.COL_LONGITUDE))

                val record = TravelRecord(
                    no = no,
                    place = place,
                    visitDate = visitDate,
                    memo = memo,
                    photoUri = photoUri,
                    latitude = latitude,
                    longitude = longitude
                )

                travelList.add(record)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return travelList
    }

    fun getTravelRecord(no: Int): TravelRecord? {
        val db = readableDatabase

        val cursor = db.query(
            TravelDbContract.TABLE_NAME,
            null,
            "${TravelDbContract.COL_NO} = ?",
            arrayOf(no.toString()),
            null,
            null,
            null
        )

        var record: TravelRecord? = null

        if (cursor.moveToFirst()) {
            record = TravelRecord(
                no = cursor.getInt(cursor.getColumnIndexOrThrow(TravelDbContract.COL_NO)),
                place = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_PLACE)),
                visitDate = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_VISIT_DATE)),
                memo = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_MEMO)) ?: "",
                photoUri = cursor.getString(cursor.getColumnIndexOrThrow(TravelDbContract.COL_PHOTO_URI)) ?: "",
                latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(TravelDbContract.COL_LATITUDE)),
                longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(TravelDbContract.COL_LONGITUDE))
            )
        }

        cursor.close()
        db.close()

        return record
    }

    fun updateTravelRecord(record: TravelRecord): Int {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(TravelDbContract.COL_PLACE, record.place)
            put(TravelDbContract.COL_VISIT_DATE, record.visitDate)
            put(TravelDbContract.COL_MEMO, record.memo)
            put(TravelDbContract.COL_PHOTO_URI, record.photoUri)
            put(TravelDbContract.COL_LATITUDE, record.latitude)
            put(TravelDbContract.COL_LONGITUDE, record.longitude)
        }

        val result = db.update(
            TravelDbContract.TABLE_NAME,
            values,
            "${TravelDbContract.COL_NO} = ?",
            arrayOf(record.no.toString())
        )

        db.close()

        return result
    }

    fun deleteTravelRecord(no: Int): Int {
        val db = writableDatabase

        val result = db.delete(
            TravelDbContract.TABLE_NAME,
            "${TravelDbContract.COL_NO} = ?",
            arrayOf(no.toString())
        )

        db.close()

        return result
    }

    fun deleteAllTravelRecords(): Int {
        val db = writableDatabase
        val result = db.delete(TravelDbContract.TABLE_NAME, null, null)
        db.close()

        return result
    }

    fun getTravelRecordCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${TravelDbContract.TABLE_NAME}", null)

        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return count
    }
}