package com.catchopportunity.androidapp.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.model.User;
import com.catchopportunity.androidapp.model.UserToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText txtEmail , txtPassword;
    private Button btnLogIn , btnRegister;

    Retrofit retrofit;
    UserClient userClient ;

    public static UserToken userLoggedIn;
    private String token="NOTVALID";
    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLoggedIn = null;
        //initiliaze the components
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister_lgn);
        btnRegister.setOnClickListener(this);

        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);



    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnLogIn.getId()){
            logIn();
        }
        if(v.getId() == btnRegister.getId()){
            register();
        }

    }

    public void register(){
        Intent intent = new Intent(this , RegisterActivity.class);
        startActivity(intent);
    }

    public void logIn(){
        final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "",
                "Please wait...", true);


        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        User loginUser = new User();
        loginUser.setEmail(email);
        loginUser.setPassword(password);
        Call<UserToken> call = userClient.loginUser(loginUser);

        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {

                if (response.code() == 401){
                    txtEmail.setError("Wrong !");
                    txtPassword.setError("Wrong !");
                    loading.dismiss();
                }
                if(response.code() == 200) {
                    userLoggedIn = response.body();
                    token = response.body().getToken();
                    loading.dismiss();
                    Intent i = new Intent(LoginActivity.this , HomeActivity.class);
                    i.putExtra("TOKEN_VALUE" , token);
                    token = "NOTVALID";
                    startActivity(i);
                }

                loading.dismiss();

            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d("HATA" , t.getMessage()+" <- code");
                Toast.makeText(LoginActivity.this, "Your request is failed.", Toast.LENGTH_SHORT).show();

                loading.dismiss();

            }
        });




    }




}
