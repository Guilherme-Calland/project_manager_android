package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util

class TodoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "todo_database", null, 1) {

    private val tableTodo: String = "todo_tasks_table"
    private val columnID: String = "todo_task_id"
    private val columnInfo: String = "todo_task_info"
    private val columnResponsible: String = "todo_task_responsible_member"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS $tableTodo ($columnID INTEGER PRIMARY KEY, $columnInfo TEXT, $columnResponsible TEXT)""")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("""DROP TABLE IF EXISTS $tableTodo""")
        onCreate(db)
    }

    fun create(task: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(columnInfo, task.info)
        contentValues.put(columnResponsible, task.responsible)
        val result = db.insert(tableTodo, null, contentValues)
        db.close()
        return result;
    }

    fun read(queryArg: String = "%") : ArrayList<Task>{
        val taskList = ArrayList<Task>()
        val query =
            """SELECT * FROM $tableTodo WHERE $columnInfo LIKE '%$queryArg%' OR $columnResponsible LIKE '%$queryArg%'"""
        val db = this.readableDatabase
        val cursor : Cursor = db.rawQuery(query, null)
        try{
            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(columnID)
                    val infoIndex = cursor.getColumnIndex(columnInfo)
                    val resIndex = cursor.getColumnIndex(columnResponsible)

                    if (validIndexes(idIndex, infoIndex, resIndex)) {
                        val id = cursor.getInt(idIndex)
                        val info = cursor.getString(infoIndex)
                        val responsible = cursor.getString(resIndex)
                        val newTask = Task(id = id, info, responsible)
                        taskList.add(newTask)
                    } else {
                        Log.e(Util.LOG_KEY, "error on reading column indexes.\n(TodoDatabaseHelper)")
                    }
                } while (cursor.moveToNext())
            }
        } catch(e: Exception) {
            Log.e(Util.LOG_KEY, "reading database error.\n(TodoDatabaseHelper)")
        }

        Log.i(Util.LOG_KEY,"retrieved data from todo database successfully.\n(TodoDatabaseHelper)")
        cursor.close()
        return taskList
    }

    fun update(task: Task):  Long{
        val values = ContentValues()
        values.put(columnInfo, task.info)
        values.put(columnResponsible, task.responsible)
        val result = this.writableDatabase.update(tableTodo, values,"$columnID=${task.id}", null).toLong()
        Log.i("testing", "$result")
        return result
    }

    fun delete(id: Int) : Int{
        val result = this.writableDatabase.delete(tableTodo, "$columnID=$id", null)
        return result
    }

    private fun validIndexes(
        idIndex: Int,
        infoIndex: Int,
        resIndex: Int
    ) = idIndex >= 0 && infoIndex >= 0 && resIndex >= 0


}