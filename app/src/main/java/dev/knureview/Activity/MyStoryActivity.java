package dev.knureview.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
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

import dev.knureview.Adapter.MyStoryAdapter;
import dev.knureview.Adapter.NavigationDrawerAdapter;
import dev.knureview.R;
import dev.knureview.VO.CommentVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class MyStoryActivity extends ActionBarActivity {

    private static final int CUR_POSITION = 1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ListView drawer;
    private NavigationDrawerAdapter drawerAdapter;

    private Typeface nanumFont;
    private TextView headerTxt;
    private TextView bottomTxt;

    private MyStoryAdapter adapter;
    private ListView listView;

    private ArrayList<CommentVO> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        bottomTxt = (TextView) findViewById(R.id.bottomTxt);
        headerTxt.setTypeface(nanumFont);
        bottomTxt.setTypeface(nanumFont);


        data = new ArrayList<CommentVO>();

        CommentVO vo = new CommentVO();
        vo.setImageResource(R.drawable.sample1);
        vo.setDescription("살 빼면 하고싶은것들 모두 다\n적어봐");
        vo.setWriteTime("2시간 전");
        vo.setLikeCnt(13);
        vo.setCommentCnt(9);
        data.add(vo);

        vo = new CommentVO();
        vo.setImageResource(R.drawable.sample2);
        vo.setDescription("설레고 싶다");
        vo.setWriteTime("10시간 전");
        vo.setLikeCnt(32);
        vo.setCommentCnt(17);
        data.add(vo);

        vo = new CommentVO();
        vo.setImageResource(R.drawable.sample3);
        vo.setDescription("내일 드디어 시험!!\n다들 시험 잘 보세요");
        vo.setWriteTime("4시간 전");
        vo.setLikeCnt(4);
        vo.setCommentCnt(1);
        data.add(vo);


        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyStoryAdapter(MyStoryActivity.this, R.layout.layout_mystory_list_row, data);
        listView.setAdapter(adapter);
    }


    AdapterView.OnItemClickListener drawerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.setSelectedIndex(position);

            if (id == 0) {

                Intent intent = new Intent(MyStoryActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            } else if (id == 1) {
                //MyStoryActivity
            } else if (id == 2) {
                Intent intent = new Intent(MyStoryActivity.this, MyProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            }
        }
    };


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


}