package com.example.medicineapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MedicineActivity : AppCompatActivity() {

    private lateinit var dbHelper: MedicineDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedicineAdapter

    private val handler = Handler()
    private val refreshInterval = 2000L // Refresh interval in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)

        // Initialize DBHelper
        dbHelper = MedicineDBHelper(this)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.medicineRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MedicineAdapter(this, mutableListOf()) // Initialize with empty list
        recyclerView.adapter = adapter

        // Set click listener for add medicine button
        val addMedicineButton: Button = findViewById(R.id.addMedicineButton)
        addMedicineButton.setOnClickListener {
            // Start AddMedicineActivity
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }

        // Start periodic data refresh
        startPeriodicDataRefresh()
    }

    private fun startPeriodicDataRefresh() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Refresh data from database
                refreshData()
                // Schedule the next refresh
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    private fun refreshData() {
        // Ensure dbHelper is initialized
        if (::dbHelper.isInitialized) {
            // Fetch medicines from the database and update the adapter
            adapter = MedicineAdapter(this, dbHelper.getAllMedicines().toMutableList())
            recyclerView.adapter = adapter
        } else {
            // Handle dbHelper being null
            // For example, show a toast message or log an error
            Toast.makeText(this, "Database helper is not initialized", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
