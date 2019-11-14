package com.metarobotics.playvandi.WIFI_NETWORK;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.metarobotics.playvandi.Models.HotSpotConnectedDeviceInfo;
import com.metarobotics.playvandi.Utils.Logger;
import com.metarobotics.playvandi.Utils.SharedPrefHelper;
import com.metarobotics.playvandi.Utils.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class MRNetConnection {
    public static final String TAG = "MRNetConnection";

    public static final int CHECK_INTERVAL = 5000;
    public static final int REACHABLE_TIMEOUT = 200;

    public static final int MODE_AP = 0;
    public static final int MODE_STATION = 1;

    public enum eNetStatus {
        NONE,
        DISABLED,
        ENABLED,
        DISCONNECTED,
        CONNECTED,
        ENTANGLED,  // e.g. 핫스팟에 두개이상의 드론이 연결되었을 경우
        FAIL
    }

    public enum eConnectionType {
        NONE,
        HOTSPOT,
        WIFI
    }

    protected WeakReference<Activity> activity;
    protected Thread thread;
    protected WifiManager wifiManager;
    protected boolean keepSeeking = true;
    protected NetworkConnectionCB connectionCb;
    protected eNetStatus currentStatus = eNetStatus.NONE;

    public eConnectionType getConnectionType() {
        return connectionType;
    }

    protected eConnectionType connectionType = eConnectionType.NONE;

    public MRNetConnection(Activity activity) {
        this.activity = new WeakReference<>(activity);
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    Activity getActivity() {
        Activity a = activity.get();
        if(a == null) return null;
        if(a.isFinishing()) return null;
        return a;
    }

    // Static methods
    public static boolean isNetworkConnected(Context context) {

        NetworkInfo activeNetwork = getNetworkInfo(context);
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static boolean isWifiConnected(Context context) {

        NetworkInfo activeNetwork = getNetworkInfo(context);

        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static boolean isHotSpotEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }
    // Static methods end

    public void registerConnectionCallBack(NetworkConnectionCB cb) {
        this.connectionCb = cb;
    }

    public void unregisterConnectionCallBack() {
        this.connectionCb = null;
    }

    public void startTrackingStatus() {
        keepSeeking = true;
    }

    public void stopTrackingStatus() {
        keepSeeking = false;
    }

    protected void flushStatusToCB(eNetStatus status) {

        if (status != currentStatus && connectionCb != null) {
            connectionCb.onNetResult(status);
        }
        currentStatus = status;
    }

    public void getHostName() {
        Logger.d("=====> getHostName");
        try {
            InetAddress address = InetAddress.getByName("192.168.43.70");
            boolean reachable = address.isReachable(5000);

            Logger.d("isHostReachable : " + reachable);
            if (reachable) {
                String name = address.getHostName();
                String cName = address.getCanonicalHostName();
                String hostAddress = address.getHostAddress();
                Logger.d("getHostName : " + name + ", cName : " + cName + ", hostAdd : " + hostAddress);
            } else Logger.e("getHostName not reachable");

        } catch (Exception e) {
            Logger.e("getHostName error => " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get HotSpot Configuration
     *
     * @return
     */
    protected WifiConfiguration getAPConfig() {
        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        try {
            for (Method m : methods) {
                if (m.getName().equals("getWifiApConfiguration")) {
                    WifiConfiguration config = (WifiConfiguration) m.invoke(wifiManager);
//                    String ssid = config.SSID;
//                    String bssid = config.BSSID;
//                    Logger.d("getAPConfig SSID : " + ssid + ", BSSID : " + bssid);
                    return config;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSSIDName() {
        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        try {
            for (Method m : methods) {
                if (m.getName().equals("getWifiApConfiguration")) {
                    WifiConfiguration config = (WifiConfiguration) m.invoke(wifiManager);
                    String ssid = config.SSID;
                    return ssid;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
        }
        return false;
    }

    // toggle wifi hotspot on or off
    public boolean configApState() {
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            Activity activity = getActivity();
            if(activity == null) return false;
            if (isApOn(activity)) {
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wificonfiguration, !isApOn(activity));
            return true;
        } catch (Exception e) {
            Logger.e("configApState exception : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // AP 모드의 MavEsp SSID, PWD 변경
    public void changeApNetParam(final String ssid, final String pwd, final int mode, final MRNetConnection.HotSpotConfigChangeCB cb){
        Activity activity = getActivity();
        if(activity != null) {
            String ip = SharedPrefHelper.getAPIP_ST(activity, null);
            if(!StringUtil.isNullOrEmpty(ip)) {
                Logger.d("changeApNetParam ip : " + ip);
                changeApNetParam(ip, ssid, pwd, mode, cb);
            }
            else Logger.e("changeApNetParam ip is NULL");
        }else Logger.e("changeApNetParam context is NULL");
    }
    public void changeApNetParam(final String ip, final String ssid, final String pwd, final int mode, final MRNetConnection.HotSpotConfigChangeCB cb) {

        if (StringUtil.isNullOrEmpty(ip) || StringUtil.isNullOrEmpty(ssid) || StringUtil.isNullOrEmpty(pwd)) {
            Logger.e("changeApNetParam String is NULL");
            return;
        }

        class ParamAsync extends AsyncTask<Void, String, String> {

            @Override
            protected String doInBackground(Void... voids) {

                StringBuilder urlBuilder = null;

                if (mode == MODE_AP) {
                    urlBuilder = new StringBuilder(
                            "http://" + ip + "/setparameters?mode=" + mode + "&ssid=" + ssid + "&pwd=" + pwd + "&reboot=1");
                } else {
                    urlBuilder = new StringBuilder(
                            "http://" + ip + "/setparameters?mode=" + mode + "&ssidsta=" + ssid + "&pwdsta=" + pwd + "&reboot=1");
                }

                try {
                    final String urlString = urlBuilder.toString();
                    Logger.d("changeApNetParam => " + urlString);
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    StringBuilder sb = new StringBuilder();

                    BufferedReader rd;
                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    } else {
                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                    conn.disconnect();

                    String result = sb.toString();

                    // 변경한 값 로컬 저장소에 저장
                    Activity activity = getActivity();
                    if(activity != null) {
                        SharedPrefHelper.setAPSSID_ST(activity.getApplicationContext(), ssid);
                        SharedPrefHelper.setAPPWD_ST(activity.getApplicationContext(), pwd);
                    }

                    Thread.sleep(1000);

                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Logger.d("changeApNetParam hotSpotConnectedList => " + result);
                if(cb != null) {
                    if (!StringUtil.isNullOrEmpty(result)) {
                        cb.onResult(true);
                    } else cb.onResult(false);
                }
            }
        }

        new ParamAsync().execute();
    }

    /**
     * Gets a list of the clients connected to the Hotspot
     *
     * @param onlyReachables   {@code false} if the list should contain unreachable (probably disconnected) clients, {@code true} otherwise
     * @param reachableTimeout Reachable Timout in miliseconds
     * @return ArrayList of {@link HotSpotConnectedDeviceInfo}
     */
    public ArrayList<HotSpotConnectedDeviceInfo> getHotSpotConnectedDeviceList(boolean onlyReachables, int reachableTimeout) {
        BufferedReader br = null;
        ArrayList<HotSpotConnectedDeviceInfo> result = null;

        try {
            result = new ArrayList<HotSpotConnectedDeviceInfo>();
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {

                String[] splitted = line.split(" +");

                if ((splitted != null) && (splitted.length >= 4)) {
                    // Basic sanity check
                    String mac = splitted[3];

                    if (mac.matches("..:..:..:..:..:..")) {
                        boolean isReachable = onlyReachables;
                        if (onlyReachables) {
                            isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);
                        }
                        if (isReachable) {
                            result.add(new HotSpotConnectedDeviceInfo(splitted[0], splitted[1], splitted[2], splitted[3], splitted[4], splitted[5], false));
                        }
                    }
                }
            }

            return result;
        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }

        return null;
    }

    public interface NetworkConnectionCB {
        void onNetResult(eNetStatus status);
    }

    public interface HotSpotConfigChangeCB {
        void onResult(boolean result);
    }
}
