package com.example.socialdance.retrofit;

import com.example.socialdance.model.Dancer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DancerApi {

    @GET("dancers")
    Call<List<Dancer>> getAllDancers();

    @GET("dancers/{id}")
    Call<Dancer> getDancerById(@Path("id") Integer id);

    @POST("dancers")
    Call<Dancer> createDancer(@Body Dancer dancer);

    @POST("dancers")
    Call<Dancer> updateDancer(@Body Dancer dancer);

    @DELETE("dancers/{id}")
    Call<Void> deleteDancer(@Path("id") Integer id);

    @GET("dancers/registration/{email}")
    Call<Integer> checkSignUpByEmail(@Path("email") String email);

    @GET("dancers/identification/{email}/{password}")
    Call<Integer> checkSignInByEmailAndPassword(@Path("email") String email, @Path("password") String password);

}
