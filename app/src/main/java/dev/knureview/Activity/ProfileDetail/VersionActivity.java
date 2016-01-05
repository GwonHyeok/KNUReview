package dev.knureview.Activity.ProfileDetail;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.knureview.Activity.MyProfileActivity;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;

/**
 * Created by DavidHa on 2015. 12. 25..
 */
public class VersionActivity extends ActionBarActivity {
    private TextView curVersionTxt;
    private TextView latVersionTxt;
    private Button downloadBtn;
    private boolean canDownLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        curVersionTxt = (TextView) findViewById(R.id.currentVersionTxt);
        latVersionTxt = (TextView) findViewById(R.id.latestVersionTxt);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);
        new VersionCheck().execute();
    }

    public void mOnClick(View view){
        if(view.getId() == R.id.downloadBtn){
            if(canDownLoad){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dev.knureview"));
                startActivity(intent);
            }
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

    private class VersionCheck extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return new NetworkUtil().getLatestVersion();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            String latVersion = s[0];
            latVersionTxt.setText(latVersion);
            if (!curVersionTxt.getText().toString().equals(latVersion)) {
                downloadBtn.setText("최신 버전 업데이트 하기");
                downloadBtn.setTextColor(getResources().getColor(R.color.white));
                downloadBtn.setBackground(getDrawable(R.drawable.fill_rounded_btn));
                canDownLoad = true;
            }else{
                canDownLoad = false;
            }
        }
    }
}
