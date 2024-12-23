package com.example.mobileappdevtest

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomePage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<TodoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        // Set up the Floating Action Button
        val addTodoButton: FloatingActionButton = findViewById(R.id.addTodoButton)
        val btn_signout: FloatingActionButton = findViewById(R.id.btn_signout)
        addTodoButton.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }
        btn_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Waiting For UR Come Back  ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Initialize the Adapter and attach it to RecyclerView
        todoAdapter = TodoAdapter(todoList)
        recyclerView.adapter = todoAdapter

        // Fetch the initial Todo list
        fetchTodoList()

    }

    // Fetch all Todo items from Firestore
    private fun fetchTodoList(priority: String? = null, passed: Boolean = false) {
        val db = FirebaseFirestore.getInstance()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val query = when { passed -> {
                // Fetch items where the due date is less than the current date
                db.collection("todos").whereLessThan("date", currentDate)
            }
            priority != null -> {
                // Fetch items with the specified priority
                db.collection("todos").whereEqualTo("priority", priority)
            }
            else -> {
                // Default case: fetch upcoming TODOs (today or in the future)
                db.collection("todos").whereGreaterThanOrEqualTo("date", currentDate)
            }
        }
        query.get().addOnSuccessListener { documents -> todoList.clear()
                for (document in documents) {
                    val todo = document.toObject(TodoItem::class.java)
                    todoList.add(todo)
                }
                todoAdapter.notifyDataSetChanged() }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }




    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    // Handle menu item selections
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.high -> {
                fetchTodoList("HIGH") // Filter and fetch high-priority Todos
                return true
            }
            R.id.medium -> {
                fetchTodoList("MEDIUM") // Filter and fetch medium-priority Todos
                return true
            }
            R.id.low -> {
                fetchTodoList("LOW") // Filter and fetch low-priority Todos
                return true
            }
            R.id.passed -> {
                fetchTodoList(passed = true) // Filter and fetch passed Todos
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
