package dev.knureview.Adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 12. 30..
 */
public class TicketAdapter extends BaseAdapter {
    private static final int ACCESS = 1;
    private static final int DENIAL = 0;
    private Activity activity;
    private int layout;
    private int size;
    private int talkAuth;

    static class ViewHolder {
        private TextView rowText;
        private Button rowBtn;
    }

    public TicketAdapter(Activity activity, int layout, int talkAuth, int size) {
        this.activity = activity;
        this.layout = layout;
        this.size = size;
        this.talkAuth = talkAuth;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.rowText = (TextView) convertView.findViewById(R.id.rowTxt);
            vh.rowBtn = (Button) convertView.findViewById(R.id.rowBtn);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (talkAuth == DENIAL && position == 0) {
            vh.rowBtn.setText("사용하기");
        } else {
            vh.rowBtn.setText("선물하기");
        }


        return convertView;
    }


}
