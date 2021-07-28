package com.codepath.quest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Answer;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;
import com.google.android.material.card.MaterialCardView;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Question> questionList;


    public NotesAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View qAndAView = LayoutInflater.from(context).inflate(R.layout.item_question_and_answer, parent, false);
            return new QAndAViewHolder(qAndAView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question = questionList.get(position);
        ((QAndAViewHolder)holder).bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void add(Question question) {
        this.questionList.add(question);
        int lastIndex = questionList.size() - 1;
        notifyItemChanged(lastIndex);
    }

    public void addAll(List<Question> questionList) {
        this.questionList.addAll(questionList);
        notifyDataSetChanged();
    }

    public List<Question> getQuestions() {
        return this.questionList;
    }

    public Question get(int position) {
        return this.questionList.get(position);
    }

    public Question remove(int position) {
        return this.questionList.remove(position);
    }

    /**
     * View holder for the question AND the answer.
     */
    public class QAndAViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView mcvQuestion;
        private MaterialCardView mcvAnswer;
        private MaterialCardView mcvArrow;
        private ImageView ivArrow;
        private LinearLayout llQuestionOptions;
        private TextView tvQuestion;
        private TextView tvAnswer;



        public QAndAViewHolder(@NonNull @NotNull View qAndAView) {
            super(qAndAView);
            tvQuestion = qAndAView.findViewById(R.id.tvQuestion);
            tvAnswer = qAndAView.findViewById(R.id.tvAnswer);
            mcvQuestion = qAndAView.findViewById(R.id.mcvQuestion);
            mcvAnswer = qAndAView.findViewById(R.id.mcvAnswer);
            mcvArrow = qAndAView.findViewById(R.id.mcvArrow);
            ivArrow = qAndAView.findViewById(R.id.ivArrow);
            llQuestionOptions = qAndAView.findViewById(R.id.llQuestionOptions);
        }

        public void bind(Question question) {
            // Bind the question description.
            String questionDescription = question.getDescription();
            tvQuestion.setText(questionDescription);

            // Bind the answer description.
            String answerDescription = question.getAnswer().getDescription();
            tvAnswer.setText(answerDescription);

            // Set up an on double click listener for the question card view to edit
            // the text.
            mcvQuestion.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                @Override
                public void onSingleClick(View view) {}

                @Override
                public void onDoubleClick(View view) {
                    startEditQuestionMode(question);
                }
            }));

            // Set up an on double click listener for the question card view to edit
            // the text.
            mcvAnswer.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                @Override
                public void onSingleClick(View view) {}

                @Override
                public void onDoubleClick(View view) {
                    startEditAnswerMode(question.getAnswer());
                }
            }));

            // Set an on click listener for the arrow.
            mcvArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llQuestionOptions.getVisibility() == View.GONE) {
                        uncollapseMenu();
                    } else {
                        collapseMenu();
                    }
                }
            });
        }
        public void uncollapseMenu() {
            // TransitionManager allows the animation effect of
            // uncollapsing the question card view's arrow.
            TransitionManager.beginDelayedTransition(mcvQuestion,
                    new AutoTransition());
            llQuestionOptions.setVisibility(View.VISIBLE);
            ivArrow.setImageResource(R.drawable.up_arrow);

            // Set up an on click listener for the trash icon.
            MaterialCardView trashIcon = llQuestionOptions.findViewById(R.id.mcvDelete);
            trashIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collapseMenu();
                    delete(getLayoutPosition());
                }
            });

        }

        public void collapseMenu() {
            TransitionManager.beginDelayedTransition(mcvQuestion,
                    new AutoTransition());
            ivArrow.setImageResource(R.drawable.down_arrow);
            llQuestionOptions.setVisibility(View.GONE);
        }

        public void startEditQuestionMode(Question question) {
            // Get the question description and copy it to the
            // edit text.
            String questionDescription = tvQuestion.getText().toString();
            tvQuestion.setVisibility(View.INVISIBLE);

            EditText etQuestion = mcvQuestion.findViewById(R.id.etQuestion);
            etQuestion.setVisibility(View.VISIBLE);
            etQuestion.setText(questionDescription);
            etQuestion.requestFocus();
            etQuestion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == false) {
                        stopEditQuestionMode(question, etQuestion);
                    }
                }
            });
        }

        public void stopEditQuestionMode(Question question, EditText etQuestion) {
            // Get the text from the edit text and put it back to the
            // question text view.
            String newQuestionDescription = etQuestion.getText().toString();
            etQuestion.setVisibility(View.GONE);
            tvQuestion.setVisibility(View.VISIBLE);
            tvQuestion.setText(newQuestionDescription);
            question.setDescription(newQuestionDescription);
            question.saveInBackground();
        }

        public void startEditAnswerMode(Answer answer) {
            // Get the answer description and copy it to the
            // edit text.
            String answerDescription = tvAnswer.getText().toString();
            tvAnswer.setVisibility(View.INVISIBLE);

            EditText etAnswer = mcvAnswer.findViewById(R.id.etAnswer);
            etAnswer.setVisibility(View.VISIBLE);
            etAnswer.setText(answerDescription);
            etAnswer.requestFocus();
            etAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == false) {
                        stopEditAnswerMode(answer, etAnswer);
                    }
                }
            });
        }

        public void stopEditAnswerMode(Answer answer, EditText etAnswer) {
            // Get the text from the edit text and put it back to the
            // answer text view.
            String newQuestionDescription = etAnswer.getText().toString();
            etAnswer.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.VISIBLE);
            tvAnswer.setText(newQuestionDescription);
            answer.setDescription(newQuestionDescription);
            answer.saveInBackground();
        }
    }

    /**
     * A stronger form of remove that also deletes the
     * question from the database.
     *
     * @param position the position in the questionList to delete
     */
    public void delete(int position) {
        Question question = remove(position);
        question.deleteInBackground();
        notifyItemRemoved(position);
    }
}
