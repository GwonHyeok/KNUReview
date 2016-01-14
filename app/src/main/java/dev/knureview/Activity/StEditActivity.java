package dev.knureview.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by DavidHa on 2015. 12. 21..
 */
public class StEditActivity extends Activity {
    public static Activity activity;
    private EditText inputEdit;
    private String description;

    private boolean isComment = false;
    private int tNo;
    private ImageView blurImage;
    private ImageView cardImage;
    private Bitmap blurBitmap;
    private CircleImageView preImg1;
    private CircleImageView preImg2;
    private CircleImageView preImg3;
    private ImageView diceBtn;

    private int[] randomArray;
    private int currentImage;
    private static int pictureCnt;

    private Animation diceAnim;
    private Animation preCircle1Anim;
    private Animation preCircle2Anim;
    private Animation preCircle3Anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);

        activity = StEditActivity.this;

        inputEdit = (EditText) findViewById(R.id.inputEdit);
        blurImage = (ImageView) findViewById(R.id.blurImage);
        cardImage = (ImageView) findViewById(R.id.cardImage);
        preImg1 = (CircleImageView) findViewById(R.id.preImg1);
        preImg2 = (CircleImageView) findViewById(R.id.preImg2);
        preImg3 = (CircleImageView) findViewById(R.id.preImg3);
        diceBtn = (ImageView) findViewById(R.id.diceBtn);

        //intent
        Intent intent = getIntent();
        String toDo = intent.getStringExtra("toDo");
        if (toDo != null) {
            isComment = true;
            tNo = intent.getIntExtra("tNo", 0);
        }

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        pictureCnt =pref.getPreferences("pictureCnt",0);

        //random
        randomArray = new int[3];
        setRandomArray();
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

        //inputEdit
        inputEdit.addTextChangedListener(inputListener);

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
            randomArray[i] = random.nextInt(pictureCnt);
            for (int j = 0; j < i; j++) {
                if (randomArray[i] == randomArray[j]) {
                    i--;
                }
            }
        }
        currentImage = randomArray[0];
    }

    public void updateCardImage(int random) {
        currentImage = random;
        Picasso.with(StEditActivity.this)
                .load("http://kureview.cafe24.com/image/" + "sample" + random + ".jpg")
                .into(cardImage);

    }

    public void updateBackgroundImage() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BitmapDrawable drawable = (BitmapDrawable) cardImage.getDrawable();
                try {
                    blurBitmap = blur(StEditActivity.this, drawable.getBitmap(), 22);
                    blurImage.setImageBitmap(blurBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateBackgroundImage();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 100);
    }

    public void updatePreCircleImage(int random, CircleImageView targetImageView) {
        String pictureURL = "sample" + random + ".jpg";
        Picasso.with(StEditActivity.this)
                .load("http://kureview.cafe24.com/image_small/" + pictureURL)
                .placeholder(R.drawable.default_img_icon)
                .noFade()
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
            if (!inputEdit.getText().toString().equals("")) {
                Intent intent = new Intent(StEditActivity.this, StConfirmActivity.class);
                intent.putExtra("pictureURL", "sample" + currentImage + ".jpg");
                intent.putExtra("description", description);
                if (isComment) {
                    intent.putExtra("tNo", tNo);
                    intent.putExtra("toDo", "comment");
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            } else {
                Toast.makeText(StEditActivity.this, "텅빈 생각은 안돼안돼", Toast.LENGTH_SHORT).show();
            }

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
            if (currentImage != randomArray[0]) {
                updateCardImage(randomArray[0]);
                updateBackgroundImage();
            }
        } else if (view.getId() == R.id.preImg2) {
            preImg2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.dice));
            if (currentImage != randomArray[1]) {
                updateCardImage(randomArray[1]);
                updateBackgroundImage();
            }

        } else if (view.getId() == R.id.preImg3) {
            preImg3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.dice));
            if (currentImage != randomArray[2]) {
                updateCardImage(randomArray[2]);
                updateBackgroundImage();
            }
        }
    }

    TextWatcher inputListener = new TextWatcher() {
        String previousString = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            previousString = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            description = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (inputEdit.getLineCount() > 6) {
                inputEdit.setText(previousString);
                inputEdit.setSelection(inputEdit.length());
            }
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }


}
