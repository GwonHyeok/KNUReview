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
import dev.knureview.VO.Cookie;

/**
 * Created by DavidHa on 2015. 11. 21..
 */
public class LoginActivity extends Activity {

    private static final String LOGIN_RESULT = "loginResult";

    private Typeface archiveFont;
    private Typeface nanumFont;

    private TextView appTitleEng;
    private EditText inputID;
    private EditText inputPW;
    private Button loginBtn;
    private TextView loginInfo;


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
            String id = inputID.getText().toString();
            String pw = inputPW.getText().toString();
            if (!id.equals("") && !pw.equals("")) {
                new getSchoolCookie().execute(id, pw);
            }
        }
    }

    private class getSchoolCookie extends AsyncTask<String, Void, Cookie> {
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
        protected void onPostExecute(Cookie cookie) {
            progressDialog.dismiss();
            if (cookie.getLoginResult().equals("success")) {
                pref.savePreferences("idno", cookie.getIdno());
                pref.savePreferences("gubn", cookie.getGubn());
                pref.savePreferences("name", cookie.getName());
                pref.savePreferences("pass", cookie.getPass());
                pref.savePreferences("auto", cookie.getAuto());
                pref.savePreferences("mjco", cookie.getMjco());
                pref.savePreferences("name_e", cookie.getName_e());
                pref.savePreferences("jsession", cookie.getJsession());
                pref.savePreferences(LOGIN_RESULT,true);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
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
}
