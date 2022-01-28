package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.guilhermecallandprojects.projectmanager.model.Member

class MembersDatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, "members_database", null, 1){

    private val tableMembers: String = "members_table"
    private val columnID: String = "member_id"
    private val columnName: String = "member_name"
    private val columnColor: String = "member_color"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS $tableMembers ($columnID INTEGER PRIMARY KEY, $columnName TEXT, $columnColor TEXT)""")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("""DROP TABLE IF EXISTS $tableMembers""")
    }

    fun create(member: Member): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(columnName, member.name)
        contentValues.put(columnColor, member.color)
        val result = db.insert(tableMembers, null, contentValues)
        db.close()
        return result
    }


}