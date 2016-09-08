package cn.insightsresearch.fgi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/16.
 */
public class DateUtil {

    public static String getDayBeforeThisDayString(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try{
            calendar.setTime(simpleDateFormat.parse(dateStr));
            //calendar.add(Calendar.DAY_OF_YEAR,-1);
            date = calendar.getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    public static String getFormatDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if(dateStr== null||"".equals(dateStr)) dateStr = "1999/01/01 00:00:00";
        Date date = null;
        try{
            date = simpleDateFormat.parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    public static String getFormatDateTime(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if(dateStr== null||"".equals(dateStr)) dateStr = "1999/01/01 00:00:00";
        Date date = null;
        try{
            date = simpleDateFormat.parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    public static String getDateTime(){
        Date date = new Date();
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String getDateTimeString(){
        Date date=new Date();
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time=sdf.format(date);
        return time;
    }

    public  static String getDateString(){
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = sdf.format(date);
        return str;
    }

    public  static String getTimeString(){
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(date);
        return str;
    }

    public static String getTime1String(){
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("ddHHmmss");
        String str = sdf.format(date);
        return str;
    }
}
