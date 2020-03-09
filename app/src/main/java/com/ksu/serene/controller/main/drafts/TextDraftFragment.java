package com.ksu.serene.controller.main.drafts;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.model.TextDraft;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextDraftFragment extends Fragment {

    Context context = this.getContext();
    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewDraft;
    private RecyclerView.LayoutManager layoutManager;
    private List<TextDraft> listTextDrafts;
    private TextAdapter adapterTextDraft;
    private String TextID;
    private String TextTitle;
    private String TextDate;
    private String TextMessage;
    private Timestamp Texttimestap;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_text_draft, container, false);
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        layoutManager = new LinearLayoutManager(root.getContext());
        listTextDrafts = new ArrayList<>();
        adapterTextDraft = new TextAdapter(getContext() ,listTextDrafts);
        recyclerViewDraft = root.findViewById(R.id.Recyclerview_Text_Draft);
        recyclerViewDraft.setLayoutManager(layoutManager);
        recyclerViewDraft.setAdapter(adapterTextDraft);

        return root;
    }//onCreate()

    @Override
    public void onResume() {
        super.onResume();
        listTextDrafts.clear();
        retrieveTextDraft();
    }// onResume


    private void retrieveTextDraft() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("TextDraft");
        final Query queryPatientDraft = reference.whereEqualTo("patinetID",patientId);
        queryPatientDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        TextID = document.getId();
                        TextTitle = document.get("title").toString();
                        TextMessage = document.get("text").toString();
                        Texttimestap = (Timestamp) document.get("timestamp");
                        TextDate = getDateFormat(Texttimestap);
                        listTextDrafts.add(new TextDraft(TextID, TextTitle, TextDate, TextMessage));
                        adapterTextDraft.notifyDataSetChanged();

                    }// for
                }// if
            }// onComplete
        });

    }// retrieveVoiceDraft

    private String getDateFormat(Timestamp timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getSeconds()*1000);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return mDay+"/"+mMonth+"/"+mYear;
    }// getDateFormat


}
