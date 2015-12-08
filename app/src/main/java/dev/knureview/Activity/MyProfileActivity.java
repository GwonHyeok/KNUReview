package dev.knureview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

/**
 * Created by DavidHa on 2015. 11. 23..
 */
public class MyProfileActivity extends ActionBarActivity {

    private static final String LOGIN_RESULT = "loginResult";
    private static final int CUR_POSITION = 2;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ListView drawer;
    private NavigationDrawerAdapter drawerAdapter;

    private Typeface nanumFont;

    private TextView headerTxt;
    private TextView bottomTxt;

    private SharedPreferencesActivity pref;
    private Cookie cookie;

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
        cookie = new Cookie();
        cookie.setIdno(pref.getPreferences("idno", null));
        cookie.setGubn(pref.getPreferences("gubn", null));
        cookie.setName(pref.getPreferences("name", null));
        cookie.setPass(pref.getPreferences("pass", null));
        cookie.setAuto(pref.getPreferences("auto", null));
        cookie.setMjco(pref.getPreferences("mjco", null));
        cookie.setName_e(pref.getPreferences("name_e", null));
        cookie.setJsession(pref.getPreferences("jsession", null));
        new SchoolGrade().execute(cookie);

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
        if (view.getId() == R.id.logoutBtn) {
            pref.savePreferences(LOGIN_RESULT, true);
            startActivity(new Intent(MyProfileActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.stay, R.anim.out_to_up);
            finish();
        }
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

    private class SchoolGrade extends AsyncTask<Cookie, Void, ArrayList<GradeVO>> {
        @Override
        protected ArrayList<GradeVO> doInBackground(Cookie... params) {
            try{
                return new NetworkUtil().getSchoolGrade(params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<GradeVO> list) {
            super.onPostExecute(list);
            Toast.makeText(MyProfileActivity.this, "" + list.get(0).getSchlYear(), 0).show();
        }
    }

}
