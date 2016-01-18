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

import dev.knureview.Activity.AlarmActivity;
import dev.knureview.Activity.MainActivity;
import dev.knureview.Activity.MyProfileActivity;
import dev.knureview.Activity.ProfileDetail.TicketActivity;
import dev.knureview.Activity.StoryActivity;
import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by saltfactory on 6/8/15.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";


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

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

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
        Intent intent = null;
        if (message.contains("소곤소곤 티켓")) {
            intent = new Intent(this, MyProfileActivity.class);
            intent.putExtra("push", "ticket");
        } else if (message.contains("누군가")) {
            intent = new Intent(this, StoryActivity.class);
            intent.putExtra("push", "msgAlarm");
        }else if(message.equals("지금부터 수강리뷰를 열람하실 수 있습니다.")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("push", "getReviewAuth");
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int color = getResources().getColor(R.color.colorPrimary);
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        String pushAlarmSound = pref.getPreferences("pushAlarmSound", "default");
        String pushAlarmSetting = pref.getPreferences("pushAlarmSetting", "default");

        Uri soundUri;
        if (pushAlarmSound.equals("default")) {
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            soundUri = null;
        }
        if (pushAlarmSetting.equals("default")) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.push_ic)
                    .setColor(color)
                    .setContentTitle("KNU REVIEW")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}