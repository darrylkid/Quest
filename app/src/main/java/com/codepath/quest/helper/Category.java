package com.codepath.quest.helper;

import android.util.Log;

import com.codepath.quest.activity.HomeActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A container that holds the common methods between all category types
 * (i.e. Subject, Section and Page)
 */
public class Category extends ParseObject {

    /**
     * Crates a subject inside the Parse database.
     *
     * @param description the name to give the subject
     * @param currentUser the user saving the subject
     */
    public void save(String description, ParseUser currentUser) {
        // Set the attributes of this subject.
        this.setDescription(description);
        this.setUser(currentUser);

        // Save the subject inside the database.
        SaveCallback saveHandler = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(HomeActivity.KEY_SUBJECT, "Category Creation Successful!");
                } else {
                    Log.e(HomeActivity.KEY_SUBJECT, "Category Creation Failed...", e);
                }
            }
        };
        this.saveInBackground(saveHandler);
    }

    public String getDescription() {
        return this.getString(HomeActivity.KEY_DESCRIPTION);
    }

    public void setDescription(String subjectName) {
        this.put(HomeActivity.KEY_DESCRIPTION, subjectName);
    }

    public void setUser(ParseUser currentUser) {
        this.put(HomeActivity.KEY_USER, currentUser);
    }
}
