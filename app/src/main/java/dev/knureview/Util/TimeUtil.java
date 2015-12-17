package dev.knureview.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DavidHa on 2015. 9. 9..
 */
public class TimeUtil {
    private Date now;
    private SimpleDateFormat CurWeekFormat;
    private SimpleDateFormat CurYearFormat;
    private SimpleDateFormat CurMonthFormat;
    private SimpleDateFormat CurDayFormat;
    private SimpleDateFormat CurHourFormat;
    private SimpleDateFormat CurMinuteFormat;
    private SimpleDateFormat CurSecondFormat;


    public TimeUtil() {
        now = new Date();
        CurWeekFormat = new SimpleDateFormat("yyyy-MM-dd");
        CurYearFormat = new SimpleDateFormat("yyyy");
        CurMonthFormat = new SimpleDateFormat("MM");
        CurDayFormat = new SimpleDateFormat("dd");
        CurHourFormat = new SimpleDateFormat("HH");
        CurMinuteFormat = new SimpleDateFormat("mm");
        CurSecondFormat = new SimpleDateFormat("ss");
    }

    public String getYear() {
        return CurYearFormat.format(now);
    }

    public int getMonth() {
        return Integer.parseInt(CurMonthFormat.format(now));
    }

    public int getDay() {
        return Integer.parseInt(CurDayFormat.format(now));
    }

    public int getDateDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getHour() {
        return Integer.parseInt(CurHourFormat.format(now));
    }

    public int getMinute() {
        return Integer.parseInt(CurMinuteFormat.format(now));
    }

    public int getSecond() {
        return Integer.parseInt(CurSecondFormat.format(now));
    }
}
