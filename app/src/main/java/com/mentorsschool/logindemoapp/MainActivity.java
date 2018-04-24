package com.mentorsschool.logindemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Declare inputs
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;

    private int counter = 5;    // This is going to keep up with the amount of attemps trying to log on

    private TextView userRegistration;  //Sets the text box
    private FirebaseAuth firebaseAuth;  //Sets the firebase Authenication
    private ProgressDialog progressDialog;  //Progress dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Sets up the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the input fields
        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (TextView)findViewById(R.id.tvRegister);

        Info.setText("No. of attempts remaining: 5");   //Set the info text

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();  //Checks if there is a firebase user logged in
        if(user != null)    //If there is a user
        {
            finish();
            startActivity(new Intent(MainActivity.this, Home.class));   //Go ahead on in the app
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Set login button listener
                validate(Name.getText().toString(),
                        Password.getText().toString()); //Validates the user
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Sets a listener for the registration button
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class ));  //Goes to register a user
            }
        });


    }

    private void validate(String userName, String userPassword) //Validate the user (default code)
    {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login complete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Home.class ));
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No. of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0)
                    {
                        Login.setEnabled(false);
                    }
                }
            }
        });
    }
}
