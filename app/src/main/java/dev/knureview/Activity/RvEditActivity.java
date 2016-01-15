package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.Adapter.SimeListViewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.ProfVO;
import dev.knureview.VO.ReviewVO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class RvEditActivity extends ActionBarActivity {
    public static Activity activity;
    @Bind(R.id.inputSbj) EditText inputSbj;
    @Bind(R.id.inputProf) EditText inputProf;
    @Bind(R.id.difcRating) RatingBar difcRating;
    @Bind(R.id.asignRating) RatingBar asignRating;
    @Bind(R.id.atendRating) RatingBar atendRating;
    @Bind(R.id.gradeRating) RatingBar gradeRating;
    @Bind(R.id.achivRating) RatingBar achivRating;
    @Bind(R.id.profList) ListView profListView;

    private MaterialDialog progressDialog;
    private SimeListViewAdapter profAdapter;
    private ArrayList<String> profStrArray;
    private ReviewVO rVo;
    private String stdNo;
    private String sbjName;
    private String profName;
    private int difcCnt;
    private int asignCnt;
    private int atendCnt;
    private int gradeCnt;
    private int achivCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_edit);

        ButterKnife.bind(this);
        //setOnListener
        profListView.setOnItemClickListener(profItemClickListener);
        inputProf.addTextChangedListener(profTextWatcher);
        difcRating.setOnRatingBarChangeListener(ratingListener);
        asignRating.setOnRatingBarChangeListener(ratingListener);
        atendRating.setOnRatingBarChangeListener(ratingListener);
        gradeRating.setOnRatingBarChangeListener(ratingListener);
        achivRating.setOnRatingBarChangeListener(ratingListener);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");
        rVo = new ReviewVO();
        rVo.setStdNo(Integer.parseInt(stdNo));
        //intent
        Intent intent = getIntent();
        sbjName = intent.getStringExtra("sbjName");
        inputSbj.setText(sbjName);

        activity = RvEditActivity.this;
        new ProfInfo().execute();

    }

    RatingBar.OnRatingBarChangeListener ratingListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (ratingBar == difcRating) {
                difcCnt = (int) rating;
            } else if (ratingBar == asignRating) {
                asignCnt = (int) rating;
            } else if (ratingBar == atendRating) {
                atendCnt = (int) rating;
            } else if (ratingBar == gradeRating) {
                gradeCnt = (int) rating;
            } else if (ratingBar == achivRating) {
                achivCnt = (int) rating;
            }
        }
    };

    TextWatcher profTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setRefreshAdapter(profAdapter, profListView, s.toString(), true);
        }
    };

    AdapterView.OnItemClickListener profItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputProf.getWindowToken(),0);
            inputProf.setText(profAdapter.getItemList().get(position));
            inputProf.setSelection(inputProf.getText().length());
            setRefreshAdapter(profAdapter, profListView, "", true);
        }
    };

    public void mOnClick(View view) {
        String title = "";
        String content = "";
        switch (view.getId()) {
            case R.id.difcHelp:
                title = "난이도";
                content = "학습 난이도가 어려울수록 배점을 높게 주시면 됩니다.";
                break;
            case R.id.asignHelp:
                title = "과제량";
                content = "과제량이 많을수록 배점을 높게 주시면 됩니다.";
                break;
            case R.id.atendHelp:
                title = "출석체크";
                content = "출석체크를 자주 부르시면 배점을 높게 주시면 됩니다.";
                break;
            case R.id.gradeHelp:
                title = "학점";
                content = "학점이 받기 쉬울수록 배점을 높게 주시면 됩니다.";
                break;
            case R.id.achivHelp:
                title = "성취감";
                content = "성취감이 높을수록 배점을 높게 주시면 됩니다.";
                break;
        }
        new MaterialDialog.Builder(RvEditActivity.this)
                .backgroundColor(getResources().getColor(R.color.white))
                .title(title)
                .titleColor(getResources().getColor(R.color.black))
                .content(content)
                .contentColor(getResources().getColor(R.color.text_lgray))
                .positiveText("확인")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }


    public void setRefreshAdapter(SimeListViewAdapter adapter, ListView listView, String str, boolean isAuto) {
        final int LIST_VIEW_MAX_HEIGHT = (int) PixelUtil.convertPixelsToDp(285, this);
        if (isAuto) {
            adapter.autoComplete(str);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if (adapter.getCount() > 6) {
            params.height = LIST_VIEW_MAX_HEIGHT * 5;
        } else {
            params.height = adapter.getCount() * LIST_VIEW_MAX_HEIGHT;
        }
        listView.setLayoutParams(params);
        adapter.notifyDataSetChanged();
    }


    private class ProfInfo extends AsyncTask<Void, Void, ArrayList<ProfVO>> {
        @Override
        protected ArrayList<ProfVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getProfList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ProfVO> result) {
            super.onPostExecute(result);
            profStrArray = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                profStrArray.add(result.get(i).getpName());
            }
            profAdapter = new SimeListViewAdapter(RvEditActivity.this,
                    R.layout.layout_auto_list_row, profStrArray);
            profListView.setAdapter(profAdapter);
        }
    }

    private class InsertReview extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new MaterialDialog.Builder(RvEditActivity.this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .content("잠시만 기다려주세요.")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                return new NetworkUtil().insertReview(rVo, sbjName, profName);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.equals("success")){
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("강의평가를 작성해주셔서 감사합니다.\n해당 과목에 대한 리뷰를 남기실 수 있습니다.\n지금 리뷰를 작성하시겠습니까?")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("작성하기")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                Intent intent = new Intent(RvEditActivity.this, RvDetailActivity.class);
                                intent.putExtra("sbjName", sbjName);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
                            }
                        })
                        .negativeText("나중에 하기")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                finish();
                            }
                        })
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .cancelable(false)
                        .show();
            }else if(result.equals("false")){
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("강의평가를 서버에 올리는데 실패하였습니다.\n다시 시도하시겠습니까?")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                new InsertReview().execute();
                            }
                        })
                        .negativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                finish();
                            }
                        })
                        .show();
            }else if(result.equals("noSbj")){
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title("해당 과목을 찾을수 없습니다.")
                        .titleColor(getResources().getColor(R.color.black))
                        .content("해당 과목이 아직 서버에 저장되지 않았습니다.\n관리자에게 알려주시면 빠른 시일 안에 해당 과목을 서버에 저장하겠습니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.complete) {
            String profStr = inputProf.getText().toString().trim();
            if (difcCnt > 0 && asignCnt > 0 && atendCnt > 0 && gradeCnt > 0 && achivCnt > 0 && !profStr.equals("")) {
                rVo.setDifc(difcCnt);
                rVo.setAsign(asignCnt);
                rVo.setAtend(atendCnt);
                rVo.setGrade(gradeCnt);
                rVo.setAchiv(achivCnt);
                profName = profStr;
               new InsertReview().execute();
            } else {
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("강의평가를 다 작성하시지 않았습니다.\n모든 항목을 작성하셔야 수강리뷰 등록이 됩니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(RvEditActivity.this)
                .backgroundColor(getResources().getColor(R.color.white))
                .content("강의평가 작성을 취소하시겠습니까 ")
                .contentColor(getResources().getColor(R.color.text_lgray))
                .positiveText("확인")
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        finish();
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
