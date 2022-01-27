package com.guilhermecallandprojects.projectmanager

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import android.view.inputmethod.EditorInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0F
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sv = menu.findItem(R.id.i_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        disableFullscreen(sv)
        setQueryListeners(sv)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setQueryListeners(sv: SearchView) {
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun disableFullscreen(sv: SearchView) {
        sv.setImeOptions(sv.getImeOptions() or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.itemId){
            R.id.i_add-> goToAddTask()
            R.id.i_members-> goToMembers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToAddTask() {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }

    private fun goToMembers(){
        val intent = Intent(this, MembersActivity::class.java)
        startActivity(intent)
    }

}