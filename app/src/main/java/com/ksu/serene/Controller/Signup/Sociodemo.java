package com.ksu.serene.Controller.Signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.Model.Token;
import com.ksu.serene.R;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_spinner_dropdown_item;


public class Sociodemo extends Fragment {

    private EditText ageET, heightET, weightET, monthlyIncomeET, chronicDiseaseET;
    private Spinner employmentStatusET, maritalStatusET,cigaretteSmokeET;
    private String age, height, weight, employmentStatus, maritalStatus, monthlyIncome, cigaretteSmoke, chronicDisease, fullName, email, GAD7Scalescore,password;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean flag;
    private Button next;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_sociodemo, container, false);


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


        ageET.addTextChangedListener(signUpTextWatcher);
        heightET.addTextChangedListener(signUpTextWatcher);
        weightET.addTextChangedListener(signUpTextWatcher);
        monthlyIncomeET.addTextChangedListener(signUpTextWatcher);
        chronicDiseaseET.addTextChangedListener(signUpTextWatcher);

        // TODO : SAVE STATUS IN SHARED PREF

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(uploadSocio()){*/

                age = ageET.getText().toString();
                height = heightET.getText().toString();
                weight = weightET.getText().toString();
                monthlyIncome = monthlyIncomeET.getText().toString();
                chronicDisease = chronicDiseaseET.getText().toString();

                //Fields Validations

                if( height.matches("") || weight.matches("") || employmentStatus.matches("") || maritalStatus.matches("") ||
                        monthlyIncome.matches("") || cigaretteSmoke.matches("") || age.matches("") || chronicDisease.matches("")) {

                    Toast.makeText(getActivity(), "All questions are required",
                            Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if(!age.matches("^[0-9]+")|| !height.matches("^[0-9]+")|| !weight.matches("^[0-9]+")||
                        !monthlyIncome.matches("^[0-9]+")){

                    Toast.makeText(getActivity(), "Please enter valid age value",
                            Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if(!chronicDisease.matches("^[ A-Za-z]+$")){

                    Toast.makeText(getActivity(), "Only letters in chronic disease field",
                            Toast.LENGTH_SHORT).show();
                    return ;
                }


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

                                            if(document.exists()) {


                                                String id = document.getId();

                                                db.collection("Patient")
                                                        .document(id).update(userSocio);


                                            }

                                        }

                                        Fragment fragmentGAD = new GAD7();

                                        FragmentManager fm = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                        fragmentTransaction.replace(R.id.layout, fragmentGAD);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();


                                    } else {

                                        Toast.makeText(getActivity(), "Error getting documents: ",
                                                Toast.LENGTH_SHORT).show();
                                        flag = false;
                                    }
                                }
                            }
                        });





             /*   else{

                    Toast.makeText(getActivity(), "WRONG",
                            Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        return view;
    }


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

            // TODO : OF ALL ANSWERED ENABLE NEXT
            // TODO : ADD ALL FIELD REQUIRED NOTE
            // TODO : ADD FRAGMENT NUMBER TO LAYOUT
            // TODO : SAVE FRAGMENT STATUS ON DB
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    public boolean uploadSocio() {

        flag = false;
        /*age = ageET.getText().toString();
        height = heightET.getText().toString();
        weight = weightET.getText().toString();
        monthlyIncome = monthlyIncomeET.getText().toString();
        chronicDisease = chronicDiseaseET.getText().toString();

        //Fields Validations

        if( height.matches("") || weight.matches("") || employmentStatus.matches("") || maritalStatus.matches("") ||
                monthlyIncome.matches("") || cigaretteSmoke.matches("") || age.matches("") || chronicDisease.matches("")) {

            Toast.makeText(getActivity(), "All questions are required",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        else if(!this.age.matches("^[0-9]+")|| !height.matches("^[0-9]+")|| !weight.matches("^[0-9]+")||
                !monthlyIncome.matches("^[0-9]+")){

            Toast.makeText(getActivity(), "Please enter valid age value",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        else if(!chronicDisease.matches("^[ A-Za-z]+$")){

            Toast.makeText(getActivity(), "Only letters in chronic disease field",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }


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

                                    if(document.exists()){


                                    String id = document.getId();

                                    db.collection("Patient")
                                            .document(id).update(userSocio);


                                        flag = true;
                                }



                                }
                            } else {

                                Toast.makeText(getActivity(), "Error getting documents: ",
                                        Toast.LENGTH_SHORT).show();
                                flag = false;
                            }
                        }
                    }
                });*/

        return flag;
    }


    /*private EditText ageET, heightET, wieghtET, monthlyIncomeET, chronicDiseaseET;
    private Spinner employmentStatusET, maritalStatusET,cigaretteSmokeET;
    private String age, height, wieght, employmentStatus, maritalStatus, monthlyIncome, cigaretteSmoke, chronicDisease, fullName, email, GAD7Scalescore,password;
    private String TAG = Sociodemo.class.getSimpleName();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button next;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if(bundle != null){
            fullName = getArguments().getString("fullName");
            email = getArguments().getString("email");
            password = getArguments().getString("password");
            GAD7Scalescore = getArguments().getString("GAD7Scalescore");
        }



        View view = inflater.inflate(R.layout.activity_sociodemo, container, false);

        ageET = view.findViewById(R.id.age);
        heightET = view.findViewById(R.id.height);
        wieghtET = view.findViewById(R.id.weight);
        employmentStatusET = (Spinner) view.findViewById(R.id.employee);
        maritalStatusET = view.findViewById(R.id.married);
        monthlyIncomeET = view.findViewById(R.id.income);
        cigaretteSmokeET = view.findViewById(R.id.smoke);
        chronicDiseaseET = view.findViewById(R.id.chronic);
        next = view.findViewById(R.id.button);

        // initiate the spinner 1
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.employmentStatus, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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





        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                age = ageET.getText().toString();
                height = heightET.getText().toString();
                wieght = wieghtET.getText().toString();
                monthlyIncome = monthlyIncomeET.getText().toString();
                chronicDisease = chronicDiseaseET.getText().toString();

                //make sure the fields are not empty
                if(age.matches("") || height.matches("")  || wieght.matches("")|| employmentStatus.matches("") ||
                        maritalStatus.matches("") || monthlyIncome.matches("") ||
                        cigaretteSmoke.matches("") || chronicDisease.matches("")){
                    Toast.makeText(getActivity(), R.string.questionnaire,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!age.matches("^[0-9]+")|| !height.matches("^[0-9]+")|| !wieght.matches("^[0-9]+")||
                        !monthlyIncome.matches("^[0-9]+")){
                    Toast.makeText(getActivity(), R.string.incomeFormat,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!chronicDisease.matches("^[ A-Za-z]+$")){
                    Toast.makeText(getActivity(), R.string.nameFormat,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //connect to firebase and create user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userf = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                            userf.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            userf.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                            }
                                        }
                                    });
                            // Create a new user
                            final Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", fullName);
                            user.put("age", age);
                            user.put("GAD-7ScaleScore", GAD7Scalescore);
                            user.put("chronicDiseases", chronicDisease);
                            user.put("employmentStatus", employmentStatus);
                            user.put("height", height);
                            user.put("maritalStatus", maritalStatus);
                            user.put("monthlyIncome", monthlyIncome);
                            user.put("smokeCigarettes", cigaretteSmoke);
                            user.put("weight", wieght);

                            // Add a new document with a generated ID
                            db.collection("Patient")
                                    .document(mAuth.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot added with");
                                            //add token
                                            Token mToken = new Token("");

                                            // Add a new document with a generated ID
                                            db.collection("Tokens")
                                                    .document(mAuth.getUid())
                                                    .set(mToken)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot added with");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error writing document", e);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });


                            sendVerificationEmail();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(Sociodemo.this, "Authentication failed.",
                           //         Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

            }
        });


        return view;
    }


    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            //showDialog("Successfully create account, we have send to your email verification link to active your account. ");
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Log.d(TAG, "there is problem the email doesn't send: ");

                        }
                    }
                });
    }*/
    // we will use it later

    /* public void showDialog(String msg) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // after email is sent just logout the user and finish this activity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Signup.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.show();
    }//end showDialog() */
}
