package com.ritesh.newsreader

import android.app.Application
import com.ritesh.newsreader.logger.AppLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewsReaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}