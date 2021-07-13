package com.codepath.quest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.zip.Inflater;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up an onClickListener for the sign up button.
        Button btnSubmitSignup = findViewById(R.id.btnSubmitSignup);
        View.OnClickListener submitHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab the text from the username and password edit text views.
                EditText etUsername = findViewById(R.id.etUsernameSignup);
                EditText etPassword = findViewById(R.id.etPasswordSignup);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Error handling for invalid user username.
                if (username.equals("")) {
                    QuestToast.pleaseEnterX(SignupActivity.this, "username");
                    return;
                }

                // Error handling for invalid user password.
                if (password.equals("")) {
                    QuestToast.pleaseEnterX(SignupActivity.this, "password");
                    return;
                }

                // Proceed in registering the user into the database.
                signup(username, password);
            }
        };
        btnSubmitSignup.setOnClickListener(submitHandler);
    }

    /**
     * Signs up a new user to the Parse database and
     * navigtes to the LoginActivity.
     *
     * @param username the username to register to the Parse database
     * @param password the password to register to the Parse database
     */
    public void signup(String username, String password) {
        // Creates a new parse user account in the database.
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);

        // Atomic Reference is used to mutate 'isSuccessful' inside
        // the anonymous local class.

        SignUpCallback signupHandler = new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                String msg = "Signup";
                if (e == null) {
                    // Success! User has signed up successfully.
                    QuestToast.xSuccessful(SignupActivity.this, msg);
                    Navigation.goToHomeActivity(SignupActivity.this);
                    finish();
                } else {
                    QuestToast.xFailed(SignupActivity.this, msg);
                }
            }
        };
        newUser.signUpInBackground(signupHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the action bar with the back menu.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miBackSignup) {
            // Back button.
            Navigation.goToLoginActivity(SignupActivity.this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}