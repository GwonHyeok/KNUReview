package dev.knureview.Activity.ProfileDetail;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 12. 25..
 */
public class VersionActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
