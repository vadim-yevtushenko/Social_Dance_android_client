package com.example.socialdance.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkService {
//    public static final String BASE_URL = "http://192.168.1.100:8080/";
    public static final String BASE_URL = "https://social-dance.herokuapp.com/";
    private static NetworkService networkService;
    private Retrofit retrofit;

    private NetworkService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static NetworkService getInstance() {
        if (networkService == null) {
            networkService = new NetworkService();
        }
        return networkService;
    }

    public SchoolApi getSchoolApi() {
        return retrofit.create(SchoolApi.class);
    }
    public DancerApi getDancerApi() {
        return retrofit.create(DancerApi.class);
    }
    public EventApi getEventApi() {
        return retrofit.create(EventApi.class);
    }

}
