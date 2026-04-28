package com.example.readingtracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Storage {

    //private const val PREF_NAME = "books_pref_v1"
    const val PREF_NAME = "books_pref_v10"

    private const val KEY_BOOKS = "books"
    private const val KEY_SESSIONS = "sessions"

    fun saveBooks(context: Context, books: List<Book>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(books)
        prefs.edit().putString(KEY_BOOKS, json).apply()
    }



    fun loadBooks(context: Context): MutableList<Book> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_BOOKS, null)

        return if (json != null) {
            val type = object : TypeToken<MutableList<Book>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun loadBooksFromAssets(context: Context): List<Book> {
        val json = context.assets.open("books.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(json, Array<Book>::class.java).toList()
    }

    fun saveSessions(context: Context, sessions: List<ReadingSession>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(sessions)
        prefs.edit().putString(KEY_SESSIONS, json).apply()
    }

    fun loadSessions(context: Context): MutableList<ReadingSession> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SESSIONS, null)

        return if (json != null) {
            val type = object : TypeToken<MutableList<ReadingSession>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun loadSessionsFromAssets(context: Context): List<ReadingSession> {
        val json = context.assets.open("sessions.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(json, Array<ReadingSession>::class.java).toList()
    }

}