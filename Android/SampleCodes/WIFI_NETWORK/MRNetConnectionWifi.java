package com.metarobotics.playvandi.WIFI_NETWORK;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.metarobotics.playvandi.Models.HotSpotConnectedDeviceInfo;
import com.metarobotics.playvandi.Utils.Logger;
import com.metarobotics.playvandi.Utils.SharedPrefHelper;
import com.metarobotics.playvandi.Utils.StringUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MRNetConnectionWifi extends MRNetConnection {

    List<ScanResult> scanResult = new ArrayList<>();

    final String SECURETYPE_WEP = "WEP";
    final String SECURETYPE_WPA = "WPA";

    public MRNetConnectionWifi(Activity activity) {
        super(activity);
        connectionType = eConnectionType.WIFI;
    }

    @Override
    public void registerConnectionCallBack(NetworkConnectionCB cb) {
        super.registerConnectionCallBack(cb);
    }

    @Override
    public void unregisterConnectionCallBack() {
        super.unregisterConnectionCallBack();
    }

    @Override
    public void startTrackingStatus() {
        super.startTrackingStatus();

        keepSeeking = true;

        thread = new Thread(wifiTracking);
        thread.setDaemon(true);
        thread.start();

        // Scan network
//        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        Activity context = getActivity();
//        if(context != null)
//            context.registerReceiver(mWifiScanReceiver, filter);
//        wifiManager.startScan();

        Logger.d(TAG, "StartTracking");
    }

    Runnable wifiTracking = new Runnable() {
        @Override
        public void run() {
            while (keepSeeking) {
                try {

                    Activity a = activity.get();
                    if(a != null) {
                        boolean isConnected = isWifiConnected(a);
                        Logger.d("wifiTracking isConnected : " + isConnected);
                        if(isConnected) flushStatusToCB(eNetStatus.CONNECTED);
                        else flushStatusToCB(eNetStatus.DISCONNECTED);
                    }

                    Thread.sleep(CHECK_INTERVAL);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.e(TAG, "Thread interrupted");
                }
            }
        }
    };

    @Override
    public void stopTrackingStatus() {
        super.stopTrackingStatus();
        keepSeeking = false;
        Activity activity = getActivity();
        if(activity != null)
            activity.unregisterReceiver(mWifiScanReceiver);
    }

    private BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getWifiScanResult();
                if(keepSeeking) wifiManager.startScan();
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                Activity activity = getActivity();
                if(activity != null)
                    activity.sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }
    };

    public void getWifiScanResult() {

        List<ScanResult> sr = wifiManager.getScanResults();

        if (sr == null || sr.size() < 1) {
            Logger.e(TAG, "getWIFIScanResult: empty");
            return;
        }

        Activity activity = getActivity();
        if(activity == null) {
            Logger.e("getWifiScanResult context is NULL");
            return;
        }
        String ssid = SharedPrefHelper.getSSID_AP(activity, null);
        String pwd = SharedPrefHelper.getPWD_AP(activity, null);
        if(StringUtil.isNullOrEmpty(ssid) || StringUtil.isNullOrEmpty(pwd)) {
            Logger.e(TAG, "getWifiscanResult SSID, PWD is NULL");
            return;
        }

        this.scanResult.clear();
        this.scanResult.addAll(sr);

        String resultMsg = ">>> Get WIFI Scan Result >>>\n";

        for (int i = 0; i < scanResult.size(); i++) {
            ScanResult result = scanResult.get(i);
            resultMsg += (i + 1) + ". SSID : " + result.SSID.toString() + ", BSSID : " + result.BSSID
                    + "\t\t RSSI : " + result.level + " dBm\n";
        }
        Logger.d(TAG, resultMsg);

        tryConnect(ssid, pwd);
    }

    public void tryConnect(String networkSSID, String networkPass) {
        if(isConnected(networkSSID)) {
            Logger.d("tryConnect already connected : " + networkSSID);
            String ip = intToInetAddress();
            if(!StringUtil.isNullOrEmpty(ip)) {
                ip = ip.replaceAll("/", "");
                Logger.d("save hotspot ip : " + ip);
                if(getActivity() != null) SharedPrefHelper.setAPIP_ST(getActivity(), ip);
            }
            connectionCb.onNetResult(eNetStatus.CONNECTED);
            stopTrackingStatus();
            return;
        }

        for (int i = 0; i < scanResult.size(); i++) {
            ScanResult result = scanResult.get(i);
            if (result.SSID.equals(networkSSID)) {
                connect(networkSSID, networkPass);
                return;
            }
        }

        Logger.e(TAG, "tryConnect: " + networkSSID + " is not exist in the network list");
        connectionCb.onNetResult(eNetStatus.FAIL);
        stopTrackingStatus();
        return;
    }

    public String intToInetAddress() {
        int hostAddress = wifiManager.getDhcpInfo().serverAddress;
        Logger.d("intToInetAddress : " + hostAddress);
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes).toString();
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    boolean isConnected(String ssid) {
        String cssid = getConnectedSSID();
        if (!StringUtil.isNullOrEmpty(cssid)) {

            if (cssid.equals(ssid)) {
                return true;
            }
        }
        return false;
    }

    void connect(String networkSSID, String networkPass) {

        Logger.d(TAG, "=== START CONNECT SSID : " + networkSSID + ", pass : " + networkPass);

        String cssid = getConnectedSSID();
        if (!StringUtil.isNullOrEmpty(cssid)) {

            Activity activity = getActivity();
            if(activity == null) {
                Logger.e("connect Wifi - Activity is NULL");
                return;
            }

            String ssid_ap = SharedPrefHelper.getSSID_AP(activity);
            if (cssid.equals(ssid_ap)) {
                Logger.d(TAG, ssid_ap + " is already connected");
                if (this.connectionCb != null) connectionCb.onNetResult(eNetStatus.CONNECTED);
                activity.unregisterReceiver(mWifiScanReceiver);
                return;
            }

        } else {
            Logger.e(TAG, "=== CONNECTED SSID Empty");
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);

        conf.status = WifiConfiguration.Status.ENABLED;
        conf.priority = 40;

        String secureType = getSecureType(networkSSID);

        if (secureType.equals(SECURETYPE_WEP)) {
            Logger.d(TAG, "Configuring WEP");

            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

            if (networkPass.matches("^[0-9a-fA-F]+$")) {
                conf.wepKeys[0] = networkPass;
            } else {
                conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
            }

            conf.wepTxKeyIndex = 0;

        } else if (secureType.equals(SECURETYPE_WPA)) {
            Logger.d(TAG, "Configuring WPA");

            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            conf.preSharedKey = "\"" + networkPass + "\"";

        } else {
            Logger.d(TAG, "Configuring OPEN network");

            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.clear();
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        }

        // add this network if device does not have it ?
        int networkId = wifiManager.addNetwork(conf);

        Logger.d(TAG, "Added networkId : " + networkId);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                Logger.d(TAG, "WifiConfiguration SSID " + i.SSID);

                boolean isDisconnected = wifiManager.disconnect();
                Logger.d(TAG, "isDisconnected : " + isDisconnected);

                boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                Logger.d(TAG, "isEnabled : " + isEnabled);

                boolean isReconnected = wifiManager.reconnect();
                Logger.d(TAG, "isReconnected : " + isReconnected);
                if (this.connectionCb != null){
                    if(isReconnected) {

                        // TODO : 연결되 있는 wifi IP 주소를 저장한다

                        connectionCb.onNetResult(eNetStatus.CONNECTED);
                        stopTrackingStatus();
                    }
                    else connectionCb.onNetResult(eNetStatus.FAIL);
                }

                break;
            }
        }
    }

    String getConnectedSSID() {
//        Activity context = getActivity();
//        if(context != null)
//            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String cssid = info.getSSID();
        if (!StringUtil.isNullOrEmpty(cssid)) {
            cssid = cssid.replace("\"", "");
            return cssid;
        }
        return null;
    }

    String getSecureType(String networkSSID) {

        Logger.d(TAG, "getSecureType ssid : " + networkSSID);
        for (int i = 0; i < scanResult.size(); i++) {
            if (scanResult.get(i).SSID.equals(networkSSID)) {
                if (scanResult.get(i).capabilities.toUpperCase().contains(SECURETYPE_WEP)) {
                    Logger.d(TAG, "getSecureType: WEP");
                    return SECURETYPE_WEP;
                } else if (scanResult.get(i).capabilities.toUpperCase().contains(SECURETYPE_WPA)) {
                    Logger.d(TAG, "getSecureType: WPA");
                    return SECURETYPE_WPA;
                }
            }
        }
        Logger.d(TAG, "getSecureType: OTHER");
        return null;
    }
}
