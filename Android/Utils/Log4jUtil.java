package com.sysgen.eom.util;

import android.app.Application;
import android.os.Environment;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jUtil {

    private final static LogConfigurator _logConfigurator = new LogConfigurator();

    public static Logger getConfiguredLog4j() {

        Logger logger = Logger.getLogger(Application.class);

        // set file name
        SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");
        String time = logfile.format(new Date());
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED)) {

            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EOM/APP_LOG/";
            String fileName = "eom_" + time + ".log";

            File downloadDir = new File(dir);
            if (downloadDir.exists() == false) {
                SLog.d("log4j create folder : " + dir);
                downloadDir.mkdirs();
            }

            // set max. number of backed up log files
            int maxBackupSize = 7;
            // set max. size of log file
            long maxFileSize = 1024 * 1024 * 20;

            // configure
            Configure(dir + fileName, maxBackupSize, maxFileSize);

        } else {
            logger.error("getConfiguredLog4j() Exception: " + "no mount");
        }
        return logger;
    }

    /**
     * Configure Log4j
     *
     * @param fileName
     *            Name of the log file
     * @param maxBackupSize
     *            Maximum number of backed up log files
     * @param maxFileSize
     *            Maximum size of log file until rolling
     */
    public static void Configure(String fileName,
                                 int maxBackupSize, long maxFileSize) {

        _logConfigurator.setFileName(fileName);
        _logConfigurator.setMaxBackupSize(maxBackupSize);
        _logConfigurator.setMaxFileSize(maxFileSize);
        _logConfigurator.configure();

    }

    /**
     * Get logging operations class
     *
     * @param name
     *            The name of the logger to retrieve.
     * @return Logging operations class
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        return logger;
    }
}
