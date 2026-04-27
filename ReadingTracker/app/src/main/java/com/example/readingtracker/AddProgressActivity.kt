package com.example.readingtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date

class AddProgressActivity : AppCompatActivity() {

    private lateinit var books: MutableList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_progress)

        val spinner = findViewById<Spinner>(R.id.spinnerBooks)
        val etCurrentPage = findViewById<EditText>(R.id.etCurrentPage)
        val btnSave = findViewById<Button>(R.id.btnSaveProgress)
        val tvInfo = findViewById<TextView>(R.id.tvCurrentInfo)

        books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            Toast.makeText(this, "No books available", Toast.LENGTH_LONG).show()
            finish()
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

        fun updateInfo(index: Int) {
            val book = books[index]
            tvInfo.text = "Current page: ${book.currentPage} / ${book.totalPages}"
        }

        updateInfo(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                updateInfo(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnSave.setOnClickListener {

            val index = spinner.selectedItemPosition
            val book = books[index]

            val newCurrentPage = etCurrentPage.text.toString().toIntOrNull()

            if (newCurrentPage == null) {
                Toast.makeText(this, "Enter valid page", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //  WARUNEK MAX
            if (newCurrentPage > book.totalPages) {
                Toast.makeText(this, "Page cannot exceed ${book.totalPages}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // nie może cofać się (opcjonalne, ale logiczne)
            if (newCurrentPage < book.currentPage) {
                Toast.makeText(this, "You cannot go backwards", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            book.currentPage = newCurrentPage

            Storage.saveBooks(this, books)


            val sessions = Storage.loadSessions(this)
            val today = java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(java.util.Date())
            sessions.add(
                ReadingSession(
                    book.title,
                    today,
                    newCurrentPage
                )
            )

            Storage.saveSessions(this, sessions)

            Toast.makeText(this, "Progress updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}