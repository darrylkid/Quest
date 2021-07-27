package com.codepath.quest.helper;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.activity.LoginActivity;
import com.codepath.quest.activity.SignupActivity;
import com.codepath.quest.fragment.NotesFragment;
import com.codepath.quest.fragment.PagesFragment;
import com.codepath.quest.fragment.SectionsFragment;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;


/**
 *  Project-defined static class responsible for
 *  navigating to different activities and fragments.
 */
public class Navigation {
    private static FragmentManager fragmentManager = null;

    public static void setFragmentManager(FragmentManager fm) { fragmentManager = fm; }
    public static FragmentManager getFragmentManager() { return fragmentManager; }

    // Activities.
    public static void goToHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void goToSignupActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        context.startActivity(intent);
    }

    public static void goToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    // Fragments.

    /**
     * Sets the current fragment to the Sections Fragment.
     *
     * @param subject the subject object for section objects to reference.
     */
    public static void fromSubjectsToSections(Subject subject) {
        SectionsFragment sectionsFragment = SectionsFragment.newInstance(subject);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.quest_fragment_container, sectionsFragment).commit();
    }

    public static void fromSectionsToPages(Section section) {
        PagesFragment pagesFragment = PagesFragment.newInstance(section);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.quest_fragment_container, pagesFragment).commit();
    }

    public static void fromPagesToNotes(Page page) {
        NotesFragment notesFragment = NotesFragment.newInstance(page);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.quest_fragment_container, notesFragment).commit();
    }

}
