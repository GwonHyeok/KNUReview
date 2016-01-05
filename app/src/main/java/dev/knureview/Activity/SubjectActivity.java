package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import dev.knureview.Adapter.SubjectAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.VO.SubjectVO;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by DavidHa on 2016. 1. 4..
 */
public class SubjectActivity extends ActionBarActivity {
    private StickyListHeadersListView stickyList;
    private SubjectAdapter adapter;
    private String dName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        stickyList = (StickyListHeadersListView)findViewById(R.id.stickyList);
        Intent intent = getIntent();
        dName = intent.getStringExtra("dName");

        new SubjectList().execute();

    }

    private class SubjectList extends AsyncTask<Void, Void, ArrayList<SubjectVO>>{
        @Override
        protected ArrayList<SubjectVO> doInBackground(Void... params) {
            try{
                return new NetworkUtil().getSubjectList(dName);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SubjectVO> result) {
            super.onPostExecute(result);
            adapter = new SubjectAdapter(SubjectActivity.this, R.layout.layout_subject_list_row, result);
            stickyList.setAdapter(adapter);
        }
    }

}
