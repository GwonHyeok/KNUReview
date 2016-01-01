package dev.knureview.Util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by davidha on 2015. 5. 31..
 */
public class PixelUtil {
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int displayDP = metrics.densityDpi;
        float dp = 0;
        if (displayDP <= 240) {
            dp = px / ((displayDP * 4f) / 160f);
        } else if (displayDP <= 320) {
            dp = px / ((displayDP * 2.25f) / 160f);
        } else if (displayDP <= 480) {
            dp = px / (displayDP / 160f);

        } else if (displayDP <= 560) {
            dp = px / ((displayDP / 1.36f) / 160f);
        } else if (displayDP <= 640) {
            dp = px / ((displayDP / 1.32f) / 160f);
        }
        return dp;
    }
}
