package com.example.readingtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etAuthor = findViewById<EditText>(R.id.etAuthor)
        val etYear = findViewById<EditText>(R.id.etYear)
        val etPages = findViewById<EditText>(R.id.etPages)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val author = etAuthor.text.toString()
            val year = etYear.text.toString().toIntOrNull() ?: 0
            val pages = etPages.text.toString().toIntOrNull() ?: 0

            val books = Storage.loadBooks(this)
            books.add(Book(title, author, year, pages, 0))

            Storage.saveBooks(this, books)

            Toast.makeText(this, "Book saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}