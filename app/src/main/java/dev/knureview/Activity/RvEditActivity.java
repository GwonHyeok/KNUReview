package dev.knureview.Activity;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import dev.knureview.Adapter.SimeListViewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.ProfVO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class RvEditActivity extends ActionBarActivity {

    private EditText inputSbj;
    private EditText inputProf;
    private RatingBar difcRating;
    private RatingBar asignRating;
    private RatingBar atendRating;
    private RatingBar gradeRating;
    private RatingBar achivRating;
    private ListView profListView;

    private SimeListViewAdapter profAdapter;
    private ArrayList<String> profStrArray;

    private String stdNo;
    private String sbjName;
    private int difcCnt;
    private int asignCnt;
    private int atendCnt;
    private int gradeCnt;
    private int achivCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_edit);

        inputSbj = (EditText) findViewById(R.id.inputSbj);
        inputProf = (EditText) findViewById(R.id.inputProf);
        difcRating = (RatingBar) findViewById(R.id.difcRating);
        asignRating = (RatingBar) findViewById(R.id.asignRating);
        atendRating = (RatingBar) findViewById(R.id.atendRating);
        gradeRating = (RatingBar) findViewById(R.id.gradeRating);
        achivRating = (RatingBar) findViewById(R.id.achivRating);

        profListView = (ListView) findViewById(R.id.profList);
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

        //intent
        Intent intent = getIntent();
        sbjName = intent.getStringExtra("sbjName");
        inputSbj.setText(sbjName);
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
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("수강리뷰를 작성해주셔서 감사합니다.\n해당 과목에 대한 강의평을 남기실 수 있습니다.\n지금 강의평을 작성하시겠습니까?")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("작성하기")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                Intent intent = new Intent(RvEditActivity.this, RvDetailActivity.class);
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
            } else {
                new MaterialDialog.Builder(RvEditActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("수강리뷰를 다 작성하시지 않았습니다.\n모든 항목을 작성하셔야 수강리뷰 등록이 됩니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

}
