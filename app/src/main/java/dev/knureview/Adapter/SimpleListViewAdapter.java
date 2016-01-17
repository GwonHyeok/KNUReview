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
import dev.knureview.VO.LectureVO;

/**
 * Created by DavidHa on 2016. 1. 9..
 */
public class SimpleListViewAdapter extends BaseAdapter {
    private Activity activity;
    private int layout;
    private ArrayList<LectureVO> lectList;
    private ArrayList<String> list;
    private ArrayList<String> showList;

    static class ViewHolder {
        private TextView rowTxt;
    }

    public SimpleListViewAdapter(Activity activity, int layout, ArrayList<String> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        showList = new ArrayList<>();
    }

    public void autoComplete(String searchStr) {
        showList.clear();
        if (!searchStr.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                if (SoundSearcher.matchString(list.get(i), searchStr)) {
                    showList.add(list.get(i));
                }
            }
        }
    }

    public void setYearListView() {
        showList.add(0,"전체");
        showList.add(list.get(0) + "년도");
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).equals(list.get(i + 1))) {
                showList.add(list.get(i + 1) + "년도");
            }
        }
    }

    public void showAllList(){
        showList = list;
    }
    public void refreshListView(ArrayList<String> newList){
        showList = newList;

    }

    public ArrayList<String> getItemList() {
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
