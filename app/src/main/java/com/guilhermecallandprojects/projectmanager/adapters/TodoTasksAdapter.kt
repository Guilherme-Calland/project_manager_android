package com.guilhermecallandprojects.projectmanager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util

class TodoTasksAdapter(private var todoTasks: ArrayList<Task>) :
    RecyclerView.Adapter<TaskHolder>() {
    private var holderList: ArrayList<TaskHolder> = ArrayList()
    private var onPressedObject: OnPressedInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return TaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holderList.add(holder)

        val task = todoTasks[position]
        holder.info.text = task.info
        if(task.responsible == ""){
            holder.responsible.visibility = View.GONE
        }else{
            holder.responsible.text = task.responsible
        }

        holder.deleteButton.setOnClickListener { onPressedObject?.onDelete(position, task) }
        holder.editButton.setOnClickListener { onPressedObject?.onEdit(position, task) }
        holder.task.setOnClickListener { toggleIconsVisibility(holder) }
    }

    private fun toggleIconsVisibility(holder: TaskHolder) {
        resetIconViews(holder)
        toggleCurrentIconView(holder)
    }

    private fun toggleCurrentIconView(holder: TaskHolder) {
        if (holder.iconsRow.isGone) {
            holder.iconsRow.visibility = VISIBLE
        } else {
            holder.iconsRow.visibility = GONE
        }
    }

    private fun resetIconViews(holder: TaskHolder) {
        for (h in holderList) {
            if (h != holder) {
                h.iconsRow.visibility = GONE
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

    fun setOnPressedObject(onPressedObject: OnPressedInterface) {
        this.onPressedObject = onPressedObject
    }
}
