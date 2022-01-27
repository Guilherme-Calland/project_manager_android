package com.guilhermecallandprojects.projectmanager.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.member.view.*

class MemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.tv_name
}