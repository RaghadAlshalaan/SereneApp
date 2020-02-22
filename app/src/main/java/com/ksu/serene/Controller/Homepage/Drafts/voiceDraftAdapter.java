package com.ksu.serene.Controller.Homepage.Drafts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksu.serene.Model.VoiceDraft;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class voiceDraftAdapter extends RecyclerView.Adapter<voiceDraftAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(VoiceDraft voiceDraft);
    }

    private List<VoiceDraft> mAdapter;
    //private final OnItemClickListener listener;

    public voiceDraftAdapter(List<VoiceDraft> mAdapter){//, OnItemClickListener listener) {
        //super(options);
        this.mAdapter = mAdapter;
        //this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Title;

        public MyViewHolder (View v) {
            super(v);
            Title = (TextView) itemView.findViewById(R.id.title_audio);
        }

        public void bind (final VoiceDraft voiceDraft , final voiceDraftAdapter.OnItemClickListener listener){
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_draft_row , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull voiceDraftAdapter.MyViewHolder holder, int position) {
        VoiceDraft textDraft = mAdapter.get(position);
        holder.Title.setText(textDraft.getTitle());
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }
}
