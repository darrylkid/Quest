package com.codepath.quest.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Section")
public class Section extends ParseObject {
    public static void createSection(String sectionName) {
    }

    // filter queries by user with
    // query.whereEqualTo(String key (the field under the object in PostQuery<object>)
    //                    ,Object obj (what should match what is obtained from the key))
    public static void querySections() {

    }

    public static void updateSectionName(String newSectionName) {

    }

    // Deleting a subject deletes the  sections, pages, and questions that
    // fall under the subject.
    public static void deleteSection() {

    }
}
