package com.catchopportunity.androidapp.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;

public class ProfileActivity extends AppCompatActivity {


    private MenuItem item_logout , item_profile , item_home , item_search , item_opp , item_qrReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Toast.makeText(this, "PROFILE ONCREATE", Toast.LENGTH_SHORT).show();
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
