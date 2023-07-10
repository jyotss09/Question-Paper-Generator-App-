package com.example.qpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MakequestionpaperActivity extends AppCompatActivity {

    Button submitButton;
    private Spinner cousrse;
    private TextView selectedcourse;

    private Spinner year;
    private TextView selectedyear;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makequestionpaper);
        ArrayList<String> quesarray = new ArrayList<>();

        submitButton = findViewById(R.id.submitButton);

        cousrse = findViewById(R.id.course);
        selectedcourse = findViewById(R.id.coursetxt);
        year = findViewById(R.id.year);
        selectedyear = findViewById(R.id.yeartxt);

        List<String> coursecat = new ArrayList<>();
        coursecat.add(0,"Select Course");
        coursecat.add(1,"BSC-IT");
        coursecat.add(2,"BSC-CS");
        coursecat.add(3,"B-COM");
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref = FirebaseDatabase.getInstance().getReference().child("Question Bank");

                String[] ques = {"What is the purpose of turing test?","Explain semantic network with examples.","What is Artificial intelligence?Explain with example.","Give the PEAS description for taxi's task environment.","Explain the working of AND-OR search tree","Explain the working of simple reflex agent.","Explain universal quantifier with example.","Define the Wumpus world problem in terms of first order logic.","How generic algorithm works?","Explain the concept of agent and environment"};
                String courseref = String.valueOf(selectedcourse.getText());
                String yearref = String.valueOf(selectedyear.getText());
//
                System.out.println(courseref);
                System.out.println(yearref);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (count >= 50) {
                                break;
                            }
                            System.out.println(child.getValue());
                            quesarray.add((String) child.getValue());
                            count++;
                        }
                        System.out.println(quesarray.toString());
                        Collections.shuffle(quesarray);
                        System.out.println(quesarray.toString());
                        String[] quesarr = new String[quesarray.size()];
                        quesarr = quesarray.toArray(quesarr);

                        System.out.println(quesarr[0]);
                        System.out.println(quesarr[1]);

                        try {
                            createpdf(quesarr,yearref,courseref);

                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void createpdf(String[] ques,String year,String course) throws FileNotFoundException{

        String subref = year +" "+course;
        DateFormat dateFormat = new SimpleDateFormat("DD-MM");
        Date date = new Date();
        String month = dateFormat.format(date);
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String randomnum = String.format("%06d", number);

        String filename = subref+month+randomnum+"(generated).pdf";

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, filename);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);

        Drawable d = getDrawable(R.drawable.header);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        Paragraph heading = new Paragraph("End Semester Exam(May 2023)").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(8).setUnderline();
//        Paragraph rules1 = new Paragraph("N. B.: (1) All questions are compulsory.").setPaddingLeft(30).setFontSize(5);
        Paragraph rules2 = new Paragraph("(1) All questions are compulsory.\n"+"(2) Make suitable assumptions wherever necessary and state the assumptions made.\n"+"(3) Answers to the same question must be written together.\n"+"(4) Numbers to the right indicate marks.\n"+"(5) Draw neat labeled diagrams wherever necessary.\n"+"(6) Use of Non-programmable calculators is allowed.").setPaddingLeft(40).setPaddingBottom(5).setFontSize(5);

        Paragraph classyear = new Paragraph("Class: "+ subref).setTextAlignment(TextAlignment.RIGHT).setFontSize(6).setPaddingRight(20);
        Paragraph subject = new Paragraph("Subject :- AI").setTextAlignment(TextAlignment.LEFT).setFontSize(6).setPaddingLeft(20);
        Paragraph PTO = new Paragraph("PTO").setTextAlignment(TextAlignment.RIGHT).setFontSize(6).setPaddingRight(20).setPaddingTop(5);

        float[] width = {15f,200f,30f};
        Table table = new Table(width);
        Table table2 = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table2.setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginTop(20.0F);

        table.addCell(new Cell().add(new Paragraph("Q1").setFontSize(5)).setBold());
        table.addCell(new Cell().add(new Paragraph("Attempt any three of the following").setFontSize(5).setBold()));
        table.addCell(new Cell().add(new Paragraph("15 Marks").setFontSize(5).setBold()));

        table.addCell(new Cell().add(new Paragraph("a.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[0]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("b.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[1]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("c.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[2]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("d.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[3]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("e.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[4]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("f.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[5]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));


        table.addCell(new Cell().add(new Paragraph("Q2").setFontSize(5)).setBold());
        table.addCell(new Cell().add(new Paragraph("Attempt any three of the following").setFontSize(5).setBold()));
        table.addCell(new Cell().add(new Paragraph("15 Marks").setFontSize(5).setBold()));

        table.addCell(new Cell().add(new Paragraph("a.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[6]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("b.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[7]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("c.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[8]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("d.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[9]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("e.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[10]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("f.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[11]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("Q3").setFontSize(5)).setBold());
        table.addCell(new Cell().add(new Paragraph("Attempt any three of the following").setFontSize(5).setBold()));
        table.addCell(new Cell().add(new Paragraph("15 Marks").setFontSize(5).setBold()));

        table.addCell(new Cell().add(new Paragraph("a.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[12]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("b.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[13]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("c.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[14]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("d.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[15]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("e.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[16]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("f.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[17]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("Q4").setFontSize(5)).setBold());
        table.addCell(new Cell().add(new Paragraph("Attempt any three of the following").setFontSize(5).setBold()));
        table.addCell(new Cell().add(new Paragraph("15 Marks").setFontSize(5).setBold()));

        table.addCell(new Cell().add(new Paragraph("a.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[18]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table.addCell(new Cell().add(new Paragraph("b.").setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(ques[19]).setFontSize(4)));
        table.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

//        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        table2.addCell(new Cell().add(new Paragraph("c.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[20]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("d.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[21]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("e.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[22]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("f.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[23]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("Q5").setFontSize(5)).setBold());
        table2.addCell(new Cell().add(new Paragraph("Attempt any three of the following").setFontSize(5).setBold()));
        table2.addCell(new Cell().add(new Paragraph("15 Marks").setFontSize(5).setBold()));

        table2.addCell(new Cell().add(new Paragraph("a.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[24]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("b.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[25]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("c.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[26]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("d.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[27]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("e.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[28]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));

        table2.addCell(new Cell().add(new Paragraph("f.").setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(ques[29]).setFontSize(4)));
        table2.addCell(new Cell().add(new Paragraph(5 + " marks.").setFontSize(4)));





        document.add(image);
        document.add(heading).add(classyear);
//        document.add(rules1);
        document.add(rules2);
        document.add(table).add(PTO);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(table2);


        document.close();

        Toast.makeText(MakequestionpaperActivity.this,"Question Paper Pdf Created named:- "+filename,Toast.LENGTH_LONG).show();

    }
});}}