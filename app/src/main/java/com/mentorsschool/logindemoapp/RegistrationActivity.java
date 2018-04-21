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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail, lowLevel, highLevel;
    private Button regButton;
    private CompoundButton switchType;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private String user_type;
    private Spinner dropdown;
    List<String> names;
    String[] nameArray;

    private DatabaseReference users, doctors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();     //Sets up the Database
        users = database.getReference("users");
        doctors = database.getReference("doctors");
        Query mQueryRef = doctors;

        dropdown = findViewById(R.id.drSpinner);
        names = new ArrayList<>();
        names.add("Steve");
        names.add("Tom");

        doctors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                ArrayList<Doctor> drList = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot2.getChildren()){
                    //Doctor dr = ds.getValue(Doctor.class);
                    //drList.add(dr);
                    Doctor dr = ds.getValue(Doctor.class);
                    String name = dr.name;
                    names.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
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
            public void onClick(View view) {
                if (validate()) {
                    //upload data to the database
                    //String user_name = userName.getText().toString().trim();
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();


                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();



                                String user_name = userName.getText().toString().trim();
                                int user_lowLevel = Integer.parseInt(lowLevel.getText().toString().trim());
                                int user_highLevel = Integer.parseInt(highLevel.getText().toString().trim());

                                if(switchType.isChecked()) {
                                    user_type = "Doctor";
                                    doctors.child(firebaseAuth.getCurrentUser().getUid()).setValue(new Doctor(user_name));
                                }
                                else
                                    user_type="Patient";

                                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(new User(user_name, user_type, user_lowLevel, user_highLevel));

                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
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
            public void onClick(View view) {
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
