package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.R;
import com.ksu.serene.controller.Reminder.Notification;
import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
import com.ksu.serene.controller.main.calendar.PatientMedicineDetailPage;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private static List<Notification> notifications;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView reminder_name, reminder_message, reminder_time;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);

            //Id = id;
            reminder_name =  view.findViewById(R.id.reminder_name);
            reminder_message = view.findViewById(R.id.reminder_message);
            reminder_time = view.findViewById(R.id.reminder_time);

            card = view.findViewById(R.id.notification_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //go to appropriate detail page by finding type & passing doc id
            Log.d("TAG", "notification clicked");
            String documentKey="";
            notifications.get(getAdapterPosition()).setRead(true);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Notifications").document( notifications.get(getAdapterPosition()).getNotificationID())
                    .update(
                            "read", true
                    );

            //navigate to item's detail page
            Intent intent = new Intent();
            switch (notifications.get(getAdapterPosition()).getType()) {
                case "med":
                    intent = new Intent(view.getContext(), PatientMedicineDetailPage.class);
                    documentKey = "MedicineID";
                    break;
                case "app":
                    intent = new Intent(view.getContext(), PatientAppointmentDetailPage.class);
                    documentKey = "AppointmentID";
                    break;
            }
            intent.putExtra(documentKey, notifications.get(getAdapterPosition()).getDocumentID());
            mContext.startActivity(intent);

        }

    }//End MyViewHolder Class


    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
        notifications = new ArrayList<Notification>(5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Notification notification = notifications.get(position);
        holder.reminder_name.setText(notification.getName());
        holder.reminder_message.setText(notification.getMessage());
        holder.reminder_time.setText(notification.getTime());
        if (notification.isRead())
            holder.itemView.findViewById(R.id.unread_yellow_bg).setVisibility(View.GONE);
        else
            holder.itemView.findViewById(R.id.unread_yellow_bg).setVisibility(View.VISIBLE);//unread


    }


    @Override
    public int getItemCount() {
        if (notifications!=null)
            return notifications.size();
        else return 0;
    }

    public void updateList(List<Notification> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No notifications found", Toast.LENGTH_SHORT).show();
            notifications = new ArrayList<Notification>();
            notifyDataSetChanged();
        }else{
            notifications = new ArrayList<Notification>();
            notifications.addAll(list);
            notifyDataSetChanged();
        }
    }


    public void updateView(){
        notifyDataSetChanged();
    }


}
