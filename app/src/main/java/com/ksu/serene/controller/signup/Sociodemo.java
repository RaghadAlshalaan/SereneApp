package com.ksu.serene.controller.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.R;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_spinner_dropdown_item;


public class Sociodemo extends Fragment {

    private EditText ageET, heightET, weightET, monthlyIncomeET, chronicDiseaseET;
    private Spinner employmentStatusET, maritalStatusET,cigaretteSmokeET;
    private String age, height, weight, employmentStatus, maritalStatus, monthlyIncome, cigaretteSmoke, chronicDisease, fullName, email, GAD7Scalescore,password;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean flag = true;
    private Button next;
    private int step = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sociodemo, container, false);


        init(view);


        ageET.addTextChangedListener(signUpTextWatcher);
        heightET.addTextChangedListener(signUpTextWatcher);
        weightET.addTextChangedListener(signUpTextWatcher);
        monthlyIncomeET.addTextChangedListener(signUpTextWatcher);
        chronicDiseaseET.addTextChangedListener(signUpTextWatcher);


        // TODO : DO SP
        // TODO : SAVE FRAGMENT STATUS ON DB

/*        updateToken(FirebaseInstanceId.getInstance().getToken());
        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
        editor.apply();*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields()){

                    // Upload socio data to user document in DB
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String userEmail = user.getEmail();

                    final Map<String, Object> userSocio = new HashMap<>();
                    userSocio.put("age", age);
                    userSocio.put("chronicDiseases", chronicDisease);
                    userSocio.put("employmentStatus", employmentStatus);
                    userSocio.put("height", height);
                    userSocio.put("maritalStatus", maritalStatus);
                    userSocio.put("monthlyIncome", monthlyIncome);
                    userSocio.put("smokeCigarettes", cigaretteSmoke);
                    userSocio.put("weight", weight);

                    db.collection("Patient").whereEqualTo("email", userEmail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            for (DocumentSnapshot document : task.getResult()) {

                                                String id = document.getId();

                                                db.collection("Patient")
                                                        .document(id).update(userSocio)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //added this toast needed in test
                                                        Toast.makeText(getActivity(), R.string.SocioSuccess,
                                                                Toast.LENGTH_SHORT).show();

                                                        GAD7 fragmentGAD = new GAD7();
                                                        FragmentManager fm = getFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                                        fragmentTransaction.replace(R.id.qContainer, fragmentGAD);
                                                        fragmentTransaction.addToBackStack(null);
                                                        fragmentTransaction.commit();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), R.string.SocioFialed,
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        } else {

                                            Toast.makeText(getActivity(), R.string.SocioFialed,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });

                }

            }
        }); // end onClick next button


        return view;

    }// end onCreate

    private void init(View view) {

        ageET = view.findViewById(R.id.a1);
        heightET = view.findViewById(R.id.a2);
        weightET = view.findViewById(R.id.a3);
        employmentStatusET = view.findViewById(R.id.a6);
        maritalStatusET = view.findViewById(R.id.a5);
        monthlyIncomeET = view.findViewById(R.id.a4);
        cigaretteSmokeET = view.findViewById(R.id.a7);
        chronicDiseaseET = view.findViewById(R.id.a8);
        next = view.findViewById(R.id.nextBtn);

        // initiate the spinner 1
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.employmentStatus, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        employmentStatusET.setAdapter(adapter);
        employmentStatusET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                employmentStatus = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // initiate the spinner 2
        ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(getContext(),
                R.array.yes_no, android.R.layout.simple_spinner_item);

        adapterM.setDropDownViewResource(simple_spinner_dropdown_item);
        maritalStatusET.setAdapter(adapterM);
        maritalStatusET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                maritalStatus = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // initiate the spinner 3
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(getContext(),
                R.array.yes_no, android.R.layout.simple_spinner_item);

        adapterS.setDropDownViewResource(simple_spinner_dropdown_item);
        cigaretteSmokeET.setAdapter(adapterS);
        cigaretteSmokeET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cigaretteSmoke = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }// end init


    private TextWatcher signUpTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            age = ageET.getText().toString().trim();
            height = heightET.getText().toString().trim();
            weight = weightET.getText().toString().trim();
            monthlyIncome = monthlyIncomeET.getText().toString().trim();
            chronicDisease = chronicDiseaseET.getText().toString().trim();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    public boolean checkFields() {

        flag = false;

        age = ageET.getText().toString();
        height = heightET.getText().toString();
        weight = weightET.getText().toString();
        monthlyIncome = monthlyIncomeET.getText().toString();
        chronicDisease = chronicDiseaseET.getText().toString();

        /*if( heightET.getText().toString().matches("") || weightET.getText().toString().matches("") || employmentStatus.matches("") || maritalStatus.matches("") ||
                monthlyIncomeET.getText().toString().matches("") || cigaretteSmoke.matches("") || ageET.getText().toString().matches("") || chronicDiseaseET.getText().toString().matches("")) {

            Toast.makeText(getActivity(), R.string.EmptyFields,Toast.LENGTH_LONG).show();
            return flag;

        }*/
        if (! checkSocioFields (age,height,weight,monthlyIncome,chronicDisease
                ,employmentStatus,maritalStatus,cigaretteSmoke) ) {
            Toast.makeText(getActivity(), R.string.EmptyFields,Toast.LENGTH_LONG).show();
            return false;
        }

        // Fields Validations
        double ageI = Double.parseDouble(age);
        if ( ! isValidAge(ageI) ) {
            Toast.makeText(getActivity(), R.string.NotValidAge,Toast.LENGTH_LONG).show();
            return flag;
        }
        /*if ((ageI > 110) || (ageI < 5)){
        }*/

        double heightI = Double.parseDouble(height);
        if (!isValidHeight(heightI)){
            Toast.makeText(getActivity(), R.string.NotValidHeight,Toast.LENGTH_LONG).show();
            return flag;
        }
        /*if ((heightI > 300) || (heightI < 50)){
        }*/

        double weightI = Double.parseDouble(weight);
        if (!isValidWeight(weightI)){
            Toast.makeText(getActivity(), R.string.NotValidWeight,Toast.LENGTH_LONG).show();
            return flag;
        }
        /*if ((weightI > 300) || (weightI < 20)){
        }*/

        double monthlyIncomeI = Double.parseDouble(monthlyIncome);
        if (!isValidMonthlyIncome(monthlyIncomeI)){
            Toast.makeText(getActivity(), R.string.NotValidMI,Toast.LENGTH_LONG).show();
            return flag;
        }
        /*if ((monthlyIncomeI > 5000000) || (monthlyIncomeI < 0)){
        }*/

        // CHECK CHRONIC DISEASE
        if (!isValidChronicDisease(chronicDisease)){
            Toast.makeText(getActivity(), R.string.NotValidCD,Toast.LENGTH_LONG).show();
            return flag;
        }
        /*if( !chronicDisease.matches("^[ A-Za-z]+$")){
        }*/

        return true;
    }

    public boolean checkSocioFields (String age, String height, String weight
            , String monthlyIncome, String chronicDiseases, String employmentStatus
            ,String maritalStatus,String cigaretteSmoke) {
        if( age.matches("") || height.matches("") || weight.matches("")
                || monthlyIncome.matches("") || chronicDiseases.matches("")
                    || employmentStatus.matches("") || maritalStatus.matches("")
                 || cigaretteSmoke.matches("")) {
            return false;
        }
        return true;
    }

    //TODO add the methods check for age height,weight, MI, and CD validation
    public boolean isValidAge (double age) {
        if ( (age > 110) || (age < 5) ) {
            return false;
        }
        return true;
    }

    public boolean isValidHeight (double height) {
        if ( (height > 300) || (height < 50) ) {
            return false;
        }
        return true;
    }

    public boolean isValidWeight (double weight) {
        if ( (weight > 300) || (weight < 20) ) {
            return false;
        }
        return true;
    }

    public boolean isValidMonthlyIncome (double MI) {
        if ( (MI > 5000000) || (MI < 0) ) {
            return false;
        }
        return true;
    }

    public boolean isValidChronicDisease (String CD) {
        if ( !CD.matches("^[ A-Za-z]+$") ) {
            return false;
        }
        return true;
    }

}
