package dev.knureview.Util;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        this.textSize = 14;
    }


    public void makeTextView(String description, LinearLayout dynamicArea) {
        final int space100 = (int) PixelUtil.convertPixelsToDp(100+textSize, activity);
        final int space40 = (int) PixelUtil.convertPixelsToDp(40+textSize, activity);
        final int space14 = (int) PixelUtil.convertPixelsToDp(14+textSize, activity);
        final int txtSize = (int) PixelUtil.convertPixelsToDp(40+textSize, activity);

        TextView text = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = space40;
        text.setLayoutParams(params);
        text.setBackgroundColor(activity.getResources().getColor(R.color.transparent_black));
        text.setSingleLine(true);

        text.setPadding(space100, space14, space100, space14);
        text.setText(description);
        text.setTextColor(activity.getResources().getColor(R.color.white));
        text.setTextSize(txtSize);

        dynamicArea.addView(text);
    }

    public void setDescription(String description, LinearLayout dynamicArea) {
        int count = 0;
        int findPosition = 0;
        String remainStr = "";
        for (int i = 0; i < description.length(); i++) {
            count++;
            char ch = description.charAt(i);
            if (ch == '\n' || count > 25) {
                String tempStr = description.substring(findPosition, i).replace("\n", "").trim();

                if (ch != ' ' && ch != '\n') {
                    int tempPosition = tempStr.lastIndexOf(' ');
                    makeTextView(tempStr.substring(0, tempPosition).trim(), dynamicArea);
                    remainStr = tempStr.substring(tempPosition).trim();
                } else {
                    makeTextView(remainStr + tempStr, dynamicArea);
                    remainStr = "";
                }
                findPosition = i;
                count = 0;
            }
        }
        if (findPosition == 0 || (count + findPosition + 1) == description.length()) {
            makeTextView(description.substring(findPosition).trim(), dynamicArea);
        }
    }
}
