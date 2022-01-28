package com.guilhermecallandprojects.projectmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.guilhermecallandprojects.projectmanager.model.Member
import com.guilhermecallandprojects.projectmanager.model.Task
import com.guilhermecallandprojects.projectmanager.utils.Util

class MembersDatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, "members_database", null, 1){

    private val tableMembers: String = "members_table"
    private val columnID: String = "member_id"
    private val columnName: String = "member_name"
    private val columnColor: String = "member_color"

    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("""CREATE TABLE IF NOT EXISTS $tableMembers ($columnID INTEGER PRIMARY KEY, $columnName TEXT, $columnColor TEXT)""")
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

    fun read() : ArrayList<Member>{
        val membersList = ArrayList<Member>()
        val query = """SELECT * FROM $tableMembers"""
        val db = this.readableDatabase
        val cursor : Cursor = db.rawQuery(query, null)
        try {
            if(cursor.moveToFirst()){
                do{
                    val idIndex = cursor.getColumnIndex(columnID)
                    val nameIndex = cursor.getColumnIndex(columnName)
                    val colorIndex = cursor.getColumnIndex(columnColor)

                    if(validIndexes(idIndex, nameIndex, colorIndex)){
                        val id = cursor.getInt(idIndex)
                        val name = cursor.getString(nameIndex)
                        val color = cursor.getString(colorIndex)
                        val member = Member(id, name, color)
                        membersList.add(member)
                    } else {
                        Log.e(Util.LOG_KEY, "error on reading column indexes.\n(MembersDatabaseHelper)")
                    }
                }while (cursor.moveToNext())
            }
        }catch(e: Exception){
            Log.e(Util.LOG_KEY, "error reading database.\n(MembersDatabaseHelper)")
        }
        Log.i(Util.LOG_KEY, "retrieved data from members database successfully.\n(MembersDatabaseHelper)")
        cursor.close()
        return membersList
    }

    fun update(member: Member) : Long{
        val values = ContentValues()
        values.put(columnName, member.name)
        values.put(columnColor, member.color)
        val result = writableDatabase.update(tableMembers, values, "$columnID=${member.id}", null).toLong()
        return result
    }

    fun delete(id : Int) : Int{
        val result = writableDatabase.delete(tableMembers, "$columnID=$id", null)
        return result
    }

    private fun validIndexes(
        idIndex: Int,
        nameIndex: Int,
        colorIndex: Int
    ) = idIndex >= 0 && nameIndex >= 0 && colorIndex >= 0


}