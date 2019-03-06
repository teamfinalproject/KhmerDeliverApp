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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kanhrithnon.khmerdeliverapp.Model.Users;

import dmax.dialog.SpotsDialog;

public class SignInActivity extends AppCompatActivity {

    EditText inputEmail,inputPassword;
    Button SignIn;
    RelativeLayout relativeLayoutSignIn;

    FirebaseDatabase db;
    FirebaseAuth auth;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //link to signup
        TextView createAccount = findViewById(R.id.link_signup);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        //find id
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        relativeLayoutSignIn = findViewById(R.id.relsignin);
        SignIn = findViewById(R.id.btn_signin);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn() {

        final SpotsDialog waitingDialog = new SpotsDialog(SignInActivity.this);
        waitingDialog.show();

        SignIn.setEnabled(false);

        if(TextUtils.isEmpty(inputEmail.getText().toString())){
            Snackbar.make(relativeLayoutSignIn,"Please enter email address",Snackbar.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(inputPassword.getText().toString())){
            Snackbar.make(relativeLayoutSignIn,"Please enter password",Snackbar.LENGTH_SHORT).show();

            return;
        }

        if(inputPassword.getText().toString().length()<= 6){
            Snackbar.make(relativeLayoutSignIn,"Password too short !!!",Snackbar.LENGTH_SHORT).show();
            return;
        }

        //sign in
        auth.signInWithEmailAndPassword(inputEmail.getText().toString(),inputPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        waitingDialog.dismiss();

                        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(relativeLayoutSignIn,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .show();

                        SignIn.setEnabled(true);
                    }
                });


    }


}
