package com.codepath.quest.model;

import android.util.Log;

import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.helper.Category;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Page")
public class Page extends Category {
        public static void queryPages(CategoryAdapter adapter, Section parentSection) {
            ParseQuery<Page> query = ParseQuery.getQuery(Page.class);

            // Filter the query to find sections under the current user.
            query.whereEqualTo(HomeActivity.KEY_USER, ParseUser.getCurrentUser());

            // Filter the query to find sections under the parent subject.
            query.whereEqualTo(HomeActivity.KEY_PARENT, parentSection);

            FindCallback<Page> findPagesCallBack = new FindCallback<Page>() {
                @Override
                public void done(List<Page> objects, ParseException e) {
                    if (e == null) {
                        // Success! We obtained the subjects!
                        adapter.addAll(objects);
                    } else {
                        // Failure in querying the subjects.
                        Log.e(HomeActivity.KEY_PAGE, "Failed to query pages.", e);
                    }
                }
            };
            query.findInBackground(findPagesCallBack);
        }

}
