package dev.knureview.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

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

    private static final String LOGIN_RESULT = "loginResult";
    private static final String STUDENT_NUMBER="studentNumber";
    private Typeface archiveFont;
    private Typeface nanumFont;

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

        inputPW.addTextChangedListener(textChangeListener);
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
            if (result.isExist() && isFreshMan) {
                //신입생 1학기 기존 사용자인 경우
                setAutoLogin();

            } else if (result.isExist() && !isFreshMan) {
                //재학생 기존 사용자인 경우
                if (!result.getMajor().equals(vo.getMajor())) {
                    //전공이 바뀌었으면 update
                    vo.setStdNo(Integer.parseInt(stdNo));
                    new UpdateMemberInfo().execute(vo);
                } else {
                    new StudentLecture().execute(cookie);
                }

            } else if (!result.isExist() && isFreshMan) {
                //신입생 1학기 새로운 사용자인 경우
                vo.setReviewAuth(1);
                vo.setTalkTicket(1);
                vo.setTalkAuth(1);
                new RegisterMember().execute(vo);

            } else if (!result.isExist() && !isFreshMan) {
                //재학생 새로운 사용자인 경우
                vo.setReviewAuth(0);
                vo.setTalkTicket(0);
                vo.setTalkTicket(0);
                new RegisterMember().execute(vo);
            }

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
        progressDialog.dismiss();
        pref.savePreferences(LOGIN_RESULT, true);
        pref.savePreferences(STUDENT_NUMBER, stdNo);

        //Main 페이지 이동
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
