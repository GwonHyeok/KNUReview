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

import java.util.ArrayList;

import dev.knureview.Adapter.SimeListViewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.LectureVO;

/**
 * Created by DavidHa on 2016. 1. 10..
 */
public class RvSbjActivity extends ActionBarActivity {
    private EditText inputYear;
    private ImageView yearImage;
    private ListView yearListView;
    private ListView sbjListView;
    private TextView termTxt;
    private TextView alarmTxt;

    private SimeListViewAdapter yearAdapter;
    private SimeListViewAdapter sbjAdapter;
    private ArrayList<LectureVO> lectList;
    private ArrayList<String> yearStrArray;
    private ArrayList<String> sbjStrArray;
    private String stdNo;
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_subject);

        inputYear = (EditText) findViewById(R.id.inputYear);
        yearImage = (ImageView) findViewById(R.id.yearImage);
        yearListView = (ListView) findViewById(R.id.yearList);
        sbjListView = (ListView) findViewById(R.id.sbjList);
        termTxt = (TextView) findViewById(R.id.termTxt);
        alarmTxt = (TextView) findViewById(R.id.alarmTxt);

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
        new StudentLecture().execute();
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

            if (selectedYearStr.equals("전체")) {
                for (int i = 0; i < lectList.size(); i++) {
                    sbjStrArray.add(lectList.get(i).getSbjName());
                }
            } else {
                sbjStrArray.clear();
                int year = Integer.parseInt(selectedYearStr.substring(0, 4));
                for (int i = 0; i < lectList.size(); i++) {
                    if (lectList.get(i).getYear() == year) {
                        sbjStrArray.add(lectList.get(i).getSbjName());
                    }
                }
            }
            sbjAdapter.refreshListView(sbjStrArray);
            setListViewHeight(sbjAdapter, sbjListView, true);
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

    public void setListViewHeight(SimeListViewAdapter adapter, ListView listView, boolean isBig) {
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
            lectList = result;
            sbjStrArray = new ArrayList<>();
            yearStrArray = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getIsReview() == 0) {
                    yearStrArray.add(String.valueOf(result.get(i).getYear()));
                    sbjStrArray.add(result.get(i).getSbjName());
                }
            }
            yearAdapter = new SimeListViewAdapter(RvSbjActivity.this, R.layout.layout_auto_list_row, yearStrArray);
            sbjAdapter = new SimeListViewAdapter(RvSbjActivity.this, R.layout.layout_rv_sbj_list_row, sbjStrArray);
            yearAdapter.setYearListView();

            setListViewHeight(yearAdapter, yearListView, false);
            setListViewHeight(sbjAdapter, sbjListView, true);
            yearListView.setAdapter(yearAdapter);
            sbjListView.setAdapter(sbjAdapter);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
