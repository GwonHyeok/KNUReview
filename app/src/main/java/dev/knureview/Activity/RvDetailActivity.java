package dev.knureview.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.CircularProgressButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.ReviewVO;

/**
 * Created by DavidHa on 2016. 1. 12..
 */
public class RvDetailActivity extends ActionBarActivity {
    private static final int ACCEPT = 1;
    private static final int DENIAL = 0;
    @Bind(R.id.difcRating)RatingBar difcRating;
    @Bind(R.id.asignRating)RatingBar asignRating;
    @Bind(R.id.atendRating)RatingBar atendRating;
    @Bind(R.id.gradeRating)RatingBar gradeRating;
    @Bind(R.id.achivRating)RatingBar achivRating;
    @Bind(R.id.inputReview)EditText inputReview;
    @Bind(R.id.confirmButton)CircularProgressButton confirmBtn;
    public static Activity activity;
    private String sbjName;
    private String stdNo;
    private String inputReviewStr;
    private ReviewVO vo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_detail);
        ButterKnife.bind(this);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");
        //intent
        Intent intent = getIntent();
        sbjName = intent.getStringExtra("sbjName");

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sbjName + " 리뷰");

        activity = RvDetailActivity.this;
        new ReviewDetail().execute();
    }
    public void mOnClick(View view){
        if(view.getId() == R.id.confirmButton){
            if(confirmBtn.getProgress() == 0) {
                inputReviewStr=inputReview.getText().toString();
                if(!inputReviewStr.equals("")) {
                    new InsertReviewDetail().execute();
                }else{
                    confirmBtn.setProgress(-1);
                    new MaterialDialog.Builder(RvDetailActivity.this)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .content("수강리뷰를 작성해주세요.")
                            .contentColor(getResources().getColor(R.color.text_lgray))
                            .positiveText("확인")
                            .positiveColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            }else if(confirmBtn.getProgress() == -1){
                confirmBtn.setProgress(0);
            }
        }
    }


    private class ReviewDetail extends AsyncTask<Void, Void, ReviewVO> {
        @Override
        protected ReviewVO doInBackground(Void... params) {
            try {
                return new NetworkUtil().getOneReview(stdNo, sbjName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ReviewVO result) {
            super.onPostExecute(result);
            vo = result;
            difcRating.setRating(result.getDifc());
            asignRating.setRating(result.getAsign());
            atendRating.setRating(result.getAtend());
            gradeRating.setRating(result.getGrade());
            achivRating.setRating(result.getAchiv());
        }
    }

    private class InsertReviewDetail extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            confirmBtn.setProgress(50);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                return new NetworkUtil().insertReviewDetail(inputReviewStr, vo.getrNo());
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                confirmBtn.setProgress(100);
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Intent intent = new Intent(RvDetailActivity.this, RvSbjActivity.class);
                        startActivity(intent);
                        RvDetailActivity.activity.finish();
                        RvEditActivity.activity.finish();
                    }
                }.sendEmptyMessageDelayed(0, 800);
            }else{
                confirmBtn.setProgress(-1);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(RvDetailActivity.this)
                .backgroundColor(getResources().getColor(R.color.white))
                .content("수강리뷰 작성을 취소하시겠습니까?\n지금 수강리뷰를 작성하지 않으셔도 추후에 수강리뷰 작성을 하실 수 있습니다.")
                .positiveText("확인")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        RvDetailActivity.activity.finish();
                        RvEditActivity.activity.finish();
                    }
                })
                .negativeText("취소")
                .cancelable(false)
                .show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
