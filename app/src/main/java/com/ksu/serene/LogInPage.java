package com.ksu.serene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.ksu.serene.Controller.Signup;
import com.ksu.serene.Controller.Homepage.home.HomeFragment;

//this is the controller
public class LogInPage extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button logIn;
    private TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        logIn = findViewById(R.id.Log_In);
        signup = findViewById(R.id.sign_up);

        signup.setOnClickListener ( new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                Intent intent = new Intent(LogInPage.this, Signup.class);
                startActivity(intent);
              }
         }
        );

        logIn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //check all fields not empty
                if (checkAllFields()){
                    //check the email is valid format and the email registered before
                    //check the password is correct
                    Patient patient = new Patient();
                    if (patient.login(email.getText().toString(), password.getText().toString())){
                        Toast.makeText(LogInPage.this, "Authentication Success.",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LogInPage.this, HomeFragment.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LogInPage.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private boolean checkAllFields (){
        if (email.getText().toString() != "" && password.getText().toString() != ""){
            return true;
        }
        return false;
    }
}
