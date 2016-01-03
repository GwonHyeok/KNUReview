package dev.knureview.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.knureview.Adapter.AlarmAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.AlarmVO;

/**
 * Created by DavidHa on 2016. 1. 1..
 */
public class AlarmActivity extends ActionBarActivity {
    private ListView listView;
    private TextView noAlarmTxt;
    private AlarmAdapter adapter;
    private String stdNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listView);
       // listView.setOnItemClickListener(listItemListener);
        noAlarmTxt = (TextView)findViewById(R.id.noAlarmTxt);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo","");

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


    private class AlarmList extends AsyncTask<Void, Void, ArrayList<AlarmVO>>{
        @Override
        protected ArrayList<AlarmVO> doInBackground(Void... params) {
            try{
                return new NetworkUtil().getAlarmList(stdNo);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlarmVO> result) {
            super.onPostExecute(result);
            if(result.size()==0){
                noAlarmTxt.setVisibility(View.VISIBLE);
            }else {
                noAlarmTxt.setVisibility(View.INVISIBLE);
                adapter = new AlarmAdapter(AlarmActivity.this, R.layout.layout_alarm_list_row, result);
                listView.setAdapter(adapter);
                adapter.showDate();
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AlarmList().execute();
    }
}
