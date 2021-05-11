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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saaq_automobile.helpers.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import com.example.saaq_automobile.model.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edFname,edLname,edCell,edMail, edPassword, edUsername;
    RadioButton rbMale,rbFemale;
    RadioGroup rgGender;
    Button btnSignUp;
    TextView signIn;

    DatabaseReference userDatabase,userChild;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        edMail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edFname = findViewById(R.id.edFname);
        edLname = findViewById(R.id.edLname);
        edCell = findViewById(R.id.edCell);
        edUsername = findViewById(R.id.edUsername);
        rbMale = findViewById(R.id.rbMale);
        rbMale.setOnClickListener(this);

        rbFemale = findViewById(R.id.rbFemale);
        rbFemale.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        signIn = findViewById(R.id.TVSignIn);
        signIn.setOnClickListener(this);

        userDatabase = FirebaseDatabase.getInstance().getReference(AppConstants.firebaseUsersReference);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btnSignUp:
                register();
                break;
            case R.id.TVSignIn:
                signIN();
                break;
        }
    }

    private void register() {

        String FName = edFname.getText().toString();
        String LName = edLname.getText().toString();
        String Email = edMail.getText().toString();
        String Password = edPassword.getText().toString();
        String Cell = edCell.getText().toString();
        String unm = edUsername.getText().toString();
        String profilepic="https://firebasestorage.googleapis.com/v0/b/saaq-automobile.appspot.com/o/images%2Fnoimage.png?alt=media&token=a307e055-ef44-46c0-a317-d39442a068d4";
        String Gender;


        if(rbMale.isChecked()==true)
        {
            Gender = rbMale.getText().toString();
        }
        else
        {
            Gender = rbFemale.getText().toString();
        }

        //Validating fields !!!!
        if (FName.isEmpty()) {
            edFname.setError("Provide your First name first!");
            edFname.requestFocus();
        } else if (LName.isEmpty()) {
            edLname.setError("Provide your Last name first!");
            edLname.requestFocus();
        }
        else if (Email.isEmpty()) {
            edMail.setError("Provide your Email first!");
            edMail.requestFocus();
        }
        else if (Password.isEmpty()) {
            edPassword.setError("Please set your secure password !!!");
            edPassword.requestFocus();
        }
        else if (unm.isEmpty()) {
            edUsername.setError("Provide your desired Username first!");
            edUsername.requestFocus();
        }
        /*else if(!unm.isEmpty())
        {
            userChild = FirebaseDatabase.getInstance().getReference().child("users").child(unm);
            userChild.addValueEventListener(this);
        }*/
        else if(Cell.isEmpty())
        {
            edCell.setError("Provide your Phone number first!");
            edCell.requestFocus();
        }
        else if (Email.isEmpty() && Password.isEmpty() && FName.isEmpty() && LName.isEmpty() && unm.isEmpty()) {
            Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
        }
        else if (!(Email.isEmpty() && Password.isEmpty()))
        {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }


        mProgressDialog.setTitle("Registering");
        mProgressDialog.setMessage("please wait...");
        mProgressDialog.show();
            mFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                /*if (mFirebaseAuth.getCurrentUser() != null){
                                    Log.e("MainACtivity.java", mFirebaseAuth.getCurrentUser().getUid());
                                } else {
                                    Log.e("MainACtivity.java", "Get current user is null!!");
                                }*/
                                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        User user = new User(FName, LName, Cell, Email, Gender, unm, profilepic);
                                        _saveUserDetailsToFirebase(mFirebaseAuth.getCurrentUser(), user);
                                    }
                                });
                            } else {
                                Log.e("MainACtivity.java", "Creation Not complete");
                                mProgressDialog.dismiss();
                            }
                        }
                    }
            );



            /*user user = new user(FName, LName, Cell, Email, Password, Gender);
            userDatabase.child(unm.toString()).setValue(user);
            Toast.makeText(this, "User with Email  has been registered successfully!!", Toast.LENGTH_LONG).show();

            Intent I = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(I);*/
        }
    }

    private void _saveUserDetailsToFirebase(FirebaseUser currentUser, User user) {

        userDatabase.child(currentUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.e("MainACtivity.java", "User Added Completed");
                    Intent I = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(I);
                } else {
                    Log.e("MainACtivity.java", "User not added");
                }
            }
        });

    }

    private void signIN() {
        Intent I = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(I);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists())
        {
            edUsername.setError("Please try another username!! ");
            Toast.makeText(this,"Username already exists !!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}