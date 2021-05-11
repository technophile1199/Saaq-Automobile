package com.example.saaq_automobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edLoginEmailId, edLogInpasswd;
    Button btnLogIn;
    TextView tvSignup;

    DatabaseReference userDatabase,userChild;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        edLoginEmailId = findViewById(R.id.edLoginEmail);
        edLogInpasswd = findViewById(R.id.edLoginpwd);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);

        tvSignup = findViewById(R.id.TVSignIn);
        tvSignup.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btnLogIn:
                logIn();
                break;
            case R.id.TVSignIn:
                signIN();
                break;
        }
    }


    private void logIn() {
            String unm= edLoginEmailId.getText().toString();
            String Password = edLogInpasswd.getText().toString();


        if (unm.isEmpty()) {
            edLoginEmailId.setError("Please enter your username !!!");
            edLoginEmailId.requestFocus();
        } else if (Password.isEmpty()) {
            edLogInpasswd.setError("Please enter your password !!!");
            edLogInpasswd.requestFocus();
        }
        else if (!(unm.isEmpty() && Password.isEmpty()))
        {
            mProgressDialog.setTitle("Logging In");
            mProgressDialog.setMessage("please wait...");
            mProgressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(unm, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Log.e("LoginActivity.java", "Signin Success");
                        Intent I = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(I);
                    } else {
                        Toast.makeText(getApplicationContext(),"Login Unsuccessfull!!",Toast.LENGTH_LONG).show();
                        Log.e("LoginActivity.java", "Signin Failed");
                    }
                }
            });


            /*userChild = FirebaseDatabase.getInstance().getReference().child("users").child(unm);
            userChild.addValueEventListener(this);*/
        }
    }

    private void signIN() {
        Intent I = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(I);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists())
        {
            String FName = snapshot.child("fname").getValue().toString();
            String LName = snapshot.child("lname").getValue().toString();
            String Mail = snapshot.child("email").getValue().toString();
            edLogInpasswd.setText(FName);
            Toast.makeText(this,"Welcome "+FName+" "+LName+" you are logged in !!!",Toast.LENGTH_LONG).show();
            Intent I = new Intent(LoginActivity.this, Dashboard.class);
            I.putExtra("userName",FName+" "+LName);
            I.putExtra("mail",Mail);
            startActivity(I);
        }
        else
        {
            Toast.makeText(this,"Data with id : "+edLoginEmailId.getText().toString()+"does not exist",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}