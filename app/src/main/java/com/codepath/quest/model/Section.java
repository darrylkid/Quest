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

@ParseClassName("Section")
public class Section extends Category {
    private static final String KEY_SECTION = "Section";

    /**
     * Crates a section inside the Parse database.
     *
     * @param description the name to give the subject
     * @param currentUser the user saving the subject
     * @param parentSubject the parent subject of this subject instance
     */
    public void save(String description, ParseUser currentUser, Subject parentSubject) {
        // Set the attributes of this subject.
        this.setDescription(description);
        this.setUser(currentUser);
        this.setParent(parentSubject);

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

    public static void querySections(CategoryAdapter adapter, Subject parentSubject) {
        ParseQuery<Section> query = ParseQuery.getQuery(Section.class);

        // Filter the query to find sections under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Filter the query to find sections under the parent subject.
        query.whereEqualTo(Constants.KEY_PARENT, parentSubject);

        FindCallback<Section> findSectionsCallBack = new FindCallback<Section>() {
            @Override
            public void done(List<Section> objects, ParseException e) {
                if (e == null) {
                    // Success! We obtained the subjects!
                    adapter.addAll(objects);
                } else {
                    // Failure in querying the subjects.
                    Log.e(KEY_SECTION, "Failed to query sections.", e);
                }
            }
        };
        query.findInBackground(findSectionsCallBack);
    }

    /**
     * Work in progress...
     */
    public Subject queryParent() {
        ParseQuery<Subject> query = ParseQuery.getQuery(Subject.class);

        // This helper class exists to pass the subject outside the
        // callback object.
        class SubjectHolder {
            Subject subject;

            private void setSubject(Subject subject) {
                this.subject = subject;
            }
        }
        SubjectHolder subjectHolder = new SubjectHolder();
        FindCallback<Subject> findParentSubjectHandler = new FindCallback<Subject>() {
            @Override
            public void done(List<Subject> subjectList, ParseException e) {
                subjectHolder.setSubject(subjectList.get(0));
                Log.i(Constants.KEY_SECTION, "Getting parent success!");
            }
        };
        query.findInBackground(findParentSubjectHandler);

        return subjectHolder.subject;
    }

}
