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
    private HashMap<Integer, String> likeHashmap;
    private HashMap<Integer, String> datePositionHashMap;
    private String date;
    private String nextDate;

    static class ViewHolder {
        private ImageView cardImage;
        private LinearLayout dynamicArea;
        private TextView writeTime;
        private ImageView likeImage;
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
        datePositionHashMap = new HashMap<Integer, String>();
        datePositionHashMap.put(0, "show");
        for (int i = 0; i < talkList.size() - 1; i++) {
            date = TimeFormat.formatDateString(talkList.get(i).getWriteTime());
            nextDate = TimeFormat.formatDateString(talkList.get(i + 1).getWriteTime());
            if (!date.equals(nextDate)) {
                datePositionHashMap.put(i + 1, "show");
            }
        }
    }

    public void setMyLikeTalk(HashMap<Integer, String> likeHashmap) {
        this.likeHashmap = likeHashmap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
            vh.dynamicArea = (LinearLayout) convertView.findViewById(R.id.dynamicArea);
            vh.commentCnt = (TextView) convertView.findViewById(R.id.commentCnt);
            vh.likeImage = (ImageView) convertView.findViewById(R.id.likeImage);
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
            try {
                if (datePositionHashMap.get(position).equals("show")) {
                    vh.date.setVisibility(View.VISIBLE);
                    vh.date.setText(TimeFormat.formatDateString(talkList.get(position).getWriteTime()));
                } else {
                    vh.date.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                vh.date.setVisibility(View.GONE);
            }
        }

        //자기 자신이 좋아요 했던 게시물 표시
        try {

            if (likeHashmap.get(talkList.get(position).gettNo()).equals("like")) {
                vh.likeImage.setImageResource(R.drawable.fill_like_ic);
            } else {
                vh.likeImage.setImageResource(R.drawable.like_ic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vh.likeImage.setImageResource(R.drawable.like_ic);
        }


        if (vh.dynamicArea.getChildCount() > 0) {
            vh.dynamicArea.removeAllViews();
        }
        talkTextUtil.setDescription(talkList.get(position).getDescription(), vh.dynamicArea);

        Picasso.with(activity)
                .load("http://kureview.cafe24.com/image/" + talkList.get(position).getPictureURL())
                .into(vh.cardImage);

        vh.writeTime.setText(TimeFormat.formatTimeString(talkList.get(position).getWriteTime()));
        vh.commentCnt.setText("" + talkList.get(position).getCommentCnt());
        vh.likeCnt.setText("" + talkList.get(position).getLikeCnt());

        return convertView;
    }
}
