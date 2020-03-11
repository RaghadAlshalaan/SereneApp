package com.ksu.serene.controller.main.drafts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.R;
import com.ksu.serene.model.VoiceDraft;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceDraftAdapter extends RecyclerView.Adapter<VoiceDraftAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<VoiceDraft> voiceDrafts;
    private View view;
    private VoiceDraftAdapter.MyViewHolder holder;
    private RelativeLayout relativeLayout;
    private VoiceDraft mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private int position;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public VoiceDraftAdapter(Context context, List<VoiceDraft> voiceDrafts) {
        this.context = context;
        this.voiceDrafts = voiceDrafts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VoiceDraftAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.text_draft_row, parent, false);
        holder = new VoiceDraftAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VoiceDraft voiceDraft = voiceDrafts.get(position);
        holder.title.setText(voiceDrafts.get(position).getTitle());
        holder.date.setText(voiceDrafts.get(position).getDate());

        // here to set the item view
    }

    @Override
    public int getItemCount() {
        return voiceDrafts.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder {
        public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        private TextView title;
        private TextView date;


        public MyViewHolder(View itemView) {
            super(itemView);

            // define variables, put extras,
            title = itemView.findViewById(R.id.text_title_name);
            date = itemView.findViewById(R.id.text_draft_sub);
            relativeLayout = itemView.findViewById(R.id.draft_item);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VoiceDraft voiceDraft = voiceDrafts.get(getAdapterPosition());
                    Intent intent = new Intent(context , VoiceDraftFragment.class);
                    CustomAudioDialogClass dialogClass = new CustomAudioDialogClass(context, voiceDraft ,intent);
                    dialogClass.dismiss();
                    dialogClass.show();
                    position = getAdapterPosition();
                }
            });


        }// MyViewHolder


    }//MyViewHolder class


}// class

