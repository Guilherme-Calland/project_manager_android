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
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setActionBarProperties()
        addButtonListen()
    }

    private fun addButtonListen() {
        btn_add.setOnClickListener {
            addTask()
        }
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addTask(){
        val todoDB = TodoDatabaseHelper(this)
        if(!titleBlank()){
            val newTask = Task(inputTask(), "joao")
            val id = todoDB.create(newTask)
            if(id > 0){
                Log.i("projectmanagerapp", "success on adding to database")
            }else{
                Log.i("projectmanagerapp", "fail on adding to database")
            }
        }else{
            Toast.makeText(this,"Please fill in all the information.", Toast.LENGTH_LONG).show()
        }
        backToMainActivity()
    }

    private fun backToMainActivity() {
        finish()
    }

    private fun inputTask() = et_task.text.toString()
    private fun titleBlank() = et_task.text.isEmpty()
}