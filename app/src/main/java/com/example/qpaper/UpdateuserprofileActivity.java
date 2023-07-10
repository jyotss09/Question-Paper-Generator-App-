package com.example.qpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateuserprofileActivity extends AppCompatActivity {

    private EditText uname;
    private EditText phone;
    private EditText desig;
    private EditText institue;
//    private EditText bckbtn;
    private Button addbtn;

//    private FirebaseAuth mauth;


    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuserprofile);
        uname = findViewById(R.id.uname);
        phone = findViewById(R.id.uphone);
        desig = findViewById(R.id.udesig);
        institue = findViewById(R.id.uinstitute);

//        bckbtn = findViewById(R.id.bckbtn);
        addbtn = findViewById(R.id.logoutbtn);


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String Userid = user.getUid();
                String Umail = user.getEmail();
                String uname_txt = uname.getText().toString();
                String phone_txt = phone.getText().toString();
                String desig_txt = desig.getText().toString();
                String institute_txt = institue.getText().toString();

                dbref = FirebaseDatabase.getInstance().getReference().child("All Users");

                final HashMap<String, Object> usermap = new HashMap<>();
                usermap.put("Username",uname_txt);
                usermap.put("Phone",phone_txt);
                usermap.put("Designation",desig_txt);
                usermap.put("Institute",institute_txt);
                usermap.put("email",Umail.toString());

                dbref.child(Userid).updateChildren(usermap).addOnCompleteListener(UpdateuserprofileActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateuserprofileActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateuserprofileActivity.this, HomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(UpdateuserprofileActivity.this, "Failed To Update Profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}