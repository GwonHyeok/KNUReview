package dev.knureview.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.knureview.R;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class StoryAdapter extends ArrayAdapter<TalkVO> {

    private Activity activity;
    private int layout;
    private ArrayList<TalkVO> talkList;
    private TalkTextUtil talkTextUtil;
    private boolean isShowDate = false;
    private ArrayList<Integer> showDatePosition;
    private String date;
    private String nextDate;

    static class ViewHolder {
        private ImageView backgroundImg;
        private LinearLayout dynamicArea;
        private TextView writeTime;
        private TextView likeCnt;
        private TextView commentCnt;
        private TextView date;
    }

    public StoryAdapter(Activity activity, int layout, ArrayList<TalkVO> talkList) {
        super(activity, layout, talkList);
        this.activity = activity;
        this.layout = layout;
        this.talkList = talkList;
    }

    public void showDate() {
        isShowDate = true;
        showDatePosition = new ArrayList<Integer>();
        showDatePosition.add(0);
        for (int i = 0; i < talkList.size() - 1; i++) {
            date = TimeFormat.formatDateString(talkList.get(i).getWriteTime());
            nextDate = TimeFormat.formatDateString(talkList.get(i + 1).getWriteTime());
            if (!date.equals(nextDate)) {
                showDatePosition.add(i + 1);
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
            vh.backgroundImg = (ImageView) convertView.findViewById(R.id.backgroundImg);
            vh.dynamicArea = (LinearLayout) convertView.findViewById(R.id.dynamicArea);
            vh.commentCnt = (TextView) convertView.findViewById(R.id.commentCnt);
            vh.likeCnt = (TextView) convertView.findViewById(R.id.likeCnt);
            vh.writeTime = (TextView) convertView.findViewById(R.id.writeTime);
            vh.date = (TextView) convertView.findViewById(R.id.date);
            talkTextUtil = new TalkTextUtil();
            talkTextUtil.setActivity(activity);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (isShowDate) {
            if ((position < showDatePosition.size()) && showDatePosition.get(position) == position) {
                vh.date.setVisibility(View.VISIBLE);
                vh.date.setText(TimeFormat.formatDateString(talkList.get(position).getWriteTime()));
            } else {
                vh.date.setVisibility(View.GONE);
            }

        }

        if (vh.dynamicArea.getChildCount() > 0) {
            vh.dynamicArea.removeAllViews();
        }
        talkTextUtil.setDescription(talkList.get(position).getDescription(), vh.dynamicArea);

        Picasso.with(activity)
                .load("http://kureview.cafe24.com/image/" + talkList.get(position).getPictureURL())
                .into(vh.backgroundImg);

        vh.writeTime.setText(TimeFormat.formatTimeString(talkList.get(position).getWriteTime()));
        vh.commentCnt.setText("" + talkList.get(position).getCommentCnt());
        vh.likeCnt.setText("" + talkList.get(position).getLikeCnt());

        return convertView;
    }


}
