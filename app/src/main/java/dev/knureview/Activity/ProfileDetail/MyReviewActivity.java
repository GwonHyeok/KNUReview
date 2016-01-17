package dev.knureview.Activity.ProfileDetail;

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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.Activity.RvDetailActivity;
import dev.knureview.Adapter.RvPreviewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.ReviewVO;

/**
 * Created by DavidHa on 2016. 1. 16..
 */
public class MyReviewActivity extends ActionBarActivity {

    @Bind(R.id.listView) ListView listView;
    @Bind(R.id.totalReview) TextView totalReviewTxt;
    private ArrayList<ReviewVO> rvList;
    private RvPreviewAdapter adapter;
    private String stdNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        ButterKnife.bind(this);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");

        //setOnClickListener
        listView.setOnItemClickListener(clickItemListener);
    }

    AdapterView.OnItemClickListener clickItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyReviewActivity.this, RvDetailActivity.class);
            intent.putExtra("sbjName", rvList.get(position).getSbjName());
            intent.putExtra("edit","edit");
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    };

    private class ReviewList extends AsyncTask<Void, Void, ArrayList<ReviewVO>> {
        @Override
        protected ArrayList<ReviewVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getReviewList(stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewVO> result) {
            super.onPostExecute(result);
            rvList = result;
            totalReviewTxt.setText("총 " + result.size() + "개의 리뷰가 있습니다.");
            adapter = new RvPreviewAdapter(MyReviewActivity.this,
                    R.layout.layout_my_review_list_row, result);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ReviewList().execute();
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
