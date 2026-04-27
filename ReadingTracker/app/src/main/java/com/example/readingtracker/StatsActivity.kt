package com.example.readingtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class StatsActivity : AppCompatActivity() {

    private lateinit var books: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val spinner = findViewById<Spinner>(R.id.spinnerBooks)
        val tvStats = findViewById<TextView>(R.id.tvStats)

        books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            tvStats.text = "No books available"
            return
        }

        val titles = books.map { it.title }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            titles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        fun updateStats(index: Int) {
            val book = books[index]

            val progressPercent = if (book.totalPages > 0)
                (book.currentPage * 100) / book.totalPages
            else 0

            val pagesLeft = book.totalPages - book.currentPage

            val estimatedDays = if (book.currentPage > 0)
                pagesLeft / 20  // zakładamy 20 stron dziennie
            else 0

            tvStats.text = """
                Title: ${book.title}
                
                Current page: ${book.currentPage} / ${book.totalPages}
                
                Progress: $progressPercent%
                
                Pages left: $pagesLeft
                
                Estimated days to finish: $estimatedDays
            """.trimIndent()
        }

        updateStats(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateStats(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}