package com.codepath.quest.model;

import android.util.Log;

import com.codepath.quest.adapter.CategoryAdapter;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Page")
public class Page extends Category {

    /**
     * Crates a page inside the Parse database.
     *
     * @param description the name to give the subject
     * @param currentUser the user saving the subject
     * @param parentSection the parent subject of this subject instance
     */
    public void save(String description, ParseUser currentUser, Section parentSection) {
        // Set the attributes of this subject.
        this.setDescription(description);
        this.setUser(currentUser);
        this.setParent(parentSection);

        // Save the subject inside the database.
        SaveCallback saveHandler = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(Constants.KEY_SUBJECT, "Category Creation Successful!");
                } else {
                    Log.e(Constants.KEY_SUBJECT, "Category Creation Failed...", e);
                }
            }
        };
        this.saveInBackground(saveHandler);
    }

        public static void queryPages(CategoryAdapter adapter, Section parentSection) {
            ParseQuery<Page> query = ParseQuery.getQuery(Page.class);

            // Filter the query to find sections under the current user.
            query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

            // Filter the query to find sections under the parent subject.
            query.whereEqualTo(Constants.KEY_PARENT, parentSection);

            FindCallback<Page> findPagesCallBack = new FindCallback<Page>() {
                @Override
                public void done(List<Page> objects, ParseException e) {
                    if (e == null) {
                        // Success! We obtained the subjects!
                        adapter.addAll(objects);
                    } else {
                        // Failure in querying the subjects.
                        Log.e(Constants.KEY_PAGE, "Failed to query pages.", e);
                    }
                }
            };
            query.findInBackground(findPagesCallBack);
        }

}
