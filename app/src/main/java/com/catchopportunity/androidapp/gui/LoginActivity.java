package com.catchopportunity.androidapp.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    UserClient userClient ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    try {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        userClient = retrofit.create(UserClient.class);

        Call<List<User>> call = userClient.getData();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> userList =new ArrayList<>();
                userList = response.body();


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }catch (Exception e){
    }









    }
}
