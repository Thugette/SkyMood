package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.model.HourlyWeather;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by owner on 04/04/2016.
 */
public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder> {

    private Context context;
    private ArrayList<HourlyWeather> weathers;

    public HourlyAdapter(Context context, ArrayList<HourlyWeather> weathers){
        this.context = context;
        this.weathers = weathers;
    }


    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.hour_row, parent, false);
        return new HourViewHolder(row);
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        HourlyWeather weather = weathers.get(position);
        holder.hour.setText(weather.getHour() + ":00");
        holder.condition.setText(weather.getCondition());
        holder.temp.setText(weather.getTemp() + " ℃");
        holder.icon.setImageBitmap(weather.getIcon());

    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView hour;
        TextView condition;
        TextView temp;

        public HourViewHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.hour_icon);
            this.hour = (TextView) itemView.findViewById(R.id.hour_hour);
            this.condition = (TextView) itemView.findViewById(R.id.hour_condition);
            this.temp = (TextView) itemView.findViewById(R.id.hour_temp);
        }
    }
}
