package com.ritesh.newsreader.util

import com.ritesh.newsreader.logger.AppLogger

object ValidationUtil {
    fun checkIfValidArgNews(str: String?): Boolean {
        AppLogger.logDebug(AppLogger.TAG, "ValidationUtil - str value: $str")
        return !(str.isNullOrEmpty()
                || str == "{country}"
                || str == "{language}"
                || str == "{source}"
                || str == "{category}"
                )
    }
}