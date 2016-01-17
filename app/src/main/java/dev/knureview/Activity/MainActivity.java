package dev.knureview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import dev.knureview.Activity.ProfileDetail.ContactActivity;
import dev.knureview.Adapter.NavigationDrawerAdapter;
import dev.knureview.Fragment.PageFragment;
import dev.knureview.R;
import dev.knureview.Util.BackPressCloseHandler;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.StudentVO;

public class MainActivity extends ActionBarActivity {
    private static final int CUR_POSITION = 0;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ListView drawer;
    private NavigationDrawerAdapter drawerAdapter;

    private Typeface nanumFont;
    private TextView headerTxt;
    private TextView bottomTxt;
    private String stdNo;
    private SharedPreferencesActivity pref;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref
        pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo","");


        //toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        //drawer adapter
        drawer = (ListView) findViewById(R.id.drawer);
        drawerAdapter = new NavigationDrawerAdapter(this, R.layout.layout_drawer_row, CUR_POSITION);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listHeader = inflater.inflate(R.layout.layout_drawer_header, null);
        drawer.addHeaderView(listHeader);
        drawer.setAdapter(drawerAdapter);
        drawer.setOnItemClickListener(drawerListener);

        //font
        nanumFont = Typeface.createFromAsset(getResources().getAssets(), "fonts/NanumGothic.ttf");
        headerTxt = (TextView) listHeader.findViewById(R.id.headerTxt);
        bottomTxt = (TextView)findViewById(R.id.bottomTxt);
        headerTxt.setTypeface(nanumFont);
        bottomTxt.setTypeface(nanumFont);

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

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    AdapterView.OnItemClickListener drawerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.setSelectedIndex(position);

            if (id == 0) {
                //MainActivity
            } else if (id == 1) {
                Intent intent = new Intent(MainActivity.this, StoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            } else if (id == 2) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            }else if(id == 3){
                new MaterialDialog.Builder(MainActivity.this)
                        .title("시간표 기능 업데이트 알림")
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("시간표 기능은 단순히 시간표만 보여주는 기능 뿐만 아니라 같이 수업듣는 학우들끼리 서로 정보를 공유하는 기능을 제공할 예정입니다.\n2월 27일에 업데이트 될 예정이오니 많이 기대해주세요~")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        new MemberInfo().execute();
    }

    private class MemberInfo extends AsyncTask<Void, Void, StudentVO> {
        @Override
        protected StudentVO doInBackground(Void... params) {
            try {
                return new NetworkUtil().getExistMemberInfo(stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO vo) {
            super.onPostExecute(vo);
            // initialize reviewAuth
            pref.savePreferences("reviewAuth", vo.getReviewAuth());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
     //   inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void mOnClick(View view){
        if(view.getId() == R.id.devContactLayout){
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }else if(view.getId() == R.id.fab){
            Intent intent = new Intent(MainActivity.this, RvSbjActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    }

    //toggle

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
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
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
