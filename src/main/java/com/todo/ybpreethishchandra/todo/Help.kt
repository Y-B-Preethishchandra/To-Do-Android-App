package com.todo.ybpreethishchandra.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Help : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        supportActionBar?.setTitle("Help")
    }
}
