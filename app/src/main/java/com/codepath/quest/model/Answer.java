package com.codepath.quest.model;

import com.codepath.quest.helper.Note;
import com.parse.ParseClassName;

@ParseClassName("Answer")
public class Answer extends Note {
    // Empty constructor for the Parse library.
    public Answer(){}

    // Parent.

    public void setParent(Question parent) {
        this.put(Constants.KEY_PARENT, parent);
    }
}
