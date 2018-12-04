package com.catchopportunity.androidapp.client;

import com.catchopportunity.androidapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
* POST", "/user/login/{token}", "Takes user token and give all datas about this user."));
		methodList.add(new Info("POST", "/user/register", "Takes user and add it to DB."));
		methodList.add(new Info("POST", "/user/{token}", "Takes user token and give all datas about this user."));
*
* */

public interface UserClient {

    @GET("user/login")
    Call<User> loginUser(@Header("Auth") String token);

    @POST("user/register")
    Call<User> registerUser(@Body User user);


}
