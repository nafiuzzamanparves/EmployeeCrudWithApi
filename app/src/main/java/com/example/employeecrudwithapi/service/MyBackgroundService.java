package com.example.employeecrudwithapi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyBackgroundService extends Service {
    private static final String TAG = "MyBackgroundService";
    private Handler handler;
    private Runnable runnable;
    private int counter = 0;

    public MyBackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Created");
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();

        // Initialize Handler and Runnable for periodic task
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                counter++;
                Log.d(TAG, "Service running... Counter: " + counter);
                // You could perform some background task here, e.g., fetch data, update UI via broadcast
                // For demonstration, we'll just log and show a toast (though toasts from services should be used sparingly)
                Toast.makeText(MyBackgroundService.this,
                        "Service running: " + counter, Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000); // Repeat every 5 seconds
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Started Command");
        Toast.makeText(this, "Service Started Command", Toast.LENGTH_SHORT).show();

        handler.post(runnable);

        // Return START_STICKY to indicate that if the system kills the service,
        // it should try to re-create it when memory is available.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();

        // Stop the periodic task when the service is destroyed
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}