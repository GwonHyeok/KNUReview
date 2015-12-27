package dev.knureview.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import dev.knureview.Push.QuickstartPreferences;
import dev.knureview.Push.RegistrationIntentService;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.StudentVO;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class SplashActivity extends Activity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";
    private static final String LOGIN_RESULT = "loginResult";
    private static final String VERSION = "version";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReLogin;
    private String appVersion;
    private String currentVersion;
    private String stdNo;

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
        registerBroadcastReceiver();

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
        currentVersion = getResources().getString(R.string.version);
        appVersion = pref.getPreferences(VERSION, "");
        stdNo = pref.getPreferences("stdNo", "");

        //handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (isReLogin) {
                    //자동로그인일 경우 Push Token Update
                    getInstanceIdToken();
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

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    public void onBackPressed() {
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registerBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)) {
                    String token = intent.getStringExtra("token");
                    new UpdatePush().execute(token);
                }
            }
        };
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        } else {
            //토큰을 가져오는데 실패
            new MemberInfo().execute(stdNo);
        }
    }

    /**
     * Google Play Service 를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private class MemberInfo extends AsyncTask<String, Void, StudentVO> {
        @Override
        protected StudentVO doInBackground(String... params) {
            try {
                return new NetworkUtil().getExistMemberInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO vo) {
            super.onPostExecute(vo);
            // initialize member info
            pref.savePreferences("reviewCnt", vo.getReviewCnt());
            pref.savePreferences("reviewAuth", vo.getReviewAuth());
            pref.savePreferences("talkCnt", vo.getTalkCnt());
            pref.savePreferences("talkWarning", vo.getTalkWarning());
            pref.savePreferences("talkAuth", vo.getTalkAuth());
            pref.savePreferences("talkTicket", vo.getTalkTicket());
            Intent intent = new Intent(SplashActivity.this, StoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    private class UpdatePush extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                new NetworkUtil().updatePushRegId(stdNo, params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Push RegId 등록 끝난후 MainActivity 로 이동
            new MemberInfo().execute(stdNo);
        }
    }


}
