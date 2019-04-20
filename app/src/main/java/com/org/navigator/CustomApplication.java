package com.org.navigator;

import android.app.Application;
import android.content.res.Configuration;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String token = FirebaseInstanceId.getInstance().getToken();
        /**
         * Static function, an instance
         * @return new instance
         */
        FirebaseMessaging.getInstance().subscribeToTopic("android");
        // the total number
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /**
         * Static function, an instance
         * @return new instance
         */
    }

}
