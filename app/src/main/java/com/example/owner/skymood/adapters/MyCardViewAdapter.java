package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.owner.skymood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

/**
 * Created by owner on 08/04/2016.
 */
public class MyCardViewAdapter extends RecyclerView.Adapter<MyCardViewAdapter.CardViewHolder>{

    private Context context;
    private ArrayList<String> data;

    public MyCardViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.my_locations_row, parent, false);
        return new CardViewHolder(root);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        String city = data.get(position);
        holder.city.setText(city);
        holder.city.setText(city);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{

        TextView city;
        TextView country;
        RadioButton radio;

        public CardViewHolder(View itemView) {
            super(itemView);

            this.city = (TextView) itemView.findViewById(R.id.mylocations_city);
            this.country = (TextView) itemView.findViewById(R.id.mylocations_country);
            this.radio = (RadioButton) itemView.findViewById(R.id.location_radio);
        }
    }
}
