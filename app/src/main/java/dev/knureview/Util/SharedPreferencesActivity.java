package dev.knureview.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by davidha on 2015. 2. 27..
 */
public class SharedPreferencesActivity {

    Context mContext;

    public SharedPreferencesActivity(Context context) {
        mContext = context;
    }

    public void savePreferences(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void savePreferences(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getPreferences(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public int getPreferences(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getInt(key, value);
        } catch (Exception e) {
            return value;
        }
    }

    public boolean getPreferences(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, value);
        } catch (Exception e) {
            return value;
        }
    }
}
