package com.ksu.serene.controller.main.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.R;
import com.ksu.serene.model.Appointment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PatientAppointmentsAdapter extends
        RecyclerView.Adapter<PatientAppointmentsAdapter.ViewHolder>  {
    private List<Appointment> appointments;
    private final Context mContext;

    public PatientAppointmentsAdapter(Context context, List<Appointment> apps) {
        this.mContext = context;
        appointments = apps;

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.appointments_row, viewGroup, false);
        return new ViewHolder(itemView);
    }


    @Override
    public int getItemCount() {

        return appointments.size();
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int pos) {
        final Appointment appointment = appointments.get(pos);
        final ViewHolder genericViewHolder = holder;

        genericViewHolder.textViewAppointment.setText(appointment.getSummary());
        genericViewHolder.textViewStartTime.setText(appointment.getStartTime());

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewAppointment;
        private TextView textViewStartTime;

        ViewHolder(View v) {
            super(v);
            initView(v);
        }

        private void initView(@NonNull final View itemView) {
            textViewAppointment = itemView.findViewById(R.id.text_view_appointment);
            textViewStartTime = itemView.findViewById(R.id.text_view_start_time);

        }

    }



}