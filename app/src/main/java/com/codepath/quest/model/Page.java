package com.codepath.quest.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Page")
public class Page extends ParseObject {
    public static void createPage(String subjectPage) {
    }

    // filter queries by user with
    // query.whereEqualTo(String key (the field under the object in PostQuery<object>)
    //                    ,Object obj (what should match what is obtained from the key))
    public static void queryPages() {

    }

    public static void updatePageName(String newPageName) {

    }

    // Deleting a subject deletes the  sections, pages, and questions that
    // fall under the subject.
    public static void deletePage() {

    }
}
