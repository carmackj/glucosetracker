package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LogGlucoseActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private Button submit;

    var user = firebase.auth().currentUser;
    var name, uid;
    if(user != null) {
        name = user.displayName;
        uid = user.uid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_glucose);

        firebaseAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DatabaseReference logRef = ref.child("logs");
                DatabaseReference newLogRef = logRef.push();

          }
        });


        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(LogGlucoseActivity.this, MainActivity.class));
            }
        });



    }
}