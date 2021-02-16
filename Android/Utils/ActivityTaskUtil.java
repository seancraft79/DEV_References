package com.sysgen.eom.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class ActivityTaskUtil {


    // 반환형식 : com.sysgen.eom.MainActivity
    public static String getTopActivityClass(Context context) {
        ComponentName cn = getRunningActivityComponent(context);
        if(cn != null) {
            return cn.getClassName();
        }
        return "";
    }

    public static String getTopActivityClassOnlyName(Context context) {
        ComponentName cn = getRunningActivityComponent(context);
        if(cn != null) {
            return getOnlyClassName(cn.getClassName());
        }
        return "";
    }

    public static boolean isApplicationInForeground(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            final ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static ComponentName getRunningActivityComponent(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        return taskInfo.get(0).topActivity;
    }

    public static ActivityInfo tryGetActivity(Context context, ComponentName componentName) {
        try {
            return context.getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String getOnlyClassName(String className) {
        if(className != null && !className.isEmpty()) {
            String cn = className.replace(" ", "");
            String[] splitted = cn.split("\\.");
            if(splitted.length > 0) return splitted[splitted.length-1];
        }
        return "";
    }
}
