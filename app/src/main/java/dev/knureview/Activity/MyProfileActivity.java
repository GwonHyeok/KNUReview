package dev.knureview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.knureview.Adapter.NavigationDrawerAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.Cookie;
import dev.knureview.VO.GradeVO;
import dev.knureview.VO.StudentVO;

/**
 * Created by DavidHa on 2015. 11. 23..
 */
public class MyProfileActivity extends ActionBarActivity {

    private static final String LOGIN_RESULT = "loginResult";
    private static final String STUDENT_NUMBER="studentNumber";
    private static final int CUR_POSITION = 2;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ListView drawer;
    private NavigationDrawerAdapter drawerAdapter;

    private Typeface nanumFont;

    private TextView headerTxt;
    private TextView bottomTxt;

    private ImageView backgroundImg;

    private TextView stdNoTxt;
    private TextView belongTxt;
    private TextView majorTxt;
    private TextView reviewCntTxt;
    private TextView reviewAuthTxt;
    private TextView talkCntTxt;
    private TextView talkWarningTxt;

    private SharedPreferencesActivity pref;
    private String stdNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        //adapter
        drawer = (ListView) findViewById(R.id.drawer);
        drawerAdapter = new NavigationDrawerAdapter(this, R.layout.layout_drawer_row, CUR_POSITION);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listHeader = inflater.inflate(R.layout.layout_drawer_header, null);
        drawer.addHeaderView(listHeader);
        drawer.setAdapter(drawerAdapter);
        drawer.setOnItemClickListener(listViewListener);

        //font
        nanumFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/NanumGothic.ttf");
        headerTxt = (TextView) listHeader.findViewById(R.id.headerTxt);
        bottomTxt = (TextView) findViewById(R.id.bottomTxt);
        headerTxt.setTypeface(nanumFont);
        bottomTxt.setTypeface(nanumFont);


        //pref
        pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences(STUDENT_NUMBER,"");

        backgroundImg = (ImageView)findViewById(R.id.backgroundImg);
        stdNoTxt = (TextView)findViewById(R.id.stdNo);
        belongTxt = (TextView)findViewById(R.id.belong);
        majorTxt = (TextView)findViewById(R.id.major);
        reviewCntTxt = (TextView)findViewById(R.id.reviewCnt);
        reviewAuthTxt = (TextView)findViewById(R.id.reviewAuth);
        talkCntTxt = (TextView)findViewById(R.id.talkCnt);
        talkWarningTxt = (TextView)findViewById(R.id.talkWarning);

        new MemberInfo().execute(stdNo);
    }


    AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.setSelectedIndex(position);

            if (id == 0) {
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();

            } else if (id == 1) {
                Intent intent = new Intent(MyProfileActivity.this, MyStoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            } else if (id == 2) {
                //MyProfileActivity
            }
        }
    };

    public void mOnClick(View view) {

    }

    //toggle

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

    }
    private class MemberInfo extends AsyncTask<String, Void, StudentVO>{
        @Override
        protected StudentVO doInBackground(String... params) {
            try{
               return new NetworkUtil().getExistMemberInfo(params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO result) {
            super.onPostExecute(result);
            stdNoTxt.setText("" + result.getStdNo());
            belongTxt.setText(result.getBelong());
            majorTxt.setText(result.getMajor());
            reviewCntTxt.setText(""+result.getReviewCnt());
            int reviewAuth =result.getReviewAuth();
            if(reviewAuth==0){
                reviewAuthTxt.setText("없음");
            }else{
                reviewAuthTxt.setText("있음");
            }
            talkCntTxt.setText(""+result.getTalkCnt());
            talkWarningTxt.setText(""+result.getTalkWarning());
        }
    }

}
