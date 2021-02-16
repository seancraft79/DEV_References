package com.sysgen.eom.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFileHelper {
    /**
     * Read text file from assets folder.
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readAssetTextFile(Context context, String fileName) throws IOException {
        AssetManager am = context.getAssets();
        boolean isFile = false;
        String[] fileNames = am.list("");
        for (String fn : fileNames) {
            if(fn.equals(fileName)) {
                isFile = true;
                break;
            }
        }
        if(!isFile) {
            Log.e("ReadAssetTextFile", "no asset file found name of " + fileName);
            return null;
        }

        String string = "";
        StringBuilder stringBuilder = new StringBuilder();

        InputStream is = am.open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
        }
        reader.close();
        is.close();
        return stringBuilder.toString();
    }

    /**
     * Read text file from storage.
     * @param context
     * @param folder
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readTextFile(Context context, String folder, String fileName) throws IOException {
        File file = new File(folder + File.separator + fileName);

        if(!file.exists()) {
            Log.e("ReadAssetTextFile", "no asset file found name of " + fileName);
            return null;
        }

        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append("\n");
        }
        reader.close();
        return stringBuilder.toString();
    }
}
