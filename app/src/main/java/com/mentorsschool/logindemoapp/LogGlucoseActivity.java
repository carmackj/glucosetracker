package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class LogGlucoseActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private Button submit;
    private RadioGroup timeGroup1;
    private RadioGroup timeGroup2;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_glucose);

        firebaseAuth = FirebaseAuth.getInstance();

        Button home = findViewById(R.id.btnHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(new Intent(LogGlucoseActivity.this, Home.class));
            }
        });

        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText levelBox = findViewById(R.id.txtNumber);               //Selects the level box

                FirebaseDatabase database = FirebaseDatabase.getInstance();     //Sets up the Database
                DatabaseReference db = database.getReference("logs").child(firebaseAuth.getCurrentUser().getUid());        //Selects the DB reference

                String userId = db.push().getKey();                             //Pushes a new entry to the DB and gets its key


                //Build Log Object//

                timeGroup1 = findViewById(R.id.timeGroup1);
                timeGroup2 = findViewById(R.id.timeGroup2);

                /*
                int group1Selected = timeGroup1.getCheckedRadioButtonId();
                int group2Selected = timeGroup2.getCheckedRadioButtonId();
                */

                String time1;
                String time2;

                int time1ID = timeGroup1.getCheckedRadioButtonId();
                RadioButton time1Button = findViewById(time1ID);
                time1 = time1Button.getText().toString();

                int time2ID = timeGroup2.getCheckedRadioButtonId();
                RadioButton time2Button = findViewById(time2ID);
                time2 = time2Button.getText().toString();

                /*
                if(group1Selected == 1)
                    time1 = "B";
                else
                    time1 = "A";

                if(group2Selected == 1)
                    time2 = "B";
                else if(group2Selected == 2)
                    time2 = "L";
                else
                    time2 = "D";
                */


                //End Build Log Object//

                Log newLog = new Log(levelBox.getText().toString(), firebaseAuth.getCurrentUser().getEmail().toString(), time1, time2);            //Creates the log
                db.child(userId).setValue(newLog);                              //Sets the empty entry to the new log

                startActivity(new Intent(LogGlucoseActivity.this, ViewLog_Text.class));
            }
        });






    }
}