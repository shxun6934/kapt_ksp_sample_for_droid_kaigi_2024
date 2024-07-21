package com.example.ksp_sample

import android.app.Application
import com.example.ksp_sample.db.AppDatabase

class KspSampleApplication : Application() {

    val db by lazy {
        AppDatabase.buildDatabase(this)
    }
}
