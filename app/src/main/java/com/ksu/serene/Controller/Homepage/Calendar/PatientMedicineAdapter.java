package com.ksu.serene.Controller.Homepage.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksu.serene.Model.Medicine;
import com.ksu.serene.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientMedicineAdapter  extends RecyclerView.Adapter<PatientMedicineAdapter.MyViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(Medicine item);
    }

    private List<Medicine> mAdapter;
    private final OnItemClickListener listener;

    public PatientMedicineAdapter(List<Medicine> mAdapter, OnItemClickListener listener) {
        //super(options);
        this.mAdapter = mAdapter;
        this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;
        TextView date; //month then day
        TextView time; //time then am or pm

        public MyViewHolder (View v) {
            super(v);
            medicineName = (TextView) itemView.findViewById(R.id.medicine_name);
            date = (TextView) itemView.findViewById(R.id.medicine_period);
            time = (TextView) itemView.findViewById(R.id.medicine_time);
        }

        public void bind (final Medicine medicine , final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(medicine);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_row , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mAdapter.get(position), listener);
        Medicine medicine = mAdapter.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.date.setText(medicine.getDay()+" ");
        holder.time.setText(medicine.getTime()+" ");
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }


}
