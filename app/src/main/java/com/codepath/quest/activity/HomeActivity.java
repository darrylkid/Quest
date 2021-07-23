package com.codepath.quest.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.codepath.quest.R;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.fragment.RecentQuestionsFragment;
import com.codepath.quest.fragment.SearchFragment;
import com.codepath.quest.fragment.SubjectsFragment;
import com.codepath.quest.helper.Category;
import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    // Constants for the data models and fragments to use.
    public static final String KEY_PARENT = "parent";
    public static final String KEY_SUBJECT = "Subject";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_PAGE = "Page";
    public static final String KEY_QUESTION = "Question";
    public static final String KEY_USER = "user";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DIALOG = "dialog";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_SUBTITLE = "subtitle";


    private Fragment currentFragment;

    // Keeping track of the current category
    // for child categories to access.
    private static Subject currentSubject;
    private static Section currentSection;
    private static Page currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Navigation.setFragmentManager(getSupportFragmentManager());
        final BottomNavigationView bnvHome = findViewById(R.id.bnvHome);

        // Set up app bar.
        Toolbar toolbar = findViewById(R.id.tbHome);
        setSupportActionBar(toolbar);

        // Initialize the home activity to display the
        // recent questions fragment.
        FragmentTransaction fragmentTransaction = Navigation.getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.quest_fragment_container, new RecentQuestionsFragment())
                .commit();

        // Set up the listener for the bottom navigation bar.
        bnvHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                // Select the fragment corresponding to the menu item clicked.
                FragmentTransaction fragmentTransaction = Navigation.getFragmentManager()
                        .beginTransaction();
                int selectedItemId = item.getItemId();
                if (selectedItemId == R.id.actionHome) {
                    currentFragment = new RecentQuestionsFragment();
                } else if (selectedItemId == R.id.actionSearch) {
                    currentFragment = new SearchFragment();
                } else if (selectedItemId == R.id.actionSubjects) {
                    currentFragment = new SubjectsFragment();
                } else {
                    // Default: select the recent questions fragment.
                    currentFragment = new RecentQuestionsFragment();
                }
                // Link the fragments' XML layouts to the fragments in Java code and show
                // the fragment.
                fragmentTransaction.replace(R.id.quest_fragment_container, currentFragment)
                        .commit();
                return true;
            }
        });
    }

    // Inflate menu resource to the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_top_with_logout, menu);
        return true;
    }

    // On click listener for the action bar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItemId = item.getItemId();

        if (selectedItemId == R.id.iLogoutHome) {
            // Log out menu item.
            logOut();
            return true;
        }
        return true;
    }

    /**
     * Logs the user out from the Parse database and
     * navigates back to the login activity.
     */
    public void logOut() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success!
                    QuestToast.successful(HomeActivity.this, "Logout");
                } else {
                    // Failure.
                    QuestToast.failed(HomeActivity.this, "Logout");
                }
            }
        });
        Navigation.goToLoginActivity(HomeActivity.this);
        finish();
    }

    /**
     * Sets the title and subtitle of the HomeActivity toolbar shared
     * between all fragments.
     *
     * @param title the text to set the action bar title
     * @param subtitle the text to set the action bar subtitle
     */
    public static void setToolbarText(Toolbar toolbar, String title, @Nullable String subtitle) {
            toolbar.setTitle(title);
            toolbar.setSubtitle(subtitle);
    }

    /**
     * Animates the color from one color to another.
     */
    public static void setToolbarColor(Toolbar toolbar, int colorStart, int ColorEnd) {
        ValueAnimator animation = ValueAnimator.ofArgb(colorStart, ColorEnd);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                toolbar.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        animation.setDuration(250);
        animation.start();
    }

    public static int getToolbarColor(Toolbar toolbar) {
        return ((ColorDrawable) toolbar.getBackground()).getColor();
    }

    public static void startProgressBar(Context context) {
        RelativeLayout progressBarContainer = ((HomeActivity)context)
                .findViewById(R.id.progress_bar_container);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    public static void stopProgressBar(Context context) {
        RelativeLayout progressBarContainer = ((HomeActivity)context)
                .findViewById(R.id.progress_bar_container);
        progressBarContainer.setVisibility(View.GONE);
    }

    public static void preventUserInteraction(Context context) {
        ((HomeActivity) context).getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                , WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    public static void allowUserInteraction(Context context) {
        ((HomeActivity) context).getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    public static void deleteSelectedItems(Context context
                                           , CategoryAdapter adapter
                                           , List<Integer> selectedItemPositions) {
        HomeActivity.startProgressBar(context);
        // If we remove each item from the adapter, we will run
        // into undefined behavior since the selectedItemPositions
        // relies on the adapter to be static.
        for (Integer itemPosition : selectedItemPositions) {
            Category category = (Category)adapter.getCategory(itemPosition);
            category.deleteInBackground();
            category.setDeleteFlag(true);
        }

        // Remove the flagged categories.
        List<ParseObject> newCategoryList = new ArrayList<>();
        for (ParseObject category: adapter.getCategoryList()) {
            if (!((Category) category).getDeleteFlag()) {
                newCategoryList.add(category);
            }
        }
        adapter.setCategoryList(newCategoryList);
        HomeActivity.stopProgressBar(context);
    }

    public static MenuItem getDeleteMenuItem(Context context, int deleteMenuItemPosition) {
        Toolbar toolbar = ((HomeActivity)context).findViewById(R.id.tbHome);
        Menu currentMenu = toolbar.getMenu();
        return currentMenu.getItem(deleteMenuItemPosition);
    }

    public static MenuItem getEditMenuItem(Context context, int editMenuItemPosition) {
        Toolbar toolbar = ((HomeActivity)context).findViewById(R.id.tbHome);
        Menu currentMenu = toolbar.getMenu();
        return currentMenu.getItem(editMenuItemPosition);
    }

    public static void startCategoryOnClickMenuListeners(Context context
                                                        , MenuItem.OnMenuItemClickListener editHandler
                                                        , MenuItem.OnMenuItemClickListener deleteHandler) {
        int editPosition = 0;
        int deletePosition = 1;
        MenuItem editMenuItem = HomeActivity.getEditMenuItem(context, editPosition);
        MenuItem deleteMenuItem = HomeActivity.getDeleteMenuItem(context, deletePosition);

        editMenuItem.setOnMenuItemClickListener(editHandler);
        deleteMenuItem.setOnMenuItemClickListener(deleteHandler);
    }

    /**
     *
     * @param subject
     */

   // public static void setAction

    public static void setCurrentSubject(Subject subject) {
        currentSubject = subject;
    }

    public static void setCurrentSection(Section section) {
        currentSection = section;
    }

    public static void setCurrentPage(Page page) {
        currentPage = page;
    }

    public static Subject getCurrentSubject() {
        return currentSubject;
    }

    public static Section getCurrentSection() {
        return currentSection;
    }

    public static Page getCurrentPage() {
        return currentPage;
    }
}