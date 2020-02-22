package com.ksu.serene.Controller.Homepage.Drafts;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.ksu.serene.Model.TextDraft;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class textDraft extends Fragment {

    Context context = this.getContext();
    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewDraft;
    private RecyclerView.LayoutManager layoutManager;
    private List<TextDraft> listTextDrafts;
    private textDraftAdapter adapterTextDraft;
    private String TextID;
    private String TextTitle;
    private String TextDate;
    private String TextMessage;
    private String Texttimestap;
    private Date TDDate;
    final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private Button add;

    public textDraft() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_text_draft, container, false);
        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        add = root.findViewById(R.id.Add_Text);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddTextDraftPage.class);
                startActivity(intent);
            }
        });

        SetDraftRecyView ( root);

        return root;
    }



    private void SetDraftRecyView (View root) {
        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        //retrieve Patient Session data
        recyclerViewDraft = (RecyclerView) root.findViewById(R.id.Recyclerview_Text_Draft);
        layoutManager = new LinearLayoutManager(context);
        recyclerViewDraft.setLayoutManager(layoutManager);
        listTextDrafts = new ArrayList<>();
        adapterTextDraft = new textDraftAdapter(listTextDrafts);

        //search in cloud firestore for patient sessions
        CollectionReference reference = FirebaseFirestore.getInstance().collection("TextDraft");

        final Query queryPatientDraft = reference.whereEqualTo("patientID",patientId);
        queryPatientDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TextID = document.getId().toString();
                        TextTitle = document.get("title").toString();
                        TextDate = document.get("day").toString();
                        TextMessage = document.get("text").toString();
                        Texttimestap = document.get("timestamp").toString();
                        //convert string to date to used in compare
                        try {
                            TDDate = DateFormat.parse(TextDate);
                        }
                        catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        listTextDrafts.add(new TextDraft(TextID, TextTitle, TextDate, TextMessage, Texttimestap));

                    }
                    adapterTextDraft.notifyDataSetChanged();
                }
            }
        });
        recyclerViewDraft.setAdapter(adapterTextDraft);
    }

}
