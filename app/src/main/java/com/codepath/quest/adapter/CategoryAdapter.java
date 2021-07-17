package com.codepath.quest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.model.Subject;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

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

    @NonNull
    @NotNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View subjectItemView = LayoutInflater.from(this.context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(subjectItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.ViewHolder holder, int position) {
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

    public void addAll(List<Subject> newSubjects) {
        // Position of the first item that will be inserted.
        int startPosition = this.categoryList.size();
        int itemCount = newSubjects.size();

        // Add the new categories to the adapter's list of categories.
        this.categoryList.addAll(newSubjects);
        notifyItemRangeInserted(startPosition, itemCount);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etCategory;

        private ViewHolder(@NonNull @NotNull View categoryItemView) {
            super(categoryItemView);
            etCategory = categoryItemView.findViewById(R.id.etCategory);
        }

        private void bind(ParseObject category) {
            // Conditionally bind the data to the edit text view
            // depending on the child of the category.
            if (category instanceof Subject) {
                Subject subject = (Subject) category;
                etCategory.setText(subject.getSubjectDescription());
            }
        }
    }

}
