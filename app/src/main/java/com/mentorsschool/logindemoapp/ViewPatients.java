package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ViewPatients extends AppCompatActivity {

    //Declare firebase objects
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsers;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDB;
    Query mQueryRef;

    //Declare view objects
    private TextView box;
    private Button btnSubmit, btnHome;
    private Spinner userSpinner;

    //Declare bundle to pass info back
    private Bundle b;
    private String drName = null;
    private String uid;

    private ArrayList<User> users;  //Array list of all of the users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        //Gets the doctor name from the bundle
        b = getIntent().getExtras();
        if(b != null)
            drName = b.getString("dr");

        //Set up firebase objects
        mDatabase = FirebaseDatabase.getInstance().getReference("logs");
        mUsers = FirebaseDatabase.getInstance().getReference("users");
        mDB = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //Set up view objects
        btnHome = findViewById(R.id.btnHome2);
        btnSubmit = findViewById(R.id.btnSubmit);
        box = findViewById(R.id.txtBox);
        box.setText("");

        mQueryRef = mUsers; //Query the database

        users = new ArrayList<>();  //Makes a new array list for the users

        mQueryRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {    //Query the database for users
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {

                for(DataSnapshot ds : dataSnapshot2.getChildren()){ //Loop through each user
                    User user = ds.getValue(User.class);    //get their doctor's name
                    if(user.doctor.equals(drName)) {        //If the current doctor is their doctor
                        box.append(user.name + "\n");       //Print their user name
                        users.add(user);                    //And add it to the arraylist
                    }
                }
                ArrayAdapter<User> adapter = new ArrayAdapter<User>(ViewPatients.this, android.R.layout.simple_spinner_item, users);    //Sets the dropdown to use the users names
                userSpinner = findViewById(R.id.spinner);   //Finds the spinner
                userSpinner.setAdapter(adapter);    //Uses the adapter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Once the doctor clicks the submit button
                User userToView = (User)userSpinner.getSelectedItem();  //gets the user from the dropdown

                b = new Bundle();   //creates a new bundle to pass
                b.putString("uid", userToView.id);  //Adds that user's ID to the bundle

                Intent intent = new Intent(ViewPatients.this, ViewLog_Text.class);  //New intent to go to the viewlog view

                intent.putExtras(b);    //Adds the bundle to the intent
                startActivity(intent);  //Goes to the view
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {    //If the user clicks the go home button, go home
                startActivity(new Intent(ViewPatients.this, Home.class));
            }
        });
    }
}
