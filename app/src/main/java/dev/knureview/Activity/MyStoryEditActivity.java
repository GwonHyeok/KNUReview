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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 12. 21..
 */
public class MyStoryEditActivity extends Activity {
    private EditText inputEdit;
    private ImageView blurImg;
    private ImageView backgroundImg;
    private Bitmap blurBitmap;
    private CircleImageView preImg1;
    private CircleImageView preImg2;
    private CircleImageView preImg3;

    private int[] randomArray;
    private int randomSize = 20;
    private Thread thread;
    private Runnable task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystory_edit);

        blurImg = (ImageView) findViewById(R.id.blurImg);
        backgroundImg = (ImageView) findViewById(R.id.backgroundImg);
        preImg1 = (CircleImageView) findViewById(R.id.preImg1);
        preImg2 = (CircleImageView) findViewById(R.id.preImg2);
        preImg3 = (CircleImageView) findViewById(R.id.preImg3);

        int first = getRandom();
        updateBackgroundImage(first);
        updatePreCircleImage(first, preImg1);
        updatePreCircleImage(getRandom(), preImg2);
        updatePreCircleImage(getRandom(), preImg3);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BitmapDrawable drawable = (BitmapDrawable) backgroundImg.getDrawable();
                try {
                    blurBitmap = blur(MyStoryEditActivity.this, drawable.getBitmap(), 22);
                    blurImg.setImageBitmap(blurBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 500);




    }

    public int getRandom() {
        Random random = new Random();
        return random.nextInt(randomSize);
    }


    public void updateBackgroundImage(int random) {
        Picasso.with(MyStoryEditActivity.this)
                .load("http://kureview.cafe24.com/image/" + "sample" + random + ".jpg")
                .into(backgroundImg);

    }

    public void updatePreCircleImage(int random, CircleImageView targetImageView) {
        String pictureURL = "sample" + random + ".jpg";
        Picasso.with(MyStoryEditActivity.this)
                .load("http://kureview.cafe24.com/image_small/" + pictureURL)
                .into(targetImageView);
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
        if (view.getId() == R.id.plusBtn) {

        } else if (view.getId() == R.id.backBtn) {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }


}
