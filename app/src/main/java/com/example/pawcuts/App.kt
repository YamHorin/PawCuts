package com.example.pawcuts

import android.app.Application
import com.example.pawcuts.utilities.AccountManager
import com.example.pawcuts.utilities.CalendarManager
import com.example.pawcuts.utilities.DataUsersManager
import com.example.pawcuts.utilities.ImageProfileManager

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        ImageProfileManager.init(this)
        AccountManager.init(this)
        DataUsersManager.init(this)
        CalendarManager.init(this)
    }
}