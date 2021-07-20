package com.codepath.quest.model;

import android.util.Log;

import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.helper.Category;
import com.codepath.quest.helper.QuestToast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Subject")
public class Subject extends Category {
    // Empty constructor required for Parse.
    public Subject(){};

    /**
     * Gets all the subjects under the current logged in user
     * and adds the subjects to the adapter.
     */
    public static void querySubjects(CategoryAdapter adapter) {
        ParseQuery<Subject> query = ParseQuery.getQuery(Subject.class);

        // Filters the query to find subjects under the current user.
        query.whereEqualTo(HomeActivity.KEY_USER, ParseUser.getCurrentUser());

        FindCallback<Subject> findSubjectsCallBack = new FindCallback<Subject>() {
            @Override
            public void done(List<Subject> subjects, ParseException e) {
                if (e == null) {
                    // Success! We obtained the subjects!
                    adapter.addAll(subjects);
                } else {
                    // Failure in querying the subjects.
                    Log.e(HomeActivity.KEY_SUBJECT, "Failed to query subjects.", e);
                }
            }
        };
        query.findInBackground(findSubjectsCallBack);
    }

    /**
     * Progress is still being made.
     *
     * @param objectId
     * @param newSubjectName
     */
    /*
    public static void updateSubjectName(String objectId, String newSubjectName) {
        ParseObject subject = new ParseObject(KEY_SUBJECT);
        subject.put(HomeActivity.KEY_DESCRIPTION, newSubjectName);
        subject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Successfully changed the subject name!
                    Log.i(KEY_SUBJECT, "Successfully changed the subject name!");
                } else {
                    Log.e(KEY_SUBJECT, "Failed to update the subject name.", e);
                }
            }
        });
    }

     */

    // Deleting a subject deletes the  sections, pages, and questions that
    // fall under the subject.

    /**
     * Progress is still being made.
     *
     */
    public static void deleteSubject() {

    }



}
