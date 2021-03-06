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
    private int rank;

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

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setOrder(int order) { this.put(Constants.KEY_ORDER, order); }

    public static void queryQuestions(NotesAdapter adapter, Page parentPage) throws ParseException {
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.include(Constants.KEY_ANSWER);
        query.include(Constants.KEY_PARENT);

        // Filters the query to find questions under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Filters the query to find questions under the current page.
        query.whereEqualTo(Constants.KEY_PARENT, parentPage);

        // Query the questions according to each Question's order (defined
        // when the user drags and drops a Question to a different position)
        query.orderByAscending(Constants.KEY_ORDER);


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



}


