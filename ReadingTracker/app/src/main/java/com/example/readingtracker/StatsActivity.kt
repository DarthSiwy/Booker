package com.example.readingtracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.LocalDate

class StatsActivity : AppCompatActivity() {

    private lateinit var books: MutableList<Book>
    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 📊 VIEW REFERENCES
        val spinner = findViewById<Spinner>(R.id.spinnerBooks)
        val tvStats = findViewById<TextView>(R.id.tvStats)
        val btnHistory = findViewById<Button>(R.id.btnHistory)

        lineChart = findViewById(R.id.lineChart)

        // 📚 LOAD DATA
        books = Storage.loadBooks(this)

        if (books.isEmpty()) {
            tvStats.text = "No books available"
            return
        }

        // 📌 SPINNER SETUP
        val titles = books.map { it.title }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            titles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        // 📜 HISTORY BUTTON
        btnHistory.setOnClickListener {
            val book = books[spinner.selectedItemPosition]

            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("bookTitle", book.title)
            startActivity(intent)
        }

        // 📊 UPDATE STATS
        fun updateStats(index: Int) {
            val book = books[index]

            val sessions = Storage.loadSessions(this)
                .filter { it.bookTitle == book.title }
                .sortedBy { it.date }

            // 📈 REAL PROGRESS CALCULATION
            var totalRead = 0
            var previousPage = 0

            for (s in sessions) {
                val diff = s.page - previousPage
                if (diff > 0) totalRead += diff
                previousPage = s.page
            }

            val daysRead = sessions.map { it.date }.distinct().size

            val avgPerDay = if (daysRead > 0)
                totalRead / daysRead
            else 0

            val progressPercent = if (book.totalPages > 0)
                (book.currentPage * 100) / book.totalPages
            else 0

            val pagesLeft = book.totalPages - book.currentPage

            // 🔥 STREAK
            val sortedDates = sessions.map { it.date }.distinct().sorted()

            var streak = 0
            var lastDate: LocalDate? = null

            for (dateStr in sortedDates) {
                val date = LocalDate.parse(dateStr)

                if (lastDate == null || date.minusDays(1) == lastDate) {
                    streak++
                } else {
                    streak = 1
                }

                lastDate = date
            }

            // 📄 TEXT OUTPUT
            tvStats.text = """
                📖 Title: ${book.title}
                
                📊 Progress: $progressPercent%
                
                📄 Current page: ${book.currentPage} / ${book.totalPages}
                
                📉 Pages left: $pagesLeft
                
                📅 Days read: $daysRead
                
                📈 Avg pages/day: $avgPerDay
                
                🔥 Reading streak: $streak days
            """.trimIndent()

            // 📊 CHART
            updateChart(book)
        }

        // 📊 INITIAL LOAD
        updateStats(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                updateStats(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // 📊 CHART FUNCTION
    private fun updateChart(book: Book) {

        val sessions = Storage.loadSessions(this)
            .filter { it.bookTitle == book.title }
            .sortedBy { it.date }

        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        sessions.forEachIndexed { index, session ->
            entries.add(Entry(index.toFloat(), session.page.toFloat()))
            labels.add(session.date)
        }

        val dataSet = LineDataSet(entries, "Reading Progress")
        val lineData = LineData(dataSet)

        lineChart.data = lineData

        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f

        lineChart.description.text = "Pages over time"

        lineChart.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}