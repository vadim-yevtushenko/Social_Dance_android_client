package com.example.socialdance.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
//    public static final String BASE_URL = "http://192.168.1.100:8080/";
    public static final String BASE_URL = "https://social-dance.herokuapp.com/";
    private static NetworkService networkService;
    private Retrofit retrofit;

    private NetworkService() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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
