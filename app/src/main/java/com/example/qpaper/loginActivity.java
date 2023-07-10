package com.example.qpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private EditText useremail;
    private EditText userpassword;
    private Button logbtn;
    private TextView regview;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        useremail = findViewById(R.id.uemail);
        userpassword = findViewById(R.id.upass);

        logbtn = findViewById(R.id.logoutbtn);
        regview = findViewById(R.id.regtxt);

        mauth = FirebaseAuth.getInstance();

        regview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,RegistrationActivity.class));
            }
        });

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = useremail.getText().toString();
                String pass_txt = userpassword.getText().toString();

                loginuser(email_txt,pass_txt);
            }

            private void loginuser(String email, String pass) {
                mauth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(loginActivity.this,new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(loginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(loginActivity.this,HomeActivity.class));
                        finish();
                    }
                });
            }
        });


    }
}