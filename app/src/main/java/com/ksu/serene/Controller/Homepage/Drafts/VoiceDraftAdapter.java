package com.ksu.serene.Controller.Homepage.Drafts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ksu.serene.Model.VoiceDraft;
import com.ksu.serene.R;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceDraftAdapter extends RecyclerView.Adapter<VoiceDraftAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(VoiceDraft item);
    }

    private List<VoiceDraft> mAdapter;
    //private final OnItemClickListener listener;

    public VoiceDraftAdapter(List<VoiceDraft> mAdapter){//, OnItemClickListener listener) {
        //super(options);
        this.mAdapter = mAdapter;
        //this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Title;
        TextView Subj;

        public MyViewHolder (View v) {
            super(v);
            Title = (TextView) itemView.findViewById(R.id.text_title_name);
            Subj = (TextView) itemView.findViewById(R.id.text_draft_sub);
        }

        public void bind (final VoiceDraft voiceDraft , final VoiceDraftAdapter.OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(voiceDraft);
                }
            });
        }
    }

    @NonNull
    @Override
    public VoiceDraftAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_draft_raw , parent , false);
        return new VoiceDraftAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoiceDraftAdapter.MyViewHolder holder, int position) {
        VoiceDraft voiceDraft = mAdapter.get(position);
        holder.Title.setText(voiceDraft.getTitle());
        holder.Subj.setText(voiceDraft.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }


}

