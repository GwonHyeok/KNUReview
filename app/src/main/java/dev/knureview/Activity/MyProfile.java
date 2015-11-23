package dev.knureview.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dev.knureview.Adapter.LeftDrawerAdapter;
import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 11. 23..
 */
public class MyProfile extends ActionBarActivity {
    private static final int CUR_POSITION = 1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toogle;
    private ListView listView;
    private LeftDrawerAdapter listViewAdapter;

    private Typeface nanumFont;
    private Typeface archiveFont;
    private TextView toolbarTxt;
    private TextView headerTxt;
    private TextView bottomTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //font
        nanumFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/NanumGothic.ttf");
        archiveFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/Archive.otf");
        toolbarTxt = (TextView)findViewById(R.id.toolbarTxt);
        headerTxt = (TextView) findViewById(R.id.headerTxt);
        bottomTxt = (TextView) findViewById(R.id.bottomTxt);
        toolbarTxt.setTypeface(archiveFont);
        headerTxt.setTypeface(nanumFont);
        bottomTxt.setTypeface(nanumFont);

        //toogle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toogle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toogle);

        //adapter
        listView = (ListView) findViewById(R.id.listView);
        listViewAdapter = new LeftDrawerAdapter(this, R.layout.layout_left_drawer_row, CUR_POSITION);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(listViewListener);
    }

    AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listViewAdapter.setSelectedIndex(position);

            if (id == 0) {
                Intent intent = new Intent(MyProfile.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();

            } else if (id == 1) {

            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toogle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toogle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

    }
}
