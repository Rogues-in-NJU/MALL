package edu.nju.mall.util;

public class CommonUtils {

    public static String fuzzyStringSplicing(String str) {
        String var = "%%%s%%";
        return String.format(var, str);
    }

}
