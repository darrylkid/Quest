package com.codepath.quest;

import android.content.Context;
import android.widget.Toast;

/**
 * Project-defined static class responsible for
 * printing Quest-specific toast messages.
 */
public class QuestToast {

    /**
     * Prints the short toast message: "Please enter the X."
     *
     * @param context the activity/fragment the toast is printing inside of
     * @param X the name of what the Toast is asking to enter
     */
    public static void pleaseEnterX(Context context, String X) {
        String errorMsg = "Please enter the " + X + ".";
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Prints the short toast message: "X failed. Please try again."
     *
     * @param context the activity/fragment the toast is printing inside of
     * @param X a description of what failed
     */
    public static void xFailed(Context context, String X) {
        String errorMsg = X + " failed. Please try again.";
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Prints the short toast message: "X Successful!"
     *
     * @param context the activity/fragment the toast is printing inside of
     * @param X a description of what was successful
     */
    public static void xSuccessful(Context context, String X) {
        String successMsg = X + " Successful!";
        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show();
    }


}
