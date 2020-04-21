package com.ksu.serene.controller.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.model.MySharedPreference;
import com.ksu.serene.R;

public class EditSocio extends AppCompatActivity {
    private Button save;
    private EditText age, height, wieght, monthlyincome, chronicD;
    private String ageS, heightS, wieghtS, monthlyincomeS, chronicDS, employmentStatus, maritalStatus, cigaretteSmoke;
    private Spinner employmentStatusET, maritalStatusET,cigaretteSmokeET;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String TAG = EditSocio.class.getSimpleName();
    private boolean flag = false;
    private ImageView back;
    private ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(EditSocio.this,
            R.array.employmentStatus, android.R.layout.simple_spinner_item);
    private ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(EditSocio.this,
            R.array.yes_no, android.R.layout.simple_spinner_item);
    private ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(EditSocio.this,
            R.array.yes_no, android.R.layout.simple_spinner_item);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_socio);
        // Inflate the layout for this fragment
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        save = findViewById(R.id.button);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        wieght = findViewById(R.id.weight);
        monthlyincome = findViewById(R.id.income);
        chronicD = findViewById(R.id.chronic);
        employmentStatusET = (Spinner) findViewById(R.id.employee);
        maritalStatusET = findViewById(R.id.married);
        cigaretteSmokeET = findViewById(R.id.smoke);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


       // final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditSocio.this,R.array.employmentStatus, android.R.layout.simple_spinner_item);
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
        //final ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(EditSocio.this,R.array.yes_no, android.R.layout.simple_spinner_item);
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
        //final ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(EditSocio.this, R.array.yes_no, android.R.layout.simple_spinner_item);
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

        //TODO retrieve all fileds from firebase
        retrieveData ();
        /*db.collection("Patient")
                .document(mAuth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                age.setText(documentSnapshot.get("age").toString());
                height.setText(documentSnapshot.get("height").toString());
                wieght.setText(documentSnapshot.get("weight").toString());
                monthlyincome.setText(documentSnapshot.get("monthlyIncome").toString());
                chronicD.setText(documentSnapshot.get("chronicDiseases").toString());
                int posEmp = adapter.getPosition(documentSnapshot.get("employmentStatus").toString());
                employmentStatusET.setSelection(posEmp);
                int posMar = adapterM.getPosition(documentSnapshot.get("maritalStatus").toString());
                maritalStatusET.setSelection(posMar);
                int posSmok = adapterS.getPosition(documentSnapshot.get("smokeCigarettes").toString());
                cigaretteSmokeET.setSelection(posSmok);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditSocio.this, "Fails to get data", Toast.LENGTH_LONG).show();
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check all fields not empty
                //check the fields in valid format
                if (checkFields()) {
                    updateInfo(age.getText().toString(), height.getText().toString(), wieght.getText().toString(),
                            monthlyincome.getText().toString(), chronicD.getText().toString(),
                            employmentStatus, maritalStatus, cigaretteSmoke);
                }
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MySharedPreference.getString(this, "age", "").equals("")) {
            age.setText(MySharedPreference.getString(this, "age", ""));
        }
         if(!MySharedPreference.getString(this, "height", "").equals("")){
            height.setText(MySharedPreference.getString(this, "height", ""));
        }
         if(!MySharedPreference.getString(this, "weight", "").equals("")){
            wieght.setText(MySharedPreference.getString(this, "weight", ""));
        }
        if(!MySharedPreference.getString(this, "monthlyIncome", "").equals("")){
            monthlyincome.setText(MySharedPreference.getString(this, "monthlyIncome", ""));
        }
         if(!MySharedPreference.getString(this, "chronicDiseases", "").equals("")) {
            chronicD.setText(MySharedPreference.getString(this, "chronicDiseases", ""));
        }


    }

    public boolean checkFields() {
        flag = false;
        ageS = age.getText().toString();
        heightS = height.getText().toString();
        wieghtS = wieght.getText().toString();
        monthlyincomeS = monthlyincome.getText().toString();
        chronicDS = chronicD.getText().toString();
        // Fields Validations
        if (!checkSocioFields(ageS,heightS,wieghtS,monthlyincomeS,chronicDS,employmentStatus,maritalStatus,cigaretteSmoke)){
            Toast.makeText(EditSocio.this, R.string.EmptyFields,Toast.LENGTH_LONG).show();
            return flag;
        }
        //age format
        double ageI = Double.parseDouble(ageS);
        if ( ! isValidAge(ageI) ) {
            Toast.makeText(EditSocio.this, R.string.NotCorrectAge,Toast.LENGTH_LONG).show();
            return flag;
        }
        //height format
        double heightI = Double.parseDouble(heightS);
        if (!isValidHeight(heightI)){
            Toast.makeText(EditSocio.this, R.string.NotCorrectHeight,Toast.LENGTH_LONG).show();
            return flag;
        }
        //weight format
        double weightI = Double.parseDouble(wieghtS);
        if (!isValidWeight(weightI)){
            Toast.makeText(EditSocio.this, R.string.NotCorrectWeight,Toast.LENGTH_LONG).show();
            return flag;
        }
        //MI format
        double monthlyIncomeI = Double.parseDouble(monthlyincomeS);
        if (!isValidMonthlyIncome(monthlyIncomeI)){
            Toast.makeText(EditSocio.this, R.string.NotCorrectMI,Toast.LENGTH_LONG).show();
            return flag;
        }
        // CHECK CHRONIC DISEASE
        if (!isValidChronicDisease(chronicDS)){
            Toast.makeText(EditSocio.this, R.string.NotCorrectCD,Toast.LENGTH_LONG).show();
            return flag;
        }
        return true;
    }

    public void updateInfo(final String newAge, final String newHeight, final String newWeight,
                           final String newIncome, final String newChronic, final String newEmployee,
                           final String newMarried,final String newSmoke ) {
        DocumentReference userName = db.collection("Patient").document(mAuth.getUid());
        // Set the "isCapital" field of the city 'DC'
        userName.update("age", newAge);
        userName.update("height",newHeight);
        userName.update("weight",newWeight);
        userName.update("monthlyIncome",newIncome);
        userName.update("chronicDiseases",newChronic);
        userName.update("employmentStatus", newEmployee);
        userName.update("maritalStatus", newMarried);
        userName.update("smokeCigarettes", newSmoke)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update Firebase User profile
                        FirebaseUser userf = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().build();
                        userf.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");

                                            MySharedPreference.putString(EditSocio.this, "age", newAge);
                                            MySharedPreference.putString(EditSocio.this, "height", newHeight);
                                            MySharedPreference.putString(EditSocio.this, "weight", newWeight);
                                            MySharedPreference.putString(EditSocio.this, "monthlyIncome", newIncome);
                                            MySharedPreference.putString(EditSocio.this, "chronicDiseases", newChronic);
                                            MySharedPreference.putString(EditSocio.this, "employmentStatus", newEmployee);
                                            MySharedPreference.putString(EditSocio.this, "maritalStatus", newMarried);
                                            MySharedPreference.putString(EditSocio.this, "smokeCigarettes", newSmoke);

                                            Toast.makeText(EditSocio.this, R.string.SocioInfoUpdateSuccess,
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Toast.makeText(EditSocio.this, R.string.SocioInfoUpdateFialed,
                                Toast.LENGTH_LONG).show();

                    }
                });




    }// updateInfo()

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

    public boolean checkSocioFields (String age, String height, String weight
            , String monthlyIncome, String chronicDiseases, String employmentStatus
            ,String maritalStatus,String cigaretteSmoke) {
        if( age.matches("") || height.matches("") || weight.matches("")
                || monthlyIncome.matches("") || chronicDiseases.matches("")
                ) {//|| employmentStatus.matches("") || maritalStatus.matches("")|| cigaretteSmoke.matches("") -> always false
            return false;
        }
        return true;
    }

    public void retrieveData () {
        db.collection("Patient")
                .document(mAuth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                age.setText(documentSnapshot.get("age").toString());
                height.setText(documentSnapshot.get("height").toString());
                wieght.setText(documentSnapshot.get("weight").toString());
                monthlyincome.setText(documentSnapshot.get("monthlyIncome").toString());
                chronicD.setText(documentSnapshot.get("chronicDiseases").toString());
                int posEmp = adapter.getPosition(documentSnapshot.get("employmentStatus").toString());
                employmentStatusET.setSelection(posEmp);
                int posMar = adapterM.getPosition(documentSnapshot.get("maritalStatus").toString());
                maritalStatusET.setSelection(posMar);
                int posSmok = adapterS.getPosition(documentSnapshot.get("smokeCigarettes").toString());
                cigaretteSmokeET.setSelection(posSmok);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditSocio.this, "Fails to get data", Toast.LENGTH_LONG).show();
            }
        });

    }

}
