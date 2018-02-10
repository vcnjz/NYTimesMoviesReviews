package com.example.workstation.nytimesmoviesreviews;

import android.app.Application;

import com.example.workstation.nytimesmoviesreviews.Model.NYTimesApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static final String API_KEY = "050100d3fab94b618da49f73185cb896";
    private static final String BASE_URL = "http://api.nytimes.com/svc/movies/v2/";
    private static NYTimesApi nyTimesApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        nyTimesApi = retrofit.create(NYTimesApi.class);
    }

    public static NYTimesApi getApi() {
        return nyTimesApi;
    }

}
