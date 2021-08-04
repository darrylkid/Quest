package com.codepath.quest.helper;

import android.content.Context;
import android.content.res.Resources;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.model.Category;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Visually clears the selection.
 */
public class SelectionHandler {
    private OnSelectionListener onSelectionListener = null;
    private CategoryAdapter adapter = null;

    public void setOnSelectionListener(OnSelectionListener listener) {
        this.onSelectionListener = listener;
    }

    public void setAdapter(CategoryAdapter adapter) {
        this.adapter = adapter;
    }

    /** Methods responsible for visually clearing the selection **/


    public void startSelectionClear(List<Integer> selectedPostions) {
        onSelectionListener.onSelectionClear(selectedPostions);
    }

    /** Methods responsible for deleting the selected items **/

    public void startSelectionDelete(List<Integer> selectedPositions) {
        onSelectionListener.onSelectionDelete(selectedPositions);
    }

    public void deleteSelectedItems(Context context
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

    /** Listeners for selection logic **/
    public View.OnClickListener createPageOnClickHandler(Page page
                                                        , int position
                                                        , MaterialCardView mcvCategory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.isSelectionModeOn()) {
                    Navigation.fromPagesToNotes(page);
                } else {
                    Context context = adapter.getAdapterContext();
                    if (!isPositionSelected(position)) {
                        startSelection(context, position, mcvCategory);
                    }
                    HomeActivity.startCategoryOnClickMenuListeners(
                            adapter.getAdapterContext()
                            , createOnEditIconClickHandler(context, position)
                            , createOnDeleteIconClickHandler()
                    );
                }
            }
        };
    }

    public View.OnClickListener createSectionOnClickHandler(Section section
                                                            , int position
                                                            , MaterialCardView mcvCategory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.isSelectionModeOn()) {
                    Navigation.fromSectionsToPages(section);
                } else {
                    Context context = adapter.getAdapterContext();
                    if (!isPositionSelected(position)) {
                        startSelection(context, position, mcvCategory);
                    }
                    HomeActivity.startCategoryOnClickMenuListeners(
                            adapter.getAdapterContext()
                            , createOnEditIconClickHandler(context, position)
                            , createOnDeleteIconClickHandler()
                    );
                }
            }
        };
    }

    public View.OnClickListener createSubjectOnClickHandler(Subject subject
                                                            , int position
                                                            , MaterialCardView mcvCategory) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.isSelectionModeOn()) {
                    Navigation.fromSubjectsToSections(subject);
                } else {
                    Context context = adapter.getAdapterContext();
                    if (!isPositionSelected(position)) {
                        startSelection(context, position, mcvCategory);
                    }
                    HomeActivity.startCategoryOnClickMenuListeners(
                            adapter.getAdapterContext()
                            , createOnEditIconClickHandler(adapter.getAdapterContext(), position)
                            , createOnDeleteIconClickHandler()
                    );
                }
            }
        };
    }

    public View.OnLongClickListener createOnLongClickSelectionHandler(int position
                                                                      , MaterialCardView mcvCategory) {
        return new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                boolean currentPosIsSelected = isPositionSelected(position);
                boolean selectionModeOn = adapter.isSelectionModeOn();
                if (selectionModeOn && currentPosIsSelected) {
                    // If the same view is long clicked twice,
                    // simply exit selection mode.
                    exitSelection();
                    return true;
                } else if (selectionModeOn) {
                    // If a view has been long clicked already
                    // and a new view has been long clicked,
                    // reset the selection process.
                    exitSelection();
                }

                // Long click for the first time.
                Context context = adapter.getAdapterContext();
                adapter.setSelectionMode(true);
                startSelection(context, position, mcvCategory);

                // Because we are setting the on menu item click
                // listener fragment-wide, we wish to spawn one
                // listener per fragment. Without this condition,
                // the user can spawn an arbritary amount of instances
                // of the same listener in one fragment, which results in
                // undefined behavior.
                HomeActivity.startCategoryOnClickMenuListeners(
                        adapter.getAdapterContext()
                        , createOnEditIconClickHandler(context, position)
                        , createOnDeleteIconClickHandler()
                );
                return true;
            }
        };
    }

    public MenuItem.OnMenuItemClickListener createOnEditIconClickHandler(Context context
                                                                         , int position) {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Show the alert dialog...
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                RelativeLayout fragmentContainer = ((HomeActivity)context).findViewById(R.id.quest_fragment_container);
                View dialogRootView = ((HomeActivity)context)
                        .getLayoutInflater()
                        .inflate(R.layout.dialog_edit_category
                                , fragmentContainer, false);
                alertDialog.setView(dialogRootView);
                alertDialog.show();

                // Set up on click listener for the button.
                Button btnEditCategory = dialogRootView.findViewById(R.id.btnEditCategorySubmit);
                btnEditCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Error handling for invalid text input.
                        EditText etCategory = dialogRootView.findViewById(R.id.etEditCategory);
                        String newCategoryInput = etCategory.getText().toString();
                        if(newCategoryInput.equals("")) {
                            QuestToast.pleaseEnter(context, "new name");
                        } else {
                            // Valid user text! Change the description in the
                            // database and notify the adapter a change has occured.
                            ((Category)adapter.getCategoryList()
                                    .get(position))
                                    .setDescription(newCategoryInput);
                            adapter.notifyItemChanged(position);
                            alertDialog.hide();
                            exitSelection();
                        }
                    }
                });

                return true;
            }
        };
    }

    public MenuItem.OnMenuItemClickListener createOnDeleteIconClickHandler() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startSelectionDelete(adapter.getSelectedCategoryPositions());
                exitSelection();
                return true;
            }
        };
    }

    /**
     * Select a view by adding it to the list of
     * selected categories and visually indicating.
     */
    private void startSelection(Context context, int position, MaterialCardView mcvCategory) {
        adapter.getSelectedCategoryPositions().add(position);
        startSelectionVisual(context, mcvCategory);
    }

    /**
     * Visually indicate the view is selected.
     */
    private void startSelectionVisual(Context context, MaterialCardView mcvCategory) {
        // Category view visual changes.
        Resources resources = context.getResources();
        int newToolbarColor = HomeActivity.getThemeColor(context, R.attr.colorSecondary);
        mcvCategory.setStrokeColor(newToolbarColor);
        mcvCategory.setStrokeWidth(resources.getInteger(R.integer.view_border_width));
        startSelectionToolbar(context, newToolbarColor);
    }

    /**
     * Start app bar visual selection mode.
     */
    private void startSelectionToolbar(Context context, int newToolbarColor) {
        Toolbar toolbar = ((HomeActivity)context).findViewById(R.id.tbHome);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_category_long_press);

        if (adapter.getSelectedCategoryPositions().size() > 1) {
            MenuItem editMenuItem = toolbar.getMenu().getItem(0);
            editMenuItem.setVisible(false);
        }

        int currentColor = HomeActivity.getToolbarColor(toolbar);
        HomeActivity.setToolbarColor(toolbar, currentColor, newToolbarColor);
    }

    private void exitSelection() {
        adapter.setSelectionMode(false);
        List<Integer> selectedCategoryPositions = adapter.getSelectedCategoryPositions();
        startSelectionClear(selectedCategoryPositions);
        selectedCategoryPositions.clear();
    }

    /**
     * Check to see if the long pressed position
     * is selected.
     *
     * @param currentPos the current position
     * @return boolean indicating whether or not the position is selected
     */
    private boolean isPositionSelected(int currentPos) {
        for (Integer selectedPos: adapter.getSelectedCategoryPositions()) {
            if (((Integer)currentPos).equals(selectedPos)) {
                return true;
            }
        }
        return false;
    }
}