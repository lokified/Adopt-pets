package com.loki.yourpet.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.loki.yourpet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.editTextPersonEmail) EditText mPersonEmail;
    @BindView(R.id.editTextPersonPassword) EditText mPersonPassword;
    @BindView(R.id.registerTextView) TextView mRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        //authenticate user log in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, PetListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mLoginButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //logs in user
        if(v == mLoginButton) {
            Intent intent = new Intent(LoginActivity.this, PetListActivity.class);
            loginWithPassword();
            Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        //navigates to create account activity
        if (v == mRegisterTextView) {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void loginWithPassword() {
        //validate the login form

        String email = mPersonEmail.getText().toString();
        String password = mPersonPassword.getText().toString();

        if(email.equals("")) {
            mPersonEmail.setError("please input your email");
            return ;
        }
        if (password.equals("")) {
            mPersonPassword.setError("please enter your password");
            return ;
        }

        //listens from firebase
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}