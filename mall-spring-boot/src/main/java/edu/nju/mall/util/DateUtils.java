package edu.nju.mall.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间工具类
 * @Author: qianen.yin
 * @CreateDate: 2020-02-03 14:35
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取当前时间，精确到秒
     * @return
     */
    public static String getTime() {
        return sdf.format(new Date());
    }
}
