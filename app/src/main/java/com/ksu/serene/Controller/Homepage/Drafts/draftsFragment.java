package com.ksu.serene.Controller.Homepage.Drafts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ksu.serene.R;

import com.google.android.material.tabs.TabLayout;

public class draftsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_drafts, container, false);

        ViewPager viewPager = root.findViewById(R.id.ViewPagerDraft);
        //setUpViewPager
        draftPageAdpater pageAdpater = new draftPageAdpater(getChildFragmentManager());
        pageAdpater.addFragment(new allDraft() , "ALL");
        pageAdpater.addFragment(new textDraft() , "TEXT");
        pageAdpater.addFragment(new voiceDraft() , "VOICE");
        viewPager.setAdapter(pageAdpater);

        TabLayout tabs = root.findViewById(R.id.TabLayoutDraft);
        tabs.setupWithViewPager(viewPager);
        return root;
    }
}