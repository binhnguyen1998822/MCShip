package com.example.nguyen.tokentesst.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nguyen.tokentesst.MainLoginActivity;
import com.example.nguyen.tokentesst.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message = remoteMessage.getData().get("dia_chi");
        String status = remoteMessage.getData().get("id_loaiship");
        String statuscancel = remoteMessage.getData().get("trang_thai");
        Log.d("xxx", status);
        if (status.equals("3")) {
            sendNotification(message, status);
        }
        if (statuscancel.equals("5")) {

            sendNotificationx(message, status);
        }


    }

    //setvalue

    private void sendNotification(String message, String status) {
        Intent intent = new Intent(this, MainLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("Thông Báo : Đơn hàng mới")
                .setContentText("Có đơn đến " + message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationx(String message, String status) {
        Intent intent = new Intent(this, MainLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("Thông Báo : Đơn hàng bị HỦY")
                .setContentText("Có đơn đến " + message + " bị HỦY")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
