package com.catchopportunity.androidapp.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

    private static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        token= getIntent().getStringExtra("TOKEN_VALUE");
        Toast.makeText(this, token+"", Toast.LENGTH_SHORT).show();

        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

        final ProgressDialog loading = ProgressDialog.show(HomeActivity.this, "",
                "Catching Opportunities..", true);

        listView = findViewById(R.id.homeList);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        btnUpdateLocation = findViewById(R.id.btnUpdateLocation);
        btnUpdateLocation.setOnClickListener(this);

        Call<List<OpportunityItem>> call= userClient.getOpportunityList(token);
        call.enqueue(new Callback<List<OpportunityItem>>() {
            @Override
            public void onResponse(Call<List<OpportunityItem>> call, Response<List<OpportunityItem>> response) {
                theList = response.body();
                initilizeListView();


                try {
                    initilizeListView();
                }catch (Exception e){
                    Log.d("HATA" , "HATA : "+ e.getMessage());

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

    public void initilizeListView(){
        OpportunityListAdapter adapter = new OpportunityListAdapter(HomeActivity.this , R.layout.list_item_opportunity , theList , token);
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
    public void onClick(View v) {

        if(v.getId() == btnUpdateLocation.getId()){

        }
        if(v.getId() == btnRefresh.getId()){

        }

    }
}
