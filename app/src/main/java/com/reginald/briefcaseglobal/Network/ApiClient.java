package com.reginald.briefcaseglobal.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reginald.briefcaseglobal.AppGlobals;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;


    private static String BASE_URL="http://102.37.0.48/Briefcase1abdul/";       // SERVER

    public static Retrofit getClient(String ip) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ip)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
