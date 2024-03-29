package com.example.qpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserinfoActivity extends AppCompatActivity {

    private Button Logoutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Logoutbtn = findViewById(R.id.logoutbtn);

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserinfoActivity.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserinfoActivity.this,loginActivity.class));
            }
        });
    }
}