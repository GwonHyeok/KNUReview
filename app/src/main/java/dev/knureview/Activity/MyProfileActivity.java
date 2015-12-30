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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import dev.knureview.Activity.ProfileDetail.ContactActivity;
import dev.knureview.Activity.ProfileDetail.MyStoryActivity;
import dev.knureview.Activity.ProfileDetail.TicketActivity;
import dev.knureview.Activity.ProfileDetail.VersionActivity;
import dev.knureview.Adapter.NavigationDrawerAdapter;
import dev.knureview.R;
import dev.knureview.Util.BackPressCloseHandler;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by DavidHa on 2015. 11. 23..
 */
public class MyProfileActivity extends ActionBarActivity {

    private static final String LOGIN_RESULT = "loginResult";
    private static final String EASTER_EGG = "easterEgg";
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
    private String belong;
    private String major;
    private int reviewCnt;
    private int reviewAuth;
    private int talkCnt;
    private int talkWarning;
    private int talkAuth;
    private int talkTicket;
    private int devCount = 0;
    private boolean getTicket = false;
    private BackPressCloseHandler backPressCloseHandler;

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
        stdNo = pref.getPreferences("stdNo", "");
        belong = pref.getPreferences("belong", "");
        major = pref.getPreferences("major", "");
        reviewCnt = pref.getPreferences("reviewCnt", 0);
        reviewAuth = pref.getPreferences("reviewAuth", 0);
        talkCnt = pref.getPreferences("talkCnt", 0);
        talkWarning = pref.getPreferences("talkWarning", 0);
        talkAuth = pref.getPreferences("talkAuth", 0);
        talkTicket = pref.getPreferences("talkTicket", 0);
        getTicket = pref.getPreferences(EASTER_EGG, false);

        backgroundImg = (ImageView) findViewById(R.id.cardImage);
        stdNoTxt = (TextView) findViewById(R.id.stdNo);
        belongTxt = (TextView) findViewById(R.id.belong);
        majorTxt = (TextView) findViewById(R.id.major);
        reviewCntTxt = (TextView) findViewById(R.id.reviewCnt);
        reviewAuthTxt = (TextView) findViewById(R.id.reviewAuth);
        talkCntTxt = (TextView) findViewById(R.id.talkCnt);
        talkWarningTxt = (TextView) findViewById(R.id.talkWarning);

        stdNoTxt.setText(stdNo);
        belongTxt.setText(belong);
        majorTxt.setText(major);
        reviewCntTxt.setText(String.valueOf(reviewCnt));
        if (reviewAuth == 0) {
            reviewAuthTxt.setText("없음");
        } else {
            reviewAuthTxt.setText("있음");
        }
        talkCntTxt.setText(String.valueOf(talkCnt));
        talkWarningTxt.setText(String.valueOf(talkWarning));
        backPressCloseHandler = new BackPressCloseHandler(this);
    }


    AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerAdapter.setSelectedIndex(position);

            if (id == 0) {
                /*
                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
                */
                new MaterialDialog.Builder(MyProfileActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("아직 준비중이에요 1월 17일에 봬요~")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else if (id == 1) {
                Intent intent = new Intent(MyProfileActivity.this, StoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
                finish();
            } else if (id == 2) {
                //MyProfileActivity
            }
        }
    };

    public void mOnClick(View view) {
        if(view.getId() == R.id.devContactLayout){
            Intent intent = new Intent(MyProfileActivity.this, ContactActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }

        if (view.getId() == R.id.versionLayout) {
            Intent intent = new Intent(this, VersionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.alarmLayout) {

        } else if (view.getId() == R.id.courseReviewLayout) {
            new MaterialDialog.Builder(MyProfileActivity.this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .content("아직 준비중이에요 1월 17일에 봬요~")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .positiveText("확인")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .show();

        } else if (view.getId() == R.id.myStoryLayout) {
            Intent intent = new Intent(MyProfileActivity.this, MyStoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.storyTicketLayout) {
            Intent intent = new Intent(MyProfileActivity.this, TicketActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.contactLayout) {
            Intent intent = new Intent(MyProfileActivity.this, ContactActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.devLayout) {
            devCount++;
            if (devCount == 5) {
                Toast.makeText(this, "왜 눌러 봤어?", Toast.LENGTH_SHORT).show();
            } else if (devCount == 10) {
                Toast.makeText(this, "사실 이건 숨겨진 기능인데...", Toast.LENGTH_SHORT).show();
            } else if (devCount == 20) {
                Toast.makeText(this, "조금 더 눌러봐", Toast.LENGTH_SHORT).show();
            } else if (devCount == 26) {
                if (getTicket) {
                    new MaterialDialog.Builder(this)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .content("티켓 받았으면서 왜 또 눌러?\n이제 티켓 안 줄꺼야! 흥!!")
                            .contentColor(getResources().getColor(R.color.text_lgray))
                            .positiveText("확인")
                            .positiveColor(getResources().getColor(R.color.colorPrimary))
                            .cancelable(false)
                            .show();
                } else {
                    new MaterialDialog.Builder(this)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .title("소곤소곤 티켓을 얻었습니다.")
                            .titleColor(getResources().getColor(R.color.black))
                            .content("개발자의 나이는 26살 이야\n넌 몇 살이니?")
                            .contentColor(getResources().getColor(R.color.text_lgray))
                            .positiveText("티켓받기")
                            .positiveColor(getResources().getColor(R.color.colorPrimary))
                            .iconRes(R.drawable.ticket_ic)
                            .maxIconSize(90)
                            .cancelable(false)
                            .show();
                    pref.savePreferences(EASTER_EGG, true);
                }
            } else if (devCount == 27) {
                Toast.makeText(this, "이제 그만 눌러!!", Toast.LENGTH_SHORT).show();
            } else if (devCount > 2 && devCount < 27) {
                Toast.makeText(this, devCount + "번 클릭", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.photoLayout) {
        } else if (view.getId() == R.id.logoutLayout) {
            new MaterialDialog.Builder(this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .title("로그아웃")
                    .titleColor(getResources().getColor(R.color.black))
                    .content("정말로 로그아웃 하시겠습니까?")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .positiveText("확인")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            pref.savePreferences(LOGIN_RESULT, false);
                            Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
                            finish();
                        }
                    })
                    .negativeText("취소")
                    .negativeColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
    }

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

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}
