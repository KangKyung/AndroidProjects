package com.example.momsee.FCMUse;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.momsee.R;
import com.example.momsee.activity_child_info;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFCMService extends FirebaseMessagingService {
    private static final String TAG = MyFCMService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // try {

            if (remoteMessage.getData().size() > 0) {
                if (true) {
                } else {
                    handleNow();
                }
            }
            if (remoteMessage.getNotification() != null) {
                sendNotification(remoteMessage.getNotification().getBody());
                Intent intent = new Intent(getApplicationContext(), OverlayService.class);

                if (remoteMessage.getNotification().getTitle().equals("LOCK")){
                    startService(intent);}

                else if (remoteMessage.getNotification().getTitle().equals("mission added")) {
                    Intent broadintent = new Intent("SEND_BROAD_CAST");
                    broadintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
                    sendBroadcast(broadintent);
                }
                else {
                    stopService(intent);
                }
            }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("MyToken", s);
    }

    private void handleNow() {


    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, activity_child_info.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());

    }


}
