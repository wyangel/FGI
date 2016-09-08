package cn.insightsresearch.fgi.Api;

import cn.insightsresearch.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/30.
 */
public class RetroFactory {
    public static final String API_SERVER = Constants.APP_SERVER;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    public static ApiService getApiService(){
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    private static Retrofit retrofitrj = new Retrofit.Builder()
            .baseUrl(API_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(httpClient.build())
            .build();

    public static ApiService getApiServiceRj(){
        ApiService apiService = retrofitrj.create(ApiService.class);
        return apiService;
    }
}
