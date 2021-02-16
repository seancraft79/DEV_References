package com.sysgen.eom.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;


import com.sysgen.eom.beans.Alarm;
import com.sysgen.eom.beans.Alarms;
import com.sysgen.eom.beans.DaysOfWeek;
import com.sysgen.eom.broadcast.SchedulePowerOffReceiver;
import com.sysgen.eom.broadcast.SchedulePowerOnReceiver;
import com.sysgen.eom.config.Configs;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmUtil {
    private static final String M12 = "h:mm aa";
    private static final String M24 = "kk:mm";
    private static final String TAG = "Alarms";

    public static void setAlertPower(Context context, Alarms alarms) {
        if (alarms.getEnable()) {
            enableAlertPowerOn(context, alarms.openAlarm);
            enableAlertPowerOff(context, alarms.closeAlarm);
            return;
        }
        disableAlertPowerOn(context, alarms.openAlarm);
        disableAlertPowerOff(context, alarms.closeAlarm);
    }

    public static void disableAlertPower(Context context, Alarms alarms) {
        disableAlertPowerOn(context, alarms.openAlarm);
        disableAlertPowerOff(context, alarms.closeAlarm);
    }

    public static AlarmManager getAlarmManager(Context context) {
//        return (AlarmManager) context.getSystemService(SchedulePowerDatabaseHelper.TABLE_NAME);
        return (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    @SuppressLint({"Recycle"})
    public static void enableAlertPowerOn(Context context, Alarm alarm) {
        SLog.d(TAG, "###enableAlertPowerOn....start");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOnReceiver.class), 0);
        getAlarmManager(context)
                .setExact(AlarmManager.RTC_WAKEUP, calculateAlarm(alarm, true), alarmIntent);
        SLog.d(TAG, "###enableAlertPowerOn....end");
    }

    public static void disableAlertPowerOn(Context context, Alarm alarm) {
        SLog.d(TAG, "###disableAlertPowerOn....");
        getAlarmManager(context).cancel(PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOnReceiver.class), 0));
    }

    @SuppressLint({"Recycle"})
    public static void enableAlertPowerOff(Context context, Alarm alarm) {
        SLog.d(TAG, "###enableAlertPowerOff....start");
        AlarmManager am = getAlarmManager(context);
        long time = calculateAlarm(alarm, false);
        Intent intent = new Intent(context, SchedulePowerOffReceiver.class);
        intent.putExtra("ALARMTIME", (alarm.hour * 60) + alarm.minute);
//        am.setExact(4, time, PendingIntent.getBroadcast(context, alarm.f9id, intent, 268435456));
        am.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, alarm.f9id, intent, 0));
        SLog.d(TAG, "###enableAlertPowerOff....end");
    }

    public static void disableAlertPowerOff(Context context, Alarm alarm) {
        SLog.d(TAG, "###disableAlertPowerOff....start");
        getAlarmManager(context).cancel(PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOffReceiver.class), 0));
        SLog.d(TAG, "###disableAlertPowerOff....end");
    }

    public static void disablePowerOnAlarm(Context context, int id) {
        if(id < 0) return;
        SLog.d(TAG, "###disablePowerOnAlarm....start id : " + id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, id, new Intent(context, SchedulePowerOnReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disablePowerOnAlarm....end");
    }

    public static void disablePowerOffAlarm(Context context, int id) {
        if(id < 0) return;
        SLog.d(TAG, "###disablePowerOffAlarm....start id : " + id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, id, new Intent(context, SchedulePowerOffReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disablePowerOffAlarm....end");
    }

    public static void setOneShotPowerOn(Context context, int id) {
        SLog.d(TAG, "###setOneShotPowerOn....start");
        PendingIntent poweronSender = PendingIntent.getBroadcast(context, id, new Intent(context, SchedulePowerOnReceiver.class), 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 2);
        getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), poweronSender);
        SLog.d(TAG, "###setOneShotPowerOn.....end");
    }

    public static void setOneShotPowerOn(Context context, Alarm alarm) {
        SLog.d(TAG, "###setOneShotPowerOn....start : " + alarm.toString());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month - 1);
        c.set(Calendar.DATE, alarm.day);
        c.set(Calendar.HOUR_OF_DAY, alarm.hour);
        c.set(Calendar.MINUTE, alarm.minute);
        c.set(Calendar.SECOND, 0);
        long curTime = System.currentTimeMillis();
        long triggerAtMillis = c.getTimeInMillis();
        if (curTime >= triggerAtMillis) {
            SLog.e(TAG, "setOneShotPowerOn.....error");
            return;
        }
        SLog.d(TAG, "setOneShotPowerOn triggerAtMillis : " + triggerAtMillis);
        getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOnReceiver.class), 0));
        SLog.d(TAG, "###setOneShotPowerOn....end");
    }

    public static void setOneShotPowerOff(Context context, int id) {
        SLog.d(TAG, "###setOneShotPowerOff.....start");
        Intent intent = new Intent(context, SchedulePowerOffReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60);
        Calendar c = Calendar.getInstance();
        intent.putExtra("ALARMTIME", (c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE) + 1);
        getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getBroadcast(context, id, intent, 0));
        SLog.d(TAG, "###setOneShotPowerOff.....end");
    }

    public static void setOneShotPowerOff(Context context, Alarm alarm) {
        SLog.d(TAG, "###setOneShotPowerOff.....start : " + alarm.toString());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month - 1);
        c.set(Calendar.DATE, alarm.day);
        c.set(Calendar.HOUR_OF_DAY, alarm.hour);
        c.set(Calendar.MINUTE, alarm.minute);
        c.set(Calendar.SECOND, 0);
        long curTime = System.currentTimeMillis();
        long triggerAtMillis = c.getTimeInMillis();
        if (curTime >= triggerAtMillis) {
            SLog.e(TAG, "###setOneShotPowerOff.....error");
            return;
        }
        Intent intent = new Intent(context, SchedulePowerOffReceiver.class);
        intent.putExtra("ALARMTIME", (alarm.hour * 60) + alarm.minute);
        getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, PendingIntent.getBroadcast(context, alarm.f9id, intent, 0));
        SLog.d(TAG, "###setOneShotPowerOff.....end");
    }

    public static void disableOneShotPowerOn(Context context, int id) {
        SLog.d(TAG, "###disableOneShotPowerOn....start id: " + id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, id, new Intent(context, SchedulePowerOnReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disableOneShotPowerOn....end");
    }

    public static void disableOneShotPowerOn(Context context, Alarm alarm) {
        SLog.d(TAG, "###disableOneShotPowerOn....start id: " + alarm.f9id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOnReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disableOneShotPowerOn....end");
    }

    public static void disableOneShotPowerOn(Context context) {
        disableOneShotPowerOn(context, Configs.ID_ONE_SHOTPOWER_ON);
    }

    public static void disableOneShotPowerOff(Context context, int id) {
        SLog.d(TAG, "###disableOneShotPowerOff....start id: " + id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, id, new Intent(context, SchedulePowerOffReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disableOneShotPowerOff....end");
    }

    public static void disableOneShotPowerOff(Context context, Alarm alarm) {
        SLog.d(TAG, "###disableOneShotPowerOff....start id: " + alarm.f9id);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, alarm.f9id, new Intent(context, SchedulePowerOffReceiver.class), 0);
        getAlarmManager(context).cancel(broadcast);
        broadcast.cancel();
        SLog.d(TAG, "###disableOneShotPowerOff....end");
    }

    public static void disableOneShotPowerOff(Context context) {
        disableOneShotPowerOff(context, Configs.ID_ONE_SHOTPOWER_OFF);
    }

    public static void disableOneShotAlarms(Context context) {
        disableOneShotPowerOn(context);
        disableOneShotPowerOff(context);
    }

    private static long calculateAlarm(Alarm alarm, boolean bOpen) {
        return calculateAlarm(alarm.hour, alarm.minute, alarm.daysOfWeek).getTimeInMillis();
    }

    public static void setExAlertPower(Context context, Alarms alarms) {
        if (alarms.getEnable()) {
            setOneShotPowerOn(context, alarms.openAlarm);
            setOneShotPowerOff(context, alarms.closeAlarm);
            return;
        }
        disableAlertPowerOn(context, alarms.openAlarm);
        disableAlertPowerOff(context, alarms.closeAlarm);
    }

    public static void disableExAlertPower(Context context, Alarms alarms) {
        disableOneShotPowerOn(context, alarms.openAlarm);
        disableOneShotPowerOff(context, alarms.closeAlarm);
    }

    static Calendar calculateAlarm(int hour, int minute, DaysOfWeek daysOfWeek) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        if (hour < nowHour || (hour == nowHour && minute <= nowMinute)) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int addDays = daysOfWeek.getNextAlarm(c);
        if (addDays > 0) {
            c.add(Calendar.DAY_OF_WEEK, addDays);
        }
        return c;
    }

    public static String formatTime(Context context, int hour, int minute, DaysOfWeek daysOfWeek) {
        return formatTime(context, calculateAlarm(hour, minute, daysOfWeek));
    }

    static String formatTime(Context context, Calendar c) {
        return c == null ? "" : (String) DateFormat.format(get24HourMode(context) ? M24 : M12, c);
    }

    public static boolean get24HourMode(Context context) {
        return DateFormat.is24HourFormat(context);
    }

    /**
     * Has power on alarm set already?
     * @param context
     * @return
     */
    public static boolean hasPowerOnAlarmSet(Context context) {

        return (PendingIntent.getBroadcast(context, Configs.ID_ONE_SHOTPOWER_ON,
                new Intent(context, SchedulePowerOnReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }

    /**
     * Has power off alarm set already?
     * @param context
     * @return
     */
    public static boolean hasPowerOffAlarmSet(Context context) {
        return (PendingIntent.getBroadcast(context, Configs.ID_ONE_SHOTPOWER_OFF,
                new Intent(context, SchedulePowerOffReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}