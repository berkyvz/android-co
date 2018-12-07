package com.catchopportunity.androidapp.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;

public class HomeActivity extends AppCompatActivity {

    private MenuItem item_logout , item_profile , item_search , item_opp , item_qrReader;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        token= getIntent().getStringExtra("TOKEN_VALUE");


        item_logout = findViewById(R.id.menu_logout);
        item_profile = findViewById(R.id.menu_profile);
        item_search = findViewById(R.id.menu_search);
        item_opp = findViewById(R.id.menu_myOpportunities);
        item_qrReader = findViewById(R.id.menu_qrCodeReader);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu , menu);
        return true;
    }
}
