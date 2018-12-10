package com.catchopportunity.androidapp.gui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.adapter.OpportunityListAdapter;
import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.calculators.DistanceCalculator;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.helpermodel.OpportunityItem;
import com.catchopportunity.androidapp.model.Opportunity;
import com.catchopportunity.androidapp.model.User;
import com.catchopportunity.androidapp.model.UserToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    UserClient userClient ;

    List<OpportunityItem> theList;

    private MenuItem item_logout , item_profile , item_home , item_search , item_opp , item_qrReader;
    private ListView listView;
    private Button btnUpdateLocation , btnRefresh;

    LocationManager mLocManager;
    LocationListener mLocListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

        final ProgressDialog loading = ProgressDialog.show(HomeActivity.this, "",
                "Catching Opportunities..", true);

        listView = findViewById(R.id.homeList);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        btnUpdateLocation = findViewById(R.id.btnUpdateLocation);
        btnUpdateLocation.setOnClickListener(this);


        Call<List<OpportunityItem>> call= userClient.getOpportunityList(LoginActivity.userLoggedIn.getToken());
        call.enqueue(new Callback<List<OpportunityItem>>() {
            @Override
            public void onResponse(Call<List<OpportunityItem>> call, Response<List<OpportunityItem>> response) {
                theList = response.body();
                initilizeListView();
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<List<OpportunityItem>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage()+"Error while taking data", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }


    public void initilizeListView(){
        OpportunityListAdapter adapter = new OpportunityListAdapter(HomeActivity.this , R.layout.list_item_opportunity , theList , LoginActivity.userLoggedIn.getToken());
        listView.setAdapter(adapter);
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

        item_home.setEnabled(false);

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

    @Override
    public void onClick(View v) {

        if(v.getId() == btnUpdateLocation.getId()){
            buttonUpdateLocation();
        }
        if(v.getId() == btnRefresh.getId()){
            buttonRefresh();

        }
       

    }

    public void buttonUpdateLocation() {
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
        final ProgressDialog loadingScreen = ProgressDialog.show(HomeActivity.this, "",
                "We are trying to find you...", true);

        mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               if(location.getLatitude() > 0 && location.getLongitude() > 0){
                   User user = new User();
                   user.setEmail(LoginActivity.userLoggedIn.getEmail());
                   user.setUid(LoginActivity.userLoggedIn.getUid());
                   user.setPassword(LoginActivity.userLoggedIn.getPassword());
                   user.setLatitude(location.getLatitude()+"");
                   user.setLongitude(location.getLongitude()+"");

                   Toast.makeText(HomeActivity.this, "Running", Toast.LENGTH_SHORT).show();

                   Call<UserToken> call = userClient.updateUser(LoginActivity.userLoggedIn.getToken() , user);
                   call.enqueue(new Callback<UserToken>() {
                       @Override
                       public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                           LoginActivity.userLoggedIn = response.body();
                           loadingScreen.dismiss();
                           mLocManager.removeUpdates(mLocListener);
                           buttonRefresh();
                       }

                       @Override
                       public void onFailure(Call<UserToken> call, Throwable t) {

                           loadingScreen.dismiss();
                           mLocManager.removeUpdates(mLocListener);

                       }
                   });
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

    public void buttonRefresh(){
        final ProgressDialog loading = ProgressDialog.show(HomeActivity.this, "",
                "Refreshing in progress...", true);
        Call<List<OpportunityItem>> call= userClient.getOpportunityList(LoginActivity.userLoggedIn.getToken());
        call.enqueue(new Callback<List<OpportunityItem>>() {
            @Override
            public void onResponse(Call<List<OpportunityItem>> call, Response<List<OpportunityItem>> response) {


                if(response.code() == 200) {
                    theList = response.body();
                    initilizeListView();

                }
                else {

                }



                loading.dismiss();
            }

            @Override
            public void onFailure(Call<List<OpportunityItem>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage()+"Error while taking data", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });



    }


}
