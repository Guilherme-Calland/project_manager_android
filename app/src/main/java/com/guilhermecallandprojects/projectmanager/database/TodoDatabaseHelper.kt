package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
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

    fun read(queryArg: String = "%") : ArrayList<Task>{
        val taskList = ArrayList<Task>()
        val query = "SELECT * FROM $table_todo WHERE $column_info LIKE '%$queryArg%' OR $column_responsible LIKE '%$queryArg%'"
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
                        Log.e("projectmanagerapp", "error on reading column indexes. (TodoDatabaseHelper)")
                    }
                } while (cursor.moveToNext())
            }
        } catch(e: Exception) {
            Log.e("projectmanagerapp", "reading database error. (TodoDatabaseHelper)")
        }

        Log.i("projectmanagerapp","retrieved data from database successfully. (TodoDatabaseHelper)")
        cursor.close()
        return taskList
    }

    fun update(task: Task): Long{
        val values = ContentValues()
        values.put(column_info, task.info)
        values.put(column_responsible, task.responsible)
        val result = writableDatabase.update(table_todo, values,"id=${task.id}", null).toLong()
        return result
    }

    fun delete(id: Int) : Int{
        val result = writableDatabase.delete(table_todo, "id=$id", null)
        return result
    }

    private fun validIndexes(
        idIndex: Int,
        infoIndex: Int,
        resIndex: Int
    ) = idIndex >= 0 && infoIndex >= 0 && resIndex >= 0


}