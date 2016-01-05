package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.knureview.Adapter.AlarmAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.AlarmVO;
import dev.knureview.VO.CommentVO;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2016. 1. 1..
 */
public class AlarmActivity extends ActionBarActivity {

    private ListView listView;
    private TextView noAlarmTxt;
    private AlarmAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String stdNo;
    private ArrayList<AlarmVO> alarmList;
    private int listPosition;
    private TalkVO vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(listItemListener);
        noAlarmTxt = (TextView) findViewById(R.id.noAlarmTxt);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");

        //refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryLight);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new AlarmList().execute();
        }
    };

    private AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPosition = position;
            int tNo = alarmList.get(position).gettNo();
            int cNo = alarmList.get(position).getcNo();
            if (tNo > 0) {
                new FindAlarmTalk().execute(tNo);
            } else if (cNo > 0) {
                new FindAlarmComment().execute(cNo);
            }
        }
    };

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


    private class AlarmList extends AsyncTask<Void, Void, ArrayList<AlarmVO>> {
        @Override
        protected ArrayList<AlarmVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getAlarmList(stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlarmVO> result) {
            super.onPostExecute(result);
            alarmList = result;
            if (result.size() == 0) {
                noAlarmTxt.setVisibility(View.VISIBLE);
            } else {
                noAlarmTxt.setVisibility(View.INVISIBLE);
                adapter = new AlarmAdapter(AlarmActivity.this, R.layout.layout_alarm_list_row, result);
                listView.setAdapter(adapter);
                adapter.showDate();
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }
    }

    private class FindAlarmTalk extends AsyncTask<Integer, Void, TalkVO> {
        @Override
        protected TalkVO doInBackground(Integer... params) {
            try {
                return new NetworkUtil().getAlarmTalk(params[0], stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TalkVO result) {
            super.onPostExecute(result);
            Intent intent = new Intent(AlarmActivity.this, StDetailActivity.class);
            intent.putExtra("tNo", result.gettNo());
            intent.putExtra("pictureURL", result.getPictureURL());
            intent.putExtra("writerStdNo", result.getStdNo());
            intent.putExtra("description", result.getDescription());
            intent.putExtra("writeTime", result.getWriteTime());
            intent.putExtra("likeCnt", result.getLikeCnt());
            intent.putExtra("commentCnt", result.getCommentCnt());
            if(result.getIsLike() == 1) {
                intent.putExtra("like", "like");
            }
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    }

    private class FindAlarmComment extends AsyncTask<Integer, Void, CommentVO> {
        @Override
        protected CommentVO doInBackground(Integer... params) {
            try {
                return new NetworkUtil().getAlarmComment(params[0], stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CommentVO result) {
            super.onPostExecute(result);
            Intent intent = new Intent(AlarmActivity.this, StDetailActivity.class);
            intent.putExtra("toDo", "comment");
            intent.putExtra("cNo", result.getCno());
            intent.putExtra("tNo", result.gettNo());
            intent.putExtra("pictureURL", result.getPictureURL());
            intent.putExtra("writerStdNo", result.getStdNo());
            intent.putExtra("description", result.getDescription());
            intent.putExtra("writeTime", result.getWriteTime());
            intent.putExtra("likeCnt", result.getLikeCnt());
            if(result.getIsLike() == 1) {
                intent.putExtra("like", "like");
            }
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AlarmList().execute();
    }
}
