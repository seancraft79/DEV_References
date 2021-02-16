package com.sysgen.eom.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.sysgen.eom.AppContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RootUtil {
    private static final String TAG = "ScreenUtil";

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public static void requestSleep(Context context) {
        SLog.d(TAG, "[ScreenUtil] requestSleep");
        if(isScreenOn(context)) {
            requestKeyPower();
        }
    }

    public static void requestWake(Context context) {
//        AppContext.getAppContext().requestWake();
        if(!isScreenOn(context)) {
            requestKeyPower();
        }
    }

    public static void requestKeyPower() {
        try {
            SLog.d(TAG, "[ScreenUtil] requestKeyPower .... start");
            Process p = Runtime.getRuntime().exec(new String[]{ "su", "-c", "input keyevent 26"});

            int result = p.waitFor();
            SLog.d(TAG, "[ScreenUtil] requestKeyPower .... end result: " + result);
        } catch (Exception ex) {
            SLog.e(TAG, "[ScreenUtil] requestKeyPower error : " + ex.getMessage());
        }
    }

    public static void requestShutdown() {
        try {
            SLog.d(TAG, "[ScreenUtil] requestShutdown .... start");
            Process p = Runtime.getRuntime().exec(new String[]{ "su", "-c", "am start -a android.intent.action.ACTION_REQUEST_SHUTDOWN" });

            int result = p.waitFor();
            SLog.d(TAG, "[ScreenUtil] requestShutdown .... end result : " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean requestReboot() {
        try {
            SLog.d(TAG, "[ScreenUtil] requestReboot .... reboot");
            Process p = Runtime.getRuntime().exec(new String[]{ "su", "-c", "reboot"});

            int result = p.waitFor();
            SLog.d(TAG, "[ScreenUtil] requestReboot .... end result : " + result);
            return true;
        } catch (Exception ex) {
            SLog.e(TAG, "[ScreenUtil] requestReboot error : " + ex.getMessage());
        }
        return false;
    }

    public static void allWinnerReboot(Context context) {
        context.sendBroadcast(new Intent("com.allwinner.reboot"));
    }

    private void wakeUpDevice(AppContext context) {
        PowerManager.WakeLock wakeLock = context.getWakeLock(); // get WakeLock reference via AppContext
        if (wakeLock.isHeld()) {
            wakeLock.release(); // release old wake lock
        }

        // create a new wake lock...
        wakeLock.acquire();

        // ... and release again
        wakeLock.release();
    }

    public static void closeSystemDialog(Activity activity) {
        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        activity.sendBroadcast(closeDialog);
    }

    public static void hideNavStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static int getScreenDimm(Context context) {
        try {
            int dim = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
            return dim;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isStatusBarVisible(Activity activity) {
        if (AppContext.getCurrentActivity() == null) return false;
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight != 0;
    }

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static String getTopActivityProc() {
        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "dumpsys activity | grep top-activity"});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result += " " + line;
            }
            is.close();
        } catch (Exception e) {
            Log.e(TAG, "[getTopActivityProc] error : " + e.getMessage());
        } finally {
            if (process != null) process.destroy();
        }
        return result.trim();
    }

    public static boolean execCommand(String command) {
        SLog.d(TAG, "[execCommand] " + command);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            process.waitFor();
            SLog.d(TAG, "[execCommand] waitFor");
            return true;
        } catch (Exception e) {
            SLog.e(TAG, "[execCommand] error : " + e.getMessage());
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }
}
