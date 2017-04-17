package com.qwik.user.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.qwik.user.restaurant_app.R;
import java.util.Random;

public class PushNotificationService extends GcmListenerService {
    Context context=PushNotificationService.this;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        int n_id=new Random().nextInt();
        Intent intent = new Intent(getApplicationContext(), Home.class);
        //createNotification(mTitle, push_msg);
        Bundle bundle=new Bundle();
        bundle.putString("message",message);
        intent.putExtras(bundle);
        PendingIntent contentIntent =PendingIntent.getActivity(getApplicationContext(), 0,intent, 0);
        Vibrator vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(500); // Vibrate for half a second (500 milli seconds)
        long[] pattern = {400, 200};
        vibr.vibrate(pattern, 1); // 400ms pause, 200ms vibration. Start repeating at index 0.
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("QwikXL")
                .setContentText(message)
                .setLights(0x0000FF, 10000, 100);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        notificationManager.notify(n_id, mBuilder.build());
    }
}
