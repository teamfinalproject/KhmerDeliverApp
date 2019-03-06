package com.kanhrithnon.khmerdeliverapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kanhrithnon.khmerdeliverapp.Model.Users;

public class SignUpActivity extends AppCompatActivity {

    EditText inputName,inputEmail,inputPhoneNumber,inputPassword;
    Button SignUp;
    RelativeLayout relativeLayoutsignup;

    FirebaseDatabase db;
    FirebaseAuth auth;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //link to login activity
        TextView login  =findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        //find id
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPhoneNumber = findViewById(R.id.input_phone_number);
        inputPassword = findViewById(R.id.input_password);
        SignUp = (Button) findViewById(R.id.btn_signup);
        relativeLayoutsignup = findViewById(R.id.relsignup);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {

        if(TextUtils.isEmpty(inputEmail.getText().toString())){
            Snackbar.make(relativeLayoutsignup,"Please enter email address",Snackbar.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(inputName.getText().toString())){
            Snackbar.make(relativeLayoutsignup,"Please enter username",Snackbar.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(inputPhoneNumber.getText().toString())){
            Snackbar.make(relativeLayoutsignup,"Please enter phone number",Snackbar.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(inputPassword.getText().toString())){
            Snackbar.make(relativeLayoutsignup,"Please enter password",Snackbar.LENGTH_SHORT).show();

            return;
        }

        if(inputPassword.getText().toString().length()<= 6){
            Snackbar.make(relativeLayoutsignup,"Password too short !!!",Snackbar.LENGTH_SHORT).show();
            return;
        }

        //signup new users
        auth.createUserWithEmailAndPassword(inputEmail.getText().toString(),inputPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //add users to db
                        Users user = new Users();
                        user.setEmail(inputEmail.getText().toString());
                        user.setName(inputName.getText().toString());
                        user.setPhone(inputPhoneNumber.getText().toString());
                        user.setPassword(inputPassword.getText().toString());

                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(relativeLayoutsignup,"SignUp Successfully",Snackbar.LENGTH_SHORT)
                                                .show();

                                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(relativeLayoutsignup,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(relativeLayoutsignup,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });


    }
}
