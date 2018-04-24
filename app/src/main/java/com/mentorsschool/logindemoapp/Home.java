package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    //Declare variables for the view
    private TextView name;
    private Button btnAddLog;
    private Button btnViewLogs;
    private Button logout;
    private Button viewPts;

    //Declare Firebase objects
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDB;
    private DatabaseReference mType;

    private User u; //User object for referencing current user

    private Bundle b;   //Bundle to pass user IDs around

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view and context
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        b = new Bundle();   //Initialize a new bundle to pass info to the next view

        //Declare firebase references
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        mDB = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Declares the input fields from the view
        viewPts = findViewById(R.id.btnViewPts);
        btnAddLog = findViewById(R.id.btnGoToAddLog);
        btnViewLogs = findViewById(R.id.btnGoToViewLogs);
        logout = findViewById(R.id.btnLogout);
        name = findViewById(R.id.txtName);

        mDB.child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {  //Queries the database for the current user
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);  //Gets the current user into a user object

                //Sets the text box with the user's name
                name.setText("Welcome:\n");
                name.append(u.name);
                name.append("\n");

                if(u.type.equals("Doctor")) //Checks if the current user is a doctor
                {
                    viewPts.setVisibility(View.VISIBLE);    //Shows the view patients button
                    b.putString("dr", u.name);
                }
                else{
                    viewPts.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAddLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Sets listener for add log button
                startActivity(new Intent(Home.this, LogGlucoseActivity.class)); //Go to add log view
            }
        });

        btnViewLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Sets listener for view log button
                Intent intentViewLogs = new Intent(Home.this, ViewLog_Text.class);  //Makes new intent to go to logs
                startActivity(intentViewLogs);  //Goes to view logs view
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Sets listener for logout button
                firebaseAuth.signOut();         //Signs out of firebase
                finish();
                startActivity(new Intent(Home.this, MainActivity.class));   //Goes to main activity
            }
        });

        viewPts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {    //Sets listener for view patients button
                Intent intent = new Intent(Home.this, ViewPatients.class);  //Creates new intent to go to View Patients view
                intent.putExtras(b);        //Adds the bundle to the intent
                startActivity(intent);      //Goes to the view patients view
            }
        });
    }
}
