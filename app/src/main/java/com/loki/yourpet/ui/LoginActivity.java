package com.loki.yourpet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loki.yourpet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.editTextPersonEmail) EditText mPersonEmail;
    @BindView(R.id.editTextPersonPassword) EditText mPersonPassword;
    @BindView(R.id.registerTextView) TextView mRegisterTextView;

    Boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        isAllFieldsChecked = checkAllFields();

        //logs in user
        if(v == mLoginButton && isAllFieldsChecked) {
            Intent intent = new Intent(LoginActivity.this, PetListActivity.class);
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

    //validate the login form
    private Boolean checkAllFields() {

        if(mPersonEmail.length() == 0 ) {
            mPersonEmail.setError("please input your email");
            return false;
        }
        if (mPersonPassword.length() == 0 ) {
            mPersonPassword.setError("please enter your password");
            return false;
        }
        else if(mPersonPassword.length() < 6) {
            mPersonPassword.setError("password must be minimum 6 characters");
            return false;
        }
        return true;
    }
}