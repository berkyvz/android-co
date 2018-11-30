package com.catchopportunity.androidapp.client;

import com.catchopportunity.androidapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserClient {

    @GET("/user")
    Call<List<User>> getData();


}
