package com.guilhermecallandprojects.projectmanager.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.setPadding
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.model.Person
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_members.*

class MembersActivity : AppCompatActivity() {
    private lateinit var membersList: ArrayList<Member>
    private lateinit var membersDB: MembersDatabaseHelper
    private lateinit var memberName: String
    private lateinit var memberColor: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        setActionBarProperties()
        membersList = ArrayList()
        membersDB = MembersDatabaseHelper(this)
        memberColor = "green"
        c_green.layoutParams.width = 20
        c_green.layoutParams.height = 20
    }

    fun onColorSelect(view: View){
        when(view.id){
            R.id.c_blue -> memberColor = "blue"
            R.id.c_green -> memberColor = "green"
            R.id.c_red -> memberColor = "red"
            R.id.c_orange -> memberColor = "orange"
            R.id.c_purple -> memberColor = "purple"
        }
        view.setPadding(5)
    }

    fun onAddBtnPressed(view: View){
        if(!blankName()){
            memberName = et_member_name.text.toString()
            val newMember = Member(name = memberName, color = memberColor)
            val result: Long = membersDB.create(newMember)
            if(result > 0){
                Log.i(Util.LOG_KEY, "insert into members database successful. (MembersActivity)")
            }else{
                Log.e(Util.LOG_KEY, "failure in adding member to database. (MembersActivity)")
            }
            finish()
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