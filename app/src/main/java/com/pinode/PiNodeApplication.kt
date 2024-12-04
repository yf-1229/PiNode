package com.pinode

import android.app.Application
import com.pinode.data.AppContainer
import com.pinode.data.AppDataContainer

class PiNodeApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}