package com.ritesh.newsreader

import android.app.Application
import com.ritesh.newsreader.logger.AppLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * @HiltAndroidApp triggers Hilt's code generation, including a base class for our application that
 * serves as the application-level dependency container.
 * This generated Hilt component is attached to the Application object's lifecycle and provides
 * dependencies to it. Additionally, it is the parent component of the app, which means that
 * other components can access the dependencies that it provides.
 */
@HiltAndroidApp
class NewsReaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}