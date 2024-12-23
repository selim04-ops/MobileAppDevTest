package com.example.mobileappdevtest

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todoList: List<TodoItem>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.todoTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.todoDescription)
        val priorityTextView: TextView = itemView.findViewById(R.id.todoPriority)
        val dateTextView: TextView = itemView.findViewById(R.id.todoDate)
        val timeTextView: TextView = itemView.findViewById(R.id.todoTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // Inflate the layout for each to-do item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_layout, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        // Bind the to-do data to the view
        val currentItem = todoList[position]
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.priorityTextView.text = currentItem.priority
        holder.dateTextView.text = currentItem.date
        holder.timeTextView.text = currentItem.time

        // Set the priority color
        when (currentItem.priority.lowercase()) {
            "low" -> holder.priorityTextView.setTextColor(Color.parseColor("#4CAF50")) // Green
            "medium" -> holder.priorityTextView.setTextColor(Color.parseColor("#FF9800")) // Orange
            "high" -> holder.priorityTextView.setTextColor(Color.parseColor("#F44336")) // Red
            else -> holder.priorityTextView.setTextColor(Color.BLACK) // Default to black for unknown priorities
        }
    }

    override fun getItemCount(): Int = todoList.size
}
