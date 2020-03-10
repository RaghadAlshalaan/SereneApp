package com.ksu.serene.controller.main.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksu.serene.model.TherapySession;
import com.ksu.serene.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientSessionsAdapter  extends RecyclerView.Adapter<PatientSessionsAdapter.MyViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(TherapySession item);
    }

    private List<TherapySession> mAdapter;
    private final OnItemClickListener listener;

    public PatientSessionsAdapter(List<TherapySession> mAdapter, OnItemClickListener listener) {
        //super(options);
        this.mAdapter = mAdapter;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName;
        TextView date; //month then day
        TextView time; //time then am or pm

        public MyViewHolder (View v) {
            super(v);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_name);
            date = (TextView) itemView.findViewById(R.id.session_date);
            time = (TextView) itemView.findViewById(R.id.session_time);
        }

        public void bind (final TherapySession session , final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(session);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_row , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mAdapter.get(position) , listener);
        TherapySession session = mAdapter.get(position);
        holder.doctorName.setText(session.getName());
        holder.date.setText(session.getDay()+" ");
        holder.time.setText(session.getTime()+" ");
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }
}

