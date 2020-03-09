package com.ksu.serene.controller.main.drafts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.model.TextDraft;
import com.ksu.serene.model.VoiceDraft;
import com.ksu.serene.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

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

    private String TextID;
    private String TextTitle;
    private String TextDate;
    private String TextMessage;
    private Timestamp Texttimestap;
    private Date TDDate;
    private String VoiceID;
    private String VoiceTitle;
    private String VoiceDate;
    private String VoiceAudio;
    public FirebaseAuth mAuth;
    private Timestamp Voicetimestap;
    private List<VoiceDraft> listVoiceDrafts;
    public voiceDraftAdapter adapterVoiceDraft;
    public LinearLayoutManager layoutManagerVoice;
    public RecyclerView recyclerViewDraftVoice;

    public LinearLayoutManager layoutManagerText;
    public RecyclerView recyclerViewDraftText;
    public textDraftAdapter adapterTextDraft;
    private View root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root =  inflater.inflate(R.layout.fragment_all_draft, container, false);

         //voice
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        layoutManagerVoice = new LinearLayoutManager(root.getContext());
        listVoiceDrafts = new ArrayList<>();
        adapterVoiceDraft = new voiceDraftAdapter(getContext() ,listVoiceDrafts);
        recyclerViewDraftVoice = (RecyclerView) root.findViewById(R.id.Recyclerview_All_DraftVoice);
        recyclerViewDraftVoice.setLayoutManager(layoutManagerVoice);
        recyclerViewDraftVoice.setAdapter(adapterVoiceDraft);


        //text
        layoutManagerText = new LinearLayoutManager(root.getContext());
        listTextDrafts = new ArrayList<>();
        adapterTextDraft = new textDraftAdapter(listTextDrafts, new textDraftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TextDraft item) {
                Intent intent = new Intent(getContext(), PatientTextDraftDetailPage.class);
                intent.putExtra("TextDraftID",item.getId());
                startActivity(intent);
            }
        });
        recyclerViewDraftText = root.findViewById(R.id.Recyclerview_All_DraftText);
        recyclerViewDraftText.setLayoutManager(layoutManagerText);
        recyclerViewDraftText.setAdapter(adapterTextDraft);

        return root;
    }


    private void retrieveVoiceDraft() {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("AudioDraft");
        final Query queryPatientDraft = reference.whereEqualTo("patientID",patientId);
        queryPatientDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        VoiceID = document.getId();
                        VoiceTitle = document.get("title").toString();
                        VoiceAudio = document.get("audio").toString();
                        Voicetimestap = (Timestamp) document.get("timestamp");

                        VoiceDate = getDateFormat(Voicetimestap);
                        listVoiceDrafts.add(new VoiceDraft(VoiceID, VoiceTitle, VoiceDate, VoiceAudio));
                        adapterVoiceDraft.notifyDataSetChanged();

                    }// for
                }// if
            }// onComplete
        });
    }// retrieveVoiceDraft

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

    @Override
    public void onResume() {
        super.onResume();
        listVoiceDrafts.clear();
        listTextDrafts.clear();
        retrieveVoiceDraft();
        retrieveTextDraft();
    }// onResume

}
