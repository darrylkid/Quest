package com.codepath.quest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.helper.SelectionHandler;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.card.MaterialCardView;
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

    public CategoryAdapter(Context context, List<ParseObject> categoryList
                          , SelectionHandler selectionHandler) {
        this.context = context;
        this.categoryList = categoryList;
        this.selectionHandler = selectionHandler;
        this.selectionModeOn = false;
        this.selectedCategoryPositions = new ArrayList<>();
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

    public boolean isSelectionModeOn() {
        return this.selectionModeOn;
    }

    public void setSelectionMode(boolean on) {
        this.selectionModeOn = on;
    }

    public Context getAdapterContext() {
        return this.context;
    }

    public List<Integer> getSelectedCategoryPositions() {
        return this.selectedCategoryPositions;
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
            // Start a roll in animation each time
            //  a category item is binded.
            YoYo.with(Techniques.RollIn).duration(250).playOn(tvCategory);
            YoYo.with(Techniques.RollIn).duration(250).playOn(mcvCategory);

            // Conditionally bind the data to the edit text view
            // depending on the child of the category.
            if (category instanceof Page) {

                Page page = (Page) category;
                tvCategory.setText(page.getDescription());

                // Set up an onclick listener for each unique page view.
                View.OnClickListener pageClickHandler
                        = selectionHandler.createPageOnClickHandler(page, getLayoutPosition(), mcvCategory);
                mcvCategory.setOnClickListener(pageClickHandler);

            } else if (category instanceof Section) {

                Section section = (Section) category;
                tvCategory.setText(section.getDescription());

                // Set up an onclick listener for each unique section view.
                View.OnClickListener sectionClickHandler
                        = selectionHandler.createSectionOnClickHandler(section, getLayoutPosition(), mcvCategory);
                mcvCategory.setOnClickListener(sectionClickHandler);

            } else if (category instanceof Subject) {

                Subject subject = (Subject) category;
                tvCategory.setText(subject.getDescription());

                // Set up an onclick listener for each unique subject view.
                View.OnClickListener subjectClickHandler
                        = selectionHandler.createSubjectOnClickHandler(subject, getLayoutPosition(), mcvCategory);
                mcvCategory.setOnClickListener(subjectClickHandler);
            }

            // Set up an on long click listener for each unique view.
            View.OnLongClickListener subjectOnLongClickSelectionHandler
                    = selectionHandler.createOnLongClickSelectionHandler(getLayoutPosition(), mcvCategory);
            mcvCategory.setOnLongClickListener(subjectOnLongClickSelectionHandler);
        }

        /** OnClick and OnLongClick definitions. **/
    }



    public void debug() {
        String selectedCategoryPositionString = "Selected category positions: ";
        for (Integer p : selectedCategoryPositions) {
            selectedCategoryPositionString += p.toString() + " | ";
        }
        String catergoyListString = "Category list that exists: ";
        for (Integer i = 0; i < categoryList.size(); i++) {
            catergoyListString += i.toString() + " | ";
        }
        Log.i("CategoryAdapter", selectedCategoryPositionString);
        Log.i("CategoryAdapter", catergoyListString);
        Log.i("CategoryAdapter", "--------------------------------------------------------------------------");
    }
}
