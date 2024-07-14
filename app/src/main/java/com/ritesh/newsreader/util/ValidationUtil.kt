package com.ritesh.newsreader.util

object ValidationUtil {
    fun checkIfValidArgNews(str: String?): Boolean {
        return !(str.isNullOrEmpty()
                || str == "{country}"
                || str == "{language}"
                || str == "{source}"
                || str == "{category}"
                )
    }
}