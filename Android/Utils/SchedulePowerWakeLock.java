package com.sysgen.eom.util;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class SchedulePowerWakeLock {
    private static final String TAG = "SchedulePowerWakeLock";
    private static WakeLock mWakeLock;

    public static void acquireCpuWakeLock(Context context) {
        SLog.d(TAG, "SchedulePowerWakeLock Acquiring cpu wake lock");
        if (mWakeLock == null) {
//            mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(805306394, TAG);
            mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
                    .newWakeLock(
                            PowerManager.PARTIAL_WAKE_LOCK
//                                    | PowerManager.ON_AFTER_RELEASE
                            , "SchedulePower::MyWakelockTag");
            mWakeLock.acquire();
        }
    }

    public static void releaseCpuLock() {
        SLog.d(TAG, "SchedulePowerWakeLock Release");
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}