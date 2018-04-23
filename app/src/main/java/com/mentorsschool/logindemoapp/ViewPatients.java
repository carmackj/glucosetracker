package com.mentorsschool.logindemoapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ViewPatients extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsers;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDB;
    Query mQueryRef;

    private TextView box;

    private Bundle b;
    private String drName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        b = getIntent().getExtras();
        if(b != null)
            drName = b.getString("dr");

        mDatabase = FirebaseDatabase.getInstance().getReference("logs");
        mUsers = FirebaseDatabase.getInstance().getReference("users");
        mDB = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        box = findViewById(R.id.txtBox);
        box.setText("");

        mQueryRef = mUsers;


        mQueryRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {

                for(DataSnapshot ds : dataSnapshot2.getChildren()){
                    User user = ds.getValue(User.class);
                    if(user.doctor.equals(drName)) {
                        box.append(user.name + "\n");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
