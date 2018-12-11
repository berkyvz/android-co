package com.catchopportunity.androidapp.client;

import com.catchopportunity.androidapp.helpermodel.OpportunityItem;
import com.catchopportunity.androidapp.model.Opportunity;
import com.catchopportunity.androidapp.model.User;
import com.catchopportunity.androidapp.model.UserToken;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/*
* POST", "/user/login/{token}", "Takes user token and give all datas about this user."));
		methodList.add(new Info("POST", "/user/register", "Takes user and add it to DB."));
		methodList.add(new Info("POST", "/user/{token}", "Takes user token and give all datas about this user."));
*
* */

public interface UserClient {

    @POST("user/login")
    Call<UserToken> loginUser(@Body User user);

    @POST("user/register")
    Call<User> registerUser(@Body User user);

    @GET("user/opportunityitems")
    Call<List<OpportunityItem>> getOpportunityList(@Header("Auth") String token);

    @POST("user/reserve/{id}")
    Call<Opportunity> reserveOpportunity(@Header("Auth") String token , @Path("id") int id);

    @DELETE("user/reserve/{id}")
    Call<Opportunity> deleteReservedOpportunity(@Header("Auth") String token , @Path("id") int id);

    @GET("user/opportunity")
    Call<List<OpportunityItem>> getReservedOpportunity(@Header("Auth") String token);

    @PUT("user")
    Call<UserToken> updateUser(@Header("Auth") String token,@Body User userNew);

   


}
