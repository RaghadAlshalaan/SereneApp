package com.ksu.serene.Controller.Homepage.Drafts;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class allDraft extends Fragment {


    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewSession;
    private List<TextDraft> listTextDrafts;
    private textDraftAdapter adapterTextDraft;//
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
        recyclerViewSession = (RecyclerView) root.findViewById(R.id.Recyclerview_All_Draft);
        listTextDrafts = new ArrayList<>();
        adapterTextDraft = new textDraftAdapter(listTextDrafts);
        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("textdraft");
        final Query queryPatientTextDraft = referenceSession.whereEqualTo("patientID",patientId);
        queryPatientTextDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TextDraft draft = document.toObject(TextDraft.class);
                        listTextDrafts.add(draft);
                    }
                    adapterTextDraft.notifyDataSetChanged();
                }
            }
        });
        recyclerViewSession.setAdapter(adapterTextDraft);

        return root;
    }

}