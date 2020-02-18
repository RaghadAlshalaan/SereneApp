package com.ksu.serene.Controller.Homepage.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.R;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;

    Adapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //bind the textviews with data received

        String location = data.get(i);
        viewHolder.location_name.setText(location);

        String level = data.get(i);
        viewHolder.location_AL.setText(level);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView location_name, location_AL;


        public ViewHolder(View itemView) {
            super(itemView);
            location_name = (TextView)itemView.findViewById(R.id.location_name);
            location_AL = (TextView)itemView.findViewById(R.id.location_AL);
        }
    }
}
