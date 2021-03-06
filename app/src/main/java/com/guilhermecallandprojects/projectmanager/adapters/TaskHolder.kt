package com.guilhermecallandprojects.projectmanager.adapters

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_add_task.view.*
import kotlinx.android.synthetic.main.task.view.*

class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var info : TextView = itemView.tv_info
    var responsible: TextView = itemView.tv_responsible_member
    var deleteButton : ImageView = itemView.iv_delete
    var editButton : ImageView = itemView.iv_edit
    var prevButton: ImageView = itemView.iv_throwPrevCol
    var nextButton : ImageView = itemView.iv_throwNextCol
    var task: LinearLayout = itemView.ll_task
    var iconsRow: LinearLayout = itemView.ll_icons
}