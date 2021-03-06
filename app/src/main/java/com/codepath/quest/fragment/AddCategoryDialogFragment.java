package com.codepath.quest.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.codepath.quest.R;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Constants;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCategoryDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCategoryDialogFragment extends DialogFragment {
    private static final String ARG_CATEGORY_TYPE = "CategoryType";
    private static final String ARG_ADAPTER = "Adapter";
    private String categoryType;

    // Required empty public constructor
    public AddCategoryDialogFragment(){}

    /**
     * Pass in the type of category to have a customized "New Subject" or
     * "New Page".
     *
     * @param categoryType the type of category i.e. Subject, Section, or Page
     * @return A new instance of fragment AddCategoryDialogFragment.
     */
    public static AddCategoryDialogFragment newInstance(String categoryType) {
        AddCategoryDialogFragment fragment = new AddCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_TYPE, categoryType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.categoryType = getArguments().getString(ARG_CATEGORY_TYPE);
        }
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the builder that builds the "New Category" dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogXML = LayoutInflater.from(getContext())
                            .inflate(R.layout.dialog_add_category, null, false);
        EditText etNewCategory = dialogXML.findViewById(R.id.etAddCategory);
        if (categoryType.equals(Constants.KEY_SUBJECT)) {
            etNewCategory.setHint(R.string.subject_hint);
        } else if (categoryType.equals(Constants.KEY_SECTION)) {
           etNewCategory.setHint(R.string.section_hint);
        } else if (categoryType.equals(Constants.KEY_PAGE)) {
            etNewCategory.setHint(R.string.page_hint);
        }

        // Focus the edit text view.
        etNewCategory.setFocusable(true);
        etNewCategory.setFocusableInTouchMode(true);
        etNewCategory.requestFocus();

        // TODO: Pop up the keyboard

        EditText finalEtNewCategory = etNewCategory;
        // Characterize the builder.
        builder.setTitle("New " + this.categoryType)
               .setView(dialogXML)
               .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       // Extract text from the edit text view inside the dialog.
                       String categoryName = finalEtNewCategory.getText().toString();

                       // Error handling for invalid text.
                       if (categoryName.equals("")) {
                           QuestToast.pleaseEnter(getContext(), categoryType);
                           return;
                       }

                       // Pass the subject name to ParentFragment.
                       Bundle bundle = new Bundle();
                       bundle.putString(Constants.KEY_DIALOG, categoryName);
                       getParentFragmentManager().setFragmentResult(Constants.KEY_REQUEST, bundle);

                       // Success!
                       QuestToast.successful(getContext(), "Create " + categoryType);
                   }
               })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Stop showing the button dialog when the user clicks cancel.
                        getDialog().cancel();
                    }
                });

        return builder.create();
    }
}