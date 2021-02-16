package com.sysgen.eom.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sysgen.eom.BuildConfig;
import com.sysgen.eom.beans.Alarm;
import com.sysgen.eom.beans.Alarms;
import com.sysgen.eom.config.Configs;
import com.sysgen.eom.helper.ServerTimeHelper;
import com.sysgen.eom.model.DeviceOp;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import static com.sysgen.eom.util.TimeUtil.getDayOfWeekKo;
import static com.sysgen.eom.util.TimeUtil.getDayOfWeekNumByStr;
import static com.sysgen.eom.util.TimeUtil.getnextDayOfWeekNumByNum;

public class AlarmSetUtil {
    public static final String TAG = "AlarmSetUtil";

    public static void setNextPowerOffAlarm(Context context) {
        if(BuildConfig.USE_SERVERTIME) {
            return;
        }
        Alarm offAlarm = getPowerOffAlarm(context);
        if(offAlarm != null) {
            SLog.d("setNextPowerOffAlarm : " + offAlarm.toString());
            AlarmUtil.setOneShotPowerOff(context, offAlarm);
        }
//        else SLog.e(TAG, "setNextPowerOffAlarm alarm is null");
    }

    public static void setNextPowerOnAlarm(Context context) {
        if(BuildConfig.USE_SERVERTIME) {
            return;
        }
        Alarm onAlarm = getPowerOnAlarm(context);
        if(onAlarm != null) {
            SLog.d("setNextPowerOnAlarm : " + onAlarm.toString());
            AlarmUtil.setOneShotPowerOn(context, onAlarm);
        }
//        else SLog.e("setNextPowerOnAlarm alarm is null");
    }

    @SuppressLint({"SimpleDateFormat"})
    public static boolean isOpenRect(Alarms alarms) {
        Calendar c = Calendar.getInstance();
        int curMinute = (c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE);
        int openMinute = (alarms.openAlarm.hour * 60) + alarms.openAlarm.minute;
        int closeMinute = (alarms.closeAlarm.hour * 60) + alarms.closeAlarm.minute;
//        SLog.d(TAG, "###isOpenRect openMinute =" + openMinute);
//        SLog.d(TAG, "###isOpenRect closeMinute =" + closeMinute);
//        SLog.d(TAG, "###isOpenRect curMinute =" + curMinute);
        if (curMinute + 5 < openMinute || curMinute > closeMinute) {
            return false;
        }
        return true;
    }

    /**
     * Start 시간과 End 시간의 중간에 있는지 여부
     * @param startAt
     * @param endAt
     * @return
     */
    public static boolean isOpenHour(Time startAt, Time endAt) {
        int hour = TimeUtil.getAppHourInt();
        int minute = TimeUtil.getAppMinuteInt();
        if(hour == 0 && minute == 0) return false;
        int curMinute = ((hour * 60) + minute);

        int openMinute = TimeUtil.getTimeToMins(startAt);
        int closeMinute = TimeUtil.getTimeToMins(endAt);
//        SLog.d(TAG, "###isOpenRect openMinute =" + openMinute);
//        SLog.d(TAG, "###isOpenRect closeMinute =" + closeMinute);
//        SLog.d(TAG, "###isOpenRect curMinute =" + curMinute);

        if(openMinute <= curMinute && curMinute <= closeMinute) {
            return true;
        }
        return false;
    }

    /**
     * 단말기가 On 하는 알람 시간 반환 (켜지는 것은 항상 하루 이상 지나서)
     * @param context
     * @return
     */
    public static Alarm getPowerOnAlarm(Context context) {
        final String TAG = "[getPowerOnAlarm] ";

        DeviceOp todayDeviceOp = getTodayDeviceOp(context);
        DeviceOp nextDayDeviceOp = getNextDayDeviceOp(context);
        if(todayDeviceOp == null || nextDayDeviceOp == null) {
//            SLog.e(TAG + "DeviceOp data is null");
            return null;
        }

        int dayOffset = getNextDayScheduleOffset(todayDeviceOp, nextDayDeviceOp);
        SLog.d(TAG + "todayDeviceOp : " + todayDeviceOp.toString() + ", nextDayDeviceOp : " + nextDayDeviceOp.toString() + ", offset : " + dayOffset);

//        Calendar c = getNextdayCalendar(dayOffset);
//        c.add(Calendar.DAY_OF_MONTH, 1);
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
//        dayOfMonth = ParseUtil.parseStringToInt(nextDayDeviceOp.getDOM(), dayOfMonth);

        int[] d = TimeUtil.getAppNextDateInt(TimeUtil.getAppYearInt(), TimeUtil.getAppMonthInt(), TimeUtil.getAppDateInt(), dayOffset);
        int year = d[0];
        int month = d[1];
        int date = d[2];

        SLog.d(TAG + "NextCalendar year: " + year + ", month: " + month + ", dayOfMonth: " + date);

        int alarmId = Configs.ID_ONE_SHOTPOWER_ON;
//        AlarmUtil.disableAlarm(context, alarmId);
        Alarm alarm = Alarm.getAlarm(alarmId, year, month, date, 9, 0);

        Time startAt = nextDayDeviceOp.getSTART_AT();
        int startHour = TimeUtil.getHours(startAt);
        int startMins = TimeUtil.getMinutes(startAt);
        SLog.d("Next alarm startHour: " + startHour + ", startMins: " + startMins);

        alarm.year = year;
        alarm.month = month;
        alarm.day = date;
        alarm.hour = startHour;
        alarm.minute = startMins;

        SLog.d(TAG + "powerOnAlarm: " + alarm.toString());

        return alarm;
    }
    // TODO : TEST
//    public static Alarm getPowerOnAlarm(Context context) {
//        final String TAG = "[getPowerOnAlarm] ";
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int date = c.get(Calendar.DAY_OF_MONTH);
//        String dayOfWeek = TimeUtil.getDayOfWeekKo();
//        SLog.d(TAG + "year: " + year + ", month: " + month + ", date: " + date + ", hourOfDay: " + ", dayOfWeek: " + dayOfWeek);
//
//        DeviceOp todayDeviceOp = getTodayDeviceOp(context);
//        if(todayDeviceOp == null) {
//            SLog.e(TAG + "getTodayDeviceOp is null");
//            return null;
//        }
//
//
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        return Alarm.getNextAlarm(Configs.ID_ONE_SHOTPOWER_ON, year, month, date, hour, minute+1);
//    }

    /**
     * 단말기가 sleep 하는 알람 시간 반환 (꺼지는 것은 켜지는 날과 항상 동일)
     * @param context
     * @return
     */
    public static Alarm getPowerOffAlarm(Context context) {
        final String TAG = "[getPowerOffAlarm] ";
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int date = c.get(Calendar.DAY_OF_MONTH);
//        String dayOfWeek = TimeUtil.getDayOfWeekKo();

        int year = TimeUtil.getAppYearInt();
        int month = TimeUtil.getAppMonthInt();
        int date = TimeUtil.getAppDateInt();

        String dayOfWeek = TimeUtil.getDayOfWeekKo(date);

        SLog.d(TAG + "year: " + year + ", month: " + month + ", date: " + date + ", hourOfDay: " + ", dayOfWeek: " + dayOfWeek);

        DeviceOp todayDeviceOp = getTodayDeviceOp(context);
        if(todayDeviceOp == null) {
            SLog.e(TAG + "getTodayDeviceOp is null");
            return Alarm.getAlarm(Configs.ID_ONE_SHOTPOWER_OFF, year, month, date, 18, 0);
        }

        int alarmId = Configs.ID_ONE_SHOTPOWER_OFF;
//        AlarmUtil.disableAlarm(context, alarmId);
        Alarm alarm = Alarm.getAlarm(alarmId, year, month, date, 18, 0);
        PrefUtil.increaseAlarmId(context);

        Time startAt = todayDeviceOp.getSTART_AT();
        Time endAt = todayDeviceOp.getEND_AT();
        SLog.d(TAG + "found dayOfWeek start : " + startAt + ", end: " + endAt);

//        boolean isInOpenHour = isInOpenHour(startAt, endAt);
//        if(!isInOpenHour) return null;

        int endHour = TimeUtil.getHours(endAt);
        int endMins = TimeUtil.getMinutes(endAt);
        SLog.d("endHour: " + endHour + ", endMins: " + endMins);
        alarm.year = year;
        alarm.month = month;
        alarm.day = date;
        alarm.hour = endHour;
        alarm.minute = endMins;

        SLog.d(TAG + "powerOffAlarm: " + alarm.toString());

        return alarm;
    }
    // TODO : TEST
//    public static Alarm getPowerOffAlarm(Context context) {
//        final String TAG = "[getPowerOffAlarm] ";
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int date = c.get(Calendar.DAY_OF_MONTH);
//        String dayOfWeek = TimeUtil.getDayOfWeekKo();
//        SLog.d(TAG + "year: " + year + ", month: " + month + ", date: " + date + ", hourOfDay: " + ", dayOfWeek: " + dayOfWeek);
//
//        DeviceOp todayDeviceOp = getTodayDeviceOp(context);
//        if(todayDeviceOp == null) {
//            SLog.e(TAG + "getTodayDeviceOp is null");
//            return null;
//        }
//
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        return Alarm.getNextAlarm(Configs.ID_ONE_SHOTPOWER_ON, year, month, date, hour, minute+1);
//    }

    /**
     * 오늘 이후 다음 스케쥴 데이터 반환
     * @param context
     * @return
     */
    public static DeviceOp getNextDayDeviceOp(Context context) {
        final String TAG = "[getNextDeviceOp] ";

        List<DeviceOp> deviceOpList = PrefUtil.getDeviceSchedule(context);
        if(deviceOpList == null || deviceOpList.size() < 1) {
            SLog.e(TAG + "failed to get deviceOpList");

            return DeviceOp.getNextDayDefaultOne();
        }

        int today = TimeUtil.getAppDateInt();

        if(today > -1) {
            while (true) {
                today++;
//                SLog.d("start nextDay: " + today);
                for (int i = 0; i < deviceOpList.size(); i++) {
                    int dow = ParseUtil.parseStringToInt(deviceOpList.get(i).getDOM(), -1);
                    if(dow > -1 && dow == today) {
                        return deviceOpList.get(i);
                    }
                }
                if(today > 31) break;
            }
        }

        // today 가 월말이어서 다음날이 1일 인 경우는 1부터 다시 시작
        today = 0;
        while (true) {
            today++;
//            SLog.d("start nextDay: " + today);
            for (int i = 0; i < deviceOpList.size(); i++) {
                int dow = ParseUtil.parseStringToInt(deviceOpList.get(i).getDOM(), -1);
                if(dow > -1 && dow == today) {
                    return deviceOpList.get(i);
                }
            }
            if(today > 31) break;
        }

        SLog.e("[getNextDayDeviceOp] can not find next day deviceOp");

        return null;
    }

    public static DeviceOp getNextDayDeviceOpWithDefault(Context context) {
        final String TAG = "[getNextDeviceOp] ";

        List<DeviceOp> deviceOpList = PrefUtil.getDeviceSchedule(context);
        if(deviceOpList == null || deviceOpList.size() < 1) {
            SLog.e(TAG + "failed to get deviceOpList");

            return DeviceOp.getNextDayDefaultOne();
        }

        int today = TimeUtil.getAppDateInt();

        if(today > -1) {
            while (true) {
                today++;
//                SLog.d("start nextDay: " + today);
                for (int i = 0; i < deviceOpList.size(); i++) {
                    int dow = ParseUtil.parseStringToInt(deviceOpList.get(i).getDOM(), -1);
                    if(dow > -1 && dow == today) {
                        return deviceOpList.get(i);
                    }
                }
                if(today > 31) break;
            }
        }

        // today 가 월말이어서 다음날이 1일 인 경우는 1부터 다시 시작
        today = 0;
        while (true) {
            today++;
//            SLog.d("start nextDay: " + today);
            for (int i = 0; i < deviceOpList.size(); i++) {
                int dow = ParseUtil.parseStringToInt(deviceOpList.get(i).getDOM(), -1);
                if(dow > -1 && dow == today) {
                    return deviceOpList.get(i);
                }
            }
            if(today > 31) break;
        }

        SLog.e("[getNextDayDeviceOp] can not find next day deviceOp");

        return DeviceOp.getNextDayDefaultOne();
    }

    /**
     * 오늘 날짜 해당 단말기 전원 스케쥴 반환
     * @param context
     * @return
     */
    public static DeviceOp getTodayDeviceOp(Context context) {
        final String TAG = "[getTodayDeviceOp] ";
        List<DeviceOp> deviceOpList = PrefUtil.getDeviceSchedule(context);
        if(deviceOpList == null || deviceOpList.size() < 1) {
            SLog.e(TAG + "failed to get deviceOpList");
            return DeviceOp.getDefaultOne();
        }

        String today = TimeUtil.getAppDateStr();

//        SLog.d(TAG, "today: " + today);
        for (int i = 0; i < deviceOpList.size(); i++) {
            String dom = deviceOpList.get(i).getDOM();
//            SLog.d(TAG, "dom: " + dom);
            if(today.equals(dom)) {
                return deviceOpList.get(i);
            }
        }
        SLog.e("[getTodayDeviceOp] can not find today deviceOp");

        return null;
    }

    public static DeviceOp getTodayDeviceOpWithDefault(Context context) {
        final String TAG = "[getTodayDeviceOp] ";
        List<DeviceOp> deviceOpList = PrefUtil.getDeviceSchedule(context);
        if(deviceOpList == null || deviceOpList.size() < 1) {
            SLog.e(TAG + "failed to get deviceOpList");
            return DeviceOp.getDefaultOne();
        }

        String today = TimeUtil.getAppDateStr();

//        SLog.d(TAG, "today: " + today);
        for (int i = 0; i < deviceOpList.size(); i++) {
            String dom = deviceOpList.get(i).getDOM();
//            SLog.d(TAG, "dom: " + dom);
            if(today.equals(dom)) {
                return deviceOpList.get(i);
            }
        }
        SLog.e("[getTodayDeviceOp] can not find today deviceOp");

        return DeviceOp.getDefaultOne();
    }

    /**
     * 오늘로부터 이후 가장 가까운 날의 단말기 전원 스케쥴 있는 날까지 기간 반환
     * @param currentDayDeviceOp
     * @param nextDayDeviceOp
     * @return
     */
    public static int getNextDayScheduleOffset(DeviceOp currentDayDeviceOp, DeviceOp nextDayDeviceOp) {
        final String TAG = "[getNextDeviceOp] ";
        int offset = 0;
        if(currentDayDeviceOp != null && nextDayDeviceOp != null) {
            int today = getDayOfWeekNumByStr(currentDayDeviceOp.getDOW());
            int nextDay = getDayOfWeekNumByStr(nextDayDeviceOp.getDOW());
            if(today == nextDay) return offset;
            while (true) {
                today = getnextDayOfWeekNumByNum(today);
                offset++;
                if(today == nextDay) return offset;
            }
        } else SLog.e(TAG + "DeviceOp data is null");

        return offset;
    }
}