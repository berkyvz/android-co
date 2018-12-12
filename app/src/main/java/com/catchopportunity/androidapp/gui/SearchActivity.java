package com.catchopportunity.androidapp.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catchopportunity.androidapp.QrCodeActivity;
import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.adapter.OpportunityListAdapter;
import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.helpermodel.OpportunityItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private MenuItem item_logout , item_profile , item_home , item_search , item_opp , item_qrReader;

    private ListView listView;
    private ImageButton btnSearch;
    private Spinner spinner;
    private EditText txtSearch;

    Retrofit retrofit;
    UserClient userClient ;

    private String whatAmISearching;

    List<OpportunityItem> theList = new ArrayList<OpportunityItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        listView = findViewById(R.id.searchList);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        spinner = findViewById(R.id.spinnerSearch);
        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this , R.array.spinnerArray , android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        whatAmISearching = "Distance";

        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == spinner.getId()){
            ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            txtSearch.setText("");
            switch (position){
                case 0:
                    whatAmISearching = "Distance";
                    txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case 1:
                    whatAmISearching = "Description";
                    txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case 2:
                    whatAmISearching = "City";
                    txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case 3:
                    whatAmISearching = "Id";
                    txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                default:
                    whatAmISearching = "Distance";
                    txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);

                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnSearch.getId()){
            search();

        }

    }

    public void search(){
        String type = whatAmISearching;
        String input = txtSearch.getText().toString();
        txtSearch.setText("");

        if(input.length() < 1){
            Toast.makeText(this, "Plese fill the blank first.", Toast.LENGTH_SHORT).show();
            return;
        }

        char frstChar = input.charAt(0);
        String firstLetter = frstChar+"";
        firstLetter = firstLetter.toUpperCase();
        String leftCharSequence = input.substring(1, input.length());
        input = firstLetter + leftCharSequence;
        


        final ProgressDialog loading = ProgressDialog.show(SearchActivity.this, "",
                "Searching ...", true);

        Call<List<OpportunityItem>> call = userClient.searchOpportunities(type , input , LoginActivity.userLoggedIn.getToken());
        call.enqueue(new Callback<List<OpportunityItem>>() {
            @Override
            public void onResponse(Call<List<OpportunityItem>> call, Response<List<OpportunityItem>> response) {
                if(response.code() == 200){
                    theList = response.body();
                    initilizeListView();
                    
                    if (response.body().size() == 0){
                        Toast.makeText(SearchActivity.this, "Nothing to show..", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(SearchActivity.this, "Error occured!", Toast.LENGTH_SHORT).show();
                }

                loading.dismiss();
            }

            @Override
            public void onFailure(Call<List<OpportunityItem>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Check your Internet connection.", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }

        });






        initilizeListView();
    }

    public void initilizeListView(){
        OpportunityListAdapter adapter = new OpportunityListAdapter(SearchActivity.this , R.layout.list_item_opportunity , theList , LoginActivity.userLoggedIn.getToken());
        listView.setAdapter(adapter);
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
        if (item.getItemId() == item_qrReader.getItemId()){
            Intent i = new Intent(this , QrCodeActivity.class);
            startActivity(i);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
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

        item_search.setEnabled(false);

        return true;
    }


}
