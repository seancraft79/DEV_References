package com.sysgen.eom.util;

import android.util.Log;

import com.sysgen.eom.BuildConfig;
import com.sysgen.eom.helper.ServerTimeHelper;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /** 요일 INTEGER **/
    public final static int MONDAY_N = 1;
    public final static int TUESDAY_N = 2;
    public final static int WEDNESDAY_N = 3;
    public final static int THURSDAY_N = 4;
    public final static int FRIDAY_N = 5;
    public final static int SATURDAY_N = 6;
    public final static int SUNDAY_N = 7;

    /** 요일 STRING **/
    public final static String MONDAY_S = "월";
    public final static String TUESDAY_S = "화";
    public final static String WEDNESDAY_S = "수";
    public final static String THURSDAY_S = "목";
    public final static String FRIDAY_S = "금";
    public final static String SATURDAY_S = "토";
    public final static String SUNDAY_S = "일";


    /**
     * 오늘 날짜 해당 요일을 숫자로 반환
     * @return
     */
    public static int getTodayOfWeekNum() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return MONDAY_N;
            case Calendar.TUESDAY:
                return TUESDAY_N;
            case Calendar.WEDNESDAY:
                return WEDNESDAY_N;
            case Calendar.THURSDAY:
                return THURSDAY_N;
            case Calendar.FRIDAY:
                return FRIDAY_N;
            case Calendar.SATURDAY:
                return SATURDAY_N;
            case Calendar.SUNDAY:
                return SUNDAY_N;
        }
        return MONDAY_N;
    }

    /**
     * 입력 요일 String 에 대한 숫자 반환
     * @param dayOfWeek
     * @return
     */
    public static int getDayOfWeekNumByStr(String dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY_S: return MONDAY_N;
            case TUESDAY_S: return TUESDAY_N;
            case WEDNESDAY_S: return WEDNESDAY_N;
            case THURSDAY_S: return THURSDAY_N;
            case FRIDAY_S: return FRIDAY_N;
            case SATURDAY_S: return SATURDAY_N;
            case SUNDAY_S: return SUNDAY_N;
        }
        return MONDAY_N;
    }

    /**
     * 오늘 날짜 해당 요일 String 반환
     * @return
     */
    public static String getDayOfWeekKo() {
        return getDayOfWeekKo(getTodayOfWeekNum());
    }

    /**
     * 어제 날짜 해당 요일 String 반환
     * @return
     */
    public static String getBeforeDayOfWeekKo() {
        int dow = getTodayOfWeekNum() - 1;
        if(dow < MONDAY_N) dow = SUNDAY_N;
        return getDayOfWeekKo(dow);
    }

    /**
     * 입력한 숫자에 해당하는 요일 String 반환
     * @param dayOfWeekNum
     * @return
     */
    public static String getDayOfWeekKo(int dayOfWeekNum) {
        switch (dayOfWeekNum) {

            case MONDAY_N:
                return MONDAY_S;
            case TUESDAY_N:
                return TUESDAY_S;
            case WEDNESDAY_N:
                return WEDNESDAY_S;
            case THURSDAY_N:
                return THURSDAY_S;
            case FRIDAY_N:
                return FRIDAY_S;
            case SATURDAY_N:
                return SATURDAY_S;
            case SUNDAY_N:
                return SUNDAY_S;
        }
        return MONDAY_S;
    }

    public static String getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "월요일";
            case Calendar.TUESDAY:
                return "화요일";
            case Calendar.WEDNESDAY:
                return "수요일";
            case Calendar.THURSDAY:
                return "목요일";
            case Calendar.FRIDAY:
                return "금요일";
            case Calendar.SATURDAY:
                return "토요일";
            case Calendar.SUNDAY:
                return "일요일";
        }
        return "";
    }

    /**
     * 오늘 날짜 요일의 다음 요일 Integer 반환
     * @return
     */
    public static int getNextDayOfWeek() {
        int dowNum = getTodayOfWeekNum() + 1;
        if(dowNum > SUNDAY_N) dowNum = MONDAY_N;
        return dowNum;
    }

    /**
     * 입력된 요일 숫자의 다음 요일 숫자를 반환
     * @param day
     * @return
     */
    public static int getnextDayOfWeekNumByNum(int day) {
        int d = day + 1;
        if(d > SUNDAY_N) return MONDAY_N;
        return d;
    }

    /**
     * 오늘 날짜 요일의 다음 요일 String 반환
     * @return
     */
    public static String getNextDayOfWeekKo() {
        int nextDayOfWeekNum = getNextDayOfWeek();
        return getDayOfWeekKo(nextDayOfWeekNum);
    }

    /**
     * 입력한 요일의 다음날 요일을 반환
     * @param dayOfWeek
     * @return
     */
    public static String getNextDayOfWeekKoByStr(String dayOfWeek) {
        int dayOfWeekNumByStr = getDayOfWeekNumByStr(dayOfWeek);
        int nextDay = getnextDayOfWeekNumByNum(dayOfWeekNumByStr);
        return getDayOfWeekKo(nextDay);
    }

    /**
     * Get Hours from sql.Time
     * @param time
     * @return
     */
    public static int getHours(Time time) {
        try {
            String[] splitted = time.toString().split(":");
            if (splitted != null && splitted.length == 3) {
                return ParseUtil.parseStringToInt(splitted[0], 0);
            }
        } catch (Exception e){}
        return 0;
    }

    /**
     * Get minutes from sql.Time
     * @param time
     * @return
     */
    public static int getMinutes(Time time) {
        try {
            String[] splitted = time.toString().split(":");
            if (splitted != null && splitted.length == 3) {
                return ParseUtil.parseStringToInt(splitted[1], 0);
            }
        } catch (Exception e){}
        return 0;
    }

    /**
     * Convert sql.Time to total minnutes
     * @param time
     * @return
     */
    public static int getTimeToMins(Time time) {
        return (getHours(time) * 60) + getMinutes(time);
    }

    /**
     * Get this year String
     * @return
     */
    public static String getThisYear() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * Get this month String
     * @return
     */
    public static String getThisMonth() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    /**
     * Get today String
     * @return
     */
    public static String getToday() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    public static String getTodayYM() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String getHour() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(date);
    }

    public static String getMinute() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return sdf.format(date);
    }

    public static Date getDate(int year, int month, int date) {
        try {
            String timeStr = year + "-" + month + "-" + date;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(timeStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDate(int year, int month, int date, int hour, int minute, int second) {
        try {
            String timeStr = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.parse(timeStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getNowString(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    // 앱에서 사용하는 시간 (연도)
    public static int getAppYearInt() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.year == null || ServerTimeHelper.year.isEmpty()) {
                ServerTimeHelper.year = getThisYear();
            }
            return ParseUtil.parseStringToInt(ServerTimeHelper.year, 2020);
        } else {
            return ParseUtil.parseStringToInt(getThisYear(), 2020);
        }
    }

    public static String getAppYearStr() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.year == null || ServerTimeHelper.year.isEmpty()) {
                ServerTimeHelper.year = getThisYear();
            }
            return ServerTimeHelper.year;
        } else {
            return getThisYear();
        }
    }

    // 앱에서 사용하는 시간 (달)
    public static int getAppMonthInt() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.month == null || ServerTimeHelper.month.isEmpty()) {
                ServerTimeHelper.month = getThisMonth();
            }
            return ParseUtil.parseStringToInt(ServerTimeHelper.month, 1);
        } else {
            return ParseUtil.parseStringToInt(getThisMonth(), 1);
        }
    }

    public static String getAppMonthStr() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.month == null || ServerTimeHelper.month.isEmpty()) {
                ServerTimeHelper.month = StringUtil.removeFirstNotDigit(getThisMonth());
            }
            return ServerTimeHelper.month;
        } else {
            return StringUtil.removeFirstNotDigit(getThisMonth());
        }
    }

    // 앱에서 사용하는 시간 (날짜)
    public static int getAppDateInt() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.date == null || ServerTimeHelper.date.isEmpty()) {
                ServerTimeHelper.date = getToday();
            }
            return ParseUtil.parseStringToInt(ServerTimeHelper.date, 1);
        } else {
            return ParseUtil.parseStringToInt(getToday(), 1);
        }
    }

    public static String getAppDateStr() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.date == null || ServerTimeHelper.date.isEmpty()) {
                ServerTimeHelper.date = StringUtil.removeFirstNotDigit(getToday());
            }
            return ServerTimeHelper.date;
        } else {
            return StringUtil.removeFirstNotDigit(getToday());
        }
    }

    // 앱에서 사용하는 시간 (시)
    public static int getAppHourInt() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.hour == null || ServerTimeHelper.hour.isEmpty()) {
                ServerTimeHelper.hour = getHour();
            }
            return ParseUtil.parseStringToInt(ServerTimeHelper.hour, 1);
        } else {
            return ParseUtil.parseStringToInt(getHour(), 1);
        }
    }

    public static int getAppMinuteInt() {
        if(BuildConfig.USE_SERVERTIME) {
            if(ServerTimeHelper.minute == null || ServerTimeHelper.minute.isEmpty()) {
                ServerTimeHelper.minute = getMinute();
            }
            return ParseUtil.parseStringToInt(ServerTimeHelper.minute, 1);
        } else {
            return ParseUtil.parseStringToInt(getMinute(), 1);
        }
    }

    // 앱에서 사용하는 시간 (요일)
    public static String getAppDayOfWeek(){
        Date d = getDate(getAppYearInt(), getAppMonthInt(), getAppDateInt());
        if(d != null) {
            return getDayOfWeek(d);
        }
        return "";
    }

    public static String getAppTime() {
        if(BuildConfig.USE_SERVERTIME) {
            return getAppYearStr() + "-" + getAppMonthStr() + "-" + getAppDateStr() + " " + ServerTimeHelper.hour + ":" + ServerTimeHelper.minute;
        } else {
            return getNowString();
        }
    }

    // 입력한 날짜의 offset 만큼 후의 날짜 반환
    public static int[] getAppNextDateInt(int year, int month, int date, int offset) {
        try {
            String dt = year + "-" + month + "-" + date;  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, offset);  // number of days to add
//            dt = sdf.format(c.getTime());  // dt is now the new date
            return new int[]{c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE)};
        } catch (Exception e) {
            return new int[]{year, month, date};
        }
    }
}

