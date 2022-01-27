package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
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
        db!!.execSQL("CREATE TABLE IF NOT EXISTS $table_todo ($column_id INTEGER PRIMARY KEY, $column_info TEXT, $column_responsible TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $table_todo")
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


}