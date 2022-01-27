package com.example.socialdance.retrofit;

import com.example.socialdance.model.Rating;
import com.example.socialdance.model.Review;
import com.example.socialdance.model.School;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SchoolApi {

    @GET("schools")
    Call<List<School>> getAllSchools();

    @GET("schools/search/{city}")
    Call<List<School>> getAllSchoolsByCity(@Path("city") String city);

    @GET("schools/owner/{id}")
    Call<List<School>> getAllSchoolsByOwnerId(@Path("id")int id);

    @GET("schools/reviews/{id}")
    Call<List<Review>> getAllReviewsBySchool(@Path("id")int id);

    @GET("schools/{id}")
    Call<School> getSchoolById(@Path("id") Integer id);

    @GET("schools/{id}/{dancerId}")
    Call<Integer> getRatingByDancerId(@Path("id") Integer schoolId, @Path("dancerId") Integer dancerId);

    @POST("schools")
    Call<School> createSchool(@Body School school);

    @POST("schools/reviews")
    Call<Void> createReview(@Body Review review);

    @POST("schools/ratings")
    Call<Void> createRating(@Body Rating rating);

    @POST("schools")
    Call<School> updateSchool(@Body School school);

    @Multipart
    @POST("schools/upload-image")
    Call<String> uploadImage(@Query("id") Integer id, @Part MultipartBody.Part image);

    @GET("schools/download-image")
    Call<ResponseBody> downloadImage (@Query("id") Integer id);

    @DELETE("schools/delete-image")
    Call<Void> deleteImage(@Query("id") Integer id);

    @DELETE("schools/{id}")
    Call<Void> deleteSchool(@Path("id") Integer id);
}
