package com.ksu.serene.Controller.Homepage.Drafts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.Model.TextDraft;
import com.ksu.serene.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class allDraft extends Fragment {

    Context context = this.getContext();
    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewTextDraft;
    private RecyclerView.LayoutManager TextDraftlayoutManager;
    private List<TextDraft> listTextDrafts;
    private textDraftAdapter adapterTextDraft;
    private String TextID;
    private String TextTitle;
    private String TextDate;
    private String TextMessage;
    private String Texttimestap;
    private Date TDDate;

    public allDraft() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_all_draft, container, false);
        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //retrieve Patient Text draft data
        recyclerViewTextDraft = (RecyclerView) root.findViewById(R.id.Recyclerview_All_Draft);
        TextDraftlayoutManager = new LinearLayoutManager(context);
        recyclerViewTextDraft.setLayoutManager(TextDraftlayoutManager);
        listTextDrafts = new ArrayList<>();
        adapterTextDraft = new textDraftAdapter(listTextDrafts);
        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("TextDraft");
        final Query queryPatientTextDraft = referenceSession.whereEqualTo("patientID",patientId);
        queryPatientTextDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TextID = document.getId();
                        TextTitle = document.get("title").toString();
                        TextDate = document.get("date").toString();
                        TextMessage = document.get("text").toString();
                        Texttimestap = document.get("timestamp").toString();
                        try {
                            TDDate = DateFormat.parse(TextDate);
                        }
                        catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        listTextDrafts.add(new TextDraft(TextID, TextTitle, TDDate, TextMessage, Texttimestap));
                    }
                    adapterTextDraft.notifyDataSetChanged();
                }
            }
        });
        recyclerViewTextDraft.setAdapter(adapterTextDraft);

        return root;
    }

}
