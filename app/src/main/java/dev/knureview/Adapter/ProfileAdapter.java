package dev.knureview.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 11. 25..
 */
public class ProfileAdapter extends BaseAdapter {

    private Activity activity;
    private String[] menu={"",""};
    private int[] icon={};

    static class ViewHolder {
        private TextView rowText;
        private ImageView rowIcon;
    }

    public ProfileAdapter(Activity activity, int resource) {

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
    public int getCount() {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            vh.rowIcon = (ImageView) convertView.findViewById(R.id.rowIcon);
            vh.rowText = (TextView) convertView.findViewById(R.id.rowTxt);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.rowText.setText(menu[position]);
        vh.rowIcon.setImageResource(icon[position]);

        return convertView;
    }
}
