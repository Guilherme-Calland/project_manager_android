package com.guilhermecallandprojects.projectmanager.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.model.Task

class MemberAdapter(private var context: Context, private var members: ArrayList<Member>)
    : RecyclerView.Adapter<MemberHolder>(){

    var holderList: ArrayList<MemberHolder> = ArrayList()
    private var onPressedObject: OnPressedInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.member, parent, false)
        return MemberHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holderList.add(holder)
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
        holder.name.setTextColor(getColor(context,color))

        holder.member.setOnClickListener { toggleIconsVisibility(holder) }
        holder.deleteButton.setOnClickListener {
            onPressedObject?.onDelete(position, member)
        }

        holder.editButton.setOnClickListener {
            onPressedObject?.onEdit(position, member)
        }
    }

    private fun toggleIconsVisibility(holder: MemberHolder) {
        resetIconViews(holder)
        toggleCurrentIconView(holder)
    }

    private fun toggleCurrentIconView(holder: MemberHolder) {
        if (holder.iconRow.isGone) {
            holder.iconRow.visibility = VISIBLE
        } else {
            holder.iconRow.visibility = GONE
        }
    }

    private fun resetIconViews(holder: MemberHolder) {
        for (h in holderList) {
            if (h != holder) {
                h.iconRow.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }

    interface OnPressedInterface{
        fun onDelete(position: Int, model: Member)
        fun onEdit(position: Int, model: Member)
    }

    fun setOnPressedObject(onPressedObject: OnPressedInterface){
        this.onPressedObject = onPressedObject
    }
}