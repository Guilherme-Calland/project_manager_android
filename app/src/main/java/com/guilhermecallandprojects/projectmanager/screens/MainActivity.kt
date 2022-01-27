package com.guilhermecallandprojects.projectmanager.screens

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import android.view.inputmethod.EditorInfo
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.TodoTasksAdapter
import com.guilhermecallandprojects.projectmanager.database.TodoDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var todoTasks = ArrayList<Task>()
    private val doingTasks = ArrayList<Task>()
    private val doneTasks = ArrayList<Task>()
    private var todoTasksAdapter = TodoTasksAdapter(todoTasks)
    private lateinit var todoDB :TodoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarProperties()
        rv_todo_list.adapter = todoTasksAdapter
        todoTasks.add(Task(info = "fazer isso", responsible = "joao"))
        todoTasks.add(Task(info = "depois isso", responsible = "pedro"))
        todoTasks.add(Task(info = "depois refaça", responsible = "ricardo"))
        todoTasks.add(Task(info = "depois jogue fora", responsible ="joao"))

        todoDB = TodoDatabaseHelper(this)
        readFromDatabase()
    }

    private fun readFromDatabase() {
        todoTasks = todoDB.read()
        for(t in todoTasks){
            Log.i("testing", t.info)
        }
        todoTasksAdapter.notifyDataSetChanged()
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sv = menu.findItem(R.id.i_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        disableFullscreen(sv)
        setQueryListeners(sv)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setQueryListeners(sv: SearchView) {
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun disableFullscreen(sv: SearchView) {
        sv.setImeOptions(sv.getImeOptions() or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.itemId){
            R.id.i_add -> goToAddTask()
            R.id.i_members -> goToMembers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToAddTask() {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }

    private fun goToMembers(){
        val intent = Intent(this, MembersActivity::class.java)
        startActivity(intent)
    }

}