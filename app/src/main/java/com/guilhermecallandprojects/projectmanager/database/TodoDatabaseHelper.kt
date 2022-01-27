package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.guilhermecallandprojects.projectmanager.model.Task

class TodoDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DatabaseBrain.DATABASE_NAME, null, DatabaseBrain.DATABASE_VERSION) {

    val table_todo: String = "todo_table"
    val column_id: String = "id"
    val column_info: String = "task_info"
    val column_responsible: String = "responsible"

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("""CREATE TABLE IF NOT EXISTS $table_todo ($column_id INTEGER PRIMARY KEY, $column_info TEXT, $column_responsible TEXT)""")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("""DROP TABLE IF EXISTS $table_todo""")
        onCreate(db)
    }

    fun create(task: Task): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(column_info, task.info)
        contentValues.put(column_responsible, task.responsible)
        val result = db.insert(table_todo, null, contentValues)
        db.close()
        return result;
    }

    fun read() : ArrayList<Task>{
        val taskList = ArrayList<Task>()
        val query = "SELECT * FROM $table_todo"
        var db = this.readableDatabase
        val cursor : Cursor = db.rawQuery(query, null)

        try{
            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(column_id)
                    val infoIndex = cursor.getColumnIndex(column_info)
                    val resIndex = cursor.getColumnIndex(column_responsible)

                    if (validIndexes(idIndex, infoIndex, resIndex)) {
                        val id = cursor.getInt(idIndex)
                        val info = cursor.getString(infoIndex)
                        val responsible = cursor.getString(resIndex)
                        val newTask = Task(id = id, info, responsible)
                        taskList.add(newTask)
                    } else {
                        Log.i("projectmanagerapp", "error on reading column indexes.")
                    }
                } while (cursor.moveToNext())
            }
        } catch(e: Exception) {
            Log.i("projectmanagerapp", "reading database error")
        }
        cursor.close()
        return taskList
    }

    private fun validIndexes(
        idIndex: Int,
        infoIndex: Int,
        resIndex: Int
    ) = idIndex >= 0 && infoIndex >= 0 && resIndex >= 0


}