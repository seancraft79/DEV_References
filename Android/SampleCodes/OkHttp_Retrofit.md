
# OkHttp

### [OkHttp](https://square.github.io/okhttp/)  

### Add dependency
[release](https://square.github.io/okhttp/#releases)  
```
implementation("com.squareup.okhttp3:okhttp:4.2.1")
```

### Get
```
public void get(String requestURL) {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.addHeader("x-api-key", RestTestCommon.API_KEY)
					.url(requestURL)
					.build(); //GET Request 
                        
                       //동기 처리시 execute함수 사용 
			Response response = client.newCall(request).execute(); 
			
			//출력 
			String message = response.body().string();
			System.out.println(message);
		} catch (Exception e){
			System.err.println(e.toString());
		}
	}
```

### Post
```
public void post(String requestURL, String jsonMessage) {
		try{
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.addHeader("x-api-key", RestTestCommon.API_KEY)
					.url(requestURL)
					.post(RequestBody.create(MediaType.parse("application/json"), jsonMessage)) //POST로 전달할 내용 설정 
					.build();

                        //동기 처리시 execute함수 사용
			Response response = client.newCall(request).execute();  

			//출력
			String message = response.body().string();
			System.out.println(message);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
```
  
# Retrofit
### [Retrofit](https://square.github.io/retrofit/)  

[github repo](https://github.com/square/retrofit)  

### Add dependency
```
    // Retrofit
    def retrofit_version = "2.6.2"

    implementation "com.squareup.retrofit2:retrofit:${retrofit_version}"
    implementation "com.squareup.retrofit2:converter-gson:${retrofit_version}"
```

### Define service
```
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAirFrameService {

    // HTTP URL
    @GET("/getuser.php/")
    Call<List<UserModel>> getData(@Query("id") String userId);
}
```

### Use in DataManager
```

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitDataManager {

    private static final String TAG = "[RetrofitDataManager] ";

    // http://ec2-13-125-72-166.ap-northeast-2.compute.amazonaws.com/login.php?userid=vincent&password=asdfsadf
    public static final String URL = "http://ec2-13-125-72-166.ap-northeast-2.compute.amazonaws.com";

    private static RetrofitDataManager instance;

    public static RetrofitDataManager getInstance() {

        if (instance == null) {
            instance = new RetrofitDataManager();
        }
        return instance;
    }

    Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void userLogin(final String userId, final String password, final UserLoginCallback cb) {

        final RetrofitUserService retrofitUserService = getRetrofit(URL).create(RetrofitUserService.class);

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                // POST
                try {

                    Thread.sleep(1000);

                    HashMap<String, Object> loginData = new HashMap<>();
                    loginData.put("userid", userId);
                    loginData.put("password", password);

                    retrofitUserService.userLogin(loginData).enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                            List<User> user = response.body();

                            if (cb != null) cb.onResult(user);
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {

                            if (cb != null) cb.onResult(null);
                        }
                    });
                } catch (Exception e) {

                    if(cb != null) cb.onResult(null);
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }
}

```