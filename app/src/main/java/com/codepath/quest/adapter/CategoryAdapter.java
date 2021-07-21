package com.codepath.quest.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.helper.SelectionClearer;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.card.MaterialCardView;
import com.parse.Parse;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Inflates text views and binds category data (i.e. subject, section, and page).
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<ParseObject> categoryList;
    private SelectionClearer selectionClearer;

    // Variables used for selection logic.
    private boolean selectionModeOn;
    private List<Integer> selectedCategoryPositions;

    public CategoryAdapter(Context context, List<ParseObject> categoryList
                          , SelectionClearer selectionClearer) {
        this.context = context;
        this.categoryList = categoryList;
        this.selectionClearer = selectionClearer;
        this.selectionModeOn = false;
        this.selectedCategoryPositions = new ArrayList<>();
    }

    public List<ParseObject> getCategoryList() {
        return this.categoryList;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View subjectItemView = LayoutInflater.from(this.context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(subjectItemView);
    }

    @Override
    public void onBindViewHolder(@NotNull CategoryAdapter.ViewHolder holder, int position) {
        ParseObject category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void add(ParseObject category) {
        this.categoryList.add(category);
        int lastPosition = categoryList.size() - 1;
        notifyItemInserted(lastPosition);
    }

    public void addAll(List<? extends ParseObject> newCategories) {
        // Position of the first item that will be inserted.
        int startPosition = this.categoryList.size();
        int itemCount = newCategories.size();

        // Add the new categories to the adapter's list of categories.
        this.categoryList.addAll(newCategories);
        notifyItemRangeInserted(startPosition, itemCount);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mcvCategory;
        private TextView tvCategory;

        private ViewHolder(@NotNull View categoryItemView) {
            super(categoryItemView);
            tvCategory = categoryItemView.findViewById(R.id.tvCategory);
            mcvCategory = categoryItemView.findViewById(R.id.mcvCategory);
        }

        private void bind(ParseObject category) {
            // Conditionally bind the data to the edit text view
            // depending on the child of the category.
            if (category instanceof Page) {

                Page page = (Page) category;
                tvCategory.setText(page.getDescription());

                // Set up an onclick listener for each unique page view.
                View.OnClickListener pageClickHandler = createPageOnClickHandler(page);
                mcvCategory.setOnClickListener(pageClickHandler);

            } else if (category instanceof Section) {

                Section section = (Section) category;
                tvCategory.setText(section.getDescription());

                // Set up an onclick listener for each unique section view.
                View.OnClickListener sectionClickHandler = createSectionOnClickHandler(section);
                mcvCategory.setOnClickListener(sectionClickHandler);

            } else if (category instanceof Subject) {

                Subject subject = (Subject) category;
                tvCategory.setText(subject.getDescription());

                // Set up an onclick listener for each unique subject view.
                View.OnClickListener subjectClickHandler = createSubjectOnClickHandler(subject);
                mcvCategory.setOnClickListener(subjectClickHandler);
            }

            // Set up an on long click listener for each unique view.
            View.OnLongClickListener subjectOnLongClickHandler = createOnLongClickHandler();
            mcvCategory.setOnLongClickListener(subjectOnLongClickHandler);
        }

        /** OnClick and OnLongClick definitions. **/

        private View.OnClickListener createPageOnClickHandler(Page page) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectionModeOn) {
                        Navigation.fromPagesToNotes(page);
                    } else {
                        startSelection();
                    }
                }
            };
        }

        private View.OnClickListener createSectionOnClickHandler(Section section) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectionModeOn) {
                        Navigation.fromSectionsToPages(section);
                    } else {
                        startSelection();
                    }
                }
            };
        }

        private View.OnClickListener createSubjectOnClickHandler(Subject subject) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectionModeOn) {
                        Navigation.fromSubjectsToSections(subject);
                    } else {
                        startSelection();
                    }
                }
            };
        }

        private View.OnLongClickListener createOnLongClickHandler() {
            return new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    // Check to see if the long pressed position
                    // is selected.
                    boolean currentPosIsSelected = false;
                    Integer currentPos = getLayoutPosition();

                    for (Integer selectedPos: selectedCategoryPositions) {
                        if (currentPos.equals(selectedPos)) {
                            currentPosIsSelected = true;
                            break;
                        }
                    }
                    if (selectionModeOn && currentPosIsSelected) {
                        // If the same view is long clicked twice,
                        // simply exit selection mode.
                        selectionClearer.startClearSelection(selectedCategoryPositions);
                        exitSelection();
                        return true;
                    } else if (selectionModeOn) {
                        // If a view has been selected already
                        // and a new view has been long clicked,
                        // reset the selection process.
                        selectionClearer.startClearSelection(selectedCategoryPositions);
                        exitSelection();
                    }



                    selectionModeOn = true;
                    startSelection();

                    return true;
                }
            };
        }

        /**
         * Select a view by adding it to the list of
         * selected categories and visually indicating.
         */
        private void startSelection() {
            selectedCategoryPositions.add(getLayoutPosition());
            startSelectionVisual();

        }

        /**
         * Visually indicate the view is selected.
         */
        private void startSelectionVisual() {
            // Category view visual changes.
            Resources resources = context.getResources();
            int newToolbarColor = resources.getColor(R.color.teal_200);
            mcvCategory.setStrokeColor(newToolbarColor);
            mcvCategory.setStrokeWidth(resources.getInteger(R.integer.view_border_width));

            // App bar visual changes.
            Toolbar toolbar = ((HomeActivity)context).findViewById(R.id.tbHome);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_category_long_press);
            int currentColor = HomeActivity.getToolbarColor(toolbar);
            HomeActivity.setToolbarColor(toolbar, currentColor, newToolbarColor);
        }

        private void exitSelection() {
            selectionModeOn = false;
            selectedCategoryPositions.clear();
        }

    }
}
