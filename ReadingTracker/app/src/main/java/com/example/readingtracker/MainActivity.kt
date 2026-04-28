package com.example.readingtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        seedBooksFromAssetsIfNeeded()
        seedSessionsFromAssetsIfNeeded()

        val btnAddBook = findViewById<Button>(R.id.btnAddBook)
        val btnAddProgress = findViewById<Button>(R.id.btnAddProgress)
        val btnBooksList = findViewById<Button>(R.id.btnBooksList)

        val btnStats = findViewById<Button>(R.id.btnStats)

        btnStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        btnAddBook.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        btnAddProgress.setOnClickListener {
            startActivity(Intent(this, AddProgressActivity::class.java))
        }

        btnBooksList.setOnClickListener {
            startActivity(Intent(this, BooksListActivity::class.java))
        }
    }


    private fun seedBooksFromAssetsIfNeeded() {
        val prefs = getSharedPreferences(Storage.PREF_NAME, Context.MODE_PRIVATE)
        val isSeeded = prefs.getBoolean("seeded", false)

        if (!isSeeded) {
            val books = Storage.loadBooksFromAssets(this)
            Storage.saveBooks(this, books)

            prefs.edit().putBoolean("seeded", true).apply()
        }
    }

    private fun seedSessionsFromAssetsIfNeeded() {
        val prefs = getSharedPreferences(Storage.PREF_NAME, Context.MODE_PRIVATE)
        val isSeeded = prefs.getBoolean("sessions_seeded", false)

        if (!isSeeded) {
            val sessions = Storage.loadSessionsFromAssets(this)
            Storage.saveSessions(this, sessions)

            prefs.edit().putBoolean("sessions_seeded", true).apply()
        }
    }
}