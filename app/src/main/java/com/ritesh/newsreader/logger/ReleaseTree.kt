package com.ritesh.newsreader.logger

import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        // Don't log VERBOSE, DEBUG
        return !(priority == Log.VERBOSE || priority == Log.DEBUG)
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        if (!isLoggable(tag, priority)) return

        /** This code is commented as logging on Firebase is not required.
         * If we want to connect we can do this way.

        val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        when (priority) {
            Log.INFO -> {
                firebaseCrashlytics.log(message)
            }
            Log.ERROR -> {
                if (throwable == null) {
                    firebaseCrashlytics.log(message)
                } else {
                    // Will record non-fatal exception in firebase crashlytics
                    firebaseCrashlytics.recordException(throwable)
                }
            }
        }
        */
    }
}