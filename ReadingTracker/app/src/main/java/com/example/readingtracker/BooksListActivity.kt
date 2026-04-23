package com.example.readingtracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BooksListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_list)

        val tvBooks = findViewById<TextView>(R.id.tvBooks)

        val books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            tvBooks.text = "No books found"
            return
        }

        val text = books.joinToString("\n\n") { book ->
            """
            Title: ${book.title}
            Author: ${book.author}
            Year: ${book.year}
            Progress: ${book.currentPage}/${book.totalPages}
            """.trimIndent()
        }

        tvBooks.text = text
    }
}