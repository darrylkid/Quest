package com.codepath.quest.model;

import android.util.Log;

import com.codepath.quest.adapter.NotesAdapter;
import com.codepath.quest.adapter.SearchAdapter;
import com.codepath.quest.helper.Note;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Question")
public class Question extends Note {
    // Empty constructor for the Parse library.
    public Question(){}

    public Answer getAnswer() {
        return (Answer) this.getParseObject(Constants.KEY_ANSWER);
    }

    public void setAnswer(Answer answer) {
        this.put(Constants.KEY_ANSWER, answer);
        this.saveInBackground();
    }

    public void setParent(Page parent) {
        this.put(Constants.KEY_PARENT, parent);
    }

    public Page getParent() {
        return (Page) this.getParseObject(Constants.KEY_PARENT);
    }

    public static void queryQuestions(NotesAdapter adapter, Page parentPage) throws ParseException {
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.include(Constants.KEY_ANSWER);
        query.include(Constants.KEY_PARENT);

        // Filters the query to find questions under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Filters the query to find questions under the current page.
        query.whereEqualTo(Constants.KEY_PARENT, parentPage);

        FindCallback<Question> findQuestionsCallBack = new FindCallback<Question>() {
            @Override
            public void done(List<Question> questionList, ParseException e) {
                if (e == null) {
                    // Success! We obtained the questions!
                    adapter.addAll(questionList);
                } else {
                    // Failure in querying the subjects.
                    Log.e(Constants.KEY_SUBJECT, "Failed to query questions.", e);
                }
            }
        };
        query.findInBackground(findQuestionsCallBack);
    }

    public static void searchQuestions(SearchAdapter adapter, String input) {
        // Query for questions.
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.include(Constants.KEY_ANSWER);
        query.include(Constants.KEY_PARENT);

        // Filters the query to find questions under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Find questions with descriptions that contain the user's input.
        query.whereContains(Constants.KEY_DESCRIPTION, input);

        FindCallback<Question> queryCallBack = new FindCallback<Question>() {
            @Override
            public void done(List<Question> results, ParseException e) {
                if (e == null) {
                    adapter.addAll(results);
                    //Answer.searchAnswers(adapter, input);
                } else {
                    Log.e("Question", "Failure in search query.", e);
                }
            }
        };
        query.findInBackground(queryCallBack);
    }



}


