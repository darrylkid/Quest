package com.codepath.quest.helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.activity.LoginActivity;
import com.codepath.quest.activity.SignupActivity;


/**
 *  Project-defined static class responsible for
 *  navigating to different activities and fragments.
 */
public class Navigation {
    // Activities.

    public static void goToHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void goToSignupActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        context.startActivity(intent);
    }

    public static void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

}
