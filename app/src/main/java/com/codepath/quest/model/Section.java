package com.codepath.quest.model;

import android.util.Log;
import android.view.View;

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

@ParseClassName("Section")
public class Section extends Category {
    private static final String KEY_SECTION = "Section";

    public Subject getParent() {
        return (Subject) this.getParseObject(Constants.KEY_PARENT);
    }

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

    public static void querySections(RecyclerView.Adapter adapter, Subject parentSubject) {
        ParseQuery<Section> query = ParseQuery.getQuery(Section.class);
        query.include(Constants.KEY_PARENT);

        // Filter the query to find sections under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Filter the query to find sections under the parent subject.
        query.whereEqualTo(Constants.KEY_PARENT, parentSubject);

        FindCallback<Section> findSectionsCallBack = new FindCallback<Section>() {
            @Override
            public void done(List<Section> sections, ParseException e) {
                if (e == null) {
                    // Success! We obtained the subjects!
                    if (adapter instanceof CategoryAdapter) {
                        ((CategoryAdapter)adapter).addAll(sections);
                    } else if (adapter instanceof CategoryInDialogAdapter) {
                        ((CategoryInDialogAdapter)adapter).addAll(sections);
                    }
                } else {
                    // Failure in querying the subjects.
                    Log.e(KEY_SECTION, "Failed to query sections.", e);
                }
            }
        };
        query.findInBackground(findSectionsCallBack);
    }

    /**
     * The mindmap version of querying the sections.
     *
     * @param mindMapRenderer the renderer for the mind map
     * @param parentSubject the parent subject to filter the section search with
     */
    public static void querySections(MindMapRenderer mindMapRenderer, Subject parentSubject) {
        ParseQuery<Section> query = ParseQuery.getQuery(Section.class);
        query.include(Constants.KEY_PARENT);

        // Filter the query to find sections under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Filter the query to find sections under the parent subject.
        query.whereEqualTo(Constants.KEY_PARENT, parentSubject);

        FindCallback<Section> findSectionsCallBack = new FindCallback<Section>() {
            @Override
            public void done(List<Section> sections, ParseException e) {
                mindMapRenderer.renderSections(sections, parentSubject);
            }
        };
        query.findInBackground(findSectionsCallBack);
    }

}
