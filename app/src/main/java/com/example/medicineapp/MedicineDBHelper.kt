package com.example.medicineapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MedicineDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {



    data class Medicine(
        val id: Long,
        val name: String,
        val days: String,
        val reminderTime: String
    )


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Medicine.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${MedicineContract.MedicineEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${MedicineContract.MedicineEntry.COLUMN_NAME_NAME} TEXT," +
                    "${MedicineContract.MedicineEntry.COLUMN_NAME_DAYS} TEXT," +
                    "${MedicineContract.MedicineEntry.COLUMN_NAME_REMINDER_TIME} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // Other methods like onUpgrade() and onDowngrade() can remain the same

    fun getAllMedicines(): List<Medicine> {
        val db = this.readableDatabase
        val cursor = db.query(
            MedicineContract.MedicineEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val medicineList = mutableListOf<Medicine>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val name = getString(getColumnIndexOrThrow(MedicineContract.MedicineEntry.COLUMN_NAME_NAME))
                val days = getString(getColumnIndexOrThrow(MedicineContract.MedicineEntry.COLUMN_NAME_DAYS))
                val reminderTime = getString(getColumnIndexOrThrow(MedicineContract.MedicineEntry.COLUMN_NAME_REMINDER_TIME))
                val medicine = Medicine(id, name, days, reminderTime)
                medicineList.add(medicine)
            }
        }
        cursor.close()

        return medicineList
    }

    fun addMedicine(medicine: Medicine): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(MedicineContract.MedicineEntry.COLUMN_NAME_NAME, medicine.name)
            put(MedicineContract.MedicineEntry.COLUMN_NAME_DAYS, medicine.days)
            put(MedicineContract.MedicineEntry.COLUMN_NAME_REMINDER_TIME, medicine.reminderTime)
        }
        val newRowId = db.insert(MedicineContract.MedicineEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun deleteMedicine(id: Long): Int {
        val db = this.writableDatabase
        val selection = "${BaseColumns._ID}=?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(MedicineContract.MedicineEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }
}
