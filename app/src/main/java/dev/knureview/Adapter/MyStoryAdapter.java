package dev.knureview.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dev.knureview.R;
import dev.knureview.VO.CommentVO;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class MyStoryAdapter extends ArrayAdapter<CommentVO> {

    static class ViewHolder {
        private ImageView backgroundImg;
        private TextView writeTime;
        private TextView likeCnt;
        private TextView commentCnt;
    }

    public MyStoryAdapter(Activity activity, int resource) {
        super(activity, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            vh.backgroundImg = (ImageView)convertView.findViewById(R.id.backgroundImg);
            
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        return convertView;
    }
}
