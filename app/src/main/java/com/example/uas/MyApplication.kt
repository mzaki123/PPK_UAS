package com.example.uas

import android.app.Application
import com.example.uas.data.SessionManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize SessionManager with the application context.
        // This ensures SessionManager is ready to use throughout the app.
        SessionManager.init(this)
    }
}
