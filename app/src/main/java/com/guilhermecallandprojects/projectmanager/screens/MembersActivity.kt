package com.guilhermecallandprojects.projectmanager.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.setPadding
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.MemberAdapter
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.member.*
import android.widget.LinearLayout




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
        setNameEditTextListeners()
    }

    private fun setNameEditTextListeners() {
        disableFullscreen(et_member_name)

        et_member_name.setOnClickListener{
            ll_membersList.visibility = GONE
            btn_add.visibility = GONE
            btn_add_h.visibility = VISIBLE
            btn_back.visibility = VISIBLE
            ll_members_activity.gravity = Gravity.CENTER_HORIZONTAL
            tv_new_member_title.visibility = GONE
        }

        et_member_name.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ll_membersList.visibility = GONE
                btn_add.visibility = GONE
                btn_add_h.visibility = VISIBLE
                btn_back.visibility = VISIBLE
                ll_members_activity.gravity = Gravity.CENTER_HORIZONTAL
                tv_new_member_title.visibility = GONE
            }
        }

        et_member_name.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                ll_membersList.visibility = GONE
                btn_add.visibility = GONE
                btn_add_h.visibility = VISIBLE
                btn_back.visibility = VISIBLE
                ll_members_activity.gravity = Gravity.CENTER_HORIZONTAL
                tv_new_member_title.visibility = GONE
            }
        }
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

    private fun disableFullscreen(view: EditText) {
        view.setImeOptions(view.getImeOptions() or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN)
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
        toGone.visibility = GONE
        toVisible.visibility = VISIBLE
    }

    fun onAddBtnPressed(view: View){
        if(!blankName()){
            memberName = et_member_name.text.toString()
            val newMember = Member(name = memberName, color = memberColor)
            val result: Long = membersDB.create(newMember)
            if(result > 0){
                Log.i(Util.LOG_KEY, "insert into members database successful.\n(MembersActivity)")
                Toast.makeText(this, "$memberName was added as a member.", Toast.LENGTH_SHORT).show()
                et_member_name.text.clear()
                readFromDatabase()
            }else{
                Log.e(Util.LOG_KEY, "failure in adding member to database.\n(MembersActivity)")
            }
        }else{
            Toast.makeText(this, "Please insert a name.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onBackBtnPressed(View: View){
        ll_membersList.visibility = VISIBLE
        btn_add.visibility = VISIBLE
        tv_new_member_title.visibility = VISIBLE
        btn_add_h.visibility = GONE
        btn_back.visibility = GONE
        ll_members_activity.gravity = Gravity.CENTER
        hideKeyboard()
    }

    fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    private fun blankName() = et_member_name.text.isEmpty()

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}