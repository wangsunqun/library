package com.wsq.util.common.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author wsq
 * 2021/1/4 11:33
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String yyyy_MM_dd_HHmmssSSS_VALUE = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String yyyy_MM_dd_HHmmss_VALUE = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HHmm_VALUE = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_VALUE = "yyyy-MM-dd HH";
    public static final String yyyy_MM_dd_VALUE = "yyyy-MM-dd";
    public static final String yyyyMMdd_VALUE = "yyyyMMdd";
    public static final String yyyy_MM_VALUE = "yyyy-MM";
    public static final String yyyyMM_VALUE = "yyyyMM";
    public static final String MM_dd_HHmmss_VALUE = "MM-dd HH:mm:ss";
    public static final String MM_dd_HHmm_VALUE = "MM-dd HH:mm";
    public static final String MM_dd_VALUE = "MM-dd";
    public static final String HHmmss_VALUE = "HH:mm:ss";
    public static final String HHmm_VALUE = "HH:mm";

    public static String localDateTime2string(LocalDateTime localDateTime, String pattern) {
        if (null == localDateTime) {
            throw new IllegalArgumentException("localDateTime is null");
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDate2string(LocalDate localDate, String pattern) {
        if (null == localDate) {
            throw new IllegalArgumentException("localDate is null");
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String date2string(Date date, String pattern) {
        if (null == date) {
            throw new IllegalArgumentException("date is null");
        }
        return DateFormatUtils.format(date, pattern);
    }

    public static String timestamp2string(Long timestamp, String pattern) {
        if (null == timestamp) {
            throw new IllegalArgumentException("timestamp is null");
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime string2localDateTime(String str, String pattern) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("localDateTime is null");
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, df);
    }

    public static LocalDate string2localDate(String str, String pattern) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("localDate is null");
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(str, df);
    }

    public static Date string2date(String str, String pattern) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("date is null");
        }
        try {
            return parseDate(str, Locale.getDefault(), pattern);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static Date timestamp2date(Long timestamp) {
        if (null == timestamp) {
            throw new IllegalArgumentException("timestamp is null");
        }
        return Date.from(Instant.ofEpochMilli(timestamp));
    }

    public static LocalDateTime timestamp2localDateTime(Long timestamp) {
        if (null == timestamp) {
            throw new IllegalArgumentException("timestamp is null");
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 计算两个时间相差的小时数目(精确到小数位)
     *
     * @param beginDateStr 开始时间
     * @param endDateStr   结束时间
     * @return 小时数
     */
    public static double getDiffHour(String beginDateStr, String endDateStr, String pattern) {
        Date beginDate = string2date(beginDateStr, pattern);
        Date endDate = string2date(endDateStr, pattern);
        return getDiffHour(beginDate, endDate);
    }

    /**
     * 计算两个时间相差的小时数目(精确到小数位)
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 小时数
     */
    public static double getDiffHour(Date beginDate, Date endDate) {
        if (null == beginDate || null == endDate) {
            throw new IllegalArgumentException("date is null");
        }
        long nh = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - beginDate.getTime();
        // 计算差多少小时
        return (double) diff / (double) nh;
    }

    /**
     * 计算两个时间相差的天数(向下取整)
     *
     * @param beginDateStr 开始时间
     * @param endDateStr   结束时间
     * @return 天数
     */
    public static int getDiffDay(String beginDateStr, String endDateStr, String pattern) {
        Date beginDate = string2date(beginDateStr, pattern);
        Date endDate = string2date(endDateStr, pattern);
        return getDiffDay(beginDate, endDate);
    }

    /**
     * 计算两个时间相差的天数目(向下取整)
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 天数
     */
    public static int getDiffDay(Date beginDate, Date endDate) {
        long nh = 1000 * 60 * 60 * 24;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - beginDate.getTime();
        // 计算差多少小时
        return (int) (diff / nh);
    }

    /**
     * 获取时间差
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     */
    public static Duration getDiff(Date beginDate, Date endDate) {
        GregorianCalendar beginCalendar = new GregorianCalendar();
        beginCalendar.setTime(beginDate);
        if (null == endDate) {
            throw new IllegalArgumentException("enddate is null");
        }
        GregorianCalendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        return Duration.between(beginCalendar.toZonedDateTime(), endCalendar.toZonedDateTime());
    }

    /**
     * 给时间做加法
     */
    public static Date plus(String dateStr, String pattern, long amountToAdd, ChronoUnit chronoUnit) {
        Date date = string2date(dateStr, pattern);
        return plus(date, amountToAdd, chronoUnit);
    }

    /**
     * 给时间做加法
     */
    public static Date plus(Date date, long amountToAdd, ChronoUnit chronoUnit) {
        if (null == date) {
            throw new IllegalArgumentException("date is null");
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plus(amountToAdd, chronoUnit);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    /**
     * 比较两个时间是否为同一天
     */
    public static boolean isSameDay(String dateStr1, String dateStr2) {
        return isSameDay(string2date(dateStr1, yyyyMMdd_VALUE), string2date(dateStr2, yyyyMMdd_VALUE));
    }

    /**
     * 判断是否是当月最后一天
     *
     * @param date 日期
     */
    public static boolean isLastDayOfMonth(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        int dayOfMonth = zonedDateTime.getDayOfMonth();
        int lastDayOfMonth = zonedDateTime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        return dayOfMonth == lastDayOfMonth;
    }
}
