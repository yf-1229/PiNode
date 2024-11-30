package com.example.pinode

import android.app.Application
import com.example.pinode.data.AppContainer
import com.example.pinode.data.AppDataContainer

class PiNodeApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}