package com.piles.common.util;


import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 基于jdk1.8的日期转换
 */
public class JdkDateUtil {

    private static final String DATE_TIME_DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_DEFAULT_PATTERN = "yyyy-MM-dd";

    /**
     * localDateTime 转换 date
     * @param localDateTime
     * @return date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        if(localDateTime==null){
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * date 转换 localDateTime
     * @param date 日期
     * @return localDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        if(date==null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);

    }

    /**
     * localDate 转换 date
     * @param date 日期
     * @return LocalDate
     */
    public static Date localDate2Date(LocalDate localDate){
        if(localDate==null){
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * date 转换 localDate
     * @param date 日期
     * @return localDate
     */
    public static LocalDate date2LocalDate(Date date){
        if(date==null){
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 格式化日期
     * @param date 日期
     * @param pattern 格式
     * @return 格式化的日志字符串
     */
    public static String format(Date date,String pattern){
        if(date==null){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern = DATE_TIME_DEFAULT_PATTERN;
        }
        LocalDateTime localDateTime = JdkDateUtil.date2LocalDateTime(date);
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期+时间
     * @param date 日期
     * @param pattern 格式
     * @return 格式化的日志字符串
     */
    public static Date parseDateTime(String dateStr,String pattern){
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern = DATE_TIME_DEFAULT_PATTERN;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        return JdkDateUtil.localDateTime2Date(localDateTime);
    }
    /**
     * 格式化日期
     * @param date 日期
     * @param pattern 格式
     * @return 格式化的日志字符串
     */
    public static Date parseDate(String dateStr,String pattern){
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern = DATE_DEFAULT_PATTERN;
        }
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        return JdkDateUtil.localDate2Date(localDate);
    }

    //测试
    public static void main(String[] args) {
        Date date = new Date();
        String format = JdkDateUtil.format(date, "yyyyMMdd");
        System.out.println(format);

        String str = "2017-08-22 17:43:34";
        Date parse = JdkDateUtil.parseDateTime(str, "yyyy-MM-dd HH:mm:ss");
        System.out.println(parse);

        String dateStr = "2017-08-22";
        Date parseDate = JdkDateUtil.parseDate(dateStr, "yyyy-MM-dd");
        System.out.println(parseDate);
    }


}
