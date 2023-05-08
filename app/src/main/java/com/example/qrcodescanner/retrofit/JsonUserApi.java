package com.example.qrcodescanner.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonUserApi {


    @POST("usher/login")
    Call<Token> loginUser(@Body User user);


    @POST("event/getForUsher")
    Call<EventList> getEvents(@Body User user);

    @POST("usher/getTicket")
    Call<Message> checkQr(@Body Ticket ticket);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("coinAdd")
    Call<Event> getToken(@Body Token coin, @Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("userAdd")
    Call<Event> setUser(@Body User user, @Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("passwordChange")
    Call<Event> setPassword(@Body User user, @Header("Authorization")String token);




}
