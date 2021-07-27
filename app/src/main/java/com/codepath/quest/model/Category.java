package com.codepath.quest.model;


import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * A container that holds the common methods between all category types
 * (i.e. Subject, Section and Page)
 */
public class Category extends ParseObject {
    private boolean deleteFlag = false;

    // Deletion.
    public void setDeleteFlag(boolean val) {
        this.deleteFlag = val;
    }

    public boolean getDeleteFlag() {
        return this.deleteFlag;
    }

    // Description.

    public String getDescription() {
        return this.getString(Constants.KEY_DESCRIPTION);
    }

    public void setDescription(String newDescription) {
        this.put(Constants.KEY_DESCRIPTION, newDescription);
        this.saveInBackground();
    }

    // User.

    public void setUser(ParseUser currentUser) {
        this.put(Constants.KEY_USER, currentUser);
    }

    // Parent.

    public void setParent(Subject parentSubject) {
        this.put(Constants.KEY_PARENT, parentSubject);
    }

    public void setParent(Section parentSection) {
        this.put(Constants.KEY_PARENT, parentSection);
    }

    public void setParent(Page parentPage) {
        this.put(Constants.KEY_PARENT, parentPage);
    }
}
