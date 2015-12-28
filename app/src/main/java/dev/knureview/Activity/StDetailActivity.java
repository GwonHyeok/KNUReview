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

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    private TextView likeCntTxt;
    private TextView commentCntTxt;
    private LinearLayout commentLayout;
    private FloatingActionButton fab;

    private TalkTextUtil talkTextUtil;
    private int tNo;
    private String pictureURL;
    private int writerStdNo;
    private String description;
    private String writeTime;
    private int likeCnt;
    private int commentCnt;
    private boolean isComment = false;
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
        likeCntTxt = (TextView) findViewById(R.id.likeCnt);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        deleteBtn = (ImageView) findViewById(R.id.deleteBtn);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        String stdNo = pref.getPreferences("stdNo", "");

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
            tNo = intent.getIntExtra("tNo", 0);
            commentCnt = intent.getIntExtra("commentCnt", 0);
            commentCntTxt.setText("" + commentCnt);
            commentLayout.setVisibility(View.VISIBLE);
        }
        pictureURL = intent.getStringExtra("pictureURL");
        writerStdNo = intent.getIntExtra("writerStdNo", 0);
        description = intent.getStringExtra("description");
        writeTime = intent.getStringExtra("writeTime");
        likeCnt = intent.getIntExtra("likeCnt", 0);

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

        //horizontal RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_horizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

    }

    //RecyclerBinder Adapter
    @NonNull
    private RecyclerBinderAdapter<DemoSectionType, DemoViewType> initAdapter(ArrayList<CommentVO> cmtList) {
        RecyclerBinderAdapter<DemoSectionType, DemoViewType> adapter
                = new RecyclerBinderAdapter<>();
        for (int i = 0; i < cmtList.size(); i++) {
            RecyclerBinder<DemoViewType> binder = new PageBinder(this, cmtList.get(i));
            adapter.add(DemoSectionType.ITEM, binder);
        }
        return adapter;
    }


    public void mOnClick(View view) {
        if (view.getId() == R.id.deleteBtn) {

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
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

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
                fab.setVisibility(View.GONE);
                RecyclerBinderAdapter<DemoSectionType, DemoViewType> normalAdapter = initAdapter(data);
                recyclerView.setAdapter(normalAdapter);
            } else {
                fab.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isComment) {
            new CommentList().execute();
        }
    }
}
