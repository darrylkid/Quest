package com.codepath.quest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnSignupInLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up onClickListener for the login button.
        btnLogin = findViewById(R.id.btnLogin);
        View.OnClickListener loginHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.goToHomeActivity(LoginActivity.this);
            }
        };
        btnLogin.setOnClickListener(loginHandler);

        // Set up onClickListener for the signup button.
        btnSignupInLoginActivity = findViewById(R.id.btnSignupInLoginActivity);
        View.OnClickListener signupHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.goToSignupActivity(LoginActivity.this);
            }
        };
        btnSignupInLoginActivity.setOnClickListener(signupHandler);
    }
}