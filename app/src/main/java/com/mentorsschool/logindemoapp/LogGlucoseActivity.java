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

    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference ref = database.getReference("logs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_glucose);

        firebaseAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //var user = firebase.auth().currentUser;
                //DatabaseReference logRef = ref.child("logs");
                //var level = findViewById(R.id.txtNumber).getSelectedItem();
                //logRef.push().setValueAsync(new Log((string)user.uid, (string)level));
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