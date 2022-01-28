package com.guilhermecallandprojects.projectmanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util

class TodoTasksAdapter(private var todoTasks: ArrayList<Task>) :
    RecyclerView.Adapter<TaskHolder>() {
    private var onPressedInterface: OnPressedInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return TaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = todoTasks[position]
        holder.info.text = task.info
        if(task.responsible == ""){
            holder.responsible.visibility = View.GONE
        }else{
            holder.responsible.text = task.responsible
        }

        holder.deleteButton.setOnClickListener {
            if (onPressedInterface != null) {
                onPressedInterface?.onDelete(position, task)
            } else {
                Log.e(Util.LOG_KEY, "error on delete button. (TodoTasksAdapter)")
            }
        }

        holder.editButton.setOnClickListener {
            if (onPressedInterface != null) {
                onPressedInterface?.onEdit(position, task)
            } else {
                Log.e(Util.LOG_KEY, "error on edit button. (TodoTasksAdapter)")
            }
        }
    }

    override fun getItemCount(): Int {
        return todoTasks.size
    }

    interface OnPressedInterface {
        fun onDelete(position: Int, model: Task)
        fun onEdit(position: Int, model: Task)
    }

    fun setOnPressedListener(onPressedInterface: OnPressedInterface) {
        this.onPressedInterface = onPressedInterface
    }
}
