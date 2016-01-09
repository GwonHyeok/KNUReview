package dev.knureview.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import dev.knureview.R;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;
import dev.knureview.VO.AlarmVO;

/**
 * Created by DavidHa on 2016. 1. 3..
 */
public class AlarmAdapter extends ArrayAdapter<AlarmVO> {
    private static final String COMMENT_MSG = "누군가 새로운 답변 카드를 남겼습니다.";
    private static final String LIKE_MSG = "누군가 당신의 카드에 공감했습니다.";
    private Activity activity;
    private int layout;
    private ArrayList<AlarmVO> alarmList;
    private TalkTextUtil talkTextUtil;
    private boolean isShowDate = false;
    private HashMap<Integer, String> datePositionHashMap;
    private String date;
    private String nextDate;


    static class ViewHolder {
        private LinearLayout dayLayout;
        private TextView dateTxt;
        private ImageView iconImage;
        private ImageView cardImage;
        private TextView messageTxt;
        private TextView writeTimeTxt;
        private LinearLayout dynamicArea;
    }

    public AlarmAdapter(Activity activity, int layout, ArrayList<AlarmVO> alarmList) {
        super(activity, layout, alarmList);
        this.activity = activity;
        this.layout = layout;
        this.alarmList = alarmList;

    }

    public void showDate() {
        isShowDate = true;
        datePositionHashMap = new HashMap<Integer, String>();
        datePositionHashMap.put(0, "show");
        for (int i = 0; i < alarmList.size() - 1; i++) {
            date = TimeFormat.formatDateString(alarmList.get(i).getWriteTime());
            nextDate = TimeFormat.formatDateString(alarmList.get(i + 1).getWriteTime());
            if (!date.equals(nextDate)) {
                datePositionHashMap.put(i + 1, "show");
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.dayLayout = (LinearLayout) convertView.findViewById(R.id.dayLayout);
            vh.dateTxt = (TextView) convertView.findViewById(R.id.date);
            vh.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
            vh.cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
            vh.messageTxt = (TextView) convertView.findViewById(R.id.message);
            vh.writeTimeTxt = (TextView) convertView.findViewById(R.id.writeTime);
            vh.dynamicArea = (LinearLayout) convertView.findViewById(R.id.dynamicArea);
            talkTextUtil = new TalkTextUtil();
            talkTextUtil.setActivity(activity);
            talkTextUtil.setTextSizeDown();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.writeTimeTxt.setText(TimeFormat.formatTimeString(alarmList.get(position).getWriteTime()));
        if (isShowDate) {
            try {
                if (datePositionHashMap.get(position).equals("show")) {
                    vh.dayLayout.setVisibility(View.VISIBLE);
                    vh.dateTxt.setText(TimeFormat.formatDateString(alarmList.get(position).getWriteTime()));
                } else {
                    vh.dayLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                vh.dayLayout.setVisibility(View.GONE);
            }
        }


        if (alarmList.get(position).getIsLike() == 1) {
            vh.messageTxt.setText(LIKE_MSG);
            vh.iconImage.setImageResource(R.drawable.alarm_like_ic);
        } else {
            vh.messageTxt.setText(COMMENT_MSG);
            vh.iconImage.setImageResource(R.drawable.alarm_comment_ic);
        }
        if (vh.dynamicArea.getChildCount() > 0) {
            vh.dynamicArea.removeAllViews();
        }
        talkTextUtil.setDescription(alarmList.get(position).getDescription(), vh.dynamicArea);
        Picasso.with(activity)
                .load("http://kureview.cafe24.com/image/" + alarmList.get(position).getPictureURL())
                .into(vh.cardImage);
        return convertView;
    }
}
