package com.org.navigator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private Context mContext;

    /**
     * Called when message is received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (mContext == null) {
            mContext = getBaseContext();
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        sendNotification("You just got a traffic alert nearby! Tap to change your route");
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String fcmmessage) {
        Intent intent = new Intent(this, ControlPanel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /**
         * Static function, an instance
         * @return new instance
         */

        //Define pending intent to trigger activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Create Notification according to builder pattern
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "Matrix");
        notificationBuilder
                .setSmallIcon(R.drawable.apple)
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                .setContentTitle("Traffic alert")
                .setContentText(fcmmessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        // the total number

        // Get Notification Manager
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * Static function, an instance
         * @return new instance
         */
        // Send notification
        notificationManager.notify(0, notificationBuilder.build());
    }

}
