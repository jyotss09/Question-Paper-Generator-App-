package com.example.qpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private TextView Username;
    private TextView desig;

    private ImageView addque;
    private FloatingActionButton uinfoview;

    private ImageView generatepapaer;

    private ImageView makeque;

    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Username = findViewById(R.id.unameview);
        desig = findViewById(R.id.desigview);
        uinfoview = findViewById(R.id.bckbtn);
        addque = findViewById(R.id.addque);
        generatepapaer = findViewById(R.id.generatepaper);
        makeque = findViewById(R.id.makeque);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String Userid = user.getUid();
        showuser(Userid);

        uinfoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserinfoActivity.class));
            }
        });
        addque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddqueActivity.class));
            }
        });

        generatepapaer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MakequestionpaperActivity.class));
            }
        });

        makeque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
    }

    private void showuser(String userid) {
        dbref = FirebaseDatabase.getInstance().getReference().child("All Users");
        dbref.child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Toast.makeText(HomeActivity.this, "Shown The data", Toast.LENGTH_SHORT).show();
                    DataSnapshot userdatas = task.getResult();
                    String uname = String.valueOf(userdatas.child("Username").getValue());
                    String designation = String.valueOf(userdatas.child("Designation").getValue());
                    String institute = String.valueOf(userdatas.child("Institute").getValue());

                    String addedcomma = designation.concat(" , ");
                    String desiginst = addedcomma.concat(institute);
                    Username.setText(uname);
                    desig.setText(desiginst);


                }else{
                    Toast.makeText(HomeActivity.this, "User Does not exists!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}