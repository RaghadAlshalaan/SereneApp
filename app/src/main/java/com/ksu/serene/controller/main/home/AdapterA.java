package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
import com.ksu.serene.model.Location;
import com.ksu.serene.R;
import com.ksu.serene.model.TherapySession;

import java.util.List;

public class AdapterA extends RecyclerView.Adapter<AdapterA.ViewHolder> {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<TherapySession> sessions;

    AdapterA(Context context, List<TherapySession> sessions){

        this.layoutInflater = LayoutInflater.from(context);
        this.sessions = sessions;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.session_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //bind the textviews with locations received

        String doctorName = sessions.get(i).getName();
        viewHolder.doctor_name.setText(doctorName);

        String date = sessions.get(i).getDay();
        viewHolder.session_date.setText(date);

        String time = sessions.get(i).getTime();
        viewHolder.session_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView doctor_name, session_date, session_time;


        public ViewHolder(View itemView) {
            super(itemView);
            doctor_name = (TextView)itemView.findViewById(R.id.doctor_name);
            session_date = (TextView)itemView.findViewById(R.id.session_date);
            session_time = (TextView)itemView.findViewById(R.id.session_time);
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent( view.getContext() , PatientAppointmentDetailPage.class);
            intent.putExtra("AppointmentID" , itemView.getId());
            mContext.startActivity(intent);
        }
    }
}

