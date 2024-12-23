package com.example.mobileappdevtest


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappdevtest.R
import com.example.mobileappdevtest.TodoItem
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddTodoActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText
    private lateinit var edtPriority: EditText // EditText for priority
    private lateinit var btnSaveTodo: Button
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        // Initialize the views
        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        edtDate = findViewById(R.id.edtDate)
        edtTime = findViewById(R.id.edtTime)
        edtPriority = findViewById(R.id.edtPriority)
        edtPriority.setOnClickListener { view ->
            showPriorityMenu(view, edtPriority)
        }
        edtDate.setOnClickListener {
            showDatePicker(edtDate)
        }

        // Time Picker
        edtTime.setOnClickListener {
            showTimePicker(edtTime)
        }
        btnSaveTodo = findViewById(R.id.btnSaveTodo)


        // Save button click listener
        btnSaveTodo.setOnClickListener {
            saveTodo()
        }
    }
    private fun saveTodo() {
        val title = edtTitle.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val date = edtDate.text.toString().trim()
        val time = edtTime.text.toString().trim()
        val priority = edtPriority.text.toString().trim()  // Get priority from EditText
        // Validation
        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty() || priority.isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
            return
        }
        // Create a TodoItem object
        val newTodo = TodoItem(
            id = db.collection("todos").document().id,  // Firestore will generate a unique ID for each document
            title = title,
            description = description,
            date = date,
            time = time,
            priority = priority
        )
        // Add to Firestore
        db.collection("todos").document(newTodo.id) .set(newTodo).addOnSuccessListener {
                Toast.makeText(this, "Todo added successfully!", Toast.LENGTH_SHORT).show()
                finish()  // Go back to the previous screen
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error adding todo", Toast.LENGTH_SHORT).show()
            }

    }
    private fun showPriorityMenu(view: View, editText: EditText) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.priority_menu, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.high -> {
                    editText.setText("HIGH")
                    Toast.makeText(this, "Selected: HIGH", Toast.LENGTH_SHORT).show()
                }
                R.id.medium -> {
                    editText.setText("MEDIUM")
                    Toast.makeText(this, "Selected: MEDIUM", Toast.LENGTH_SHORT).show()
                }
                R.id.low -> {
                    editText.setText("LOW")
                    Toast.makeText(this, "Selected: LOW", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)
            },
            hour, minute, true // Use true for 24-hour format, false for AM/PM
        )
        timePickerDialog.show()
    }

}
