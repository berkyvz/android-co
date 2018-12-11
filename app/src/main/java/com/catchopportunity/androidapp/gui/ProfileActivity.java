package com.catchopportunity.androidapp.gui;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.model.User;
import com.catchopportunity.androidapp.model.UserToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Retrofit retrofit;
    UserClient userClient ;


    LocationManager mLocManager;
    LocationListener mLocListener;



    private MenuItem item_logout , item_profile , item_home , item_search , item_opp , item_qrReader;

    private TextView txtEmail;
    private EditText txtPassword , txtLatitude , txtLongitude;

    private Button btnSave , btnFind ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

        txtEmail = findViewById(R.id.profile_txtEmail);
        txtPassword = findViewById(R.id.profile_txtPassword);
        txtLatitude = findViewById(R.id.profile_txtLatitude);
        txtLongitude = findViewById(R.id.profile_txtLongitude);


        txtEmail.setText(LoginActivity.userLoggedIn.getEmail());
        txtPassword.setText(LoginActivity.userLoggedIn.getPassword());
        txtLatitude.setText(LoginActivity.userLoggedIn.getLatitude());
        txtLongitude.setText(LoginActivity.userLoggedIn.getLongitude());


        btnSave = findViewById(R.id.profile_btnSave);
        btnFind = findViewById(R.id.profile_btnFind);

        btnSave.setOnClickListener(this);
        btnFind.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == btnFind.getId()){
            findMe();
        }
        if(v.getId() == btnSave.getId()){
           saveNewUser();
        }

    }

    public void findMe(){

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
            final ProgressDialog loadingScreen = ProgressDialog.show(ProfileActivity.this, "",
                    "We are trying to find you...", true);

            mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            mLocListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if(location.getLatitude() > 0 && location.getLongitude() > 0){
                        txtLatitude.setText(location.getLatitude()+"");
                        txtLongitude.setText(location.getLongitude()+"");
                        loadingScreen.dismiss();

                    }
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
                mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, mLocListener);
                mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, mLocListener);


            }catch (Exception e){
                loadingScreen.dismiss();
            }


    }

    public void saveNewUser(){

        if(txtLongitude.getText().toString().equals("")){
            txtLongitude.setError("ERROR: You can use 'Find Me'");
            return;
        }
        if(txtLatitude.getText().toString().equals("")){
            txtLatitude.setError("ERROR: You can use 'Find Me'");
            return;
        }

        if(txtPassword.getText().toString().length() < 5 ){
            txtPassword.setError("It must be at least 5 character.");
            return;
        }

        if(txtPassword.getText().toString().length() >= 5){

            final ProgressDialog loading = ProgressDialog.show(ProfileActivity.this, "",
                    "Updating User...", true);

            User newUser = new User();
            newUser.setLatitude(txtLatitude.getText().toString());
            newUser.setLongitude(txtLongitude.getText().toString());
            newUser.setPassword(txtPassword.getText().toString());
            newUser.setUid(LoginActivity.userLoggedIn.getUid());
            newUser.setEmail(LoginActivity.userLoggedIn.getEmail());


            Call<UserToken> call = userClient.updateUser(LoginActivity.userLoggedIn.getToken() , newUser);
            call.enqueue(new Callback<UserToken>() {
                @Override
                public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                    if(response.code() == 200){
                        LoginActivity.userLoggedIn = response.body();
                        loading.dismiss();

                        txtLatitude.setText(response.body().getLatitude());
                        txtLongitude.setText(response.body().getLongitude());

                    }
                }

                @Override
                public void onFailure(Call<UserToken> call, Throwable t) {

                }
            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu , menu);

        item_logout = menu.findItem(R.id.menu_logout);
        item_profile = menu.findItem(R.id.menu_profile);
        item_home = menu.findItem(R.id.menu_home);
        item_search = menu.findItem(R.id.menu_search);
        item_opp = menu.findItem(R.id.menu_myOpportunities);
        item_qrReader = menu.findItem(R.id.menu_qrCodeReader);

        item_profile.setEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == item_home.getItemId()){
            Intent i = new Intent(this , HomeActivity.class);
            startActivity(i);
            finish();
        }
        if(item.getItemId() == item_logout.getItemId()){
            Intent i = new Intent(this , LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if(item.getItemId() == item_opp.getItemId()){
            Intent i = new Intent(this , CaughtActivity.class);
            startActivity(i);
            finish();
            return true;

        }
        if(item.getItemId() == item_profile.getItemId()){
            Intent i = new Intent(this , ProfileActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if(item.getItemId() == item_search.getItemId()){
            Intent i = new Intent(this , SearchActivity.class);
            startActivity(i);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
