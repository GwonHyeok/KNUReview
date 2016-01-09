package dev.knureview.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import dev.knureview.Adapter.AutoCompleteAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.VO.DepartmentVO;
import dev.knureview.VO.SubjectVO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class ReviewEditActivity extends ActionBarActivity {

    private EditText inputYear;
    private EditText inputTerm;
    private EditText inputDept;
    private EditText inputSbj;
    private EditText inputProf;
    private ListView deptListView;
    private ListView sbjListView;
    private ListView profListView;
    private AutoCompleteAdapter deptAdapter;
    private AutoCompleteAdapter sbjAdapter;
    private AutoCompleteAdapter profAdapter;
    private ArrayList<String> deptStrArray;
    private ArrayList<String> sbjStrArray;
    private ArrayList<String> profStrArray;

    private boolean nextSbj = false;
    private boolean nextProf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_edit);

        inputYear = (EditText)findViewById(R.id.inputYear);
        inputTerm = (EditText)findViewById(R.id.inputTerm);
        inputDept = (EditText) findViewById(R.id.inputDept);
        inputSbj = (EditText) findViewById(R.id.inputSbj);
        inputProf = (EditText) findViewById(R.id.inputProf);
        deptListView = (ListView) findViewById(R.id.deptList);
        sbjListView = (ListView) findViewById(R.id.sbjList);
        profListView = (ListView) findViewById(R.id.profList);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //listView event
        deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputDept.setText(deptAdapter.getItemList().get(position));
                nextSbj = true;
                setRefreshAdapter(deptAdapter, deptListView, "");
                new SubjectList().execute(inputDept.getText().toString());
            }
        });

        sbjListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nextProf = true;
                setRefreshAdapter(deptAdapter, deptListView, "");
                inputSbj.setText(sbjAdapter.getItemList().get(position));
            }
        });

        profListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //학과
        inputDept.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextSbj = false;
                inputSbj.setText("");
                setRefreshAdapter(deptAdapter, deptListView, s.toString());
            }
        });

        //과목
        inputSbj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nextSbj) {
                    nextProf = false;
                    setRefreshAdapter(sbjAdapter, sbjListView, s.toString());
                }
            }
        });

        new DepartmentList().execute();
    }

    public void setRefreshAdapter(AutoCompleteAdapter adapter, ListView listView, String str) {
        final int LIST_VIEW_MAX_HEIGHT = (int) PixelUtil.convertPixelsToDp(285, this);
        adapter.refreshListView(str);
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if (adapter.getCount() > 6) {
            params.height = LIST_VIEW_MAX_HEIGHT * 5;
        } else {
            params.height = adapter.getCount() * LIST_VIEW_MAX_HEIGHT;
        }
        listView.setLayoutParams(params);
        adapter.notifyDataSetChanged();
    }


    private class DepartmentList extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getDepartmentList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            deptStrArray = result;
            deptAdapter = new AutoCompleteAdapter(ReviewEditActivity.this, result);
            deptListView.setAdapter(deptAdapter);
        }
    }

    private class SubjectList extends AsyncTask<String, Void, ArrayList<SubjectVO>> {
        @Override
        protected ArrayList<SubjectVO> doInBackground(String... params) {
            try {
                return new NetworkUtil().getSubjectList(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SubjectVO> result) {
            super.onPostExecute(result);
            sbjStrArray = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                sbjStrArray.add(result.get(i).getsName());
            }
            sbjAdapter = new AutoCompleteAdapter(ReviewEditActivity.this, sbjStrArray);
            sbjListView.setAdapter(sbjAdapter);
        }
    }
}
