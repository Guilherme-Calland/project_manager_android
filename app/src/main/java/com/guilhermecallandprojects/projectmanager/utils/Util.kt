package com.guilhermecallandprojects.projectmanager.utils

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService

class Util : AppCompatActivity(){
    companion object{
        const val LOG_KEY = "projectmanagerapp"

        fun disableFullscreen(editText: EditText? = null, searchView: SearchView? = null) {
            if(editText != null){
                editText?.setImeOptions(editText.getImeOptions() or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN)
            } else {
                searchView?.setImeOptions(searchView.getImeOptions() or EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN)
            }
        }
    }
}
