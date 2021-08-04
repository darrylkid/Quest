package com.codepath.quest.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.model.Category;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * Like the main Category Adapter except for moving the
 * questions
 */
public class CategoryInDialogAdapter extends RecyclerView.Adapter<CategoryInDialogAdapter.ViewHolder> {
    Context context;
    List<Category> catergories;
    MaterialCardView mcvSelected;
    int selectedPos;
    Subject clickedSubject;
    Dialog dialog;


    public CategoryInDialogAdapter(Context context, List<Category> categories, Dialog dialog) {
        this.context = context;
        this.catergories = categories;
        mcvSelected = null;
        selectedPos = -1;
        this.dialog = dialog;
        clickedSubject = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = catergories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return catergories.size();
    }

    public void addAll(List<? extends Category> categories) {
        this.catergories.clear();
        this.catergories.addAll(categories);
        notifyDataSetChanged();
    }

    public List<Category> getCategories() {
        return this.catergories;
    }

    public int getSelectedPosition() {
        return this.selectedPos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mcvCategory;
        private TextView tvCategory;
        private TextView tvMiniNavigationTitle;
        private MaterialCardView back;
        private Button select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mcvCategory = itemView.findViewById(R.id.mcvCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvMiniNavigationTitle = dialog.findViewById(R.id.tvMiniNavigationTitle);
            back = dialog.findViewById(R.id.mcvMiniNavigationBack);
            select = dialog.findViewById(R.id.btnNavigationSelect);
        }

        public void bind(Category category) {
            tvCategory.setText(category.getDescription());

            // We want the on click behavior to be different between
            // (the subject & section) and the page.
            if (category instanceof Subject) {
                Subject subject = (Subject) category;
                tvMiniNavigationTitle.setText(R.string.subjects);
                select.setEnabled(false);

                // There's nothing to go back to when the user
                // sees the list of subjects. Simply hide the back button.
                back.setVisibility(View.GONE);

                // Set the clicked subject so that when the user
                // clicks back when looking at the list of pages,
                // the user sees the correct sections spawned.
                clickedSubject = subject;

                // Set up an on click listener for this Subject item.
                mcvCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Section.querySections(CategoryInDialogAdapter.this, subject);
                    }
                });
            } else if (category instanceof Section) {
                Section section = (Section) category;
                tvMiniNavigationTitle.setText(R.string.sections);
                select.setEnabled(false);

                // In case the user presses the back button
                // when seeing the list of pages.
                clearSelectedCategory(context.getResources());

                // Set up an on click listener for the back button.
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Subject.querySubjects(CategoryInDialogAdapter.this);
                    }
                });

                // Set up an on click listener for this Section item.
                mcvCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Page.queryPages(CategoryInDialogAdapter.this, section);
                    }
                });
            } else if (category instanceof Page) {
                tvMiniNavigationTitle.setText(R.string.pages);

                // Set up an on click listener for the back button.
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Section.querySections(CategoryInDialogAdapter.this, clickedSubject);
                    }
                });

                // Set up an on click listener for this Page item.
                mcvCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select.setEnabled(true);
                        startSelectionVisual();
                        mcvSelected = mcvCategory;
                        selectedPos = getLayoutPosition();
                    }
                });
            }
        }

        private void startSelectionVisual() {
            // Clear the selection on the previously selected item
            Resources resources = context.getResources();
            clearSelectedCategory(resources);

            // Category view visual changes
            int newToolbarColor = HomeActivity.getThemeColor(context, R.attr.colorSecondary);
            mcvCategory.setStrokeColor(newToolbarColor);
            mcvCategory.setStrokeWidth(resources.getInteger(R.integer.view_border_width));
        }

        private void clearSelectedCategory(Resources resources) {
            if (mcvSelected != null) {
                mcvSelected.setStrokeColor(resources.getColor(R.color.white));
                mcvSelected.setStrokeWidth(0);
            }
        }
    }
}

