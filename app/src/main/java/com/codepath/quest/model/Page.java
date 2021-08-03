package com.codepath.quest.model;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.adapter.MiniCategoryAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("Page")
public class Page extends Category {

    public Section getParent() {
        return (Section) this.getParseObject(Constants.KEY_PARENT);
    }

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

        public static void queryPages(RecyclerView.Adapter adapter, Section parentSection) {
            ParseQuery<Page> query = ParseQuery.getQuery(Page.class);
            query.include(Constants.KEY_PARENT);

            // Filter the query to find sections under the current user.
            query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

            // Filter the query to find sections under the parent subject.
            query.whereEqualTo(Constants.KEY_PARENT, parentSection);

            FindCallback<Page> findPagesCallBack = new FindCallback<Page>() {
                @Override
                public void done(List<Page> pages, ParseException e) {
                    if (e == null) {
                        // Success! We obtained the subjects!
                        if (adapter instanceof CategoryAdapter) {
                            ((CategoryAdapter)adapter).addAll(pages);
                        } else if (adapter instanceof MiniCategoryAdapter) {
                            ((MiniCategoryAdapter)adapter).addAll(pages);
                        }

                    } else {
                        // Failure in querying the subjects.
                        Log.e(Constants.KEY_PAGE, "Failed to query pages.", e);
                    }
                }
            };
            query.findInBackground(findPagesCallBack);
        }

}
