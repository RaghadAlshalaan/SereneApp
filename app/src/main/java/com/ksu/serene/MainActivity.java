package com.ksu.serene;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ksu.serene.controller.fitbitDataWorker.FitbitWorker;
import com.ksu.serene.controller.main.profile.PatientProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public ImageView profile;
    private RelativeLayout w1, w2;
    private ImageView ok1, ok2;
    private LinearLayout overbox;
    private Animation from_small, from_nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

/*        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));*/

        init();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_report, R.id.navigation_drafts, R.id.navigation_calendar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        if(getExtras().equals("1")){

            overbox.setAlpha(1);
            overbox.startAnimation(from_nothing);

            w1.setAlpha(1);
            w1.startAnimation(from_small);

        }else{
            w1.setVisibility(View.GONE);
            w2.setVisibility(View.GONE);
            overbox.setVisibility(View.GONE);
        }


        profile = findViewById(R.id.profile_icon);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentOne = new PatientProfile();

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentOne);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                profile.setVisibility(View.GONE);
            }
        });


    }// end onCreate()

    private void init() {

        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        overbox = findViewById(R.id.overbox);
        ok1 = findViewById(R.id.ok1);
        ok2 = findViewById(R.id.ok2);
        from_small = AnimationUtils.loadAnimation(this, R.anim.from_small);
        from_nothing = AnimationUtils.loadAnimation(this, R.anim.from_nothing);

        ok1.setOnClickListener(this);
        ok2.setOnClickListener(this);

        w1.setAlpha(0);
        w2.setAlpha(0);
        overbox.setAlpha(0);

    }

    private String getExtras() {

        if (getIntent().getExtras() != null) {

            String step = getIntent().getExtras().getString("first");
            return step;
        }

        return "0";
    }


    @Override
    public void onClick(View v) {

        switch ( v.getId() ){
            case R.id.ok1 :
                w1.setVisibility(View.GONE);

                w2.setAlpha(1);
                w2.startAnimation(from_nothing);
                break;

            case R.id.ok2 :
                w2.setVisibility(View.GONE);
                overbox.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        profile.setVisibility(View.VISIBLE);
    }
}
