package com.codepath.quest.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionsFragment extends Fragment {
    private RecyclerView rvSections;
    private CategoryAdapter sectionAdapter;
    private Subject parentSubject;

    // Required empty public constructor
    public SectionsFragment() {}

    /**
     * Creates a new instance of the Sections Fragment.
     *
     * @param
     * @return A new instance of fragment SectionsFragment.
     */
    public static SectionsFragment newInstance(Subject subject) {
        SectionsFragment fragment = new SectionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(HomeActivity.KEY_SUBJECT, subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentSubject = getArguments().getParcelable(HomeActivity.KEY_SUBJECT);
        }

        // Set up the adapter.
        List<ParseObject> sectionList = new ArrayList<>();
        sectionAdapter = new CategoryAdapter(getContext(), sectionList);

        // Let HomeActivity know that what the current subject is.
        HomeActivity.setCurrentSubject(parentSubject);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sections, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the action bar title to the subject name.
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        HomeActivity.setActionBarText(actionBar, parentSubject.getDescription(), "");

        // Set up the recycler view.
        rvSections = view.findViewById(R.id.rvSections);
        rvSections.setAdapter(sectionAdapter);
        rvSections.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load up the sections under the current user and parent subject.
        if (sectionAdapter.getCategoryList().size() == 0) {
            Section.querySections(sectionAdapter, parentSubject);
        }

    }

    private void setActionBarText(ActionBar actionBar, String title, @Nullable String subtitle) {
        actionBar.setTitle(title);
        actionBar.setSubtitle(subtitle);
    }
}