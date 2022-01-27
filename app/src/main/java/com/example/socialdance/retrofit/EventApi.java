package com.example.socialdance.retrofit;

import com.example.socialdance.model.Event;

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

public interface EventApi {

    @GET("events")
    Call<List<Event>> getAllEvents();

    @GET("events/search/{city}")
    Call<List<Event>> getAllEventsByCity(@Path("city") String city);

    @GET("events/owner/{id}")
    Call<List<Event>> getAllEventsByOwnerId(@Path("id") int id);

    @GET("events/{id}")
    Call<Event> getEventById(@Path("id") Integer id);

    @POST("events")
    Call<Event> createEvent(@Body Event event);

    @POST("events")
    Call<Event> updateEvent(@Body Event event);

    @Multipart
    @POST("events/upload-image")
    Call<String> uploadImage(@Query("id") Integer id, @Part MultipartBody.Part image);

    @GET("events/download-image")
    Call<ResponseBody> downloadImage (@Query("id") Integer id);

    @DELETE("events/delete-image")
    Call<Void> deleteImage(@Query("id") Integer id);

    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Path("id") Integer id);
}
