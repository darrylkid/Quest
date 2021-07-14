package com.codepath.quest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.codepath.quest.R;
import com.codepath.quest.fragment.RecentQuestionsFragment;
import com.codepath.quest.fragment.SearchFragment;
import com.codepath.quest.fragment.SubjectsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

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


}