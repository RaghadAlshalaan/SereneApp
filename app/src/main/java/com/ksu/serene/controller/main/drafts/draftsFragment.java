package com.ksu.serene.controller.main.drafts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ksu.serene.R;

import com.google.android.material.tabs.TabLayout;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class draftsFragment extends Fragment {

    private View root;
    //floating buttons
    private FloatingActionButton add, addVoice, addText;
    private Animation fabOpen, fabClose, rotateFor, rotateBac;
    private boolean isopen = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_drafts, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, getActivity());

        ViewPager viewPager = root.findViewById(R.id.ViewPagerDraft);

        //setUpViewPager
        draftPageAdpater pageAdpater = new draftPageAdpater(getChildFragmentManager());
        pageAdpater.addFragment(new allDraft() , getResources().getString(R.string.All));
        pageAdpater.addFragment(new TextDraftFragment() , getResources().getString(R.string.TEXT));
        pageAdpater.addFragment(new VoiceDraftFragment() , getResources().getString(R.string.VOICE));
        viewPager.setAdapter(pageAdpater);

        TabLayout tabs = root.findViewById(R.id.TabLayoutDraft);
        tabs.setupWithViewPager(viewPager);

        //dec floating buttons
        add = root.findViewById(R.id.button_expandable_110_250);
        addVoice = root.findViewById(R.id.AddVoiceButton);
        //by defual hide
        addVoice.hide();
        addText = root.findViewById(R.id.AddTextButton);
        addText.hide();
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateFor= AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);
        rotateBac= AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        addVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddVoiceDraftPage.class));
            }
        });
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddTextDraftPage.class));
            }
        });

        //installButton110to250();

        return root;

    }//onCreate()

    @Override
    public void onResume() {
        isopen = true;
        animateFab();
        addVoice.hide();
        addText.hide();
        super.onResume();
    }


    /*private void installButton110to250() {

        final AllAngleExpandableButton button = root.findViewById(R.id.button_expandable_110_250);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.ic_add_symbol ,R.drawable.ic_recorder_microphone, R.drawable.ic_text};
        int[] color = { R.color.colorAccent, R.color.colorAccent , R.color.colorAccent};

        for (int i = 0; i < 3; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 7);
            } else {
                buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 6);
            }
            buttonData.setBackgroundColorId(getContext(), color[i]);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);
        setListener(button);

    }


    private void setListener(final AllAngleExpandableButton button) {

        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1:
                       //add voice
                        startActivity(new Intent(getContext(), AddVoiceDraftPage.class));
                        break;

                    case 2:
                        //add text
                        startActivity(new Intent(getContext(), AddTextDraftPage.class));
                        break;
                }
            }

            @Override
            public void onExpand() {
            }

            @Override
            public void onCollapse() {
            }
        });
    }*/

    private void animateFab() {
        if (isopen){
            add.startAnimation(rotateFor);
            //addMed.startAnimation(fabClose);
            //addApp.startAnimation(fabClose);
            addVoice.setClickable(false);
            addText.setClickable(false);
            addVoice.hide();
            addText.hide();
            isopen = false;
        }
        else {
            add.startAnimation(rotateBac);
            addVoice.show();
            addText.show();
            addVoice.startAnimation(fabOpen);
            addText.startAnimation(fabOpen);
            addVoice.setClickable(true);
            addText.setClickable(true);
            isopen = true;
        }
    }

}