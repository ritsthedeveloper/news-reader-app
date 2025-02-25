package com.ritesh.newsreader.logger

import com.ritesh.newsreader.BuildConfig
import timber.log.Timber

/**
 * This class provides a layer where we can connect with any 3rd party tool for logging or our own logging mechanism.
 * Thus it helps to maintain the loose coupling between the appcode and logging tool.
 */
object AppLogger : Logger  {

    const val TAG : String = "NewsReader"

    fun init() {
        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
            Timber.plant(AppDebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        logDebug(TAG,"Timber Initialised")
    }

    /**
     * For all the logs, used for debugging purpose.
     */
    override fun logDebug(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).d(message, *args)
//        Log.d(TAG,message)
    }

    /**
     * For all the logs, to be used as additional info message with optional format args and
     * are logged on crash reporting tool, Crashlytics in our case.
     */
    override fun logInfo(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).i(message, *args)
//        Log.i(TAG,message)
    }

    /**
     * For all the warning logs.
     */
    fun logWarning(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).w(message, *args)
    }

    override fun logException(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).e(message, *args)
    }

    override fun logException(tag: String, message: String?) {
        if (message != null) {
            logException(tag, message, null)
        } else {
            logException(tag, "APP Exception", null)
        }
    }

    /**
     * For all the exceptions which are to be logged on crash reporting tool,
     * Crashlytics in our case.
     */
    override fun logException(tag: String, message: String, throwable: Throwable?) {
        Timber.tag(tag).e(throwable, "TAP Exception: $message")
    }

//    override fun d(tag: String, msg: String) {
//        Log.d(tag, msg)
////        logDebug(tag,msg)
//    }
}
