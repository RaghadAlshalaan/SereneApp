package com.ksu.serene.Controller.Homepage.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.Model.Location;
import com.ksu.serene.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Location> locations;

    Adapter(Context context, List<Location> locations){
        this.layoutInflater = LayoutInflater.from(context);
        this.locations = locations;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //bind the textviews with locations received

        String location_name = locations.get(i).getName();
        viewHolder.location_name.setText(location_name);

        String level = locations.get(i).getAL_level();
        viewHolder.location_AL.setText(level);

        long daysBetween = locations.get(i).getDaysBetween();
        viewHolder.daysBetween.setText(daysBetween+"");
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView location_name, location_AL, daysBetween;


        public ViewHolder(View itemView) {
            super(itemView);
            location_name = (TextView)itemView.findViewById(R.id.location_name);
            location_AL = (TextView)itemView.findViewById(R.id.location_AL);
            daysBetween = (TextView)itemView.findViewById(R.id.numOfDays);
        }
    }
}
