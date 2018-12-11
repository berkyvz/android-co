package com.catchopportunity.androidapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.catchopportunity.androidapp.R;
import com.catchopportunity.androidapp.api.Api;
import com.catchopportunity.androidapp.client.UserClient;
import com.catchopportunity.androidapp.gui.CaughtActivity;
import com.catchopportunity.androidapp.gui.LoginActivity;
import com.catchopportunity.androidapp.helpermodel.OpportunityItem;
import com.catchopportunity.androidapp.model.Opportunity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CaughtListAdapter  extends ArrayAdapter<OpportunityItem> {

    private Context context;
    private int resource;
    private List<OpportunityItem> list;
    private String token;

    private Retrofit retrofit;
    private UserClient userClient ;


    public CaughtListAdapter(Context context , int resource , List<OpportunityItem> list , String token){
        super(context , resource , list);
        this.context=context;
        this.resource=resource;
        this.list=list;
        this.token = token;

        retrofit = Api.getClient();
        userClient = retrofit.create(UserClient.class);

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);

        final OpportunityItem oItem = list.get(position);

        TextView txtId = view.findViewById(R.id.caughtlist_id);
        TextView txtDistance = view.findViewById(R.id.caughtlist_distance);
        TextView txtOwner = view.findViewById(R.id.caughtlist_owner);
        TextView txtPrice = view.findViewById(R.id.caughtlist_price);

        Button btnCancel = view.findViewById(R.id.caughtlist_btnCancel);
        Button btnRoute = view.findViewById(R.id.caughtlist_btnRoute);



        txtId.setText(oItem.getOpportunity().getOid()+"");

        if(Double.parseDouble(oItem.getDistance()) > 1000){
            int km = (int) Double.parseDouble(oItem.getDistance()) / 1000;
            String value = km + " km";

            txtDistance.setText(value);
        }
        else {
            int m = (int)Double.parseDouble(oItem.getDistance());
            String value = m + " m";
            txtDistance.setText(value);
        }

        txtOwner.setText(oItem.getName()+"");
        txtPrice.setText(oItem.getOpportunity().getPrice()+"");


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loadingScreen = ProgressDialog.show(context, "",
                        "Canceling Opportunity...", true);
                Call<Opportunity> call = userClient.deleteReservedOpportunity(LoginActivity.userLoggedIn.getToken(),list.get(position).getOpportunity().getOid());
                call.enqueue(new Callback<Opportunity>() {
                    @Override
                    public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                        if(response.code() == 200){
                            Toast.makeText(context, "Opportunity removed.", Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            CaughtListAdapter.this.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(context, "Error occured.", Toast.LENGTH_SHORT).show();
                        }
                        loadingScreen.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Opportunity> call, Throwable t) {
                        Toast.makeText(context , "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingScreen.dismiss();
                    }
                });
            }
        });


        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a Uri from an intent string. Use the result to create an Intent.
                String latitude = oItem.getOpportunity().getLatitude();
                String longitude = oItem.getOpportunity().getLongitude();
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);

            }
        });



        return view;
    }

}
