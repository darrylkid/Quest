package com.codepath.quest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnSignupInLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add automatic login functionality by checking to see
        // an user is logged in already.
        if (ParseUser.getCurrentUser() != null) {
            Navigation.goToHomeActivity(LoginActivity.this);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        // Set up onClickListener for the login button.
        btnLogin = findViewById(R.id.btnLogin);
        View.OnClickListener loginHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab the text from the username and password edit text views.
                EditText etUsernameLogin = findViewById(R.id.etUsernameLogin);
                EditText etPasswordLogin = findViewById(R.id.etPasswordLogin);
                String username = etUsernameLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Error handling for valid user input.
                if (username.equals("")) {
                    QuestToast.pleaseEnterX(LoginActivity.this, "username");
                    return;
                }

                if (password.equals("")) {
                    QuestToast.pleaseEnterX(LoginActivity.this, "password");
                    return;
                }

                // Valid input. Try logging in.
                login(username, password);
                finish();
            }
        };
        btnLogin.setOnClickListener(loginHandler);

        // Set up onClickListener for the signup button.
        btnSignupInLoginActivity = findViewById(R.id.btnSignupInLoginActivity);
        View.OnClickListener signupHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.goToSignupActivity(LoginActivity.this);
                finish();
            }
        };
        btnSignupInLoginActivity.setOnClickListener(signupHandler);
    }


    /**
     * Signs in the user from the Parse database with the given
     * username and password. Goes to the HomeActivity if successful.
     *
     * @param username the username to login with from the Parse database
     * @param password the password to login with from the Parse database
     */
    public void login(String username, String password) {
        LogInCallback loginHandler = new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                String msg = "Login";
                if (e == null) {
                    // Success! The user is successfully logged in and
                    // can go to the HomeActivity.
                    QuestToast.xSuccessful(LoginActivity.this, msg);
                    Navigation.goToHomeActivity(LoginActivity.this);
                    finish();
                } else {
                    // Login failed.
                    QuestToast.xFailed(LoginActivity.this, msg);
                }
            }
        };
        ParseUser.logInInBackground(username, password, loginHandler);
    }
}