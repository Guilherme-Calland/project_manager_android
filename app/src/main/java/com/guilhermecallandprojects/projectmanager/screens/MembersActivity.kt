package com.guilhermecallandprojects.projectmanager.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guilhermecallandprojects.projectmanager.R

class MembersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        setActionBarProperties()
    }

    private fun setActionBarProperties() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}