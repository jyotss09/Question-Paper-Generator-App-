package com.example.qpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText useremail;
    private EditText userpassword;
    private Button regbtn;
    private TextView loginview;

    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        useremail = findViewById(R.id.uemail);
        userpassword = findViewById(R.id.upass);
        regbtn = findViewById(R.id.logoutbtn);
        loginview = findViewById(R.id.regtxt);

//        ActivityCompat.requestPermissions(RegistrationActivity.this,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE});

        mauth = FirebaseAuth.getInstance();
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = useremail.getText().toString();
                String pass_txt = userpassword.getText().toString();

                if(TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(pass_txt)){
                    Toast.makeText(RegistrationActivity.this,"Please enter the emailid and password",Toast.LENGTH_SHORT).show();
                } else if (userpassword.length()<8) {

                    Toast.makeText(RegistrationActivity.this, "Password should be atleeast of 8 digits",Toast.LENGTH_SHORT).show();
                }else{
                    registeruser(email_txt,pass_txt);
                }
            }
        });

        loginview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,loginActivity.class));
            }
        });
    }
    private void registeruser(String email, String pass) {
        mauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegistrationActivity.this ,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this,UpdateuserprofileActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegistrationActivity.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}