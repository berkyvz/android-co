package com.catchopportunity.androidapp.api;


import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit=null;
   private static String Base_Url= " http://10.0.2.2:8082/";
    //private static String Base_Url= " http://159.65.226.217:8082/";


    public static Retrofit getClient(){
       try {

           OkHttpClient okHttpClient = new OkHttpClient.Builder()
                   .connectTimeout(30, TimeUnit.SECONDS)
                   .readTimeout(30, TimeUnit.SECONDS)
                   .writeTimeout(30, TimeUnit.SECONDS)
                   .build();


           if(retrofit== null){
               retrofit = new Retrofit.Builder()
                       .baseUrl(Base_Url)
                       .addConverterFactory(GsonConverterFactory.create())
                       .client(okHttpClient)
                       .build();
               return retrofit;
           }
           return retrofit;

       }catch (Exception e){


       }
       return  null;
    }


}
