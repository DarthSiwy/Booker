package com.example.readingtracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "History"

        val tvHistory = findViewById<TextView>(R.id.tvHistory)

        val bookTitle = intent.getStringExtra("bookTitle") ?: return

        val sessions = Storage.loadSessions(this)
            .filter { it.bookTitle == bookTitle }
            .sortedByDescending { it.date }

        if (sessions.isEmpty()) {
            tvHistory.text = "No history"
            return
        }

        val text = sessions.joinToString("\n\n") {
            "${it.date} - page ${it.page}"
        }

        tvHistory.text = text
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}