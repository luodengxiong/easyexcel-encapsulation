package com.howie.easyexcelmethodencapsulation.excel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc: 基于Java8的时间日期工具类
 **/
public final class TimeUtil {

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;

    private TimeUtil() {
        // no construct function
    }

    /**
     * String 转时间
     *
     * @param timeStr
     * @return
     */
    public static LocalDateTime parseTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    public static LocalDate parseDate(String timeStr) {
        return LocalDate.parse(timeStr, TimeFormat.SHORT_DATE_PATTERN_LINE.formatter);
    }

    /**
     * String 转时间
     *
     * @param timeStr
     * @param format  时间格式
     * @return
     */
    public static LocalDateTime parseTime(String timeStr, TimeFormat format) {
        return LocalDateTime.parse(timeStr, format.formatter);
    }

    public static LocalDate parseDate(String dateString, TimeFormat format) {
        return LocalDate.parse(dateString, format.formatter);
    }

    /**
     * 时间转 String
     *
     * @param time
     * @return
     */
    public static String parseTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    public static String parseDate(LocalDate date) {
        return TimeFormat.SHORT_DATE_PATTERN_LINE.formatter.format(date);
    }

    /**
     * 时间转 String
     *
     * @param time
     * @param format 时间格式
     * @return
     */
    public static String parseTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDatetime() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return
     */
    public static String getCurrentDatetime(TimeFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    public static void main(String[] args) {

/*        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //获取当前时间
        LocalDate inputDate = LocalDate.now();

        //当天开始
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        //当天结束
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        String todayStartTime = today_start.format(DEFAULT_DATETIME_FORMATTER);
        String todayEndTime = today_end.format(DEFAULT_DATETIME_FORMATTER);
        System.out.println("todayStartTime:"+todayStartTime);
        System.out.println("todayEndTime:"+todayEndTime);

        //本周开始时间
        TemporalAdjuster FIRST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        String weekStart = df.format(inputDate.with(FIRST_OF_WEEK));
        System.out.println("weekStart:"+weekStart);
        //本周结束时间
        TemporalAdjuster LAST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
        String weekEnd = df.format(inputDate.with(LAST_OF_WEEK));
        System.out.println("weekEnd:"+weekEnd);

        //本月的第一天
        String monthStart = df.format(LocalDate.of(inputDate.getYear(), inputDate.getMonth(), 1));
        System.out.println("monthStart:"+monthStart);
        //本月的最后一天
        String monthEnd = df.format(inputDate.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("monthEnd:"+monthEnd);*/
        //yyyy-MM-dd
        Pattern datePatternLong=Pattern.compile(" (([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        //yyyyMMdd
        Pattern datePattern=Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        String filePathFull="D:\\TFS\\SAAS平台\\01开发库\\0111项目管理\\011103会议纪要\\07.其他\\LYZH-会议记录-团建活动-20190531.docx";
        Matcher matcher=datePattern.matcher(filePathFull);
        if(matcher.find()) {
            String date = matcher.group(1);
            System.out.println("date:" + date);
            LocalDate fileDate = TimeUtil.parseDate(date,TimeUtil.TimeFormat.SHORT_DATE_PATTERN_NONE);
            System.out.println("fileDate:"+TimeUtil.parseDate(fileDate));
        }

    }

}