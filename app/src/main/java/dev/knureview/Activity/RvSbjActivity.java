package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.Adapter.SimpleListViewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.Util.TimeUtil;
import dev.knureview.VO.LectureVO;

/**
 * Created by DavidHa on 2016. 1. 10..
 */
public class RvSbjActivity extends ActionBarActivity {
    @Bind(R.id.inputYear)
    EditText inputYear;
    @Bind(R.id.yearImage)
    ImageView yearImage;
    @Bind(R.id.yearList)
    ListView yearListView;
    @Bind(R.id.sbjList)
    ListView sbjListView;
    @Bind(R.id.termTxt)
    TextView termTxt;
    @Bind(R.id.alarmTxt)
    TextView alarmTxt;

    private SimpleListViewAdapter yearAdapter;
    private SimpleListViewAdapter sbjAdapter;
    private ArrayList<LectureVO> lectList;
    private ArrayList<String> yearStrArray;
    private ArrayList<String> sbjStrArray;
    private String yearStr;
    private String stdNo;
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_subject);

        ButterKnife.bind(this);
        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        term = pref.getPreferences("term", "");
        stdNo = pref.getPreferences("stdNo", "");
        termTxt.setText(term);

        inputYear.setOnTouchListener(touchListener);
        yearListView.setOnItemClickListener(yearClickListener);
        sbjListView.setOnItemClickListener(sbjClickListener);

    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (view.getId() == R.id.inputYear) {
                    changeListViewState(yearListView, yearImage);
                }
                return true;
            }
            return false;
        }
    };

    AdapterView.OnItemClickListener yearClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            yearListView.setVisibility(View.GONE);
            alarmTxt.setVisibility(View.INVISIBLE);
            yearImage.setImageResource(R.drawable.expand_arrow_ic);
            String selectedYearStr = yearAdapter.getItemList().get(position);
            inputYear.setText(selectedYearStr);
            refresh(selectedYearStr);
        }
    };

    AdapterView.OnItemClickListener sbjClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(RvSbjActivity.this, RvEditActivity.class);
            intent.putExtra("sbjName", sbjAdapter.getItemList().get(position));
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    };

    public void refresh(String selectedYearStr) {
        if (selectedYearStr.equals("전체")) {
            for (int i = 0; i < lectList.size(); i++) {
                if (lectList.get(i).getIsReview() == 0) {
                    sbjStrArray.add(lectList.get(i).getSbjName());
                }
            }
        } else {
            sbjStrArray.clear();
            int year = Integer.parseInt(selectedYearStr.substring(0, 4));
            for (int i = 0; i < lectList.size(); i++) {
                if (lectList.get(i).getYear() == year
                        && lectList.get(i).getIsReview() == 0) {
                    sbjStrArray.add(lectList.get(i).getSbjName());
                }
            }
        }
        sbjAdapter.refreshListView(sbjStrArray);
        setListViewHeight(sbjAdapter, sbjListView, true);
    }

    public void setListViewHeight(SimpleListViewAdapter adapter, ListView listView, boolean isBig) {
        int LIST_VIEW_MAX_HEIGHT = (int) PixelUtil.convertPixelsToDp(285, this);
        if (isBig) {
            LIST_VIEW_MAX_HEIGHT = (int) PixelUtil.convertPixelsToDp(455, this);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = adapter.getCount() * LIST_VIEW_MAX_HEIGHT;
        listView.setLayoutParams(params);
        adapter.notifyDataSetChanged();
    }


    public void changeListViewState(ListView listView, ImageView imageView) {
        if (listView.getVisibility() == View.GONE) {
            listView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.collapse_arrow_ic);
        } else if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.expand_arrow_ic);
        }
    }

    private class StudentLecture extends AsyncTask<Void, Void, ArrayList<LectureVO>> {
        @Override
        protected ArrayList<LectureVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getMyLecture(stdNo, term.substring(0, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<LectureVO> result) {
            super.onPostExecute(result);
            try {
                lectList = result;
                sbjStrArray = new ArrayList<>();
                yearStrArray = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getIsReview() == 0) {
                        yearStrArray.add(String.valueOf(result.get(i).getYear()));
                        sbjStrArray.add(result.get(i).getSbjName());
                    }
                }
                yearAdapter = new SimpleListViewAdapter(RvSbjActivity.this, R.layout.layout_auto_list_row, yearStrArray);
                sbjAdapter = new SimpleListViewAdapter(RvSbjActivity.this, R.layout.layout_rv_sbj_list_row, sbjStrArray);
                yearAdapter.setYearListView();

                setListViewHeight(yearAdapter, yearListView, false);
                setListViewHeight(sbjAdapter, sbjListView, true);
                yearListView.setAdapter(yearAdapter);
                sbjListView.setAdapter(sbjAdapter);
                if (!yearStr.equals("")) {
                    refresh(yearStr);
                    alarmTxt.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                int year = Integer.parseInt(new TimeUtil().getCurYear()) + 1;
                new MaterialDialog.Builder(RvSbjActivity.this)
                        .title("수강리뷰 평가 불가능")
                        .titleColor(getResources().getColor(R.color.black))
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("수강리뷰를 평가할 수 있는 과목이 없습니다.\n신입생들은 " +year +"년부터 평가하실 수 있습니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                finish();
                            }
                        })
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .cancelable(false)
                        .maxIconSize(96)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        yearStr = inputYear.getText().toString();
        new StudentLecture().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
