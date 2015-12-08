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

import java.util.ArrayList;

import dev.knureview.R;
import dev.knureview.Util.PixelUtil;
import dev.knureview.VO.CommentVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class MyStoryAdapter extends ArrayAdapter<CommentVO> {

    private Activity activity;
    private int layout;
    private ArrayList<CommentVO> data;

    static class ViewHolder {
        private ImageView backgroundImg;
        private LinearLayout dynamicArea;
        private TextView writeTime;
        private TextView likeCnt;
        private TextView commentCnt;
    }

    public MyStoryAdapter(Activity activity, int layout, ArrayList<CommentVO> data) {
        super(activity, layout, data);
        this.activity = activity;
        this.layout = layout;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        LinearLayout dynamicArea = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.backgroundImg = (ImageView) convertView.findViewById(R.id.backgroundImg);

            vh.commentCnt = (TextView) convertView.findViewById(R.id.commentCnt);
            vh.likeCnt = (TextView) convertView.findViewById(R.id.likeCnt);
            vh.writeTime = (TextView) convertView.findViewById(R.id.writeTime);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        dynamicArea = (LinearLayout) convertView.findViewById(R.id.dynamicArea);
        if (dynamicArea.getChildCount() > 0) {
            dynamicArea.removeAllViews();
        }
        setDescription(data.get(position).getDescription(), dynamicArea);


        return convertView;
    }

    private void setDescription(String description, LinearLayout dynamicArea) {
        int count = 0;
        int findPosition = 0;
        String remainStr = "";
        for (int i = 0; i < description.length(); i++) {
            count++;
            char ch = description.charAt(i);
            if (ch == '\n' || count > 25) {
                String tempStr = description.substring(findPosition, i).replace("\n", "").trim();

                if (ch != ' ' && ch != '\n') {
                    int tempPosition = tempStr.lastIndexOf(' ');
                    makeTextView(tempStr.substring(0, tempPosition).trim(), dynamicArea);
                    remainStr = tempStr.substring(tempPosition).trim();
                } else {
                    makeTextView(remainStr + tempStr, dynamicArea);
                    remainStr = "";
                }
                findPosition = i;
                count = 0;
            }
        }
        if (findPosition == 0) {
            makeTextView(description, dynamicArea);
        }
    }

    private void makeTextView(String description, LinearLayout dynamicArea) {
        final int space100 = (int) PixelUtil.convertPixelsToDp(100, activity);
        final int space40 = (int) PixelUtil.convertPixelsToDp(40, activity);
        final int space14 = (int) PixelUtil.convertPixelsToDp(14, activity);
        final int txtSize = (int) PixelUtil.convertPixelsToDp(40, activity);

        TextView text = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = space40;
        text.setLayoutParams(params);
        text.setBackgroundColor(activity.getResources().getColor(R.color.bg_color));
        text.setSingleLine(true);

        text.setPadding(space100, space14, space100, space14);
        text.setText(description);
        text.setTextColor(activity.getResources().getColor(R.color.white));
        text.setTextSize(txtSize);

        dynamicArea.addView(text);
    }
}
