package dev.knureview.Push;

/**
 * Created by DavidHa on 2015. 8. 17..
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.net.URLDecoder;

import dev.knureview.Activity.MyStoryActivity;
import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by saltfactory on 6/8/15.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private SharedPreferencesActivity pref;
    private int count = 0;

    /**
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = "";
        try {
            message = URLDecoder.decode(data.getString("message"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pref = new SharedPreferencesActivity(this);
        count = pref.getPreferences("Push", 0);
        count++;
        pref.savePreferences("Push", count);
        if (count > 0) {
            ShortcutBadger.with(getApplicationContext()).count(count);
        }
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "PushCount: " + count);

        // GCM 으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     *
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MyStoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int color = getResources().getColor(R.color.colorPrimary);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.push_ic)
                .setColor(color)
                .setContentTitle("KNU REVIEW")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}