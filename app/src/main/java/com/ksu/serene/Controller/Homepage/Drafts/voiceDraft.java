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
import com.ksu.serene.Model.VoiceDraft;
import com.ksu.serene.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class voiceDraft extends Fragment {

    Context context = this.getContext();
    //set for patient's Text Draft
    private String patientId;
    private RecyclerView recyclerViewDraft;
    private RecyclerView.LayoutManager layoutManager;
    private List<VoiceDraft> listVoiceDrafts;
    private voiceDraftAdapter adapterVoiceDraft;
    private String VoiceID;
    private String VoiceTitle;
    private String VoiceDate;
    private String VoiceAudio;
    private String Voicetimestap;


    public voiceDraft() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_voice_draft, container, false);

        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return root;
    }

    private void SetAudioDraftRecyView (View root) {
        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        //retrieve Patient text draft data
        recyclerViewDraft = (RecyclerView) root.findViewById(R.id.Recyclerview_Voice_Draft);
        layoutManager = new LinearLayoutManager(context);
        recyclerViewDraft.setLayoutManager(layoutManager);
        listVoiceDrafts = new ArrayList<>();
        adapterVoiceDraft = new voiceDraftAdapter(listVoiceDrafts);

        //search in cloud firestore for patient sessions
        CollectionReference reference = FirebaseFirestore.getInstance().collection("AudioDraft");

        final Query queryPatientDraft = reference.whereEqualTo("patientID",patientId);
        queryPatientDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        VoiceID = document.getId().toString();
                        VoiceTitle = document.get("title").toString();
                        VoiceDate = document.get("day").toString();
                        VoiceAudio = document.get("audio").toString();
                        Voicetimestap = document.get("timestamp").toString();

                        listVoiceDrafts.add(new VoiceDraft(VoiceID, VoiceTitle, VoiceDate, VoiceAudio, Voicetimestap));

                    }
                    adapterVoiceDraft.notifyDataSetChanged();
                }
            }
        });
        recyclerViewDraft.setAdapter(adapterVoiceDraft);
    }

}
