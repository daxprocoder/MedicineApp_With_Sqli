package  com.example.medicineapp

import android.content.Context
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(private val context: Context, private var medicineList: MutableList<MedicineDBHelper.Medicine>) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNameTextView: TextView = itemView.findViewById(R.id.medicineNameTextView)
        val medicineDaysTextView: TextView = itemView.findViewById(R.id.medicineDaysTextView)
        val medicineTimeTextView: TextView = itemView.findViewById(R.id.medicineTimeTextView)
        val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMedicine = medicineList[position]
        holder.medicineNameTextView.text = currentMedicine.name
        holder.medicineDaysTextView.text = currentMedicine.days
        holder.medicineTimeTextView.text = currentMedicine.reminderTime

        holder.deleteImageView.setOnClickListener {
            val dbHelper = MedicineDBHelper(context)
            val db = dbHelper.writableDatabase

            // Define the selection clause and arguments
            val selection = "${BaseColumns._ID}=?"
            val selectionArgs = arrayOf(currentMedicine.id.toString())

            // Attempt to delete the medicine entry
            val deletedRows = db.delete(MedicineContract.MedicineEntry.TABLE_NAME, selection, selectionArgs)
            db.close()

            // Check if the deletion was successful
            if (deletedRows > 0) {
                // If successful, remove the item from the list and notify the adapter
                medicineList.removeAt(position)
                notifyItemRemoved(position)
                Toast.makeText(context, "Medicine deleted successfully!", Toast.LENGTH_SHORT).show()
            } else {
                // If deletion failed, display an error message
                Toast.makeText(context, "Failed to delete medicine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return medicineList.size
    }

    // Method to update the dataset and notify the adapter
    fun updateData(newList: MutableList<MedicineDBHelper.Medicine>) {
        medicineList.clear()
        medicineList.addAll(newList)
        notifyDataSetChanged()
    }
}
