package dev.knureview.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 11. 22..
 */
public class SearchActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
