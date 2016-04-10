package com.example.owner.skymood.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.model.MyLocation;

import java.util.List;

/**
 * Created by owner on 10/04/2016.
 */
public class TestAdapter extends ArrayAdapter<TestAdapter.ViewHolder>{

    private List<MyLocation> myLocations;
    private Context context;
    private int selectedPosition = -1;

    public TestAdapter(Context context, int resource, List<MyLocation> myLocations) {
        super(context, resource);
        this.context = context;
        this.myLocations = myLocations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.my_locations_row, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


        holder.checkBox.setTag(position); // This line is important.


        MyLocation location = myLocations.get(position);
        holder.city.setText(location.getCity());
        holder.country.setText(location.getCountry());
        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
        } else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        return convertView;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView city;
        TextView country;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            this.city = (TextView) itemView.findViewById(R.id.mylocations_city);
            this.country = (TextView) itemView.findViewById(R.id.mylocations_country);
            //this.checkBox = (CheckBox) itemView.findViewById(R.id.check);
        }
    }


}
