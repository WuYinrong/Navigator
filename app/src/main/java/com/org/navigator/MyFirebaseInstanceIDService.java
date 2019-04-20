package com.org.navigator;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInstanceIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        /**
         * Static function, an instance
         * @return new instance
         */
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        /**
         * Static function, an instance
         * @return new instance
         */
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

}
