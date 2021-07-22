package com.codepath.quest.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.helper.SelectionHandler;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.Parse;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Inflates text views and binds category data (i.e. subject, section, and page).
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<ParseObject> categoryList;
    private SelectionHandler selectionHandler;

    // Variables used for selection logic.
    private boolean selectionModeOn;
    private List<Integer> selectedCategoryPositions;
    private int numberOfOnClickMenuItemListeners;

    public CategoryAdapter(Context context, List<ParseObject> categoryList
                          , SelectionHandler selectionHandler) {
        this.context = context;
        this.categoryList = categoryList;
        this.selectionHandler = selectionHandler;
        this.selectionModeOn = false;
        this.selectedCategoryPositions = new ArrayList<>();
        numberOfOnClickMenuItemListeners = 0;
    }

    public List<ParseObject> getCategoryList() {
        return this.categoryList;
    }

    public void setCategoryList(List<ParseObject> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
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

    public void remove(ParseObject category) {
        this.categoryList.remove(category);
    }

    public ParseObject getCategory(int position) {
        return this.categoryList.get(position);
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
                        if (!isPositionSelected(getLayoutPosition())) {
                            startSelection();
                        }
                        HomeActivity.startCategoryOnClickMenuListeners(
                                context
                                , createOnEditIconClickHandler()
                                , createOnDeleteIconClickHandler()
                        );
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
                        if (!isPositionSelected(getLayoutPosition())) {
                            startSelection();
                        }
                        HomeActivity.startCategoryOnClickMenuListeners(
                                context
                                , createOnEditIconClickHandler()
                                , createOnDeleteIconClickHandler()
                        );
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
                        if (!isPositionSelected(getLayoutPosition())) {
                            startSelection();
                        }
                        HomeActivity.startCategoryOnClickMenuListeners(
                                context
                                , createOnEditIconClickHandler()
                                , createOnDeleteIconClickHandler()
                        );
                    }
                }
            };
        }

        private View.OnLongClickListener createOnLongClickHandler() {
            return new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    boolean currentPosIsSelected = isPositionSelected(getLayoutPosition());

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

                    selectionModeOn = true;
                    startSelection();

                    // Because we are setting the on menu item click
                    // listener fragment-wide, we wish to spawn one
                    // listener per fragment. Without this condition,
                    // the user can spawn an arbritary amount of instances
                    // of the same listener in one fragment, which results in
                    // undefined behavior.
                    HomeActivity.startCategoryOnClickMenuListeners(
                            context
                            , createOnEditIconClickHandler()
                            , createOnDeleteIconClickHandler()
                    );
                    numberOfOnClickMenuItemListeners++;
                    return true;
                }
            };
        }

        public MenuItem.OnMenuItemClickListener createOnEditIconClickHandler() {
            return new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    QuestToast.successful(context, "Edit Icon Click");
                    return true;
                }
            };
        }

        public MenuItem.OnMenuItemClickListener createOnDeleteIconClickHandler() {
            return new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    selectionHandler.startSelectionDelete(selectedCategoryPositions);
                    numberOfOnClickMenuItemListeners = 0;
                    exitSelection();
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
            startSelectionToolbar(newToolbarColor);

        }

        /**
         * Start app bar visual selection mode.
         */
        private void startSelectionToolbar(int newToolbarColor) {
            Toolbar toolbar = ((HomeActivity)context).findViewById(R.id.tbHome);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_category_long_press);

            if (selectedCategoryPositions.size() > 1) {
                MenuItem editMenuItem = toolbar.getMenu().getItem(0);
                editMenuItem.setVisible(false);
            }

            int currentColor = HomeActivity.getToolbarColor(toolbar);
            HomeActivity.setToolbarColor(toolbar, currentColor, newToolbarColor);
        }

        private void exitSelection() {
            selectionModeOn = false;
            selectionHandler.startSelectionClear(selectedCategoryPositions);
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
            for (Integer selectedPos: selectedCategoryPositions) {
                if (((Integer)currentPos).equals(selectedPos)) {
                    return true;
                }
            }
            return false;
        }

    }
}
