package dev.knureview.Activity;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import dev.knureview.Adapter.MyStoryAdapter;
import dev.knureview.Adapter.NavigationDrawerAdapter;
import dev.knureview.R;
import dev.knureview.Util.BackPressCloseHandler;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class StoryActivity extends ActionBarActivity {

    public static Activity activity;
    private static final int CUR_POSITION = 1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ListView drawer;
    private NavigationDrawerAdapter drawerAdapter;
    private FloatingActionButton fab;

    private Typeface nanumFont;
    private TextView headerTxt;
    private TextView bottomTxt;

    private MyStoryAdapter adapter;
    private ListView listView;

    private ArrayList<TalkVO> talkList;
    private int listPosition;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        activity = StoryActivity.this;

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

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(listItemListener);

        //fab
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new TalkList().execute();
    }

    public void mOnClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent intent = new Intent(StoryActivity.this, StEditActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }else if(view.getId() == R.id.devLayout){

        }
    }

    AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPosition = position;
            Intent intent = new Intent(StoryActivity.this, StDetailActivity.class);
            intent.putExtra("tNo", talkList.get(position).gettNo());
            intent.putExtra("pictureURL", talkList.get(position).getPictureURL());
            intent.putExtra("stdNo", talkList.get(position).getStdNo());
            intent.putExtra("description", talkList.get(position).getDescription());
            intent.putExtra("writeTime", talkList.get(position).getWriteTime());
            intent.putExtra("likeCnt", talkList.get(position).getLikeCnt());
            intent.putExtra("commentCnt", talkList.get(position).getCommentCnt());
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    };

    AdapterView.OnItemClickListener drawerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.setSelectedIndex(position);

            if (id == 0) {
                /*
                Intent intent = new Intent(StoryActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
                */
                new MaterialDialog.Builder(StoryActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("아직 준비중이에요 1월 17일에 봬요~")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else if (id == 1) {
                //StoryActivity
            } else if (id == 2) {
                Intent intent = new Intent(StoryActivity.this, MyProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            }
        }
    };


    //toggle

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
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

    private class TalkList extends AsyncTask<Void, Void, ArrayList<TalkVO>> {
        @Override
        protected ArrayList<TalkVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getAllTalkList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TalkVO> data) {
            super.onPostExecute(data);
            talkList = data;
            adapter = new MyStoryAdapter(StoryActivity.this, R.layout.layout_story_list_row, data);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setSelection(listPosition);
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}