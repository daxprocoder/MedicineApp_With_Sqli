package com.example.medicineapp
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineapp.MedicineContract
import com.example.medicineapp.MedicineDBHelper

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var medicineNameEditText: EditText
    private lateinit var checkboxSunday: CheckBox
    private lateinit var checkboxMonday: CheckBox
    private lateinit var checkboxTuesday: CheckBox
    private lateinit var checkboxWednesday: CheckBox
    private lateinit var checkboxThursday: CheckBox
    private lateinit var checkboxFriday: CheckBox
    private lateinit var checkboxSaturday: CheckBox
    private lateinit var timePickerReminder: TimePicker
    private lateinit var addMedicineButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine)

        // Initialize views
        medicineNameEditText = findViewById(R.id.editTextMedicineName)
        checkboxSunday = findViewById(R.id.checkboxSunday)
        checkboxMonday = findViewById(R.id.checkboxMonday)
        checkboxTuesday = findViewById(R.id.checkboxTuesday)
        checkboxWednesday = findViewById(R.id.checkboxWednesday)
        checkboxThursday = findViewById(R.id.checkboxThrusday)
        checkboxFriday = findViewById(R.id.checkboxFriday)
        checkboxSaturday = findViewById(R.id.checkboxSaturday)
        timePickerReminder = findViewById(R.id.timePickerReminder)
        addMedicineButton = findViewById(R.id.buttonAddMedicine)

        // Set click listener for add medicine button
        addMedicineButton.setOnClickListener {
            // Get medicine name
            val medicineName = medicineNameEditText.text.toString().trim()

            // Get selected days
            val daysList = mutableListOf<String>()
            if (checkboxSunday.isChecked) daysList.add("Sunday")
            if (checkboxMonday.isChecked) daysList.add("Monday")
            if (checkboxTuesday.isChecked) daysList.add("Tuesday")
            if (checkboxWednesday.isChecked) daysList.add("Wednesday")
            if (checkboxThursday.isChecked) daysList.add("Thursday")
            if (checkboxFriday.isChecked) daysList.add("Friday")
            if (checkboxSaturday.isChecked) daysList.add("Saturday")
            val selectedDays = daysList.joinToString(", ")

            // Get reminder time
            val hour = timePickerReminder.hour
            val minute = timePickerReminder.minute
            val reminderTime = String.format("%02d:%02d", hour, minute)

            // Add medicine to database
            addMedicine(medicineName, selectedDays, reminderTime)
        }
    }

    private fun addMedicine(medicineName: String, selectedDays: String, reminderTime: String) {
        val dbHelper = MedicineDBHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(MedicineContract.MedicineEntry.COLUMN_NAME_NAME, medicineName)
            put(MedicineContract.MedicineEntry.COLUMN_NAME_DAYS, selectedDays)
            put(MedicineContract.MedicineEntry.COLUMN_NAME_REMINDER_TIME, reminderTime)
        }

        val newRowId = db.insert(MedicineContract.MedicineEntry.TABLE_NAME, null, values)
        if (newRowId != -1L) {
            Toast.makeText(this, "Medicine added successfully!", Toast.LENGTH_SHORT).show()
            // Redirect to MedicineActivity after adding medicine
            val intent = Intent(this, MedicineActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish current activity to prevent going back to AddMedicineActivity
        } else {
            Toast.makeText(this, "Failed to add medicine", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
}

