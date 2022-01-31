package com.guilhermecallandprojects.projectmanager.adapters

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.member.view.*

class MemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var memberElement: LinearLayout = itemView.ll_member_element
    var memberName: TextView = itemView.tv_member_name
    var removeElement: TextView = itemView.tv_remove_element
    var iconRow: LinearLayout = itemView.ll_icons
    var deleteButton: ImageView = itemView.iv_delete
    var editButton: ImageView = itemView.iv_edit
}