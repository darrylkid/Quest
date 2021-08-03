package com.codepath.quest.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Answer;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                          implements DraggableItemAdapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Question> questionList;


    public NotesAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;

        // Required for the drag-and-drop functionality to work.
        setHasStableIds(true);
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
     * Methods for implementing draggable view functionality.
     */

    @Override
    public boolean onCheckCanStartDrag(@NonNull RecyclerView.ViewHolder holder, int position, int x, int y) {
        View qAndAItem = holder.itemView;
        RelativeLayout qAndADraggable = qAndAItem.findViewById(R.id.rlQAndA);

        // Check if the x and y position of where the user long pressed is inside
        // the qAndAItem.
        int width = qAndADraggable.getWidth();
        int height = qAndADraggable.getHeight();
        int draggableXPos = qAndADraggable.getLeft();
        int draggableYPos = qAndADraggable.getTop();

        return ((x >= draggableXPos) && (x < (draggableXPos + width))
                && (y >= draggableYPos) && (y < draggableYPos + height));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull RecyclerView.ViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        // Move the item in the Model space.
        Question removed = questionList.remove(fromPosition);
        questionList.add(toPosition, removed);

        // Update the order in the database so that
        // querying questions will remember the order.
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            question.setOrder(i);
            question.saveInBackground();
        }
    }

    @Override
    public long getItemId(int position) {
        // Required for the drag-and-drop functionality to work.
        return questionList.get(position).hashCode();
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) { return false; }

    @Override
    public void onItemDragStarted(int position) {}

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {}

    /**
     * View holder for the question AND the answer.
     */
    public class QAndAViewHolder extends AbstractDraggableItemViewHolder {
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

            // Set up an on click listener for the move arrow icon
            MaterialCardView moveIcon = llQuestionOptions.findViewById(R.id.mcvMove);
            moveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = HomeActivity.buildDialog(context, R.layout.dialog_category_navigtion, false);

                    // Set up the adapter and recycler view.
                    MiniCategoryAdapter adapter = new MiniCategoryAdapter(context, new ArrayList<>(), dialog);
                    RecyclerView rvMiniNavigation = dialog.findViewById(R.id.rvMiniNavigation);
                    rvMiniNavigation.setAdapter(adapter);
                    rvMiniNavigation.setLayoutManager(new LinearLayoutManager(context));

                    Subject.querySubjects(adapter);

                    // MiniCategoryAdapter needs to know what to query when the
                    // the back card view is clicked.
                    MaterialCardView back = dialog.findViewById(R.id.mcvMiniNavigationBack);

                    // Set up an on click listener for the cancel button.
                    Button cancel = dialog.findViewById(R.id.btnNativagionCancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });

                    // Set up an on click listener for the select button.
                    Button select = dialog.findViewById(R.id.btnNavigationSelect);
                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Page parentPage = (Page) adapter.getCategories().get(adapter.getSelectedPosition());

                            // Error handling for when the user selects the page
                            // the question is currently residing in.
                            if (HomeActivity.getCurrentPage().getObjectId().equals(parentPage.getObjectId())) {
                                Toast.makeText(context
                                        , "Question cannot be moved to the same Page. Please try again"
                                        , Toast.LENGTH_SHORT);
                                return;
                            }

                            // Get the selected item and move the question to
                            // its new page.
                            int fromPosition = getLayoutPosition();
                            Question moved = questionList.get(fromPosition);
                            moved.setParent(parentPage);
                            moved.saveInBackground();

                            // Render the changes.
                            questionList.remove(fromPosition);
                            notifyItemRemoved(fromPosition);

                            dialog.hide();
                        }
                    });

                    dialog.show();
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
            HomeActivity.showKeyboard(context, etQuestion);
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

            // Parse doesn't like empty strings.
            if (etQuestion.getText().toString().length() == 0) {
                newQuestionDescription = " ";
            }

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
            etAnswer.setFocusableInTouchMode(true);
            HomeActivity.showKeyboard(context, etAnswer);

            etAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        stopEditAnswerMode(answer, etAnswer);
                    }
                }
            });
        }

        public void stopEditAnswerMode(Answer answer, EditText etAnswer) {
            // Get the text from the edit text and put it back to the
            // answer text view.
            String newAnswerDescription = etAnswer.getText().toString();
            etAnswer.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.VISIBLE);
            tvAnswer.setText(newAnswerDescription);

            // Parse doesn't like empty strings.
            if (etAnswer.getText().toString().length() == 0) {
                newAnswerDescription = " ";
            }
            answer.setDescription(newAnswerDescription);
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
