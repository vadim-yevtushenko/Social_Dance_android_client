package com.example.socialdance.retrofit;

import com.example.socialdance.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventApi {

    @GET("events")
    Call<List<Event>> getAllEvents();

    @GET("events/owner/{id}")
    Call<List<Event>> getAllEventsByOwnerId(@Path("id") int id);

    @GET("events/{id}")
    Call<Event> getEventById(@Path("id") Integer id);

    @POST("events")
    Call<Event> createEvent(@Body Event event);

    @POST("events")
    Call<Event> updateEvent(@Body Event event);

    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Path("id") Integer id);
}
