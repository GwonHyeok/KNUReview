package dev.knureview.Util;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 12. 20..
 */
public class TalkTextUtil {
    private static Activity activity;
    private int textSize;


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setTextSizeUP() {
        this.textSize = 10;
    }


    private void makeTextView(String description, LinearLayout dynamicArea) {
        final int space70 = (int) PixelUtil.convertPixelsToDp(70 + textSize, activity);
        final int space40 = (int) PixelUtil.convertPixelsToDp(40 + textSize, activity);
        final int space14 = (int) PixelUtil.convertPixelsToDp(14 + textSize, activity);
        final int txtSize = (int) PixelUtil.convertPixelsToDp(40 + textSize, activity);

        TextView text = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = space40;
        text.setLayoutParams(params);
        text.setBackgroundColor(activity.getResources().getColor(R.color.dim_color));
        text.setSingleLine(true);

        text.setPadding(space70, space14, space70, space14);
        text.setText(description);
        text.setTextColor(activity.getResources().getColor(R.color.white));
        text.setTextSize(txtSize);

        dynamicArea.addView(text);
    }

    public void setDescription(String description, LinearLayout dynamaicArea) {
        int count = 0;
        int findPosition = 0;
        for (int i = 0; i < description.length(); i++) {
            count++;
            char ch = description.charAt(i);

            if (ch == '\n' || count > 21) {
                String tempStr = description.substring(findPosition, i).replace("\n", "").trim();
                makeTextView(tempStr, dynamaicArea);
                findPosition = i;
                count = 1;
            }
        }

        if (findPosition == 0 || findPosition + count == description.length()) {
            makeTextView(description.substring(findPosition).trim(), dynamaicArea);
        }
    }

}
