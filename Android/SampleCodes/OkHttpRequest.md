
# OkHttpRequest.java

### [OkHttp](https://square.github.io/okhttp/)  

```
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import timber.log.Timber;


public class HttpRequest {

    private static final String TAG = "HttpRequest";

    public void Get(final String url, final IHttpResult cb) {

        Timber.tag(TAG);

        Timber.d("HttpRequest Get");

        class NetworkJop extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Timber.d("Http result => " + result);
                    return result;
                }
                catch (IOException e) {
                    Timber.e(e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(cb != null) cb.onResult(s);
            }
        }

        new NetworkJop().execute();
    }

    public void Post(final String url, final String json, final IHttpResult cb) {
        Timber.d("HttpRequest Post");

        class NetworkJob extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {

                OkHttpClient client = new OkHttpClient();

                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try  {
                    Response response = client.newCall(request).execute();
                    return response.body().toString();
                }
                catch (IOException e) {
                    Timber.e(e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(cb != null) cb.onResult(s);
            }
        }

        new NetworkJob().execute();
    }

    public interface IHttpResult {
        void onResult(String result);
    }
}

```

