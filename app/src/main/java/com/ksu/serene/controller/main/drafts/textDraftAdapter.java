package com.ksu.serene.controller.main.drafts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksu.serene.model.TextDraft;
import com.ksu.serene.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class textDraftAdapter extends RecyclerView.Adapter<textDraftAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(TextDraft item);
    }

    private List<TextDraft> mAdapter;
    private final OnItemClickListener listener;

    public textDraftAdapter(List<TextDraft> mAdapter, OnItemClickListener listener) {
        //super(options);
        this.mAdapter = mAdapter;
        this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Title;
        TextView Subj;
        TextView date;
        TextView time;

        public MyViewHolder (View v) {
            super(v);
            Title = itemView.findViewById(R.id.text_title_name);
            Subj = itemView.findViewById(R.id.text_draft_sub);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }

        public void bind (final TextDraft textDraft , final textDraftAdapter.OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(textDraft);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_draft_row, parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull textDraftAdapter.MyViewHolder holder, int position) {
        holder.bind(mAdapter.get(position) , listener);
        TextDraft textDraft = mAdapter.get(position);
        holder.Title.setText(textDraft.getTitle());
        if(textDraft.getMessage().length() > 20)
            holder.Subj.setText(textDraft.getMessage().subSequence(0,20)+"...");
        else
            holder.Subj.setText(textDraft.getMessage());
        holder.date.setText(textDraft.getDate());
        holder.time.setText(textDraft.getTimestap());
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }


}

