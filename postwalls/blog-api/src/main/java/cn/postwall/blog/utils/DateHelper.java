package cn.postwall.blog.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DateHelper {

    /**
     * 获取当前时间的 年
     * @return
     */
    public static String getNowYear(){
        int year = LocalDateTime.now().getYear();
        return ""+year;
    }

    /**
     * 获取当前时间的 月
     * @return
     */
    public static String getNowMonth(){
        int monthValue = LocalDateTime.now().getMonthValue();
        return ""+monthValue;
    }

    /**
     * 获取当前时间的 日
     * @return
     */
    public static String getNowDay(){
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        return ""+dayOfMonth;
    }

    /**
     * 获取当前时间  格式: yyyy-MM-dd
     * @return
     */
    public static String getYDM(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 获取当前时间  格式: yyyy/MM/dd
     * @return
     */
    public static String getYDM2(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }

    public static String getYDMHMS() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 时间戳转 yyyy-MM-dd HH：mm：ss
     * @param timestamp
     * @return
     */
    public static String formatTimeStamp(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(timestamp));
    }

    /**
     * 获取指定时间，指定格式
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(date);
    }

}
