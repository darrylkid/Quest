package com.codepath.quest.helper;

import android.util.Log;

import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A container that holds the common methods between all category types
 * (i.e. Subject, Section and Page)
 */
public class Category extends ParseObject {
    public String getDescription() {
        return this.getString(HomeActivity.KEY_DESCRIPTION);
    }

    public void setDescription(String subjectName) {
        this.put(HomeActivity.KEY_DESCRIPTION, subjectName);
    }

    public void setUser(ParseUser currentUser) {
        this.put(HomeActivity.KEY_USER, currentUser);
    }

    public void setParent(Subject parentSubject) {
        this.put(HomeActivity.KEY_PARENT, parentSubject);
    }

    public void setParent(Section parentSection) {
        this.put(HomeActivity.KEY_PARENT, parentSection);
    }

    public void setParent(Page parentPage) {
        this.put(HomeActivity.KEY_PARENT, parentPage);
    }

}
