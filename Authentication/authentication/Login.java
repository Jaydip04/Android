package com.jaydip.authentication_app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jaydip.authentication_app.MainActivity;
import com.jaydip.authentication_app.R;

public class Login extends AppCompatActivity {

    private TextView openReg;
    private EditText logEmail,logpass;
    private Button loginBtn;

    private String email,pass;

    private FirebaseAuth auth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        openReg = findViewById(R.id.openReg);
        logEmail = findViewById(R.id.logEmail);
        logpass = findViewById(R.id.logpasss);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        openReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }

    private void validateData() {

        email = logEmail.getText().toString();
        pass = logpass.getText().toString();


        if(email.isEmpty()){
            logEmail.setError("Required");
            logEmail.requestFocus();
        }else if(pass.isEmpty()){
            logpass.setError("Required");
            logpass.requestFocus();
        }else{
            loginUser();
        }
    }

    private void loginUser() {
        pd.setMessage("loading...");
        pd.show();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    openMain();
                }else{
                    pd.dismiss();
                    Toast.makeText(Login.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Login.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMain() {

        startActivity(new Intent(Login.this, MainActivity.class));
        finish();

    }


    private void openRegister() {

        startActivity(new Intent(Login.this,Register.class));
        finish();

    }
}