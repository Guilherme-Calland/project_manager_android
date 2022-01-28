package com.guilhermecallandprojects.projectmanager.screens

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.database.TodoDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    private var id: Int? = null
    private var info: String? = null
    private var responsible: String? = null
    private var addOrEdit: String = "Add"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setActionBarProperties()
        addButtonListen()

        val bundle: Bundle? = intent.extras
        if(bundle != null){
            addOrEdit = "Edit"
            btn_add.setText("Done")
            id = bundle.getInt("id")
            info = bundle.getString("info")
            responsible = bundle.getString("responsible")
            et_task.setText(info)
        }
        tv_title.text = "$addOrEdit task"
    }

    private fun addButtonListen() {
        btn_add.setOnClickListener {
            val task = Task(id= id)
            addTask(task)
        }
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addTask(task: Task){
        val todoDB = TodoDatabaseHelper(this)
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
            }else{
                Log.e(Util.LOG_KEY, "fail on database $addOrEdit.\n(AddTaskActivity)")
            }

            backToMainActivity()
        }else{
            Toast.makeText(this,"Please fill in all the information.", Toast.LENGTH_LONG).show()
        }
    }

    private fun backToMainActivity() {
        finish()
    }

    private fun blanckTask() = et_task.text.isEmpty()
}