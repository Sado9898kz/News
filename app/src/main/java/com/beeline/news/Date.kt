package com.beeline.news

import java.util.Date
import java.text.SimpleDateFormat
import java.util.*


fun Date.format(pattern: String = "EEE, dd MMM"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return "Дата выхода новости: ${dateFormat.format(this)}"
}