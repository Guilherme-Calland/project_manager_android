package com.guilhermecallandprojects.projectmanager.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util

class TaskAdapter(private var context: Context, private var taskList: ArrayList<Task>, var adapterID: Int) :
    RecyclerView.Adapter<TaskHolder>() {

    private var holderList: ArrayList<TaskHolder> = ArrayList()
    private var onPressedObject: OnPressedInterface? = null
    private val memberDB = MembersDatabaseHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task, parent, false)
        return TaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holderList.add(holder)
        val task = taskList[position]
        holder.info.text = task.info

        val responsible = memberDB.fetchMember(task.responsible?: "")
        //TODO: apaga isso
        Log.i("testing", "${responsible}")
        if(responsible != null){
            holder.responsible.visibility = VISIBLE
            holder.responsible.text = task.responsible
            val color = Util.nameToColor(responsible.color)
            if(color!=null){ holder.responsible.setTextColor(getColor(context, color)) }
        } else {
            holder.responsible.visibility = GONE
        }

        if(task.id!= null){
            holder.deleteButton.setOnClickListener { onPressedObject?.onDelete(task.id, adapterID) }
        } else {
            Log.e("projectmanagerapp", "A null id was passed for deletion.")
        }
        holder.editButton.setOnClickListener { onPressedObject?.onEdit(task, adapterID) }
        holder.nextButton.setOnClickListener{ onPressedObject?.onNext(task, adapterID) }
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

    fun resetIconViews(holder: TaskHolder? = null) {
        for (h in holderList) {
            if (h != holder) {
                h.iconsRow.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    interface OnPressedInterface {
        fun onDelete(taskID: Int, adapterID: Int)
        fun onEdit(task: Task, adapterID: Int)
        fun onNext(task: Task, adapterID: Int)
    }

    fun setOnPressedObject(onPressedObject: OnPressedInterface) {
        this.onPressedObject = onPressedObject
    }
}
