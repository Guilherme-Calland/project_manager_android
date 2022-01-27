package com.guilhermecallandprojects.projectmanager.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_add_task.view.*
import kotlinx.android.synthetic.main.task.view.*

class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var info : TextView = itemView.tv_info
    var responsible : TextView = itemView.tv_responsible
}