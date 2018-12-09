package com.catchopportunity.androidapp.gui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText txtEmail, txtPassword, txtLatitude, txtLongitude;
    private Button btnFindLocation, btnRegister;


    LocationManager mLocManager;
    LocationListener mLocListener;


    private Retrofit retrofit;
    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);


        txtEmail = (EditText) findViewById(R.id.register_email);
        txtPassword = (EditText) findViewById(R.id.register_password);
        txtLatitude = (EditText) findViewById(R.id.register_latitude);
        txtLongitude = (EditText) findViewById(R.id.register_longitude);

        btnFindLocation = (Button) findViewById(R.id.btnFindLocation);
        btnFindLocation.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnRegister.getId()){
            register();
        }
        if(v.getId() == btnFindLocation.getId()){
            findMyLocation();
        }


    }

    public void register(){
        final ProgressDialog loadingScreen = ProgressDialog.show(RegisterActivity.this, "",
                "Registering...", true);



        User user = new User();
        if(txtEmail.getText().toString().equals("")|| txtPassword.getText().toString().equals("") || txtLatitude.getText().toString().equals("") || txtLongitude.getText().toString().equals("") ){
            Toast.makeText(this, "Please be sure that you fill all of the blank areas.", Toast.LENGTH_SHORT).show();
            loadingScreen.dismiss();
            return;
        }

        user.setEmail(txtEmail.getText().toString());
        user.setPassword(txtPassword.getText().toString());
        user.setLatitude(txtLatitude.getText().toString());
        user.setLongitude(txtLongitude.getText().toString());



        try{

            Call<User> call = userClient.registerUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User added.", Toast.LENGTH_SHORT).show();
                        loadingScreen.dismiss();
                    }
                    loadingScreen.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
                    loadingScreen.dismiss();
                }
            });


        }catch (Exception e){
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingScreen.dismiss();
        }


    }

    public void findMyLocation(){


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            } , 10);
            return;
        }

        final ProgressDialog loadingScreen = ProgressDialog.show(RegisterActivity.this, "",
                "We are trying to find you...", true);

        mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                txtLatitude.setText(location.getLatitude()+"");
                txtLongitude.setText(location.getLongitude()+"");
                loadingScreen.dismiss();



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

       try {
          mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mLocListener);
          mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mLocListener);


       }catch (Exception e){
           loadingScreen.dismiss();
       }


    }

    @Override
    protected void onPause() {
        super.onPause();
       try {
          mLocManager.removeUpdates(mLocListener);

       }catch (Exception e){

       }
    }
}
