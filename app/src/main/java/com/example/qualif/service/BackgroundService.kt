package com.example.qualif.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BackgroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroundService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            for (i in 1..5) {
                Log.d("BackgroundService", "Running Operation $i")
                Thread.sleep(1000)
            }
            stopSelf()
        }.start()

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackgroundService", "Service Destroyed")
    }
}