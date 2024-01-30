package com.mbh.moviebrowser.api.common

import java.io.BufferedReader
import java.io.InputStreamReader

fun Any.readJsonFile(filename: String) = javaClass.classLoader
    ?.getResourceAsStream(filename).use { inputStream ->
        val builder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(inputStream))
        var string = reader.readLine()
        while (string != null) {
            builder.append(string)
            string = reader.readLine()
        }
        builder.toString()
    }