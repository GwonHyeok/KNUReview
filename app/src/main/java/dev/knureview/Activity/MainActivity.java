package dev.knureview.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import dev.knureview.Adapter.LeftDrawerAdapter;
import dev.knureview.Fragment.PageFragment;
import dev.knureview.R;

public class MainActivity extends ActionBarActivity {
    private static final int CUR_POSITION = 0;
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
        setContentView(R.layout.activity_main);

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

        //fragment
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("1학기", PageFragment.class)
                .add("2학기", PageFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

    AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listViewAdapter.setSelectedIndex(position);

            if (id == 0) {

            } else if (id == 1) {
                Intent intent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toogle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);

        }

        return super.onOptionsItemSelected(item);
    }

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
