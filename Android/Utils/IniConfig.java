/**
 * Created by Sean Lee 2020.
 */
package com.sysgen.eom.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Multi thread safe ini config file utilization.
 */
public class IniConfig {
    private static final String TAG = "IniConfig";
    public String path;
    private List<String> keys;
    private volatile Map<String, String> iniValues;

    public IniConfig(String filePath) {
        path = filePath.trim();
        if(path == null || path.isEmpty()) Log.e(TAG, "IniConfig: path is null");
        else {
            check();
            keys = getAllKeys(path);
        }
    }

    public synchronized boolean updateOne(String key, String value) {
        if(!check()) return false;
        iniValues.remove(key);
        iniValues.put(key, value);

        return setIniValues(path, iniValues);
    }

    public synchronized boolean updateMany(Map<String, String> values) {
        if(!check()) return false;

        for (Map.Entry<String, String> entry : values.entrySet()) {
            iniValues.remove(entry.getKey());
            iniValues.put(entry.getKey(), entry.getValue());
        }

        return setIniValues(path, iniValues);
    }

    public synchronized String getOneByKey(String key) {
        if(!check()) return null;
        for (Map.Entry<String, String> entry : iniValues.entrySet()) {
            if(entry.getKey().equals(key)) return entry.getValue();
        }
        return null;
    }

    public synchronized Map<String, String> getManyByKey(List<String> keys) {
        if(!check()) return null;
        Map<String, String> data = new HashMap<>();
        for (String key : keys) {
            for (Map.Entry<String, String> entry : iniValues.entrySet()) {
                if(entry.getKey().equals(key)) data.put(entry.getKey(), entry.getValue());
            }
        }
        return data;
    }

    public synchronized Map<String, String> getAll(){
        if(check()) return new HashMap<>(iniValues);
        return null;
    }

    public void reloadConfig() {
        iniValues = getAllIniValues(path);
    }

    /**
     * Check whether local holding values map is null or not.
     * @return
     */
    protected boolean check() {
        if(iniValues == null || iniValues.size() == 0) iniValues = getAllIniValues(path);
        return iniValues != null && iniValues.size() > 0;
    }

    /**
     * Save key value pair.
     * @param path
     * @param key
     * @param value
     * @return
     */
    protected synchronized boolean setIniValue(String path, String key, String value){
        Map<String, String> allIniValues = getAllIniValues(path);
        if(allIniValues == null || allIniValues.size() < 1) {
            Log.e(TAG, "setIniValue can not load existing values");
            return false;
        }

        allIniValues.remove(key);
        allIniValues.put(key, value);
        return setIniValues(path, allIniValues);
    }

    /**
     * Save list of key value pairs.
     * @param path
     * @param data
     * @return
     */
    protected synchronized boolean setIniValues(String path, Map<String, String> data){
        Log.d(TAG, "setIniValues path: " + path + ", data: " + data.toString());
        FileOutputStream fos = null;
        try{
            File configFile = new File(path);
            if(!configFile.exists()){
                Log.e(TAG, "Can not find file at " + path);
                return false;
            }

            Properties ini = new Properties();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                ini.setProperty(entry.getKey(), entry.getValue());
            }

            fos = new FileOutputStream(path, false);
            ini.store(fos, "Initialized");

            return true;
        } catch(Exception e){
            Log.e(TAG, "setIniValues error : " + e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, "setIniValues error : " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get one value by passed key.
     * @param path
     * @param key
     * @return
     */
    protected String getIniValueByKey(String path, String key){
        FileInputStream is = null;
        try{
            File configFile = new File(path);
            if(!configFile.exists()){
                Log.e(TAG, "Can not find file at " + path);
                return null;
            }

            is = new FileInputStream(path);
            Properties ini = new Properties();
            ini.load(is);

            return ini.getProperty(key);

        }catch(Exception e){
            Log.e(TAG, "getIniValue error : " + e.getMessage());
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                Log.e(TAG, "getIniValue error : " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 전달된 key 에 매칭하는 value 반환
     * @param path
     * @param keys : 원하는 필드의 키
     * @return
     */
    protected Map<String, String> getIniValuesByKeys(String path, List<String> keys){
        FileInputStream is = null;
        try{
            File configFile = new File(path);
            if(!configFile.exists()){
                Log.e(TAG, "Can not find file at " + path);
                return null;
            }

            is = new FileInputStream(path);
            Properties ini = new Properties();
            ini.load(is);

            Map<String, String> data = new HashMap<>();
            for (int i = 0; i < keys.size(); i++) {
                String p = ini.getProperty(keys.get(i));
                data.put(keys.get(i), p);
            }

            return data;

        }catch(Exception e){
            Log.e(TAG, "getIniValue error : " + e.getMessage());
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                Log.e(TAG, "getIniValue error : " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 모든 key, value 값들 반환
     * @param path
     * @return
     */
    protected Map<String, String> getAllIniValues(String path){
        FileInputStream is = null;
        try{
            File configFile = new File(path);
            if(!configFile.exists()){
                Log.e(TAG, "Can not find file at " + path);
                return null;
            }

            is = new FileInputStream(path);
            Properties ini = new Properties();
            ini.load(is);

            Set<Object> allKeys = ini.keySet();

            Map<String, String> data = new HashMap<>();

            for (Object key : allKeys) {
                String k = (String) key;
                if(k != null && !k.isEmpty()) {
                    data.put(k, ini.getProperty(k));
                }
            }

            return data;

        }catch(Exception e){
            Log.e(TAG, "getIniValue error : " + e.getMessage());
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                Log.e(TAG, "getIniValue error : " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 모든 key 값들의 list 반환
     * @param path
     * @return
     */
    protected List<String> getAllKeys(String path){
        FileInputStream is = null;
        try{
            File configFile = new File(path);
            if(!configFile.exists()){
                Log.e(TAG, "Can not find file at " + path);
                return null;
            }

            is = new FileInputStream(path);
            Properties ini = new Properties();
            ini.load(is);
            Set<Object> keySet = ini.keySet();
            List<String> allKeys = new ArrayList<>();
            for (Object key : keySet) {
                String k = (String) key;
                if(k != null && !k.isEmpty())
                    allKeys.add(k);
            }
            return allKeys;

        }catch(Exception e){
            Log.e(TAG, "getIniValue error : " + e.getMessage());
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                Log.e(TAG, "getIniValue error : " + e.getMessage());
            }
        }
        return null;
    }

    public String toString() {
        String data = "data: ";
        for (Map.Entry<String, String> entry : iniValues.entrySet()) {
            data += "[KEY: " + entry.getKey() + ", VALUE: " + entry.getValue() + "], ";
        }
        return data;
    }
}
