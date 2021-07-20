package com.codepath.quest.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment extends Fragment {
    private RecyclerView rvSubjects;
    private CategoryAdapter subjectAdapter;

    // Required empty public constructor
    public SubjectsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the adapter.
        List<ParseObject> subjectList = new ArrayList<>();
        subjectAdapter = new CategoryAdapter(getContext(), subjectList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the action bar title.
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        HomeActivity.setActionBarText(actionBar, getString(R.string.subjects), "");

        // Set logout icon to be invisible.
        ActionMenuItemView logoutIcon = getActivity().findViewById(R.id.iLogoutHome);
        logoutIcon.setVisibility(View.INVISIBLE);

        // Set up the recycler view.
        rvSubjects = view.findViewById(R.id.rvSubjects);
        rvSubjects.setAdapter(subjectAdapter);
        rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load up the subjects already created by the current user.
        if (subjectAdapter.getCategoryList().size() == 0) {
            Subject.querySubjects(subjectAdapter);
        }


        // Set up an onclick listener for the
        // "Add Subject" floating action button
        FloatingActionButton fabNewSubject = view.findViewById(R.id.fabNewSubject);
        View.OnClickListener newSubjectHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a "New Subject" dialog.
                AddCategoryDialogFragment fragment = AddCategoryDialogFragment.newInstance("Subject");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment.show(fragmentManager,"Subject");

                // Gets invoked when the user presses the "Create" option inside
                // the dialog and their input is valid.
                FragmentResultListener onAddSubjectDialogResult = new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NotNull String requestKey, @NonNull Bundle result) {
                        // When the user creates a new subject from the dialog,
                        // add it to the recycler view for the subject to be displayed.
                        String subjectName = result.getString(HomeActivity.KEY_DIALOG);
                        Subject subject = new Subject();
                        subject.save(subjectName, ParseUser.getCurrentUser());
                        subjectAdapter.add(subject);
                    }
                };
                fragmentManager.setFragmentResultListener(HomeActivity.KEY_REQUEST,
                                             SubjectsFragment.this,
                                                          onAddSubjectDialogResult);
            }
        };
        fabNewSubject.setOnClickListener(newSubjectHandler);
    }
}