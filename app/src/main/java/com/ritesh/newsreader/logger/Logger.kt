package com.ritesh.newsreader.logger

interface Logger {
//    fun d(tag: String, msg: String)
    fun logDebug(tag: String, message: String, vararg args: Any?)
    fun logInfo(tag: String, message: String, vararg args: Any?)
    fun logException(tag: String, message: String?)
    fun logException(tag: String, message: String, vararg args: Any?)
    /**
     * For all the exceptions which are to be logged on crash reporting tool,
     * eg.Crashlytics.
     */
    fun logException(tag: String, message: String, throwable: Throwable?)
}