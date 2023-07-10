package com.example.qpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddqueActivity extends AppCompatActivity {
    private Spinner cousrse;
    private TextView selectedcourse;

    private EditText questiontxt;
    private Spinner year;

    private Button addquentn;
    private TextView selectedyear;

    private EditText qmarks;

    DatabaseReference dbref;

    DatabaseReference qbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addque);

        cousrse = findViewById(R.id.course);
        selectedcourse = findViewById(R.id.coursetxt);
        year = findViewById(R.id.year);
        selectedyear = findViewById(R.id.yeartxt);
        questiontxt = findViewById(R.id.question);
        addquentn = findViewById(R.id.addquentn);
        qmarks = findViewById(R.id.qmarks);


        List<String> coursecat = new ArrayList<>();
        coursecat.add(0,"Select Course");
        coursecat.add(1,"BSC IT");
        coursecat.add(2,"BSC CS");
        coursecat.add(3,"B COM");
        coursecat.add(4,"Other");

        ArrayAdapter<String> coursedataadapter;
        coursedataadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,coursecat);
        coursedataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cousrse.setAdapter(coursedataadapter);

        cousrse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Course")){
                }else{
                selectedcourse.setText(parent.getSelectedItem().toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> yearcat = new ArrayList<>();
        yearcat.add(0,"Select Year");
        yearcat.add(1,"FY");
        yearcat.add(2,"SY");
        yearcat.add(3,"TY");

        ArrayAdapter<String> yeardataadapter;
        yeardataadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,yearcat);
        yeardataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        year.setAdapter(yeardataadapter);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Year")){
                }else{
                    selectedyear.setText(parent.getSelectedItem().toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addquentn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref = FirebaseDatabase.getInstance().getReference().child("All Question");
                String courseref = String.valueOf(selectedcourse.getText());
                String yearref = String.valueOf(selectedyear.getText());
                String uniquequestionID = UUID.randomUUID().toString();
//
//                System.out.println(courseref);
//                System.out.println(yearref);
//                System.out.println(uniquequestionID);




                final HashMap<String, Object> questionmap = new HashMap<>();
                questionmap.put("Question",questiontxt.getText().toString());
                questionmap.put("Marks",qmarks.getText().toString());

                qbref = dbref.child(yearref).child(courseref).child(uniquequestionID);

                qbref.updateChildren(questionmap).addOnCompleteListener(AddqueActivity.this ,new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddqueActivity.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddqueActivity.this, "Failed To Add Question", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                String qtxt = questiontxt.getText().toString();
                DatabaseReference questionbankref = FirebaseDatabase.getInstance().getReference().child("Question Bank");
                questionbankref.child(uniquequestionID).setValue(qtxt);





            }
        });
    }
}