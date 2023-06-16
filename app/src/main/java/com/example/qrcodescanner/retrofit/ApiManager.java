package com.example.qrcodescanner.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    private static JsonUserApi service;
    private static ApiManager apiManager;
    public static String ip = "http://192.168.0.103:3001/api/";
    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())/*.client(getUnsafeOkHttpClient().build())*/
                .build();




        service = retrofit.create(JsonUserApi.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void loginUser(User user,Callback<Token> callback) {
        Call<Token> userCall = service.loginUser(user);
        userCall.enqueue(callback);
    }

    public void getEvents(String token, Callback<EventList> callback) {
        Call<EventList> tokenCall = service.getEvents(token);
        tokenCall.enqueue(callback);
    }


   /* public void setPassword(User user,String token,Callback<Event> callback) {
        Call<Event> setPassword = service.setPassword(user,token);
        setPassword.enqueue(callback);
    }*/

    public void checkQr(Ticket ticket, String token,Callback<Message> callback) {
        Call<Message> tokenCall = service.checkQr(ticket, token);
        tokenCall.enqueue(callback);
    }

}