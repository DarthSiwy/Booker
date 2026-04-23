package com.example.readingtracker

data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val totalPages: Int,
    var currentPage: Int = 0
)