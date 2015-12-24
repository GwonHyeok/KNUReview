package dev.knureview.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class SplashActivity extends Activity {
    private static final String LOGIN_RESULT = "loginResult";
    private boolean isReLogin;

    private Typeface archiveFont;
    private Typeface nanumFont;

    private TextView appTitleEng;
    private TextView appCopyRight;
    private Handler handler;

    private SharedPreferencesActivity pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appTitleEng = (TextView) findViewById(R.id.appTitleEng);
        appCopyRight = (TextView) findViewById(R.id.appCopyRight);

        //글씨체
        archiveFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/Archive.otf");
        nanumFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/NanumGothic.ttf");
        appTitleEng.setTypeface(archiveFont);
        appCopyRight.setTypeface(nanumFont);

        //pref
        pref = new SharedPreferencesActivity(SplashActivity.this);
        isReLogin = pref.getPreferences(LOGIN_RESULT, false);

        //handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (isReLogin) {
                    Intent intent = new Intent(SplashActivity.this, MyStoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onBackPressed() {
    }
}
