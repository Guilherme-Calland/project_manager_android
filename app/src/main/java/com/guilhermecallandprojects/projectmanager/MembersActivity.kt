package com.guilhermecallandprojects.projectmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MembersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}