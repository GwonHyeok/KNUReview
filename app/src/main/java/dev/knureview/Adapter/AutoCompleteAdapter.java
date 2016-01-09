package dev.knureview.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.knureview.R;
import dev.knureview.Util.SoundSearcher;

/**
 * Created by DavidHa on 2016. 1. 9..
 */
public class AutoCompleteAdapter extends BaseAdapter {
    private Activity activity;
    private int layout;
    private ArrayList<String> list;
    private ArrayList<String> showList;

    static class ViewHolder {
        private TextView rowTxt;
    }

    public AutoCompleteAdapter(Activity activity, ArrayList<String> list) {
        this.activity = activity;
        this.layout = R.layout.layout_auto_list_row;
        this.list = list;
        showList = new ArrayList<>();
    }

    public void refreshListView(String searchStr) {
        showList.clear();
        if (!searchStr.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                if (SoundSearcher.matchString(list.get(i), searchStr)) {
                    showList.add(list.get(i));
                }
            }
        }
    }
    public ArrayList<String> getItemList(){
        return showList;
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
        return showList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.rowTxt = (TextView) convertView.findViewById(R.id.rowTxt);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.rowTxt.setText(showList.get(position));

        return convertView;
    }
}
