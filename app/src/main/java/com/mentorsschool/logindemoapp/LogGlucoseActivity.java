package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class LogGlucoseActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_glucose);

        firebaseAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText levelBox = findViewById(R.id.txtNumber);               //Selects the level box

                FirebaseDatabase database = FirebaseDatabase.getInstance();     //Sets up the Database
                DatabaseReference db = database.getReference("logs");        //Selects the DB reference

                String userId = db.push().getKey();                             //Pushes a new entry to the DB and gets its key

                Log newLog = new Log(levelBox.getText().toString());            //Creates the log
                db.child(userId).setValue(newLog);                              //Sets the empty entry to the new log
                startActivity(new Intent(LogGlucoseActivity.this, HomeActivity.class));
          }
        });


        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(LogGlucoseActivity.this, LoginActivity.class));
            }
        });



    }
}