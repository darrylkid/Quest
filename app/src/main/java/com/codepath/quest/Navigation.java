package com.codepath.quest;

import android.content.Context;
import android.content.Intent;


/**
 *  Project-defined static class responsible for
 *  navigating to different activities and fragments.
 */
public class Navigation {
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
