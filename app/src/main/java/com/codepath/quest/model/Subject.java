package com.codepath.quest.model;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.adapter.CategoryInDialogAdapter;
import com.codepath.quest.helper.MindMapRenderer;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Subject")
public class Subject extends Category {
    // Empty constructor required for Parse.
    public Subject(){};


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
                    Log.i(Constants.KEY_SUBJECT, "Category Creation Successful!");
                } else {
                    Log.e(Constants.KEY_SUBJECT, "Category Creation Failed...", e);
                }
            }
        };
        this.saveInBackground(saveHandler);
    }

    /**
     * Gets all the subjects under the current logged in user
     * and adds the subjects to the adapter.
     */
    public static void querySubjects(RecyclerView.Adapter adapter) {
        ParseQuery<Subject> query = ParseQuery.getQuery(Subject.class);

        // Filters the query to find subjects under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        FindCallback<Subject> findSubjectsCallBack = new FindCallback<Subject>() {
            @Override
            public void done(List<Subject> subjects, ParseException e) {
                if (e == null) {
                    // Success! We obtained the subjects!
                    if (adapter instanceof CategoryAdapter) {
                        ((CategoryAdapter)adapter).addAll(subjects);
                    } else if (adapter instanceof CategoryInDialogAdapter) {
                        ((CategoryInDialogAdapter)adapter).addAll(subjects);
                    }
                } else {
                    // Failure in querying the subjects.
                    Log.e(Constants.KEY_SUBJECT, "Failed to query subjects.", e);
                }
            }
        };
        query.findInBackground(findSubjectsCallBack);
    }

    /**
     * The mindmap version of querying the sections.
     *
     * @param mindMapRenderer the renderer for the mind map
     */
    public static void querySubjects(MindMapRenderer mindMapRenderer) {
        ParseQuery<Subject> query = ParseQuery.getQuery(Subject.class);

        // Filter the query to find subjects under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        FindCallback<Subject> findSectionsCallBack = new FindCallback<Subject>() {
            @Override
            public void done(List<Subject> subjects, ParseException e) {
                mindMapRenderer.renderSubjects(subjects);
            }
        };
        query.findInBackground(findSectionsCallBack);
    }



}
