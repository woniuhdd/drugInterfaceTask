package com.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    public static final String NORM_DATETIME_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static final String NORM_DATE_PATTERN="yyyy-MM-dd";
    /**
     * date类型进行格式化输出（返回类型：String）
     * @param date
     * @return
     */
    public static String dateTimeFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(NORM_DATETIME_PATTERN);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将"2015-08-31 21:08:06"型字符串转化为Date
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date StringToDateTime(String str) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(NORM_DATETIME_PATTERN);
        Date date = formatter.parse(str);
        return date;
    }

    /**
     * date类型进行格式化输出（返回类型：String）
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(NORM_DATE_PATTERN);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将"2015-08-31"型字符串转化为Date
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date StringToDate(String str) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(NORM_DATE_PATTERN);
        Date date = formatter.parse(str);
        return date;
    }

    /**
     * date类型进行格式化输出（返回类型：String）
     * @param date
     * @return
     */
    public static String dateFormat(Date date,String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 字符串转化为Date
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date StringToDate(String str,String pattern) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = formatter.parse(str);
        return date;
    }
}
