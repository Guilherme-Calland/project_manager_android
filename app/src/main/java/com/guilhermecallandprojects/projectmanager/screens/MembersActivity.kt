package com.guilhermecallandprojects.projectmanager.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.MemberAdapter
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.task.*

class MembersActivity : AppCompatActivity() {
    private lateinit var membersList: ArrayList<Member>
    private lateinit var membersDB: MembersDatabaseHelper
    private lateinit var memberAdapter: MemberAdapter

    private var oldMember: Member? = null
    private var editedMember: Member? = null
    private var currentColor: String? = "green"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        setActionBarProperties()
        initializeMemberList()
        setNameEditTextListeners()
        setButtonListeners()
    }

    private fun setButtonListeners() {
        btn_add_h.setOnClickListener { addMember() }
        btn_add.setOnClickListener { addMember() }
        arrow_back.setOnClickListener { resetLayout() }
        btn_back_h.setOnClickListener { finish() }
    }

    private fun setToAddMode() {
        tv_new_member_title.setText(R.string.activity_members_add_title)
        btn_add.setText(R.string.button_add)
        btn_add_h.setText(R.string.button_done)
        arrow_back.visibility = GONE
        et_member_name.text.clear()
        editedMember = null
    }

    private fun addMember() {
        if (!blankName()) {
            val currentName: String = et_member_name.text.toString()
            var result: Long = 0
            if(isNewMember()){
                val newMember = Member(name = currentName, color = currentColor)
                val sameNameQuery: ArrayList<Member> = membersDB.read(currentName)
                if(sameNameQuery.isEmpty()){
                    result = membersDB.create(newMember)
                }else{
                    Toast.makeText(this, "This project already has a member with this name.", Toast.LENGTH_SHORT).show()
                }
            }else{
                editedMember?.name = currentName
                editedMember?.color = currentColor
                val sameNameQuery: ArrayList<Member> = membersDB.read(currentName)
                if(sameNameQuery.isEmpty() || currentName == oldMember?.name){
                    //TODO: this not working
                    Log.i("testing", "${ oldMember!!.name }")
                    result = membersDB.update(editedMember!!)
                }else{
                    Toast.makeText(this, "This project already has a member with this name.", Toast.LENGTH_SHORT).show()
                }
            }

            if (result > 0) {
                if(isNewMember()){
                    Log.i(Util.LOG_KEY, "insert into members database successful.\n(MembersActivity)")
                    Toast.makeText(this, "$currentName was added as a member.", Toast.LENGTH_SHORT)
                        .show()
                    et_member_name.text.clear()
                }else{
                    Log.i(Util.LOG_KEY, "updated member successful.\n(MembersActivity)")
                    resetLayout()
                    setToAddMode()
                }
                readFromDatabase()
            } else {
                if(isNewMember()) {
                    Log.e(Util.LOG_KEY, "failure in adding member to database.\n(MembersActivity)")
                }else{
                    Log.e(Util.LOG_KEY, "failure in editing member to database.\n(MembersActivity)")

                }
            }
        } else {
            Toast.makeText(this, "Please insert a name.", Toast.LENGTH_SHORT).show()
        }
        memberAdapter.resetIconViews()
    }


    private fun isNewMember() = editedMember == null

    private fun setNameEditTextListeners() {
        Util.disableFullscreen(editText = et_member_name)
        et_member_name.setOnClickListener{ ajustLayoutForTextInput() }
        et_member_name.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { ajustLayoutForTextInput() }
        }
    }

    private fun ajustLayoutForTextInput() {
        ll_membersList.visibility = GONE
        ll_buttons_v.visibility = GONE
        ll_buttons_h.visibility = VISIBLE
        arrow_back.visibility = VISIBLE
        ll_members_activity.gravity = Gravity.CENTER_HORIZONTAL
        tv_new_member_title.visibility = GONE
    }

    inner class OnPressedConcreteClass : MemberAdapter.OnPressedInterface{
        override fun onDelete(position: Int, model: Member) {
            deleteMember(model)
            readFromDatabase()
            memberAdapter.resetIconViews()
            showMembersIfNotEmpty()
        }

        override fun onEdit(position: Int, member: Member) {
            setToEditMode(member)
        }
    }

    private fun setToEditMode(member: Member){
        oldMember = member
        changeColor(member.color)
        editedMember = member
        tv_new_member_title.setText(R.string.activity_members_edit_title)
        btn_add.setText(R.string.button_done)
        btn_add_h.setText(R.string.button_done)
        btn_back_h.visibility = VISIBLE
        et_member_name.setText(member.name)
        memberAdapter.resetIconViews()
    }

    private fun deleteMember(model: Member) {
        if (model.id != null) {
            val result: Int = membersDB.delete(model.id)
            if (result > 0) {
                Log.i(Util.LOG_KEY, "element was deleted successfully.\n(MembersActivity)")
            } else {
                Log.e(Util.LOG_KEY, "error on deleting the element.\n(MemberActivity)")
            }
        } else {
            Log.e(
                Util.LOG_KEY,
                "reading a null id while trying to delete on database.\n(MembersActivity)"
            )
        }
    }

    private fun initializeMemberList() {
        membersList = ArrayList()
        membersDB = MembersDatabaseHelper(this)
        memberAdapter = MemberAdapter(this, membersList)
        rv_members_list.adapter = memberAdapter
        memberAdapter.setOnPressedObject( OnPressedConcreteClass() )
        readFromDatabase()
        showMembersIfNotEmpty()
    }

    private fun showMembersIfNotEmpty() {
        if (membersList.isNotEmpty()) {
            ll_membersList.visibility = VISIBLE
        } else {
            ll_membersList.visibility = GONE
        }
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
        memberAdapter.resetIconViews()
    }

    private fun resetColors() {
        switchViewVisibility(c_green_chosen, c_green_unchosen)
        switchViewVisibility(c_blue_chosen, c_blue_unchosen)
        switchViewVisibility(c_purple_chosen, c_purple_unchosen)
        switchViewVisibility(c_red_chosen, c_red_unchosen)
        switchViewVisibility(c_orange_chosen, c_orange_unchosen)
    }

    private fun changeColor(color: String?) {
        resetColors()
        currentColor = color
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

    private fun resetLayout(){
        ll_membersList.visibility = VISIBLE
        ll_buttons_v.visibility = VISIBLE
        tv_new_member_title.visibility = VISIBLE
        ll_buttons_h.visibility = GONE
        arrow_back.visibility = GONE
        ll_members_activity.gravity = Gravity.CENTER
        memberAdapter.resetIconViews()
        showMembersIfNotEmpty()
        hideKeyboard()
    }

    private fun hideKeyboard(){
        if(currentFocus != null){
            val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun blankName() = et_member_name.text.isNullOrEmpty()

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}