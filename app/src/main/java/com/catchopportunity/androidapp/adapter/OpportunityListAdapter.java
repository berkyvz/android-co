package com.catchopportunity.androidapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.catchopportunity.androidapp.gui.HomeActivity;
import com.catchopportunity.androidapp.helpermodel.OpportunityItem;
import com.catchopportunity.androidapp.model.Opportunity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OpportunityListAdapter extends ArrayAdapter<OpportunityItem> {

    private Context context;
    private int resource;
    private List<OpportunityItem> list;
    private String token;

    private Retrofit retrofit;
    private UserClient userClient ;


    public OpportunityListAdapter(Context context , int resource , List<OpportunityItem> list , String token){
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
        View view = layoutInflater.inflate(resource , null);

        OpportunityItem oItem = list.get(position);

        TextView txtDistance = view.findViewById(R.id.oplist_distance);
        final TextView txtId = view.findViewById(R.id.oplist_id);
        TextView txtDesc1 = view.findViewById(R.id.oplist_desc1);
        TextView txtDesc2 = view.findViewById(R.id.oplist_desc2);
        TextView txtDesc3 = view.findViewById(R.id.oplist_desc3);
        TextView txtCount = view.findViewById(R.id.oplist_count);
        TextView txtName = view.findViewById(R.id.oplist_name);
        TextView txtPrice = view.findViewById(R.id.oplist_price);
        TextView txtCity = view.findViewById(R.id.oplist_city);
        Button btnAdd = view.findViewById(R.id.oplist_button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(context, "",
                        "Reserving Opportunity for you..", true);
                Call<Opportunity> call = userClient.reserveOpportunity(token,list.get(position).getOpportunity().getOid());
                call.enqueue(new Callback<Opportunity>() {
                    @Override
                    public void onResponse(Call<Opportunity> call, Response<Opportunity> response) {
                        if(response.code() == 200){
                            Opportunity o = response.body();
                            Toast.makeText(context, "Opportunity reserved for you.", Toast.LENGTH_SHORT).show();
                            list.get(position).getOpportunity().setCount( (Integer.parseInt((list.get(position).getOpportunity().getCount()) ) -1) + "" );
                            OpportunityListAdapter.this.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(context, "Error while reserving opportunity..", Toast.LENGTH_SHORT).show();
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Opportunity> call, Throwable t) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                    }

                });


            }
        });





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

        txtName.setText(oItem.getName().toString());
        txtId.setText(oItem.getOpportunity().getOid().toString()+"");
        txtDesc1.setText(oItem.getOpportunity().getDesc1().toString());
        txtCount.setText("Count :"+oItem.getOpportunity().getCount().toString());
        txtDesc2.setText(oItem.getOpportunity().getDesc2().toString());
        txtDesc3.setText(oItem.getOpportunity().getDesc3().toString());
        txtPrice.setText(oItem.getOpportunity().getPrice()+ "TL");
        txtCity.setText(oItem.getOpportunity().getCity().toString());


    return view;

    }
}
