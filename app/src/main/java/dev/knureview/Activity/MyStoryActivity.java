package dev.knureview.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.knureview.R;
import dev.knureview.Util.PixelUtil;

/**
 * Created by DavidHa on 2015. 11. 26..
 */
public class MyStoryActivity extends ActionBarActivity {
    private LinearLayout dynamicArea;
    private ImageView backgroundImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mystory_list_row);
        dynamicArea = (LinearLayout) findViewById(R.id.dynamicArea);

        makeTextView("속닥속닥");
        makeTextView("속닥속닥이 곧 오픈됩니다. 테스트용 길이 체크 ㅋㅋㅋㅋ");
        makeTextView("속닥속닥이 곧 오픈됩니다. 테스트용 길이 체크 ㅋㅋㅋㅋ");
        makeTextView("속닥속닥이 곧 오픈됩니다. 테스트용 길이 ");
        makeTextView("속닥속닥이 곧 오픈됩니다. 길이 체크 ㅋㅋㅋㅋ");
        makeTextView("속닥속닥이 곧 오픈됩니다. ");
    }

    public void makeTextView(String description) {
        final int space100 = (int) PixelUtil.convertPixelsToDp(100, this);
        final int space40 = (int) PixelUtil.convertPixelsToDp(40, this);
        final int space14 = (int) PixelUtil.convertPixelsToDp(14, this);
        final int txtSize = (int) PixelUtil.convertPixelsToDp(40, this);

        TextView text = new TextView(MyStoryActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = space40;
        text.setLayoutParams(params);
        text.setBackgroundColor(getResources().getColor(R.color.bg_color));
        text.setSingleLine(true);

        text.setPadding(space100, space14, space100, space14);
        text.setText(description);
        text.setTextColor(getResources().getColor(R.color.white));
        text.setTextSize(txtSize);

        dynamicArea.addView(text);
    }
}
