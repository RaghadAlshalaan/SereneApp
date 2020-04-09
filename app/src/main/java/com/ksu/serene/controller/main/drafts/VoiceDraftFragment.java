package com.ksu.serene.controller.main.drafts;


import android.content.Context;
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
import com.ksu.serene.model.VoiceDraft;
import com.ksu.serene.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class VoiceDraftFragment extends Fragment {

    Context context = this.getContext();
    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewDraft;
    private RecyclerView.LayoutManager layoutManager;
    private List<VoiceDraft> listVoiceDrafts;
    public VoiceDraftAdapter adapterVoiceDraft;
    private String VoiceID;
    private String VoiceTitle;
    private String VoiceDate;
    private String VoiceAudio;
    private Timestamp Voicetimestap;
    private View root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_voice_draft, container, false);
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        layoutManager = new LinearLayoutManager(root.getContext());
        listVoiceDrafts = new ArrayList<>();
        adapterVoiceDraft = new VoiceDraftAdapter(getContext() ,listVoiceDrafts);

        recyclerViewDraft = root.findViewById(R.id.Recyclerview_Voice_Draft);
        recyclerViewDraft.setLayoutManager(layoutManager);

        recyclerViewDraft.setAdapter(adapterVoiceDraft);

        return root;
    }// onCreateView()

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
//        listVoiceDrafts.clear();
        retrieveVoiceDraft();
    }// onResume


}// class
