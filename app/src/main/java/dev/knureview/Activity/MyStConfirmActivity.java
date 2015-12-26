package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.CircularProgressButton;
import com.squareup.picasso.Picasso;

import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.Util.TalkTextUtil;

/**
 * Created by DavidHa on 2015. 12. 23..
 */
public class MyStConfirmActivity extends Activity {
    private static final int ACCEPT = 1;
    private static final int DENIAL = 0;
    public static Activity activity;
    private String stdNo;
    private int talkAuth;
    private String pictureURL;
    private String description;
    private ImageView cardImage;
    private ImageView blurImage;
    private Bitmap blurBitmap;
    private LinearLayout dynamicArea;
    private TalkTextUtil talkTextUtil;
    private CircularProgressButton confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_confirm);

        activity = MyStConfirmActivity.this;
        cardImage = (ImageView) findViewById(R.id.cardImage);
        blurImage = (ImageView) findViewById(R.id.blurImage);
        dynamicArea = (LinearLayout) findViewById(R.id.dynamicArea);
        confirmBtn = (CircularProgressButton) findViewById(R.id.confirmButton);
        confirmBtn.setProgress(0);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");
        talkAuth = pref.getPreferences("talkAuth", 0);

        //talkTextUtil
        talkTextUtil = new TalkTextUtil();
        talkTextUtil.setActivity(this);
        talkTextUtil.setTextSizeUP();

        Intent intent = getIntent();
        pictureURL = intent.getStringExtra("pictureURL");
        description = intent.getStringExtra("description");

        //initial
        Picasso.with(this)
                .load("http://kureview.cafe24.com/image/" + pictureURL)
                .into(cardImage);
        talkTextUtil.setDescription(description, dynamicArea);
        updateBackgroundImage();
    }

    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

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

    public void mOnClick(View view) {
        if (view.getId() == R.id.backBtn) {
            finish();
        } else if (view.getId() == R.id.confirmButton) {
            if (confirmBtn.getProgress() == 0
                    && talkAuth == ACCEPT) {
                new Talk().execute();
            } else if (confirmBtn.getProgress() == -1) {
                confirmBtn.setProgress(0);
            }else if(confirmBtn.getProgress() == 0
                    && talkAuth == DENIAL){
                confirmBtn.setProgress(-1);
                new MaterialDialog.Builder(this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title("소곤소곤 티켓이 없습니다.")
                        .titleColor(getResources().getColor(R.color.black))
                        .content("티켓이 있는 분만 글을 남기실 수 있어요\n주변 친구들한테 소곤소곤 티켓을 요청해봐요")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .iconRes(R.drawable.ticket_ic)
                        .maxIconSize(90)
                        .show();

            }
        }
    }

    public void updateBackgroundImage() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BitmapDrawable drawable = (BitmapDrawable) cardImage.getDrawable();
                try {
                    blurBitmap = blur(MyStConfirmActivity.this, drawable.getBitmap(), 22);
                    blurImage.setImageBitmap(blurBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateBackgroundImage();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 100);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

    private class Talk extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            confirmBtn.setProgress(50);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return new NetworkUtil().insertTalk(stdNo, pictureURL, description);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == true) {
                confirmBtn.setProgress(100);
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Intent intent = new Intent(MyStConfirmActivity.this, MyStoryActivity.class);
                        startActivity(intent);
                        MyStoryActivity.activity.finish();
                        MyStEditActivity.activity.finish();
                        MyStConfirmActivity.activity.finish();
                    }
                }.sendEmptyMessageDelayed(0, 800);
            } else {
                confirmBtn.setProgress(-1);
            }
        }
    }


}
