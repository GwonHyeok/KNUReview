package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

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
    private ImageView cardImage;
    private Bitmap blurBitmap;
    private CircleImageView preImg1;
    private CircleImageView preImg2;
    private CircleImageView preImg3;
    private ImageView diceBtn;

    private int[] randomArray;
    private int currentImage;
    private final int IMAGE_COUNT = 20;

    private Animation diceAnim;
    private Animation preCircle1Anim;
    private Animation preCircle2Anim;
    private Animation preCircle3Anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystory_edit);

        blurImg = (ImageView) findViewById(R.id.blurImg);
        cardImage = (ImageView) findViewById(R.id.backgroundImg);
        preImg1 = (CircleImageView) findViewById(R.id.preImg1);
        preImg2 = (CircleImageView) findViewById(R.id.preImg2);
        preImg3 = (CircleImageView) findViewById(R.id.preImg3);

        diceBtn = (ImageView) findViewById(R.id.diceBtn);


        //random
        randomArray = new int[3];
        setRandomArray();
        currentImage = randomArray[0];
        updateCardImage(randomArray[0]);
        updateBackgroundImage();
        updatePreCircleImage(randomArray[0], preImg1);
        updatePreCircleImage(randomArray[1], preImg2);
        updatePreCircleImage(randomArray[2], preImg3);

        //animation
        diceAnim = AnimationUtils.loadAnimation(this, R.anim.dice);
        preCircle1Anim = AnimationUtils.loadAnimation(this, R.anim.pre_circle1);
        preCircle2Anim = AnimationUtils.loadAnimation(this, R.anim.pre_circle2);
        preCircle3Anim = AnimationUtils.loadAnimation(this, R.anim.pre_circle3);
        diceAnim.setAnimationListener(animationListener);
        preCircle1Anim.setAnimationListener(animationListener);
        preCircle2Anim.setAnimationListener(animationListener);
        preCircle3Anim.setAnimationListener(animationListener);
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            preImg1.setImageResource(R.color.white);
            preImg2.setImageResource(R.color.white);
            preImg3.setImageResource(R.color.white);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            updateCardImage(randomArray[0]);
            updateBackgroundImage();
            updatePreCircleImage(randomArray[0], preImg1);
            updatePreCircleImage(randomArray[1], preImg2);
            updatePreCircleImage(randomArray[2], preImg3);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void setRandomArray() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            randomArray[i] = random.nextInt(IMAGE_COUNT);
            for (int j = 0; j < i; j++) {
                if (randomArray[i] == randomArray[j]) {
                    i--;
                }
            }
        }
    }

    public void updateCardImage(int random) {
        Picasso.with(MyStoryEditActivity.this)
                .load("http://kureview.cafe24.com/image/" + "sample" + random + ".jpg")
                .into(cardImage);

    }

    public void updateBackgroundImage() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BitmapDrawable drawable = (BitmapDrawable) cardImage.getDrawable();
                try {
                    blurBitmap = blur(MyStoryEditActivity.this, drawable.getBitmap(), 22);
                    blurImg.setImageBitmap(blurBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 200);
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
        if (view.getId() == R.id.nextBtn) {

        } else if (view.getId() == R.id.backBtn) {
            finish();
        } else if (view.getId() == R.id.diceBtn) {
            diceBtn.startAnimation(diceAnim);
            preImg1.startAnimation(preCircle1Anim);
            preImg2.startAnimation(preCircle2Anim);
            preImg3.startAnimation(preCircle3Anim);
            setRandomArray();

        } else if (view.getId() == R.id.preImg1) {
            preImg1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.dice));
            if(currentImage!=randomArray[0]){
                updateCardImage(randomArray[0]);
                updateBackgroundImage();
            }
        } else if (view.getId() == R.id.preImg2) {
            preImg2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.dice));
            if(currentImage!=randomArray[1]){
                updateCardImage(randomArray[1]);
                updateBackgroundImage();
            }

        } else if (view.getId() == R.id.preImg3) {
            preImg3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.dice));
            if(currentImage!=randomArray[2]){
                updateCardImage(randomArray[2]);
                updateBackgroundImage();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }


}
