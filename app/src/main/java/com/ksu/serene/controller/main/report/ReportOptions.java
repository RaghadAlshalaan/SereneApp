package com.ksu.serene.controller.main.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ksu.serene.R;

import java.util.ArrayList;
import java.util.List;

public class ReportOptions extends RecyclerView.Adapter<ReportOptions.MyViewHolder>{


        private Context mContext;
        private static List<String> options;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView option_name;
            private CardView card;

            public MyViewHolder(View view) {
                super(view);

                option_name =  view.findViewById(R.id.option_name);
                card = view.findViewById(R.id.option_card);

                card.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                // TODO : PRINT & SHARE

            }

        }//End MyViewHolder Class


        public ReportOptions(Context mContext, String[] o) {
            this.mContext = mContext;
            options = new ArrayList<String>(2);
            options.add(o[0]);
            options.add(o[1]);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.three_dots, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String option = options.get(position);
            holder.option_name.setText(option);
        }


        @Override
        public int getItemCount() {
            if (options!=null)
                return options.size();
            else return 0;
        }

        public void updateList(List<String> list){
            if (list.isEmpty()){
                options = new ArrayList<String>();
                notifyDataSetChanged();
            }else{
                options = new ArrayList<String>();
                options.addAll(list);
                notifyDataSetChanged();
            }
        }


        public void updateView(){
            notifyDataSetChanged();
        }





}
