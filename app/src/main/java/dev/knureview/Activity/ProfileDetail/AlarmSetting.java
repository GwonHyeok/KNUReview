package dev.knureview.Activity.ProfileDetail;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rey.material.widget.CheckBox;

import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by DavidHa on 2016. 1. 5..
 */
public class AlarmSetting extends ActionBarActivity {
    private CheckBox pushCheck;
    private CheckBox silentCheck;
    private SharedPreferencesActivity pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pushCheck = (CheckBox) findViewById(R.id.pushCheck);
        silentCheck = (CheckBox) findViewById(R.id.silentCheck);

        //pref
        pref = new SharedPreferencesActivity(this);
        String pushAlarmSound = pref.getPreferences("pushAlarmSound", "default");
        String pushAlarmSetting = pref.getPreferences("pushAlarmSetting", "default");

        if (pushAlarmSound.equals("default")) {
            silentCheck.setChecked(true);
        } else {
            silentCheck.setChecked(false);
        }

        if(pushAlarmSetting.equals("default")){
            pushCheck.setChecked(true);
        }else{
            pushCheck.setChecked(false);
        }


    }

    public void mOnClick(View view) {
        if (view.getId() == R.id.pushSetting) {
            if (pushCheck.isChecked()) {
                pushCheck.setChecked(false);
                silentCheck.setChecked(false);
                pref.savePreferences("pushAlarmSetting","silent");
                pref.savePreferences("pushAlarmSound", "silent");
            } else {
                pushCheck.setChecked(true);
                pref.savePreferences("pushAlarmSetting", "default");
            }
        } else if (view.getId() == R.id.silentSetting) {
            if (silentCheck.isChecked()) {
                silentCheck.setChecked(false);
                pref.savePreferences("pushAlarmSound","silent");
            } else {
                silentCheck.setChecked(true);
                pref.savePreferences("pushAlarmSound","default");
            }
        } else if (view.getId() == R.id.pushCheck) {
            if (pushCheck.isChecked()) {
                pushCheck.setChecked(true);
                pref.savePreferences("pushAlarmSetting", "default");
            } else {
                pushCheck.setChecked(false);
                silentCheck.setChecked(false);
                pref.savePreferences("pushAlarmSetting","silent");
                pref.savePreferences("pushAlarmSound", "silent");
            }
        } else if (view.getId() == R.id.silentCheck) {
            if (silentCheck.isChecked()) {
                silentCheck.setChecked(true);
                pref.savePreferences("pushAlarmSound","default");
            } else {
                silentCheck.setChecked(false);
                pref.savePreferences("pushAlarmSound","silent");
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

}
