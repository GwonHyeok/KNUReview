package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import dev.knureview.Adapter.SubjectAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.SubjectVO;

/**
 * Created by DavidHa on 2016. 1. 4..
 */
public class SubjectActivity extends ActionBarActivity {
    private TextView totalSubjectTxt;
    private ListView listView;
    private SubjectAdapter adapter;
    private ArrayList<SubjectVO> sbjList;
    private String dName;
    private String gradeName;
    private int reviewAuth;
    private int term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalSubjectTxt = (TextView)findViewById(R.id.totalSubject);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(clickListener);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        reviewAuth= pref.getPreferences("reviewAuth",0);

        Intent intent = getIntent();
        dName = intent.getStringExtra("dName");
        gradeName = intent.getStringExtra("gradeName");
        term = intent.getIntExtra("term", 0);
        getSupportActionBar().setTitle(dName + "(" + term + "학기" + ")");
        new SubjectList().execute();
    }

    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(reviewAuth == 1) {
                Intent intent = new Intent(SubjectActivity.this, ReviewActivity.class);
                intent.putExtra("sNo", sbjList.get(position).getsNo());
                intent.putExtra("sName", sbjList.get(position).getsName());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }else{
                new MaterialDialog.Builder(SubjectActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("수강리뷰를 볼 수 있는 권한이 없습니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
    };

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
            sbjList = result;
            adapter = new SubjectAdapter(SubjectActivity.this, R.layout.layout_subject_list_row, result);
            totalSubjectTxt.setText("총 " + result.size() + "개의 과목이 있습니다.");
            adapter.setDeptName(dName);
            listView.setAdapter(adapter);
            listView.setSelection(adapter.getGradePosition(gradeName));
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
