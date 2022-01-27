package com.guilhermecallandprojects.projectmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.model.Task

class MemberAdapter(private var members: ArrayList<Member>)
    : RecyclerView.Adapter<MemberHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.member, parent, false)
        return MemberHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        val member = members[position]
        holder.name.text = member.name
        var color: Int = R.color.white
        when(member.color){
            "blue" -> color = R.color.light_blue
            "red" -> color = R.color.light_red
            "green" -> color = R.color.light_green
            "purple" -> color = R.color.light_purple
            "orange" -> color = R.color.light_orange
        }
        holder.name.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return members.size
    }
}