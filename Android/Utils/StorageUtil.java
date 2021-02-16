package com.sysgen.eom.util;

import android.os.StatFs;

import java.io.File;

public class StorageUtil {

    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpaceInBytes(String path) {
        long availableSpace = -1L;
        StatFs stat = new StatFs(path);
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

        return availableSpace;
    }

    /**
     * @return Number of kilo bytes available on External storage
     */
    public static long getAvailableSpaceInKB(String path){
        final long SIZE_KB = 1024L;
        return getAvailableSpaceInBytes(path) / SIZE_KB;
    }

    /**
     * @return Number of Mega bytes available on External storage
     */
    public static long getAvailableSpaceInMB(String path){
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        return  getAvailableSpaceInBytes(path) / SIZE_MB;
    }

    /**
     * @return Number of gega bytes available on External storage
     */
    public static long getAvailableSpaceInGB(String path){
        final long SIZE_KB = 1024L;
        final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
        return  getAvailableSpaceInBytes(path) / SIZE_GB;
    }

    /**
     * Return the size of a directory in bytes
     */
    public static long getDirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for(int i = 0; i < fileList.length; i++) {
                    // Recursive call if it's a directory
                    if(fileList[i].isDirectory()) {
                        result += getDirSize(fileList[i]);
                    } else {
                        // Sum the file size in bytes
                        result += fileList[i].length();
                    }
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    /**
     * Return the size of a directory in Mega bytes
     */
    public static long getDirSizeInMB(File dir) {
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
        return getDirSize(dir) / SIZE_MB;
    }
}
