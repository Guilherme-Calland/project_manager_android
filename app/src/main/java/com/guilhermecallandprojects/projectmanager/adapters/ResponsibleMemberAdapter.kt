package com.guilhermecallandprojects.projectmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.utils.Util

class ResponsibleMemberAdapter(val context: Context, private var members: ArrayList<Member>)
    : RecyclerView.Adapter<MemberHolder>() {

    private var onPressedObject: OnPressedInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.member, parent, false)
        return MemberHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        if(isMemberElement(position)){
            val member = members[position]
            setHolderViews(holder, member)
            holder.memberElement.setOnClickListener { onPressedObject?.onPressedMember(member) }
        }else{
            showRemoveElement(holder)
            holder.removeElement.setOnClickListener { onPressedObject?.onPressedRemove() }
        }
    }

    private fun setHolderViews(
        holder: MemberHolder,
        member: Member
    ) {
        holder.memberName.text = member.name
        val color = Util.nameToColor(member.color)
        if (color != null) {
            holder.memberName.setTextColor(getColor(context, color))
        }
    }

    private fun showRemoveElement(holder: MemberHolder) {
        holder.memberElement.visibility = GONE
        holder.removeElement.visibility = VISIBLE
    }

    private fun isMemberElement(position: Int) = position > 0

    override fun getItemCount(): Int {
        return members.size
    }

    interface OnPressedInterface{
        fun onPressedMember(member: Member)
        fun onPressedRemove()
    }

    fun setOnPressedObject(onPressedObject: OnPressedInterface){
        this.onPressedObject = onPressedObject
    }

}