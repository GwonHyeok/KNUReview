package dev.knureview.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DavidHa on 2015. 8. 11..
 */
public class TimeFormat {
    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msg = null;
        try {
            Date tempDate = format.parse(strDate);

            long curTime = System.currentTimeMillis();
            long regTime = tempDate.getTime();
            long diffTime = (curTime - regTime) / 1000;


            if (diffTime < TIME_MAXIMUM.SEC) {
                // sec
                msg = "방금 전";
            } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                // min
                msg = diffTime + "분 전";
            } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                // hour
                msg = (diffTime) + "시간 전";
            } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                // day
                msg = (diffTime) + "일 전";
            } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                // day
                msg = (diffTime) + "달 전";
            } else {
                msg = (diffTime) + "년 전";
            }
        } catch (ParseException e) {

        }
        return msg;
    }

    public static String formatDateString(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM.dd E요일");
        String msg = null;
        try{
            Date date = format.parse(strDate);
            msg = dateFormat.format(date).toString();

        }catch (ParseException e){

        }
        return msg;
    }
}