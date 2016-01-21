package dev.knureview.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DavidHa on 2015. 9. 9..
 */
public class TimeUtil {
    private Date now;
    private SimpleDateFormat yearMDFormat;
    private SimpleDateFormat yearFormat;
    private SimpleDateFormat monthFormat;
    private SimpleDateFormat dayFormat;
    private SimpleDateFormat hourFormat;
    private SimpleDateFormat minuteFormat;
    private SimpleDateFormat secondFormat;


    public TimeUtil() {
        now = new Date();
        yearMDFormat = new SimpleDateFormat("yyyy-MM-dd");
        yearFormat = new SimpleDateFormat("yyyy");
        monthFormat = new SimpleDateFormat("MM");
        dayFormat = new SimpleDateFormat("dd");
        hourFormat = new SimpleDateFormat("HH");
        minuteFormat = new SimpleDateFormat("mm");
        secondFormat = new SimpleDateFormat("ss");
    }

    public String getCurYear() {
        return yearFormat.format(now);
    }

    public int getCurMonth() {
        return Integer.parseInt(monthFormat.format(now));
    }
    public int getMonth(Date date){ return Integer.parseInt(monthFormat.format(date));}

    public int getCurDay() {
        return Integer.parseInt(dayFormat.format(now));
    }
    public int getDay(Date date){ return Integer.parseInt(dayFormat.format(date));}
    public int getCurDateDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getCurHour() {
        return Integer.parseInt(hourFormat.format(now));
    }

    public int getCurMinute() {
        return Integer.parseInt(minuteFormat.format(now));
    }

    public int getCurSecond() {
        return Integer.parseInt(secondFormat.format(now));
    }

    public Date changeStrToDate(String str){
        Date date=null;
        try {
             date = yearMDFormat.parse(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }
    public String changeDateToStr(Date date){
        String str = null;
        try{
            str = yearMDFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
}
