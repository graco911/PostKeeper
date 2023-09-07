package com.gracodev.postkeeper

import android.app.Application
import com.gracodev.postkeeper.Utils.Prefs
import com.gracodev.postkeeper.data.modules.createAppModules
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModules = createAppModules()

        startKoin {
            androidContext(applicationContext)
            modules(appModules)
        }

        AppCenter.start(
            this,
            getString(R.string.app_center_id), Analytics::class.java, Crashes::class.java
        )

        Prefs.init(this)
    }
}