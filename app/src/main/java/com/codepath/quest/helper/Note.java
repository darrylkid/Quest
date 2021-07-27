package com.codepath.quest.helper;

import com.codepath.quest.model.Constants;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Note extends ParseObject {
    // Description.

    public String getDescription() {
        return this.getString(Constants.KEY_DESCRIPTION);
    }

    public void setDescription(String newDescription) {
        this.put(Constants.KEY_DESCRIPTION, newDescription);
    }

    // User
    public void setUser(ParseUser currentUser) {
        this.put(Constants.KEY_USER, currentUser);
    }
}
