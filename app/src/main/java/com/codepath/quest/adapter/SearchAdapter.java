package com.codepath.quest.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codepath.quest.R;
import com.codepath.quest.model.Answer;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Question> results;

    public SearchAdapter(Context context, List<Question> results) {
        this.context = context;
        this.results = results;
    }

    @NotNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull SearchAdapter.ViewHolder holder, int position) {
        Question result = results.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void addAll(List<Question> results) {
        this.results.clear();
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mcvResult;
        private TextView tvQuestionResult;
        private TextView tvAnswerResult;
        private TextView tvDirectory;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            this.mcvResult = itemView.findViewById(R.id.mcvResult);
            this.tvQuestionResult = itemView.findViewById(R.id.tvQuestionResult);
            this.tvAnswerResult = itemView.findViewById(R.id.tvAnswerResult);
            this.tvDirectory = itemView.findViewById(R.id.tvDirectory);
        }

        public void bind(Question result) {
            tvQuestionResult.setText(result.getDescription());
            tvAnswerResult.setText(result.getAnswer().getDescription());

            // Get Question directory descriptions. The reason for the chained
            // function call is so the data of the reference can be included.
            getPageParent(result);
        }

        private void getPageParent(Question question) {
            question.getParent().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject page, ParseException e) {
                    getSectionParent((Page) page);
                }
            });
        }

        private void getSectionParent(Page page) {
            page.getParent().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject section, ParseException e) {
                    getSubjectParentAndCompleteBind(page, (Section) section);
                }
            });
        }

        private void getSubjectParentAndCompleteBind(Page page, Section section) {
            section.getParent().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    Subject subject = (Subject) object;
                    String directory = subject.getDescription() + " / " + section.getDescription()
                            + " / " + page.getDescription();
                    tvDirectory.setText(directory);
                }
            });
        }
    }
}
