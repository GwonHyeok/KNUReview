package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.Adapter.ReviewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.VO.ReviewVO;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class ReviewActivity extends ActionBarActivity {
    private String sName;
    private int sNo;
    private ReviewAdapter adapter;
    @Bind(R.id.totalReview) TextView totalReviewTxt;
    @Bind(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sNo = intent.getIntExtra("sNo", 0);
        sName = intent.getStringExtra("sName");
        getSupportActionBar().setTitle(sName);
    }

    private class ReviewList extends AsyncTask<Void, Void, ArrayList<ReviewVO>> {
        @Override
        protected ArrayList<ReviewVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getReviewListFromSbj(sNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewVO> result) {
            super.onPostExecute(result);
            adapter = new ReviewAdapter(ReviewActivity.this, R.layout.layout_my_review_list_row, result);
            listView.setAdapter(adapter);
            totalReviewTxt.setText("총 " + result.size() + "개의 리뷰가 있습니다.");
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
