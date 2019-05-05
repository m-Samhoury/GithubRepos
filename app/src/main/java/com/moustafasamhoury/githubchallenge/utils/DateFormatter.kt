package com.moustafasamhoury.githubchallenge.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

object DateFormatter {
    fun formatDateToServer(date: Date) = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
}