package edu.nju.mall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间工具类
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:35
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdfInDay = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当前时间，精确到秒
     *
     * @return
     */
    public static String getTime() {
        return sdf.format(new Date());
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getToday() {
        return sdfInDay.format(new Date());
    }

    public static boolean overDue(String time) {
        try {
            Date d = sdf.parse(time);
            Date now = new Date();
            int hours = (int) ((now.getTime() - d.getTime()) / (1000 * 60 * 60));
            return hours >= 2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
