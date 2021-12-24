package com.example.socialdance.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialdance.model.Dancer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DancerApi {

    @GET("dancers")
    Call<List<Dancer>> getAllDancers();

    @GET("dancers/{id}")
    Call<Dancer> getDancerById(@Path("id") Integer id);

    @GET("dancers/search/{city}")
    Call<List<Dancer>> getDancersByCity(@Path("city") String city);

    @GET("dancers/search")
    Call<List<Dancer>> getDancersByNameAndSurname(@Query("name") String name, @Query("surname") String surname);

    @POST("dancers")
    Call<Dancer> createDancer(@Body Dancer dancer);

    @POST("dancers")
    Call<Dancer> updateDancer(@Body Dancer dancer);

    @DELETE("dancers/{id}")
    Call<Void> deleteDancer(@Path("id") Integer id);

    @GET("dancers/registration")
    Call<Integer> checkSignUpByEmail(@Query("email") String email);

    @GET("dancers/identification")
    Call<Integer> checkSignInByEmailAndPassword(@Query("email") String email, @Query("password") String password);

}
