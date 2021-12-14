package com.example.socialdance.retrofit;

import com.example.socialdance.model.School;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SchoolApi {

    @GET("schools")
    Call<List<School>> getAllSchools();

    @GET("schools/{id}")
    Call<School> getSchoolById(@Path("id") Integer id);

    @POST("schools")
    Call<School> createSchool(@Body School school);

    @POST("schools")
    Call<School> updateSchool(@Body School school);

    @DELETE("schools/{id}")
    Call<School> deleteSchool(@Path("id") Integer id);
}
