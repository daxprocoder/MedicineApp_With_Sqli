package com.example.medicineapp

import android.provider.BaseColumns

object MedicineContract {
    // Table contents are grouped together in an anonymous object.
    object MedicineEntry : BaseColumns {
        const val TABLE_NAME = "medicine"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_DAYS = "days"
        const val COLUMN_NAME_REMINDER_TIME = "reminder_time"
    }
}
