package dev.knureview.Binder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.HashMap;

import dev.knureview.Activity.StDetailActivity;
import dev.knureview.R;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;
import dev.knureview.VO.CommentVO;
import jp.satorufujiwara.binder.recycler.RecyclerBinder;

public class PageBinder extends RecyclerBinder<DemoViewType> {

    private CommentVO vo;
    private Activity activity;
    private TalkTextUtil talkTextUtil;
    private HashMap<Integer, String> likeHashMap;

    public PageBinder(Activity activity, CommentVO vo, HashMap<Integer, String> likeHashmap) {
        super(activity, DemoViewType.PAGE);
        this.activity = activity;
        this.vo = vo;
        this.likeHashMap = likeHashmap;
        talkTextUtil = new TalkTextUtil();
        talkTextUtil.setActivity(activity);
    }


    @Override
    public int layoutResId() {
        return R.layout.layout_st_detail_list_row;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (vh.dynamicArea.getChildCount() > 0) {
            vh.dynamicArea.removeAllViews();
        }
        vh.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StDetailActivity.class);
                intent.putExtra("toDo", "comment");
                intent.putExtra("tNo", vo.gettNo());
                intent.putExtra("cNo", vo.getCno());
                intent.putExtra("pictureURL", vo.getPictureURL());
                intent.putExtra("writerStdNo", vo.getStdNo());
                intent.putExtra("description", vo.getDescription());
                intent.putExtra("writeTime", vo.getWriteTime());
                intent.putExtra("likeCnt", vo.getLikeCnt());
                try {
                    intent.putExtra("like", likeHashMap.get(vo.getCno()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }
        });
        talkTextUtil.setDescription(vo.getDescription(), vh.dynamicArea);
        Picasso.with(activity)
                .load("http://kureview.cafe24.com/image/" + vo.getPictureURL())
                .into(vh.cardImage);
        vh.writeTime.setText(TimeFormat.formatTimeString(vo.getWriteTime()));
        //자기 자신이 좋아요 했던 게시물 표시
        try {

            if (likeHashMap.get(vo.getCno()).equals("like")) {
                vh.likeImage.setImageResource(R.drawable.fill_like_ic);
            } else {
                vh.likeImage.setImageResource(R.drawable.like_ic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        vh.likeCnt.setText("" + vo.getLikeCnt());


    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView cardImage;
        private LinearLayout dynamicArea;
        private TextView writeTime;
        private ImageView likeImage;
        private TextView likeCnt;

        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.cmtCardImage);
            dynamicArea = (LinearLayout) itemView.findViewById(R.id.cmtDynamicArea);
            writeTime = (TextView) itemView.findViewById(R.id.cmtWriteTime);
            likeImage = (ImageView) itemView.findViewById(R.id.likeImage);
            likeCnt = (TextView) itemView.findViewById(R.id.cmtLikeCnt);
        }
    }
}
