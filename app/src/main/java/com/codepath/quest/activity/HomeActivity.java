package com.codepath.quest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.quest.R;
import com.codepath.quest.fragment.RecentQuestionsFragment;
import com.codepath.quest.fragment.SearchFragment;
import com.codepath.quest.fragment.SubjectsFragment;
import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

public class HomeActivity extends AppCompatActivity {
    // Constants for the data models and fragments to use.
    public static final String KEY_PARENT = "parent";
    public static final String KEY_USER = "user";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DIALOG = "dialog";
    public static final String KEY_REQUEST = "request";

    private Fragment currentFragment;
    private final int FRAG_CONTAINER_ID = R.id.rlHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final BottomNavigationView bnvHome = findViewById(R.id.bnvHome);

        // Initialize the home activity to display the
        // recent questions fragment.
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(FRAG_CONTAINER_ID, new RecentQuestionsFragment())
                .commit();

        // Set up the listener for the bottom navigation bar.
        bnvHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                // Select the fragment corresponding to the menu item clicked.
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                int selectedItemId = item.getItemId();
                if (selectedItemId == R.id.actionHome) {
                    currentFragment = new RecentQuestionsFragment();
                } else if (selectedItemId == R.id.actionSearch) {
                    currentFragment = new SearchFragment();
                } else if (selectedItemId == R.id.actionSubjects) {
                    currentFragment = new SubjectsFragment();
                } else {
                    // Default: select the recent questions fragment.
                    currentFragment = new RecentQuestionsFragment();
                }
                // Link the fragments' XML layouts to the fragments in Java code and show
                // the fragment.
                fragmentTransaction.replace(FRAG_CONTAINER_ID, currentFragment)
                        .commit();
                return true;
            }
        });
    }



    // Inflate menu resource to the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_top, menu);
        return true;
    }

    // On click listener for the action bar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItemId = item.getItemId();

        if (selectedItemId == R.id.iLogoutHome) {
            // Log out menu item.
            logOut();
            return true;
        }
        return true;
    }


    /**
     * Logs the user out from the Parse database and
     * navigates back to the login activity.
     */
    public void logOut() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success!
                    QuestToast.xSuccessful(HomeActivity.this, "Logout");
                } else {
                    // Failure.
                    QuestToast.xFailed(HomeActivity.this, "Logout");
                }
            }
        });
        Navigation.goToLoginActivity(HomeActivity.this);
        finish();
    }
}