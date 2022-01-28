package com.guilhermecallandprojects.projectmanager.adapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.member.view.*

class MemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var member: LinearLayout = itemView.ll_member
    var iconRow: LinearLayout = itemView.ll_icons
    var name: TextView = itemView.tv_name
    var deleteButton: ImageView = itemView.iv_delete
    var editButton: ImageView = itemView.iv_edit
}