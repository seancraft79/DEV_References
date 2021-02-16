package com.sysgen.eom.util;

import android.os.Debug;
import android.util.Log;

import com.sysgen.eom.AppContext;
import com.sysgen.eom.BuildConfig;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SLog {
    private static final String TAG = "EOM";

    private static boolean ISDEBUG = BuildConfig.DEBUG;
    public static final String RO_AUTO_SHUNDOWN = "ro.sp.auto.shutdown";
    public static final boolean LOG_POOL = BuildConfig.LOG_POOL; /** LOG POOL **/
    public static List<String> logPool = new ArrayList<>();

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if(ISDEBUG) {
            if(!BuildConfig.FILE_LOG && BuildConfig.LOG) Log.d(tag, msg);
            if(BuildConfig.FILE_LOG) fileLogDebug(tag, msg);
            if(BuildConfig.LOG_POOL) pushToLogPool(tag, msg);
        }
    }

    public static void e(String msg){
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if(ISDEBUG) {
            if(!BuildConfig.FILE_LOG && BuildConfig.LOG) Log.e(tag, msg);
            if(BuildConfig.FILE_LOG) fileLogError(tag, msg);
            if(BuildConfig.LOG_POOL) pushToLogPool(tag, msg);
        }
    }

    public static void pushToLogPool(String tag, String msg) {
        if(LOG_POOL) {
            logPool.add("[" + TimeUtil.getNowString() + "] [" + tag + "] " + msg);
            if(logPool.size() > 1000) logPool.remove(0);
        }
    }

    public static void fileLogDebug(String tag, String msg) {
        Logger logger = AppContext.getLogger();
        if(logger != null) logger.debug("[" + tag + "] " + msg);
    }

    public static void fileLogError(String tag, String msg) {
        Logger logger = AppContext.getLogger();
        if(logger != null) logger.error("[" + tag + "] " + msg);
    }

    /**
     * File log config 전의 로그들을 넣어줌
     */
    public static void insertExistingToScreenLogs() {
        if(logPool != null && logPool.size() > 0) {
            for (int i = 0; i < logPool.size(); i++) {
                fileLogDebug(TAG, logPool.get(i));
            }
        }
    }
}
