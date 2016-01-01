package dev.knureview.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import dev.knureview.Push.QuickstartPreferences;
import dev.knureview.Push.RegistrationIntentService;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.Util.TimeUtil;
import dev.knureview.VO.Cookie;
import dev.knureview.VO.StudentVO;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class LoginActivity extends Activity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LoginActivity";
    private static final String LOGIN_RESULT = "loginResult";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Typeface archiveFont;
    private Typeface nanumFont;
    private ImageView loginImage;
    private TextView appTitleEng;
    private EditText inputID;
    private EditText inputPW;
    private Button loginBtn;
    private TextView loginInfo;
    private String stdNo;
    private StudentVO vo;
    private Cookie cookie;
    boolean isFreshMan = false;


    private MaterialDialog progressDialog;
    private SharedPreferencesActivity pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        archiveFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/Archive.otf");
        nanumFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/NanumGothic.ttf");

        loginImage = (ImageView)findViewById(R.id.loginImage);
        appTitleEng = (TextView) findViewById(R.id.appTitleEng);
        inputID = (EditText) findViewById(R.id.inputID);
        inputPW = (EditText) findViewById(R.id.inputPW);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginInfo = (TextView) findViewById(R.id.loginInfo);

        //글씨체
        appTitleEng.setTypeface(archiveFont);
        inputID.setTypeface(nanumFont);
        inputPW.setTypeface(nanumFont);
        loginBtn.setTypeface(nanumFont);
        loginInfo.setTypeface(nanumFont);

        //pref
        pref = new SharedPreferencesActivity(this);

        //StudentVO
        vo = new StudentVO();

        //register BroadcastReceiver
        registerBroadcastReceiver();

        inputPW.addTextChangedListener(textChangeListener);

        //blur
        BitmapDrawable drawable = (BitmapDrawable) loginImage.getDrawable();
        Bitmap blurBitmap = blur(this, drawable.getBitmap(), 12);
        loginImage.setImageBitmap(blurBitmap);
    }

    TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0 && !inputID.equals("")) {
                loginBtn.setTextColor(getResources().getColor(R.color.white));
            } else {
                loginBtn.setTextColor(getResources().getColor(R.color.white_20));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void mOnClick(View view) {
        if (view.getId() == R.id.loginBtn) {
            stdNo = inputID.getText().toString();
            String pw = inputPW.getText().toString();
            if (!stdNo.equals("") && !pw.equals("")) {
                new SchoolCookie().execute(stdNo, pw);
            }
        }
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

    public Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
    }


    //학교 서버에서 쿠키값 가져오기
    private class SchoolCookie extends AsyncTask<String, Void, Cookie> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new MaterialDialog.Builder(LoginActivity.this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .content("잠시만 기다려주세요.")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        }

        @Override
        protected Cookie doInBackground(String[] params) {
            try {
                return new NetworkUtil().loginSchoolServer(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cookie result) {

            //학교 서버 로그인 성공했을 경우
            if (result.getLoginResult().equals("success")) {
                //학번 저장
                vo.setStdNo(Integer.parseInt(stdNo));
                //학생 소속, 전공 정보 가져오기 (쿠키값 전달)
                cookie = result;
                new StudentInfo().execute(cookie);

            } else {
                progressDialog.dismiss();
                new MaterialDialog.Builder(LoginActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title("잘못된 비밀번호")
                        .titleColor(getResources().getColor(R.color.black))
                        .content("입력된 비밀번호가 틀립니다. 다시 시도하세요.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
    }

    //학교 서버로부터 학생 소속, 전공 정보 가져오는 클래스
    private class StudentInfo extends AsyncTask<Cookie, Void, StudentVO> {
        @Override
        protected StudentVO doInBackground(Cookie... params) {
            try {
                return new NetworkUtil().getStudentInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO result) {
            super.onPostExecute(result);

            //소속, 전공 초기화
            vo.setBelong(result.getBelong());
            vo.setMajor(result.getMajor());
            vo.setName(result.getName());

            String year = stdNo.substring(0, 4);
            TimeUtil timeUtil = new TimeUtil();

            //2월 ~ 7월 14일까지
            if (year.equals(timeUtil.getYear())
                    && (timeUtil.getMonth() >= 2
                    && ((timeUtil.getMonth() <= 7) && timeUtil.getDay() < 15))) {
                //신입생 1학기 일 경우
                isFreshMan = true;
            } else {
                isFreshMan = false;
            }
            new LookupMember().execute();
        }
    }

    //기존 KNU REVIEW 사용자인지 확인 하는 클래스
    private class LookupMember extends AsyncTask<Void, Void, StudentVO> {
        @Override
        protected StudentVO doInBackground(Void... params) {
            try {
                return new NetworkUtil().getExistMemberInfo(stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO result) {
            super.onPostExecute(result);
            vo.setIsExist(result.isExist());

            if (result.isExist() && isFreshMan) {
                //신입생 1학기 기존 사용자인 경우
                vo.setReviewCnt(result.getReviewCnt());
                vo.setReviewAuth(result.getReviewAuth());
                vo.setTalkAuth(result.getTalkAuth());
                vo.setTalkCnt(result.getTalkCnt());
                vo.setTalkTicket(result.getTalkTicket());
                vo.setTalkWarning(result.getTalkWarning());
                setAutoLogin();
            } else if (result.isExist() && !isFreshMan) {
                //재학생 기존 사용자인 경우
                vo.setReviewCnt(result.getReviewCnt());
                vo.setReviewAuth(result.getReviewAuth());
                vo.setTalkAuth(result.getTalkAuth());
                vo.setTalkCnt(result.getTalkCnt());
                vo.setTalkTicket(result.getTalkTicket());
                vo.setTalkWarning(result.getTalkWarning());
                new UpdateMemberInfo().execute(vo);
            } else if (!result.isExist() && isFreshMan) {
                //신입생 1학기 새로운 사용자인 경우
                vo.setReviewAuth(1);
                vo.setTalkAuth(1);
                vo.setTalkTicket(1);
                new RegisterMember().execute(vo);

            } else if (!result.isExist() && !isFreshMan) {
                //재학생 새로운 사용자인 경우
                vo.setReviewAuth(0);
                vo.setTalkAuth(0);
                vo.setTalkTicket(1);
                new RegisterMember().execute(vo);
            }

        }
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

    //Update Push RegId
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
    }

    //기존 사용자 Update Info 클래스
    private class UpdateMemberInfo extends AsyncTask<StudentVO, Void, Void> {
        @Override
        protected Void doInBackground(StudentVO... params) {
            try {
                new NetworkUtil().updateMemberInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new StudentLecture().execute(cookie);
        }
    }

    //새로운 사용자 등록 클래스
    private class RegisterMember extends AsyncTask<StudentVO, Void, Void> {
        @Override
        protected Void doInBackground(StudentVO... params) {
            try {
                new NetworkUtil().setMemberInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isFreshMan) {
                //신입생 1학기 새로운 사용자인 경우
                setAutoLogin();
            } else {
                //재학생 새로운 사용자인 경우
                new StudentLecture().execute(cookie);
            }
        }
    }


    private class StudentLecture extends AsyncTask<Cookie, Void, Void> {

        @Override
        protected Void doInBackground(Cookie[] params) {
            try {
                new NetworkUtil().setStudentLecture(params[0], stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setAutoLogin();
        }

    }

    private void setAutoLogin() {
        //자동로그인 설정
        getInstanceIdToken();
        progressDialog.dismiss();
        pref.savePreferences(LOGIN_RESULT, true);
        pref.savePreferences("stdNo", stdNo);
        pref.savePreferences("belong", vo.getBelong());
        pref.savePreferences("major", vo.getMajor());
        pref.savePreferences("reviewCnt", vo.getReviewCnt());
        pref.savePreferences("reviewAuth", vo.getReviewAuth());
        pref.savePreferences("talkCnt", vo.getTalkCnt());
        pref.savePreferences("talkWarning", vo.getTalkWarning());
        pref.savePreferences("talkAuth", vo.getTalkAuth());
        pref.savePreferences("talkTicket", vo.getTalkTicket());

        //Main 페이지 이동
        Intent intent = new Intent(LoginActivity.this, StoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
