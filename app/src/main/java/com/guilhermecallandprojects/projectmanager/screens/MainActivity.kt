package com.guilhermecallandprojects.projectmanager.screens

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.TodoTasksAdapter
import com.guilhermecallandprojects.projectmanager.database.TodoDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.task.*

class MainActivity : AppCompatActivity() {
    private lateinit var todoTasks: ArrayList<Task>
    private lateinit var doingTasks: ArrayList<Task>
    private lateinit var doneTasks: ArrayList<Task>
    private lateinit var todoTasksAdapter : TodoTasksAdapter
    private lateinit var todoDB :TodoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarProperties()
        todoTasks = ArrayList()
        todoTasksAdapter = TodoTasksAdapter(todoTasks)
        rv_todo_list.adapter = todoTasksAdapter
        todoTasksAdapter.setOnPressedListener(OnPressedConcreteClass())
        todoDB = TodoDatabaseHelper(this)
        readFromDatabase()
    }

    override fun onResume() {
        readFromDatabase()
        super.onResume()
    }

    inner class OnPressedConcreteClass : TodoTasksAdapter.OnPressedInterface{
        override fun onDelete(position: Int, model: Task){
            deleteTask(model)
            readFromDatabase()
        }

        override fun onEdit(position: Int, model: Task) {
            goToAddTask(model)
            readFromDatabase()
        }
    }

    private fun deleteTask(model: Task) {
        if (model.id != null) {
            val result: Int = todoDB.delete(model.id)
            if (result > 0) {
                Log.i(Util.LOG_KEY, "element was deleted successfully!\n(MainActivity)")
            } else {
                Log.e(Util.LOG_KEY, "error on deleting the element.\n(MainActivity)")
            }
        } else {
            Log.e(Util.LOG_KEY, "was passed a null id for deletion.\n(MainActivity)")
        }
    }

    private fun readFromDatabase(query: String = "%") {
        todoTasks.clear()
        val todoTasksTemp = todoDB.read(query)
        for(t in todoTasksTemp){
            todoTasks.add(t)
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
        Util.disableFullscreen(searchView = sv)
        setQueryListeners(sv)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setQueryListeners(sv: SearchView) {
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                readFromDatabase(query)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.itemId){
            R.id.i_add -> goToAddTask()
            R.id.i_members -> goToMembers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToAddTask(task: Task? = null) {
        val intent = Intent(this, AddTaskActivity::class.java)

        if(task!=null){
            intent.putExtra("id", task.id)
            intent.putExtra("info", task.info)
            intent.putExtra("responsible", task.responsible)
        }

        startActivity(intent)
    }

    private fun goToMembers(){
        val intent = Intent(this, MembersActivity::class.java)
        startActivity(intent)
    }

}