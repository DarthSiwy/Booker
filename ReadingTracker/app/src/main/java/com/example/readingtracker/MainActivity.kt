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

        seedBooksIfNeeded()

        val btnAddBook = findViewById<Button>(R.id.btnAddBook)
        val btnAddProgress = findViewById<Button>(R.id.btnAddProgress)
        val btnBooksList = findViewById<Button>(R.id.btnBooksList)

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


    private fun seedBooksIfNeeded() {
        val prefs = getSharedPreferences(Storage.PREF_NAME, Context.MODE_PRIVATE)
        //val prefs = getSharedPreferences("books_pref", Context.MODE_PRIVATE)

        val isSeeded = prefs.getBoolean("seeded", false)

        if (!isSeeded) {
            val books = listOf(
                Book("The Hobbit", "J.R.R. Tolkien", 1937, 310, 120),
                Book("1984", "George Orwell", 1949, 328, 200),
                Book("Harry Potter and the Philosopher's Stone", "J.K. Rowling", 1997, 223, 223),
                Book("Clean Code", "Robert C. Martin", 2008, 464, 150),
                Book("Atomic Habits", "James Clear", 2018, 320, 80),
                Book("Dune1", "Frank Herbert", 1965, 896, 80)
            )

            Storage.saveBooks(this, books)

            prefs.edit().putBoolean("seeded", true).apply()
        }
    }
}