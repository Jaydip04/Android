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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaydip.authentication_app.MainActivity;
import com.jaydip.authentication_app.R;


import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText regName,regEmail,regpass;
    private Button register;
    private String name,email,pass;
    private FirebaseAuth auth;
    private TextView openLog;

    private DatabaseReference reference;
    private DatabaseReference dbRef;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);

        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regpass = findViewById(R.id.regpass);
        openLog = findViewById(R.id.openLog);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        openLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }

    private void openLogin() {

        startActivity(new Intent(Register.this, Login.class));
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser() != null){
            openMain();
        }
    }

    private void openMain() {

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    private void validateData() {
        name = regName.getText().toString();
        email = regEmail.getText().toString();
        pass = regpass.getText().toString();

        if(name.isEmpty()){
            regName.setError("Required");
            regName.requestFocus();
        }else if(email.isEmpty()){
            regEmail.setError("Required");
            regEmail.requestFocus();
        }else if(pass.isEmpty()){
            regpass.setError("Required");
            regpass.requestFocus();
        }else{
            createUser();
        }
    }

    private void createUser() {
        pd.setMessage("loading...");
        pd.show();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    uploadData();
                }else{
                    pd.dismiss();
                    Toast.makeText(Register.this, "Error......"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Register.this, "Error..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        pd.setMessage("loading...");
        pd.show();
        dbRef =  reference.child("users");
        String key = dbRef.push().getKey();

        HashMap<String,String> user = new HashMap<>();
        user.put("Key",key);
        user.put("name",name);
        user.put("email",email);
        user.put("pass",pass);

        dbRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                    openMain();
                }else{
                    pd.dismiss();
                    Toast.makeText(Register.this, "Error..."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Register.this, "Error..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}