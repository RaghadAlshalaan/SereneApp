package com.ksu.serene.Controller.Homepage.Drafts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.Model.TextDraft;
import com.ksu.serene.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<TextDraft> textDrafts;
    private View view;
    private TextAdapter.MyViewHolder holder;
    private RelativeLayout relativeLayout;
    private TextDraft mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private int position;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public TextAdapter(Context context, List<TextDraft> textDrafts) {
        this.context = context;
        this.textDrafts = textDrafts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TextAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.text_draft_raw, parent, false);
        holder = new TextAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(textDrafts.get(position).getTitle());
        holder.date.setText(textDrafts.get(position).getDate());

        // here to set the item view
    }

    @Override
    public int getItemCount() {
        return textDrafts.size();
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

                    TextDraft textDraft = textDrafts.get(getAdapterPosition());
                    position = getAdapterPosition();
                    //todo: go to view text draft
                    Intent intent = new Intent(context , TextDraftView.class);
                    intent.putExtra("title" , textDrafts.get(position).getTitle());
                    intent.putExtra("message" , textDrafts.get(position).getMessage());
                    context.startActivity(intent);

                }//onClick
            });
        }// MyViewHolder
    }//MyViewHolder class


    public void deleteDraft(String id) {

        firebaseFirestore.collection("TextDraft").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        removeAt(position);
    }// deleteDraft

    public void removeAt(int position) {
        textDrafts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, textDrafts.size());
    }// removeAt



}// class
