package com.guilhermecallandprojects.projectmanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Task

class TodoTasksAdapter(var todoTasks : ArrayList<Task>)
    : RecyclerView.Adapter<TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return TaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = todoTasks[position]
        holder.info.text = task.info
        holder.responsible.text = task.responsible
    }

    override fun getItemCount(): Int {
        return todoTasks.size
    }
}
