package com.ksu.serene.controller.main.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.R;
import com.ksu.serene.model.Event;

import java.util.List;

public class eventsAdapter  extends RecyclerView.Adapter<eventsAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<Event> events;

    eventsAdapter(Context context, List<Event> events){
        this.layoutInflater = LayoutInflater.from(context);
        this.events = events;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //bind the textviews with locations info received

        String event_name = events.get(i).getSummary();
        viewHolder.event_name.setText(event_name);

        String event_date = events.get(i).getStartTime();
        viewHolder.event_date.setText(event_date);

        String event_time = events.get(i).getTime();
        viewHolder.event_time.setText(event_time);


    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView event_name, event_date, event_time;


        public ViewHolder(View itemView) {
            super(itemView);

            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
            event_time = itemView.findViewById(R.id.event_time);

        }


    }

}
