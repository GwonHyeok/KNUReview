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

import dev.knureview.R;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class MyStoryAdapter extends ArrayAdapter<TalkVO> {

    private Activity activity;
    private int layout;
    private ArrayList<TalkVO> data;
    private TalkTextUtil talkTextUtil;

    static class ViewHolder {
        private ImageView backgroundImg;
        private LinearLayout dynamicArea;
        private TextView writeTime;
        private TextView likeCnt;
        private TextView commentCnt;
    }

    public MyStoryAdapter(Activity activity, int layout, ArrayList<TalkVO> data) {
        super(activity, layout, data);
        this.activity = activity;
        this.layout = layout;
        this.data = data;
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
            talkTextUtil = new TalkTextUtil();
            talkTextUtil.setActivity(activity);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (vh.dynamicArea.getChildCount() > 0) {
            vh.dynamicArea.removeAllViews();
        }
        talkTextUtil.setDescription(data.get(position).getDescription(), vh.dynamicArea);

        Picasso.with(activity)
                .load("http://kureview.cafe24.com/image/"+data.get(position).getPictureURL())
                .into(vh.backgroundImg);

        vh.writeTime.setText(TimeFormat.formatTimeString(data.get(position).getWriteTime()));
        vh.commentCnt.setText("" + data.get(position).getCommentCnt());
        vh.likeCnt.setText("" + data.get(position).getLikeCnt());

        return convertView;
    }




}
