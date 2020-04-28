package com.ksu.serene.controller.main.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.model.Location;
import com.ksu.serene.R;

import java.util.List;

public class locationsAdapter extends RecyclerView.Adapter<locationsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Location> locations;

    locationsAdapter(Context context, List<Location> locations){
        this.layoutInflater = LayoutInflater.from(context);
        this.locations = locations;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.location_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //bind the textviews with locations info received

        String location_name = locations.get(i).getName();
        viewHolder.location_name.setText(location_name);

//        String level = locations.get(i).getAL_level();
//        viewHolder.location_AL.setText(level);

        String nearestLoc = locations.get(i).getNearestLoc();
        viewHolder.nearestLoc.setText(nearestLoc);

        int frequency = locations.get(i).getFrequency();
        viewHolder.frequency.setText(String.valueOf(frequency));

    }


    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView location_name, location_AL, nearestLoc, frequency;


        public ViewHolder(View itemView) {
            super(itemView);

            location_name = itemView.findViewById(R.id.parent_loc);
            //location_AL = itemView.findViewById(R.id.location_AL);
            nearestLoc = itemView.findViewById(R.id.nearestPlaces);
            frequency = itemView.findViewById(R.id.freqN);

        }


    }
}
