package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import dev.knureview.Binder.PageBinder;
import dev.knureview.R;
import dev.knureview.Binder.DemoSectionType;
import dev.knureview.Binder.DemoViewType;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;
import dev.knureview.VO.CommentVO;
import jp.satorufujiwara.binder.recycler.RecyclerBinder;
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter;

/**
 * Created by DavidHa on 2015. 12. 1..
 */
public class StDetailActivity extends Activity {

    private RecyclerView recyclerView;
    private ImageView blurImg;
    private ImageView backgroundImg;
    private ImageView deleteBtn;
    private Bitmap blurBitmap;
    private LinearLayout dynamicArea;
    private TextView writeTimeTxt;
    private ImageView likeImage;
    private TextView likeCntTxt;
    private TextView commentCntTxt;
    private LinearLayout commentLayout;
    private FloatingActionButton fab;

    private TalkTextUtil talkTextUtil;
    private int tNo;
    String stdNo;
    private String pictureURL;
    private int writerStdNo;
    private String description;
    private String writeTime;
    private int likeCnt;
    private int commentCnt;
    private HashMap<Integer, String> likeHashMap;
    private String like;
    private boolean isComment = false;
    private boolean isLike = false;
    private int cNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        writeTimeTxt = (TextView) findViewById(R.id.writeTime);
        blurImg = (ImageView) findViewById(R.id.blurImg);
        backgroundImg = (ImageView) findViewById(R.id.cardImage);
        dynamicArea = (LinearLayout) findViewById(R.id.dynamicArea);
        commentCntTxt = (TextView) findViewById(R.id.commentCnt);
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
        likeImage = (ImageView) findViewById(R.id.likeImage);
        likeCntTxt = (TextView) findViewById(R.id.likeCnt);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        deleteBtn = (ImageView) findViewById(R.id.deleteBtn);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");

        //talkTextUtil
        talkTextUtil = new TalkTextUtil();
        talkTextUtil.setActivity(this);
        talkTextUtil.setTextSizeUP();

        //get intent
        Intent intent = getIntent();
        String toDo = intent.getStringExtra("toDo");
        if (toDo != null) {
            isComment = true;
            cNo = intent.getIntExtra("cNo", 0);
            commentLayout.setVisibility(View.INVISIBLE);

        } else {
            isComment = false;
            commentCnt = intent.getIntExtra("commentCnt", 0);
            commentCntTxt.setText("" + commentCnt);
            commentLayout.setVisibility(View.VISIBLE);
        }

        tNo = intent.getIntExtra("tNo", 0);
        pictureURL = intent.getStringExtra("pictureURL");
        writerStdNo = intent.getIntExtra("writerStdNo", 0);
        description = intent.getStringExtra("description");
        writeTime = intent.getStringExtra("writeTime");
        likeCnt = intent.getIntExtra("likeCnt", 0);
        like = intent.getStringExtra("like");

        //initial
        talkTextUtil.setDescription(description, dynamicArea);
        writeTimeTxt.setText(TimeFormat.formatTimeString(writeTime));
        likeCntTxt.setText("" + likeCnt);
        Picasso.with(this)
                .load("http://kureview.cafe24.com/image/" + pictureURL)
                .into(backgroundImg);
        BitmapDrawable drawable = (BitmapDrawable) backgroundImg.getDrawable();
        blurBitmap = blur(this, drawable.getBitmap(), 22);
        blurImg.setImageBitmap(blurBitmap);

        if (stdNo.equals(String.valueOf(writerStdNo))) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        if (like == null) {
            likeImage.setImageResource(R.drawable.like_ic);
            isLike = false;
        } else {
            likeImage.setImageResource(R.drawable.fill_like_ic);
            isLike = true;
        }

        //horizontal RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_horizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

    }

    public void mOnClick(View view) {
        if (view.getId() == R.id.deleteBtn) {
            new MaterialDialog.Builder(this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .title("이야기 삭제")
                    .titleColor(getResources().getColor(R.color.black))
                    .content("이야기를 삭제하시겠습니까?")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .positiveText("확인")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            if (isComment) {
                                //comment 삭제
                                new CommentDelete().execute();
                            } else {
                                //talk 삭제
                                new TalkDelete().execute();
                            }
                        }
                    })
                    .negativeText("취소")
                    .negativeColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        } else if (view.getId() == R.id.backBtn) {
            finish();
        } else if (view.getId() == R.id.fab) {
            Intent intent = new Intent(StDetailActivity.this, StEditActivity.class);
            intent.putExtra("toDo", "comment");
            intent.putExtra("tNo", tNo);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.commentLayout) {
            Intent intent = new Intent(StDetailActivity.this, StEditActivity.class);
            intent.putExtra("toDo", "comment");
            intent.putExtra("tNo", tNo);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        } else if (view.getId() == R.id.likeLayout) {
            int like = Integer.parseInt(likeCntTxt.getText().toString());
            if (isLike) {
                decreaseLike(like);
            } else {
                increaseLike(like);
            }

        }
    }

    private void increaseLike(int likeCnt) {
        likeImage.setImageResource(R.drawable.fill_like_ic);
        if (isComment) {
            new IncreaseCmtLike().execute();
        } else {
            new IncreaseTlkLike().execute();
        }
        likeCntTxt.setText(String.valueOf(++likeCnt));
        isLike = true;
    }

    private void decreaseLike(int likeCnt) {
        likeImage.setImageResource(R.drawable.like_ic);
        if (isComment) {
            new DecreaseCmtLike().execute();
        } else {
            new DecreaseTlkLike().execute();
        }
        likeCntTxt.setText(String.valueOf(--likeCnt));
        isLike = false;
    }

    public Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
    }


    //Async

    private class CommentList extends AsyncTask<Void, Void, ArrayList<CommentVO>> {
        @Override
        protected ArrayList<CommentVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getAllCommentList(tNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<CommentVO> data) {
            super.onPostExecute(data);
            if (data != null) {
                fab.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                commentCntTxt.setText(String.valueOf(data.size()));
                RecyclerBinderAdapter<DemoSectionType, DemoViewType> normalAdapter = initAdapter(data);
                recyclerView.setAdapter(normalAdapter);
                normalAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                commentCntTxt.setText("0");
            }

        }
    }

    //RecyclerBinder Adapter
    @NonNull
    private RecyclerBinderAdapter<DemoSectionType, DemoViewType> initAdapter(ArrayList<CommentVO> cmtList) {
        RecyclerBinderAdapter<DemoSectionType, DemoViewType> adapter
                = new RecyclerBinderAdapter<>();
        for (int i = 0; i < cmtList.size(); i++) {
            RecyclerBinder<DemoViewType> binder = new PageBinder(this, cmtList.get(i), likeHashMap);
            adapter.add(DemoSectionType.ITEM, binder);
        }
        return adapter;
    }

    private class IncreaseCmtLike extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().increaseCommentLike(cNo, stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class IncreaseTlkLike extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().increaseTalkLike(tNo, stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class DecreaseCmtLike extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().decreaseCommentLike(cNo, stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class DecreaseTlkLike extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().decreaseTalkLike(tNo, stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class TalkDelete extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().deleteTalk(tNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

    private class CommentDelete extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new NetworkUtil().deleteComment(cNo, tNo, stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }


    private class FavouriteComment extends AsyncTask<Void, Void, HashMap<Integer, String>> {
        @Override
        protected HashMap<Integer, String> doInBackground(Void... params) {
            try {
                return new NetworkUtil().findMyLikeComment(stdNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> result) {
            super.onPostExecute(result);
            likeHashMap = result;
            if (!isComment) {
                new CommentList().execute();
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FavouriteComment().execute();

    }
}
