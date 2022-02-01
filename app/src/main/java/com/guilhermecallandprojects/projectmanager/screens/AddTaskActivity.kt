package com.guilhermecallandprojects.projectmanager.screens

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.ResponsibleMemberAdapter
import com.guilhermecallandprojects.projectmanager.database.DatabaseBrain as dbb
import com.guilhermecallandprojects.projectmanager.database.MembersDatabaseHelper
import com.guilhermecallandprojects.projectmanager.database.TaskDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.member.*

class AddTaskActivity : AppCompatActivity() {
    private var idFromBundle: Int? = null
    private var infoFromBundle: String? = null
    private var responsibleNameFromBundle: String? = null
    private var databaseNameFromBundle: String? = null
    private var currentResponsible: Member? = null

    private lateinit var taskDB: TaskDatabaseHelper
    private lateinit var membersDB: MembersDatabaseHelper
    private lateinit var memberList: ArrayList<Member>
    private lateinit var responsibleMemberAdapter: ResponsibleMemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setActionBarProperties()
        setButtonListeners()
        setTaskEditTextListeners()
        initializeMembersList()
        retrieveBundle()
    }

    private fun initializeMembersList() {
        membersDB = MembersDatabaseHelper(this)
        memberList = ArrayList()
        responsibleMemberAdapter = ResponsibleMemberAdapter(this, memberList)
        rv_responsible_list.adapter = responsibleMemberAdapter
        responsibleMemberAdapter.setOnPressedObject(OnPressedConcreteClass())
        readFromMembersDatabase()
    }

    inner class OnPressedConcreteClass : ResponsibleMemberAdapter.OnPressedInterface {
        override fun onPressedMember(member: Member) {
            hideMembersAndShowResponsible(member)
        }

        override fun onPressedRemove() {
            hideMembersAndRemoveResponsible()
        }
    }

    private fun hideMembersAndRemoveResponsible() {
        ll_responsible.visibility = VISIBLE
        ll_responsible_list.visibility = GONE
        tv_responsible.setText(R.string.symbol_add)
        tv_responsible.setTextColor(resources.getColor(R.color.light_gray))
        currentResponsible = null
    }

    private fun hideMembersAndShowResponsible(member: Member) {
        ll_responsible.visibility = VISIBLE
        ll_responsible_list.visibility = GONE
        tv_responsible.text = member.name
        val color = Util.nameToColor(member.color)
        if (color != null) {
            tv_responsible.setTextColor(resources.getColor(color))
        }
        currentResponsible = member
    }

    private fun readFromMembersDatabase() {
        val membersTemp = membersDB.read()
        memberList.clear()
        for (m in membersTemp) {
            memberList.add(m)
        }

        val removeElement = Member()
        memberList.add(0, removeElement)
        responsibleMemberAdapter.notifyDataSetChanged()
    }

    private fun setTaskEditTextListeners() {
        Util.disableFullscreen(editText = et_task)
        et_task.setOnClickListener { adaptLayoutForTextInput() }
        et_task.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                adaptLayoutForTextInput()
            }
        }
    }

    private fun adaptLayoutForTextInput() {
        tv_title.visibility = GONE
        btn_add.visibility = GONE
        ll_add_task_layout.gravity = Gravity.CENTER_HORIZONTAL
        ll_buttons_h.visibility = VISIBLE
        arrow_back.visibility = VISIBLE
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun retrieveBundle() {
        val bundle: Bundle? = intent.extras
        if (editingTask(bundle)) {
            tv_title.setText(R.string.activity_add_task_edit_task_title)
            btn_add.setText(R.string.button_done)
            btn_add_h.setText(R.string.button_done)
            idFromBundle = bundle!!.getInt("id")
            infoFromBundle = bundle.getString("info")
            responsibleNameFromBundle = bundle.getString("responsible")
            databaseNameFromBundle = bundle.getString("databaseName")
            taskDB = TaskDatabaseHelper(this, databaseNameFromBundle ?: dbb.TODO_DATABASE_NAME)
            et_task.setText(infoFromBundle)

            if(responsibleNameFromBundle != null){
                currentResponsible = membersDB.fetchMember(responsibleNameFromBundle!!)
                if(currentResponsible != null){
                    tv_responsible.text = currentResponsible!!.name
                    val color = Util.nameToColor(currentResponsible!!.color)
                    if (color != null) {
                        tv_responsible.setTextColor(resources.getColor(color))
                    }else{
                        Log.e(Util.LOG_KEY, "color value given was null.\n(AddTaskActivity)")
                    }
                }
            }
        } else {
            taskDB = TaskDatabaseHelper(this, dbb.TODO_DATABASE_NAME)
        }
    }

    private fun editingTask(bundle: Bundle?) = bundle != null

    private fun setButtonListeners() {
        btn_add.setOnClickListener { addTask() }
        btn_add_h.setOnClickListener { addTask() }
        arrow_back.setOnClickListener { resetLayout() }
        btn_back_h.setOnClickListener { finish() }
        tv_responsible.setOnClickListener { showMembersList() }
    }

    private fun showMembersList() {
        if (memberList.size > 1) {
            ll_responsible.visibility = GONE
            ll_responsible_list.visibility = VISIBLE
        } else {
            Toast.makeText(this, "This project has no members.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun resetLayout() {
        tv_title.visibility = VISIBLE
        btn_add.visibility = VISIBLE
        ll_buttons_h.visibility = GONE
        arrow_back.visibility = GONE
        ll_add_task_layout.gravity = Gravity.CENTER
        hideKeyboard()
    }

    private fun addTask() {
        val info = et_task.text.toString()
        if (!blankInput()) {
            val result: Long
            if (addingTask()) {
                val newTask = Task(info = info, responsible = currentResponsible?.name ?: "")
                result = taskDB.create(newTask)
                if (result > 0) {
                    Toast.makeText(this, "New task added.", Toast.LENGTH_SHORT).show()
                    et_task.text.clear()
                } else {
                    Log.e(Util.LOG_KEY, "failed to add to database.\n(AddTaskActivity)")
                }
            } else if (editingTask()) {
                val task = Task(idFromBundle, info, currentResponsible?.name ?: "")
                result = taskDB.update(task)
                if (result > 0) {
                } else {
                    Log.e(Util.LOG_KEY, "failed to update task.\n(AddTaskActivity)")
                }
                finish()
            }
        } else {
            Toast.makeText(this, "Please fill in all the information.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addingTask() = idFromBundle == null
    private fun editingTask() = idFromBundle != null
    private fun blankInput() = et_task.text.isEmpty()
}