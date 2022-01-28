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
import com.guilhermecallandprojects.projectmanager.database.TodoDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.member.*

class AddTaskActivity : AppCompatActivity() {

    private var id: Int? = null
    private var info: String? = null
    private var responsible: String? = null
    private var addOrEdit: String = "Add"
    private lateinit var todoDB: TodoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setActionBarProperties()
        setButtonListeners()
        retrieveBundle()
        setTaskEditTextListeners()
        todoDB = TodoDatabaseHelper(this)
        tv_title.text = "$addOrEdit task"

    }

    private fun setTaskEditTextListeners() {
        Util.disableFullscreen(editText = et_task)
        et_task.setOnClickListener {
            adaptLayoutToKeyboard()
        }

        et_task.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                adaptLayoutToKeyboard()
            }
        }
    }

    private fun adaptLayoutToKeyboard() {
        tv_title.visibility = GONE
        btn_add.visibility = GONE
        ll_responsible_member.visibility = GONE
        ll_add_task_layout.gravity = Gravity.CENTER_HORIZONTAL
        btn_back.visibility = VISIBLE
        btn_add_h.visibility = VISIBLE
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    private fun retrieveBundle() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            addOrEdit = "Edit"
            btn_add.setText("Done")
            id = bundle.getInt("id")
            info = bundle.getString("info")
            responsible = bundle.getString("responsible")
            et_task.setText(info)
        }
    }

    private fun setButtonListeners() {
        val task = Task(id= id)

        btn_add.setOnClickListener {
            addTask(task)
        }

        btn_add_h.setOnClickListener{
            addTask(task)
        }

        btn_back.setOnClickListener {
            resetLayout()
        }
    }

    private fun resetLayout() {
        tv_title.visibility = VISIBLE
        btn_add.visibility = VISIBLE
        ll_responsible_member.visibility = VISIBLE
        ll_add_task_layout.gravity = Gravity.CENTER
        btn_back.visibility = GONE
        btn_add_h.visibility = GONE
        hideKeyboard()
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addTask(task: Task){
        val info = et_task.text.toString()

        if(!blanckTask()){
            var result: Long = 0
            if(addOrEdit=="Add"){
                val newTask = Task(info = info, responsible = "")
                result = todoDB.create(newTask)
            }else if(addOrEdit=="Edit"){
                task.info = info
                task.responsible = "ricardo"
                result = todoDB.update(task)
            }

            if(result > 0){
                Log.i(Util.LOG_KEY, "$addOrEdit to database successful.\n(AddTaskActivity)")
                Toast.makeText(this, "New task added.", Toast.LENGTH_SHORT).show()
                et_task.text.clear()
            }else{
                Log.e(Util.LOG_KEY, "fail on database $addOrEdit.\n(AddTaskActivity)")
            }
        }else{
            Toast.makeText(this,"Please fill in all the information.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun blanckTask() = et_task.text.isEmpty()
}