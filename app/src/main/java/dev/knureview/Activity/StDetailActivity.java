package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poliveira.apps.parallaxlistview.ParallaxListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.knureview.R;
import dev.knureview.Util.TalkTextUtil;
import dev.knureview.Util.TimeFormat;

/**
 * Created by DavidHa on 2015. 12. 1..
 */
public class StDetailActivity extends Activity {

    private ImageView blurImg;
    private ImageView backgroundImg;
    private Bitmap blurBitmap;
    private LinearLayout dynamicArea;
    private TextView writeTimeTxt;
    private TextView likeCntTxt;
    private TextView commentCntTxt;

    private TalkTextUtil talkTextUtil;
    private int tNo;
    private String pictureURL;
    private int stdNo;
    private String description;
    private String writeTime;
    private int likeCnt;
    private int commentCnt;
    ArrayAdapter<String> mAdapter;
    private ImageView headerBlurImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        writeTimeTxt = (TextView) findViewById(R.id.writeTime);
        blurImg = (ImageView)findViewById(R.id.blurImg);

        //talkTextUtil
        talkTextUtil = new TalkTextUtil();
        talkTextUtil.setActivity(this);
        talkTextUtil.setTextSizeUP();

        //get intent
        Intent intent = getIntent();
        tNo = intent.getIntExtra("tNo", 0);
        pictureURL = intent.getStringExtra("pictureURL");
        stdNo = intent.getIntExtra("stdNo", 0);
        description = intent.getStringExtra("description");
        writeTime = intent.getStringExtra("writeTime");
        likeCnt = intent.getIntExtra("likeCnt", 0);
        commentCnt = intent.getIntExtra("commentCnt", 0);

        List<String> mStrings = new ArrayList<>();


        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStrings);

        ParallaxListView mListView = (ParallaxListView) findViewById(R.id.view);
        mListView.setAdapter(mAdapter);


        final View headerView = getLayoutInflater().inflate(R.layout.layout_story_detail_header, mListView, false);

        backgroundImg = (ImageView) headerView.findViewById(R.id.backgroundImg);
        dynamicArea = (LinearLayout) headerView.findViewById(R.id.dynamicArea);
        headerBlurImg = (ImageView) headerView.findViewById(R.id.headerBlurImg);
        commentCntTxt = (TextView) headerView.findViewById(R.id.commentCnt);
        likeCntTxt = (TextView) headerView.findViewById(R.id.likeCnt);


        talkTextUtil.setDescription(description, dynamicArea);
        writeTimeTxt.setText(TimeFormat.formatTimeString(writeTime));
        commentCntTxt.setText("" + commentCnt);
        likeCntTxt.setText("" + likeCnt);
        Picasso.with(this)
                .load("http://kureview.cafe24.com/image/" + pictureURL)
                .into(backgroundImg);
        BitmapDrawable drawable = (BitmapDrawable) backgroundImg.getDrawable();
        blurBitmap = blur(this, drawable.getBitmap(), 22);
        blurImg.setImageBitmap(blurBitmap);
        headerBlurImg.setImageBitmap(blurBitmap);


        mListView.setParallaxView(headerView);
    }


    public void mOnClick(View view) {
        if (view.getId() == R.id.plusBtn) {

        } else if (view.getId() == R.id.backBtn) {
            finish();
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
}
