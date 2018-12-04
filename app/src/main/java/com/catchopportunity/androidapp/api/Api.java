package com.catchopportunity.androidapp.api;


import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit=null;
    private static String Base_Url= " http://10.0.2.2:8080/";


    public static Retrofit getClient(){
       try {
           if(retrofit== null){
               retrofit = new Retrofit.Builder()
                       .baseUrl(Base_Url)
                       .addConverterFactory(GsonConverterFactory.create())
                       .client(new OkHttpClient())
                       .build();
               return retrofit;
           }
           return retrofit;

       }catch (Exception e){


       }
       return  null;
    }


}
