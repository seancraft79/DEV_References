package com.sysgen.eom.util;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.sysgen.eom.BuildConfig;
import com.sysgen.eom.listener.CallBack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    ** RockChip **
    PID   USER     TIME   COMMAND
    1     root     1:08   /init --second-stage

    ** AllWiner **
    PID   USER     COMMAND
    1     0        /init --second-stage
 */


public class ProcessUtil {
    private static final String TAG = "ProcessUtil";

    // PROCESS LINE STRING KEYS
    public static final String KEY_PID = "PID";
    public static final String KEY_USER = "USER";
    public static final String KEY_TIME = "TIME";
    public static final String KEY_COMMAND = "COMMAND";

    /**
     * Process lines를 parsing 해서 Map 리스트 데이터로 반환
     * exception: COMMAND 에서 제외하고 싶은 프로세스 명
     */
    public static List<Map<String, String>> getParsedLines(String ... exception) {

        List<String> lines = getProcessLines();
        if(lines == null || lines.size() < 1) {
            SLog.e("getParsedLines null");
            return null;
        }
//        SLog.d("getParsedLines lines : " + lines.size());
        List<String> schema = getParseTokenFromLine(lines.get(0));
//        SLog.d("getParsedLines schema => " + schema.toString());
        List<Map<String, String>> process = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
//            SLog.d("line: " + lines.get(i));
            Map<String, String> parseLine = parseProcesLine(schema, lines.get(i));

            boolean isException;
            if(exception != null && exception.length > 0) {

                isException = false;
                for (int j = 0; j < exception.length; j++) {
                    if(parseLine.get(KEY_COMMAND).contains(exception[j]))
                        isException = true;
                }
                if(!isException) process.add(parseLine);
            }

            else process.add(parseLine);
        }
        return process;
    }

    /**
     * Schema 대로 process line 을 parse 해서 반환
     * @param schema
     * @param line
     * @return
     */
    public static Map<String, String> parseProcesLine(List<String> schema, String line)
    {
        List<String> strList = getParseTokenFromLine(line);

        if(strList != null && strList.size() > 2) {
            Map<String, String> process = new HashMap<>();

            for (int i = 0; i < schema.size(); i++) {

                if(i < schema.size() - 1) {
                    process.put(schema.get(i).trim(), strList.get(i).trim());
                } else {
                    String str = "";
                    for (int j = i; j < strList.size(); j++) str += " " + strList.get(j);
                    process.put(schema.get(i).trim(), str.trim());
                }
            }

//            for (Map.Entry<String, Object> entry : process.entrySet()) {
//                System.out.println("process " + entry.getKey() + " -> " + entry.getValue());
//            }
            return process;
        } else System.out.println("parsed line size lacks ...");
        return null;
    }

    /**
     * Process line
     * @param line
     * @return
     */
    public static List<String> getParseTokenFromLine(String line)
    {
//        System.out.println("getParseTokenFromLine: " + line);

        String beforeStr = null;
        String str = "";
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            String s = line.substring(i, i+1);

            if(!isNullOrEmpty(s)) {
                str += s;
            }
            else {

                if(!isNullOrEmpty(beforeStr) && !isNullOrEmpty(str)) {
//                    System.out.println("add i: " + i + ", tokenIndex: " + tokenIndex + ", str: " + str);
                    strList.add(str.trim());
                    str = "";
                }
            }

            if(i >= line.length() - 1) {
//                System.out.println("last add i: " + i + ", str: " + str);
                strList.add(str.trim());
            }

            beforeStr = s;
        }
        return strList;
    }

    /**
     * Get process info lines
     * @return
     */
    public static List<String> getProcessLines() {
//        SLog.d("getProcessLines");
        List<String> lines = new ArrayList<>();
        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        try {

            String cmd = "busybox ps -e";
            process = runtime.exec(new String[]{"su", "-c", cmd});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                if(line != null && !line.isEmpty()) lines.add(line);
            }

        } catch (Exception e) {
            SLog.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                try {
                    process.destroy();
                } catch (Exception e){}
            }catch (Exception e) {}
        }
        return lines;
    }

    /**
     * Process line 에 Time(00:00) 이 존재하는지 여부
     * @param line
     * @return
     */
    public static boolean isTimeExist(String line) {
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("\\d:\\d{2}\\s*");
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * 이전 프로세스 리스트와 현재 프로세스 리스트를 비교하여 새로운 프로세스만 추출
     * @param before
     * @param current
     * @return
     */
    public static List<Map<String, Object>> processDiffMap(List<Map<String, Object>> before, List<Map<String, Object>> current) {

        if(before == null || before.size() < 1 || current == null || current.size() < 1) return null;

        List<Map<String, Object>> diffList = new ArrayList<>();
        for (Map<String, Object> nowMap : current) {
            boolean isInPid = false;
            for (Map<String, Object> beforeMap : before) {
                if(nowMap.get(KEY_PID).equals(beforeMap.get(KEY_PID))) isInPid = true;
            }
            if(!isInPid) {
                String pid = (String)nowMap.get(KEY_PID);
                diffList.add(nowMap);
            }
        }
        return diffList;
    }

    /**
     * 이전 프로세스 리스트와 현재 프로세스 리스트를 비교하여 새로운 프로세스만 추출
     * @param before
     * @param current
     * @return
     */
    public static HashSet<String> processDiffList(List<Map<String, String>> before, List<Map<String, String>> current) {
        HashSet<String> diffList = new HashSet<>();

        if(before == null || before.size() < 1 || current == null || current.size() < 1) return null;

        boolean isIn;

        for (Map<String,String> currentProcess : current) {
            isIn = false;
            for (Map<String,String> beforeProcess : before) {
                if(currentProcess.get(KEY_COMMAND).equals(beforeProcess.get(KEY_COMMAND))) isIn = true;
            }
            if(!isIn) diffList.add(currentProcess.get(KEY_PID));
        }

        return diffList;
    }

    public static void killProcessByPid(String pid) {

        Log.d(TAG, "KillProcessByPid ... start pid : " + pid);

        Process process = null;
        Runtime runtime = Runtime.getRuntime();

        try {
            String cmd = "kill -9 " + pid;
            process = runtime.exec(new String[]{"su", "-c", cmd});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG,"killProcessByPid result : " + line);
            }
            Log.d(TAG, "KillProcessByPid ... end");

        } catch (Exception e) {
            Log.e(TAG, "killProcessByPid error : " + e.getMessage());
        } finally {
            try {
                process.destroy();
            } catch (Exception e){}
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if(str == null || str.length() < 1)
            return true;

        str = removeSpace(str);
        if(str.isEmpty() || str.length() < 1)
            return true;

        return false;
    }

    public static String removeSpace(String s) {

        String withoutspaces = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                withoutspaces += s.charAt(i);

        }
        return withoutspaces;
    }

    public static int parseStringToInt(String s, int defaultValue) {
        try {
            return s.matches("-?\\d+") ? Integer.parseInt(s) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Is Shell process running?
     * @param callBack
     */
    public void checkShProcess(String processName, CallBack<String> callBack) {

        Log.d(TAG, "checkShProcess ... start ...");

        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        try {

//            String cmd = "busybox ps -e|grep \"<processName>\"|grep -v grep|wc -l";
            String cmd = "busybox ps -e|grep \"" + processName + "\"|grep -v grep|wc -l";
            process = runtime.exec(new String[]{"su", "-c", cmd});
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "checkShProcess line result : " + line);
                if (line.equals("0")) {
                    callBack.onResult("0");  // No
                } else callBack.onResult("1");  // Ok
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            callBack.onResult("error");
        } finally {
            try {
                process.destroy();
            } catch (Exception e){}
        }
    }

    private volatile boolean isAutoLaunching = false;
    private volatile boolean isAutoRunning = false;
    public synchronized void startAutoProcess() {
        if(!BuildConfig.AUTO_RUN) return;

        if(isAutoLaunching || isAutoRunning) return;

        Log.d(TAG, "startAutoProcess ... start checkShProcess ...");

        isAutoLaunching = true;

        checkShProcess("eom", result -> {

            Log.d(TAG, "startAutoProcess check result => " + result);

            if(result.equals("0")/* If no proces running */) {

                Log.d(TAG, "startAutoProcess ... start ...");

                Process process = null;
                Runtime runtime = Runtime.getRuntime();
                try {
                    final String command = "sh " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/EOM/eom_check.sh &";
                    Log.d(TAG, "startAutoProcess command :" + command);
                    process = runtime.exec(new String[]{"su", "-c", command});
                    process.getErrorStream().close();
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.waitFor();

                    Thread.sleep(1000);
                    isAutoLaunching = false;
                    isAutoRunning = true;

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        process.destroy();
                    } catch (Exception e){}
                }
            }
        });
    }
}
