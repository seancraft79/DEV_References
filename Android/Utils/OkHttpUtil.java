package com.sysgen.eom.util;

import android.util.Log;

import com.sysgen.eom.listener.CallBack;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {

    private static final String TAG = "OkHttpCall";

    public static final String RESPONSE_ERROR = "error";

    private static OkHttpUtil instance;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static final MediaType PLAINTEXT
            = MediaType.get("text/html; charset=UTF-8");

    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private OkHttpUtil() {
    }

    public synchronized static OkHttpUtil getInstance() {
        if(instance == null) instance = new OkHttpUtil();
        return instance;
    }


    public void getAsync(final String url, final CallBack<Response> callBack) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                SLog.e(TAG, "OkHttpCall error : " + e.getMessage());
                if(callBack != null) callBack.onResult(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(callBack != null) callBack.onResult(response);
            }
        });
    }

    public void getAsync(final String baseUrl, Map<String, String> queryData, final CallBack<Response> callBack) {

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(baseUrl).newBuilder();
        if(queryData != null && queryData.size() > 0) {

            for (Map.Entry<String, String> entry: queryData.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        final String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                SLog.e(TAG, "OkHttpCall get error : " + e.getMessage());
                if(callBack != null) callBack.onResult(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(callBack != null) callBack.onResult(response);
            }
        });
    }

    public void get(final String url, final CallBack<Response> callBack) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(callBack != null) callBack.onResult(response);
        } catch (IOException e) {
            SLog.e(TAG, "OkHttpCall get error : " + e.getMessage());
            if(callBack != null) callBack.onResult(null);
        }
    }

    public void get(final String baseUrl, Map<String, String> queryData, final CallBack<Response> callBack) {

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(baseUrl).newBuilder();
        if(queryData != null && queryData.size() > 0) {

            for (Map.Entry<String, String> entry: queryData.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        final String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(callBack != null) callBack.onResult(response);
        } catch (IOException e) {
            SLog.e(TAG, "OkHttpCall get error : " + e.getMessage());
            if(callBack != null) callBack.onResult(null);
        }
    }

    public void post(MediaType mediaType, String url, String json, final CallBack<String> callBack) {

        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(callBack != null) callBack.onResult(response.body().string());
        } catch (IOException e) {
            SLog.e(TAG, "OkHttpCall postAsyncThread error : " + e.getMessage());
            if(callBack != null) callBack.onResult(RESPONSE_ERROR);
        }
    }

    public void post(final String url, final Map<String, String> postData, final CallBack<Response> callBack) {

        FormBody.Builder fb = new FormBody.Builder();

        if(postData != null && postData.size() > 0) {
            for (Map.Entry<String, String> entry: postData.entrySet()) {
                fb.add(entry.getKey(), entry.getValue());
            }
        }

        FormBody body = fb.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(callBack != null) callBack.onResult(response);
        } catch (IOException e) {
            SLog.e(TAG, "OkHttpCall postAsyncThread error : " + e.getMessage());
            if(callBack != null) callBack.onResult(null);
        }
    }

    public void postAsync(final String url, final Map<String, String> postData, final CallBack<Response> callBack) {

        FormBody.Builder fb = new FormBody.Builder();

        if(postData != null && postData.size() > 0) {
            for (Map.Entry<String, String> entry: postData.entrySet()) {
                fb.add(entry.getKey(), entry.getValue());
            }
        }

        FormBody body = fb.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                SLog.e(TAG, "OkHttpCall get error : " + e.getMessage());
                if(callBack != null) callBack.onResult(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(callBack != null) callBack.onResult(response);
            }
        });
    }

    public static String getJsonString(Map<String, String> data) {
        String json = "{";

        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            json += "'" + entry.getKey() + "':'" + entry.getValue() + "'";

            if(iterator.hasNext()) json += ",";
        }

        json += "}";

        return json;
    }
}
