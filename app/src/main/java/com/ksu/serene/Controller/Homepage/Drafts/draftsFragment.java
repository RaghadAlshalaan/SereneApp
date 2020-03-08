package com.ksu.serene.Controller.Homepage.Drafts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.ksu.serene.R;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class draftsFragment extends Fragment {
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_drafts, container, false);

        ViewPager viewPager = root.findViewById(R.id.ViewPagerDraft);
        //setUpViewPager
        draftPageAdpater pageAdpater = new draftPageAdpater(getChildFragmentManager());
        pageAdpater.addFragment(new allDraft() , "ALL");
        pageAdpater.addFragment(new TextDraftFragment() , "TEXT");
        pageAdpater.addFragment(new VoiceDraftFragment() , "VOICE");
        viewPager.setAdapter(pageAdpater);

        TabLayout tabs = root.findViewById(R.id.TabLayoutDraft);
        tabs.setupWithViewPager(viewPager);



        installButton110to250();

        return root;
    }//onCreate()


    private void installButton110to250() {

        final AllAngleExpandableButton button = (AllAngleExpandableButton) root.findViewById(R.id.button_expandable_110_250);
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
    }



}