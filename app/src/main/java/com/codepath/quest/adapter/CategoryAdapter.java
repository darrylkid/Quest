package com.codepath.quest.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.Navigation;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Inflates text views and binds category data (i.e. subject, section, and page).
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<ParseObject> categoryList;


    public CategoryAdapter(Context context, List<ParseObject> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
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
                View.OnClickListener pageClickHandler = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.fromPagesToNotes(page);
                    }
                };
                mcvCategory.setOnClickListener(pageClickHandler);
            } else if (category instanceof Section) {
                Section section = (Section) category;
                tvCategory.setText(section.getDescription());

                // Set up an onclick listener for each unique section view.
                View.OnClickListener sectionClickHandler = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.fromSectionsToPages(section);
                    }
                };
                mcvCategory.setOnClickListener(sectionClickHandler);
            } else if (category instanceof Subject) {
                Subject subject = (Subject) category;
                tvCategory.setText(subject.getDescription());

                // Set up an onclick listener for each unique subject view.
                View.OnClickListener subjectClickHandler = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.fromSubjectsToSections(subject);
                    }
                };
                mcvCategory.setOnClickListener(subjectClickHandler);
            }

        }
    }

}
