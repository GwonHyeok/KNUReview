package dev.knureview.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import dev.knureview.R;

/**
 * Created by DavidHa on 2016. 1. 7..
 */
public class ReviewActivity extends ActionBarActivity {
    private String sName;
    private int sNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sNo = intent.getIntExtra("sNo", 0);
        sName = intent.getStringExtra("sName");
        getSupportActionBar().setTitle(sName);
    }

    //private class ReviewList extends AsyncTask<Void, Void, ArrayList<>>
}
