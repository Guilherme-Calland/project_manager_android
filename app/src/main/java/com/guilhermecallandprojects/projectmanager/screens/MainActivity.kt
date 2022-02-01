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
import com.guilhermecallandprojects.projectmanager.R
import com.guilhermecallandprojects.projectmanager.adapters.AdapterBrain as ab
import com.guilhermecallandprojects.projectmanager.adapters.TaskAdapter
import com.guilhermecallandprojects.projectmanager.database.DatabaseBrain as dbb
import com.guilhermecallandprojects.projectmanager.database.TaskDatabaseHelper
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //lists
    private lateinit var todoTasks : ArrayList<Task>
    private lateinit var doingTasks: ArrayList<Task>
    private lateinit var doneTasks : ArrayList<Task>

    //adapters
    private lateinit var todoAdapter : TaskAdapter
    private lateinit var doingAdapter: TaskAdapter
    private lateinit var doneAdapter : TaskAdapter

    //databases
    private lateinit var todoDB : TaskDatabaseHelper
    private lateinit var doingDB: TaskDatabaseHelper
    private lateinit var doneDB : TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBarProperties()
        initializeLists()
    }

    private fun initializeLists() {
        //lists
        todoTasks  = ArrayList()
        doingTasks = ArrayList()
        doneTasks  = ArrayList()

        //adapters
        todoAdapter  = TaskAdapter(this, todoTasks, ab.TODO_ADAPTER_ID)
        todoAdapter.setOnPressedObject( OnPressedConcreteClass() )
        rv_todo.adapter = todoAdapter

        doingAdapter = TaskAdapter(this, doingTasks, ab.DOING_ADAPTER_ID)
        doingAdapter.setOnPressedObject( OnPressedConcreteClass() )
        rv_doing.adapter = doingAdapter

        doneAdapter  = TaskAdapter(this, doneTasks, ab.DONE_ADAPTER_ID)
        doingAdapter.setOnPressedObject( OnPressedConcreteClass() )
        rv_done.adapter = doneAdapter

        //databases
        todoDB  = TaskDatabaseHelper(this, dbb.TODO_DATABASE_NAME)
        doingDB = TaskDatabaseHelper(this, dbb.DOING_DATABASE_NAME)
        doneDB  = TaskDatabaseHelper(this, dbb.DONE_DATABASE_NAME)
        readFromDatabase()
    }

    override fun onResume() {
        readFromDatabase()
        todoAdapter.resetIconViews()
        super.onResume()
    }

    inner class OnPressedConcreteClass : TaskAdapter.OnPressedInterface{
        override fun onDelete(taskID: Int, adapterID: Int){
            val db: TaskDatabaseHelper? = when(adapterID) {
                ab.TODO_ADAPTER_ID -> todoDB
                ab.DOING_ADAPTER_ID -> doingDB
                ab.DONE_ADAPTER_ID -> doneDB
                else -> null
            }
            if(db!=null){
                deleteTask(taskID, db)
            } else {
                Log.e("projectmanagerapp", "null database was passed to delete item.\n(MainActivity)")
            }
            readFromDatabase(adapterID = adapterID)
        }

        override fun onEdit(model: Task, adapterID: Int) {
            goToAddTask(model, adapterID)
            readFromDatabase(adapterID = adapterID)
        }

        override fun onNext(task: Task, adapterID: Int) {
            when(adapterID){
                //TODO: delete item and create in next database
            }
        }
    }

    private fun deleteTask(taskID: Int, db: TaskDatabaseHelper) {
        val result: Int = db.delete(taskID)
        if (result > 0) {
            Log.i(Util.LOG_KEY, "element was deleted successfully!\n(MainActivity)")
            readFromDatabase()
        } else {
            Log.e(Util.LOG_KEY, "error on deleting the element.\n(MainActivity)")
        }
    }

    private fun readFromDatabase(query: String = "%", adapterID: Int? = null) {
        if(readingForAll(adapterID)){
            reloadLists(query)
            notifyAdapters()
        }else{
            reloadList(query, adapterID!!)
        }
    }

    private fun readingForAll(adapterID: Int?) = adapterID == null

    private fun reloadList(query: String, adapterID: Int){
        when(adapterID){
            ab.TODO_ADAPTER_ID -> {
                todoTasks.clear()
                val tasksTemp = todoDB.read(query)
                for (t in tasksTemp) { todoTasks.add(t) }
                todoAdapter.notifyDataSetChanged()
            }

            ab.DOING_ADAPTER_ID -> {
                doingTasks.clear()
                val tasksTemp = doingDB.read(query)
                for(t in tasksTemp){ doingTasks.add(t) }
                todoAdapter.notifyDataSetChanged()
            }

            ab.DONE_ADAPTER_ID -> {
                doneTasks.clear()
                val tasksTemp = doingDB.read(query)
                for(t in tasksTemp){ doneTasks.add(t) }
                todoAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun reloadLists(query: String) {
        todoTasks.clear()
        val todoTasksTemp = todoDB.read(query)
        for (t in todoTasksTemp) {
            todoTasks.add(t)
        }

        doingTasks.clear()
        val doingTasksTemp = doingDB.read(query)
        for (t in doingTasksTemp) {
            doingTasks.add(t)
        }

        doneTasks.clear()
        val doneTasksTemp = doneDB.read(query)
        for (t in doneTasksTemp) {
            doneTasks.add(t)
        }
    }

    private fun notifyAdapters() {
        todoAdapter.notifyDataSetChanged()
        doingAdapter.notifyDataSetChanged()
        doneAdapter.notifyDataSetChanged()
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

    private fun goToAddTask(task: Task? = null, adapterID: Int?=null) {
        val intent = Intent(this, AddTaskActivity::class.java)

        if(editingTask(task, adapterID)){
            intent.putExtra("id", task!!.id)
            intent.putExtra("info", task.info)
            intent.putExtra("responsible", task.responsible)
            val databaseName: String? = when(adapterID){
                ab.TODO_ADAPTER_ID -> dbb.TODO_DATABASE_NAME
                ab.DOING_ADAPTER_ID -> dbb.DOING_DATABASE_NAME
                ab.DONE_ADAPTER_ID -> dbb.DONE_DATABASE_NAME
                else -> null
            }
            intent.putExtra("databaseName", databaseName)
        }
        
        startActivity(intent)
    }

    private fun editingTask(
        task: Task?,
        adapterID: Int?
    ) = task != null && adapterID != null

    private fun goToMembers(){
        val intent = Intent(this, MembersActivity::class.java)
        startActivity(intent)
    }

}