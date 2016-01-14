package dev.knureview.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import dev.knureview.R;

/**
 * Created by DavidHa on 2016. 1. 12..
 */
public class RvDetailActivity extends ActionBarActivity {
    private String sbjName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_detail);

        //intent
        Intent intent = getIntent();
        sbjName = intent.getStringExtra("sbjName");

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("("+sbjName+")"+"리뷰");
    }

}
