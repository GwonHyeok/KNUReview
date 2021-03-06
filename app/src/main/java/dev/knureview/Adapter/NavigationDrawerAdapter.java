package dev.knureview.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.knureview.R;
import dev.knureview.Util.TimeUtil;

/**
 * Created by DavidHa on 2015. 11. 22..
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private String mTitles[] = {"수강리뷰", "소곤소곤", "학사일정", "시간표", "내프로필"};
    private int mIcons[] = {R.drawable.review, R.drawable.my_story, R.drawable.day_1, R.drawable.timetable, R.drawable.my_profile,};
    private Activity activity;
    private int resource;
    private Typeface font;
    private int selectedIndex;
    private int unSelectedColor;
    private int curDay;
    int resID;

    static class ViewHolder {
        private LinearLayout rowLayout;
        private ImageView rowImg;
        private TextView rowTxt;
    }

    public NavigationDrawerAdapter(Activity activity, int resource, int currentPosition) {
        this.activity = activity;
        this.resource = resource;
        font = Typeface.createFromAsset(activity.getResources()
                .getAssets(), "fonts/NanumGothic.ttf");
        unSelectedColor = activity.getResources()
                .getColor(R.color.colorPrimaryLight);
        selectedIndex = currentPosition;
        curDay = new TimeUtil().getCurDay();
        resID = activity.getResources()
                .getIdentifier("day_" + curDay, "drawable", activity.getPackageName());
        mIcons[2] = resID;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index - 1;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public String getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            vh = new ViewHolder();
            vh.rowLayout = (LinearLayout) convertView.findViewById(R.id.rowLayout);
            vh.rowImg = (ImageView) convertView.findViewById(R.id.rowImg);
            vh.rowTxt = (TextView) convertView.findViewById(R.id.rowTxt);
            vh.rowTxt.setTypeface(font);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.rowImg.setImageResource(mIcons[position]);
        vh.rowTxt.setText(mTitles[position]);

        if (selectedIndex != -1 && position == selectedIndex) {
            //selected
            vh.rowLayout.setBackgroundColor(activity.getResources()
                    .getColor(R.color.colorPrimary));

        } else {
            //un selected
            vh.rowLayout.setBackgroundColor(unSelectedColor);
        }


        return convertView;
    }
}
