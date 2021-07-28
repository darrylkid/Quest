package com.codepath.quest.model;

import android.util.Log;

import com.codepath.quest.adapter.SearchAdapter;
import com.codepath.quest.helper.Note;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Answer")
public class Answer extends Note {
    // Empty constructor for the Parse library.
    public Answer(){}

    // Parent.

    public void setParent(Question parent) {
        this.put(Constants.KEY_PARENT, parent);
    }

    public static void searchAnswers(SearchAdapter adapter, String input) {
        // Query for questions.
        ParseQuery<Question> questionQuery = ParseQuery.getQuery(Question.class);
        questionQuery.include(Constants.KEY_ANSWER);
        questionQuery.include(Constants.KEY_PARENT);

        // Filters the query to find questions under the current user.
        questionQuery.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Find questions with descriptions that contain the user's input.
        questionQuery.whereContains(Constants.KEY_DESCRIPTION, input);

        FindCallback<Question> queryCallBack = new FindCallback<Question>() {
            @Override
            public void done(List<Question> results, ParseException e) {
                if (e == null) {
                    adapter.addAll(results);
                } else {
                    Log.e("Question", "Failure in search query.", e);
                }
            }
        };
        questionQuery.findInBackground(queryCallBack);
    }

}
