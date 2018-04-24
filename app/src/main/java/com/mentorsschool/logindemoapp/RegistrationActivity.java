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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    //Creates variables for the edit fields
    private EditText userName, userPassword, userEmail, lowLevel, highLevel;
    private Button regButton;
    private CompoundButton switchType;
    private TextView userLogin;
    private String user_type;
    private Spinner dropdown;

    List<String> names; //List of doctors

    //Declare Firebase Info
    private FirebaseAuth firebaseAuth;
    private DatabaseReference users, doctors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set up the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        //Set up firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();     //Sets up the Database
        users = database.getReference("users");
        doctors = database.getReference("doctors");
        Query mQueryRef = doctors;

        //Finds the dropdown
        dropdown = findViewById(R.id.drSpinner);
        names = new ArrayList<>();  //Sets up an ArrayList of doctors
        names.add("");


        doctors.addValueEventListener(new ValueEventListener() {    //Listens for Doctors added to the table
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String drName = ds.child("name").getValue(String.class);    //Gets the Doctors Name
                    names.add(drName);  //Adds it to the array list
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(  //Set the dropdown to the names
                        RegistrationActivity.this,
                        android.R.layout.simple_spinner_item,
                        names
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Listen for when the registration button is clicked
                if (validate()) {
                    String user_email = userEmail.getText().toString().trim();          //Gets the user email
                    String user_password = userPassword.getText().toString().trim();    //Gets the user password


                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {    //Create a firebase user
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                //Begin making hte user table entry
                                //Get info from the fields
                                String user_name = userName.getText().toString().trim();
                                int user_lowLevel = Integer.parseInt(lowLevel.getText().toString().trim());
                                int user_highLevel = Integer.parseInt(highLevel.getText().toString().trim());

                                //If the user is a doctor, make a doctor entry and set their type
                                if(switchType.isChecked()) {
                                    user_type = "Doctor";
                                    doctors.child(firebaseAuth.getCurrentUser().getUid()).setValue(new Doctor(user_name));
                                }
                                else
                                    user_type="Patient";

                                String doctor = dropdown.getSelectedItem().toString();  //Get their doctor if they selected one

                                //Add the user to the table
                                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(new User(firebaseAuth.getCurrentUser().getUid(),user_name, user_type, user_lowLevel, user_highLevel, doctor));

                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));   //Go to main screen
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Listener for if user clicks the login button
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews()
    {
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton= (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        switchType = findViewById(R.id.swType);
        lowLevel = findViewById(R.id.txtLowLevel);
        highLevel = findViewById(R.id.txtHighLevel);

    }

    private Boolean validate()
    {
        Boolean result = false;
        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }
}
