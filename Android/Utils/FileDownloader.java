package com.sysgen.dev.appupdater.utils;

import android.util.Log;

import com.sysgen.dev.appupdater.helpers.OkHttpCall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class FileDownloader {
    private static final String TAG = "FileDownloader";
    private static final boolean enableLog = true;

    public static Future<Boolean> downloadFilesAsync(List<FileDownloadInfo> fileDownloadInfos, ExecutorService executorService) {
        return downloadFilesAsync(fileDownloadInfos, executorService, false);
    }

    public static Future<Boolean> downloadFilesAsync(List<FileDownloadInfo> fileDownloadInfos, ExecutorService executorService, boolean abortWhenFail) {
        Callable<Boolean> c = () -> {
            boolean downloadSucess = true;
            for (int i = 0; i < fileDownloadInfos.size(); i++) {
                boolean isDone = downloadFile(fileDownloadInfos.get(i).getUrl(), fileDownloadInfos.get(i).getFolderPath(), fileDownloadInfos.get(i).getFileName());
                if(!isDone) {
                    downloadSucess = false;
                    if(abortWhenFail) return false;
                }
            }
            return downloadSucess;
        };
        return executorService.submit(c);
    }

    public static Future<Boolean> downloadFileAsync(String url, String folderPath, String fileName, ExecutorService executorService) {
        Callable<Boolean> c = () -> downloadFile(url, folderPath, fileName);
        return executorService.submit(c);
    }

    /**
     * Download file from server
     * @param url
     * @param folderPath
     * @param fileName : Include file extension
     */
    public static boolean downloadFile(String url, String folderPath, String fileName) {
        if(enableLog) Log.d(TAG, "[downloadFile] start ... url: " + url + ", folder: " + folderPath + ", fileName: " + fileName);

        final String tempFilePath = folderPath + "/temp_" + fileName;
        final String saveFilePath = folderPath + "/" + fileName;

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = OkHttpCall.getInstance().getClient();

        try (Response response = client.newCall(request).execute()) {

            int code = response.code();
            if(code >= HTTP_BAD_REQUEST) {
                if(enableLog) Log.e(TAG, "[downloadFile] error code : " + code);
                return false;
            }

            InputStream instream = response.body().byteStream();
            int contentLength = (int) response.body().contentLength();

            FileOutputStream out = new FileOutputStream(new File(tempFilePath));
            byte buf[] = new byte[8192];
            int len;
            int totalRead = 0;

            while ((len = instream.read(buf)) > 0) {
                totalRead += len;
                out.write(buf, 0, len);
            }

            if(enableLog) Log.d(TAG, "[downloadFile] end ... contentLength: " + contentLength + ", totalRead : " + totalRead);

            out.close();
            instream.close();

            File tempFile = new File(tempFilePath);
            File newFile = new File(saveFilePath);

            if(newFile.exists()) {
                if(newFile.delete()) if(enableLog) Log.d(TAG, "[downloadFile] delete existing file : " + fileName);
            }

            if(totalRead < 1) {
                if(enableLog) Log.e(TAG, "[downloadFile] fail totalRead 0");
                tempFile.delete();
                return false;
            }

            if(contentLength > 0) {
                if(contentLength != totalRead) {
                    if(enableLog) Log.e(TAG ,"[downloadFile] fail fileSize not valid");
                    tempFile.delete();
                    return false;
                }
            }

            Log.d(TAG, "[downloadFile] renamefile from: " + tempFile.getAbsolutePath() + " -> to: " + newFile.getAbsolutePath());

            if (renameFile(tempFile, newFile)) {
                if(enableLog) Log.d(TAG,"[downloadFile] done");
                return true;

            } else {
                // 모든 파일 삭제
                tempFile.delete();
                newFile.delete();

                if(enableLog) Log.e(TAG, "[downloadFile] rename fail");
            }

        } catch (IOException e) {
            // 모든 파일 삭제
            File tempFile = new File(tempFilePath);
            File newFile = new File(saveFilePath);
            tempFile.delete();
            newFile.delete();

            if(enableLog) Log.e(TAG, "[downloadFile] OkHttpCall error : " + e.getMessage());
        }
        return false;
    }

    public static boolean renameFile(File filename, File newFilename) {
        try {
            if (filename.exists()) {
                return filename.renameTo(newFilename);
            }
        } catch (Exception e) {
            if(enableLog) Log.e(TAG, "renameFile error : " + e.getMessage());
        }
        return false;
    }

    /** File download url model **/
    public static class FileDownloadInfo {
        String url;
        String folderPath;
        String fileName;

        public FileDownloadInfo(String url, String folderPath, String fileName) {
            this.url = url;
            this.folderPath = folderPath;
            this.fileName = fileName;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setFolderPath(String folderPath) {
            this.folderPath = folderPath;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getUrl() {
            return url;
        }

        public String getFolderPath() {
            return folderPath;
        }

        public String getFileName() {
            return fileName;
        }

        @Override
        public String toString() {
            return "[url: " + url + "]" +
                    ", [folderPath: " + folderPath + "]" +
                    ", [fileName: " + fileName + "]";
        }
    }
}
