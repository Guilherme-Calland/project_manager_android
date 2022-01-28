package com.guilhermecallandprojects.projectmanager.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.setPadding
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.MemberAdapter
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_members.*

class MembersActivity : AppCompatActivity() {
    private lateinit var membersList: ArrayList<Member>
    private lateinit var membersDB: MembersDatabaseHelper
    private lateinit var memberAdapter: MemberAdapter
    private lateinit var memberName: String
    private lateinit var memberColor: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        setActionBarProperties()
        initializeMemberList()
    }

    private fun initializeMemberList() {
        membersList = ArrayList()
        membersDB = MembersDatabaseHelper(this)
        memberAdapter = MemberAdapter(this, membersList)
        rv_members_list.adapter = memberAdapter
        readFromDatabase()
    }

    private fun readFromDatabase() {
        membersList.clear()
        val membersTemp = membersDB.read()
        for(m in membersTemp){
            membersList.add(m)
        }
        memberAdapter.notifyDataSetChanged()
    }

    fun onColorSelect(view: View){
        resetColors()
        when(view){
            c_blue -> { changeColor("blue") }
            c_green -> { changeColor("green") }
            c_red -> { changeColor("red") }
            c_orange -> { changeColor("orange") }
            c_purple -> { changeColor("purple") }
        }
    }

    private fun resetColors() {
        switchViewVisibility(c_green_chosen, c_green_unchosen)
        switchViewVisibility(c_blue_chosen, c_blue_unchosen)
        switchViewVisibility(c_purple_chosen, c_purple_unchosen)
        switchViewVisibility(c_red_chosen, c_red_unchosen)
        switchViewVisibility(c_orange_chosen, c_orange_unchosen)
    }

    private fun changeColor(color: String) {
        memberColor = color
        when(color){
            "green" -> { switchViewVisibility(c_green_unchosen, c_green_chosen) }
            "blue" -> { switchViewVisibility(c_blue_unchosen, c_blue_chosen) }
            "purple" -> { switchViewVisibility(c_purple_unchosen, c_purple_chosen) }
            "red" -> { switchViewVisibility(c_red_unchosen, c_red_chosen) }
            "orange" -> { switchViewVisibility(c_orange_unchosen, c_orange_chosen) }
        }
    }

    private fun switchViewVisibility(toGone: View, toVisible: View){
        toGone.visibility = View.GONE
        toVisible.visibility = View.VISIBLE
    }

    fun onAddBtnPressed(view: View){
        if(!blankName()){
            memberName = et_member_name.text.toString()
            val newMember = Member(name = memberName, color = memberColor)
            val result: Long = membersDB.create(newMember)
            if(result > 0){
                Log.i(Util.LOG_KEY, "insert into members database successful.\n(MembersActivity)")
            }else{
                Log.e(Util.LOG_KEY, "failure in adding member to database.\n(MembersActivity)")
            }
        }else{
            Toast.makeText(this, "Please insert a name.", Toast.LENGTH_LONG).show()
        }
    }

    private fun blankName() = et_member_name.text.isEmpty()

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}