package com.metarobotics.playvandi.WIFI_NETWORK;

import android.app.Activity;
import android.content.Context;
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
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class MRNetConnectionHotSpot extends MRNetConnection {

    private static final String TAG = "MRNetConnectionHotSpot";

    protected HotSpotConnectedDeviceInfo connectedDroneHotSpot = null;
    ArrayList<HotSpotConnectedDeviceInfo> hotSpotConnectedList;

    public MRNetConnectionHotSpot(Activity activity) {
        super(activity);
        connectionType = eConnectionType.HOTSPOT;
    }

    @Override
    public void registerConnectionCallBack(NetworkConnectionCB cb) {
        super.registerConnectionCallBack(cb);
    }

    @Override
    public void unregisterConnectionCallBack() {
        super.unregisterConnectionCallBack();
        if (thread != null) thread.interrupt();
        thread = null;
    }

    @Override
    public void startTrackingStatus() {
        super.startTrackingStatus();

        keepSeeking = true;

        thread = new Thread(hotspotTracking);
        thread.setDaemon(true);
        thread.start();

        Logger.d(TAG, "StartTracking");
    }

    @Override
    public void stopTrackingStatus() {
        super.stopTrackingStatus();

        keepSeeking = false;

        if (thread != null) thread.interrupt();
        thread = null;
    }

    Runnable hotspotTracking = new Runnable() {
        @Override
        public void run() {

            while (keepSeeking) {
                try {

                    checkStatus();

                    Thread.sleep(CHECK_INTERVAL);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.e(TAG, "Thread interrupted");
                }
            }
        }
    };

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
    public WifiConfiguration getAPConfig() {
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
    public boolean isApOn() {
        try {
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
            if (isApOn()) {
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wificonfiguration, !isApOn());
            return true;
        } catch (Exception e) {
            Logger.e("configApState exception : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public HotSpotConnectedDeviceInfo getHotSpotConnectedDeviceByMacAddr(final String macAddr, boolean onlyReachables, int reachableTimeout) {

        ArrayList<HotSpotConnectedDeviceInfo> result = getHotSpotConnectedDeviceList(onlyReachables, reachableTimeout);

        if (result != null) {

            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getMacAddress().equals(macAddr)) {
                    return result.get(i);
                }
            }
        }

        return null;
    }

    void checkStatus() {
        hotSpotConnectedList = getHotSpotConnectedDeviceList(true, REACHABLE_TIMEOUT);

        if (hotSpotConnectedList == null || hotSpotConnectedList.size() < 1) {
            setNetStatus(false);
            return;
        }

        Logger.d(TAG, "===== CHECK STATUS =====");
        for (int i = 0; i < hotSpotConnectedList.size(); i++) {
            Logger.d("i : " + i + " => " + hotSpotConnectedList.get(i).toString());
//            getParameters(i, hotSpotConnectedList.get(i).ipAddress);
        }

        getParameters(0);

        // TODO : DEBUG
//        String ssid = "";
//        String bSsid = "";
//        WifiConfiguration apConfig = getAPConfig();
//        if (apConfig != null) {
//            ssid = apConfig.SSID;
//            bSsid = apConfig.BSSID;
//        }
//        boolean isApOn = isApOn();
//        Logger.d("isApOn [" + isApOn + "], AP_SSID [" + ssid + "], BSSID [" + bSsid + "]");
    }

    public void getParameters(final int index) {

        final String ip = hotSpotConnectedList.get(index).getIpAddress();

        class ParamAsync extends AsyncTask<Void, String, String> {

            @Override
            protected String doInBackground(Void... voids) {

                final StringBuilder urlBuilder = new StringBuilder("http://" + ip + "/getparameters");
                try {
                    String urlString = urlBuilder.toString();
                    Logger.d("getParam => " + urlString);
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

                    return result;

                } catch (Exception e) {
//                    Logger.e("getParameters error : " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (StringUtil.isNullOrEmpty(result)) {
                    Logger.d("getParam " + ip + " => " + index + " getParameters => NULL");
                    hotSpotConnectedList.get(index).setIsMav(false);
                } else {
                    Logger.d("getParam " + ip + " => " + index + " getParameters => " + hotSpotConnectedList.get(index).getMacAddress());
                    hotSpotConnectedList.get(index).setIsMav(true);
                }

                if (index == hotSpotConnectedList.size() - 1) {

                    connectedDroneHotSpot = getMavespConnectedDevice();

                    if (connectedDroneHotSpot != null && !isExceedConnectLimit()) {
                        setNetStatus(true);

                    } else if (isExceedConnectLimit()){
                        flushStatusToCB(eNetStatus.ENTANGLED);
                        stopTrackingStatus();

                    }
                    else {
                        flushStatusToCB(eNetStatus.DISCONNECTED);
                        stopTrackingStatus();
                    }
                }

                int next = index + 1;
                if (next < hotSpotConnectedList.size()) getParameters(next);
            }
        }

        new ParamAsync().execute();
    }

    void setNetStatus(boolean isMavConnected) {

        if (connectedDroneHotSpot != null && isMavConnected) {

            Activity activity = getActivity();
            if(activity != null){
                SharedPrefHelper.setMacAddr(activity.getApplicationContext(), connectedDroneHotSpot.getMacAddress());
                SharedPrefHelper.setAPIP_ST(activity.getApplicationContext(), connectedDroneHotSpot.getIpAddress());
                SharedPrefHelper.setAPSSID_ST(activity, getSSIDName());
            }

            // Drone is coneected to HotSpot
            Logger.d("Drone connected hotspot ip [" + connectedDroneHotSpot.getIpAddress() + "], MACADDR [" + connectedDroneHotSpot.getMacAddress() + "]");

            flushStatusToCB(eNetStatus.CONNECTED);

            stopTrackingStatus();

        } else {
            Logger.e("Drone NOT connected hotspot");
            flushStatusToCB(eNetStatus.DISCONNECTED);
        }
        Logger.d("-");
    }

    HotSpotConnectedDeviceInfo getMavespConnectedDevice() {
        if (hotSpotConnectedList == null || hotSpotConnectedList.size() < 1) return null;
        int accum = 0;
        int index = 0;
        for (int i = 0; i < hotSpotConnectedList.size(); i++) {
            if (hotSpotConnectedList.get(i).isMav()) {
                accum++;
                index = i;
            }
        }

        if (accum != 1) return null;

        return hotSpotConnectedList.get(index);
    }

    boolean isExceedConnectLimit() {
        if (hotSpotConnectedList == null || hotSpotConnectedList.size() < 1) return false;
        int accum = 0;
        for (int i = 0; i < hotSpotConnectedList.size(); i++) {
            if (hotSpotConnectedList.get(i).isMav()) {
                accum++;
            }
        }

        if (accum > 1) return true;
        return false;
    }

    public void createHotSpot() {
        Activity activity = getActivity();
        if(activity == null) return;

        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Logger.d(TAG, "createHotSpot isWifi : " + wifiManager.isWifiEnabled());
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.System.canWrite(context.getApplicationContext())) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()));
//                context.startActivityForResult(intent, 200);
//
//            }
//        }

        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = "phone1234";
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        try {
//            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
//            boolean apstatus=(Boolean) setWifiApMethod.invoke(wifiManager, netConfig, true);
//
//            Logger.d(TAG, "apstatus : " + apstatus);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            boolean isWifi = (Boolean) isWifiApEnabledmethod.invoke(wifiManager);
//            while(!(Boolean)isWifi){};

            Logger.d(TAG, "isWifi : " + isWifi);

            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            int apstate = (Integer) getWifiApStateMethod.invoke(wifiManager);

            Logger.d(TAG, "apstate : " + apstate);

            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig = (WifiConfiguration) getWifiApConfigurationMethod.invoke(wifiManager);
            Logger.d(TAG, "===> Client\nSSID:" + netConfig.SSID + "\nPassword:" + netConfig.preSharedKey + "\n");

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG, "crateHotSpot exception : " + e.getMessage());
        }
    }
}
