package com.codepath.quest.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.CategoryAdapter;
import com.codepath.quest.helper.OnSelectionClearListener;
import com.codepath.quest.helper.SelectionClearer;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    private SelectionClearer selectionClearer;

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
        selectionClearer = new SelectionClearer();
        sectionAdapter = new CategoryAdapter(getContext(), sectionList, selectionClearer);

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

        // Set up the toolbar.
        startSectionsFragmentToolbar();

        // Set up the recycler view.
        rvSections = view.findViewById(R.id.rvSections);
        rvSections.setAdapter(sectionAdapter);
        rvSections.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load up the sections under the current user and parent subject.
        if (sectionAdapter.getCategoryList().size() == 0) {
            Section.querySections(sectionAdapter, parentSubject);
        }

        // Set up an onclick listener for the
        // "Add Section" floating action button
        FloatingActionButton fabNewSection = view.findViewById(R.id.fabNewSection);
        View.OnClickListener newSectionHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a "New Section" dialog.
                AddCategoryDialogFragment fragment = AddCategoryDialogFragment.newInstance("Section");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment.show(fragmentManager,"Section");

                // Gets invoked when the user presses the "Create" option inside
                // the dialog and their input is valid.
                FragmentResultListener onAddSectionDialogResult = new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NotNull String requestKey, @NonNull Bundle result) {
                        // When the user creates a new subject from the dialog,
                        // add it to the recycler view for the subject to be displayed.
                        String sectionName = result.getString(HomeActivity.KEY_DIALOG);
                        Section section = new Section();
                        section.save(sectionName, ParseUser.getCurrentUser(), parentSubject);
                        sectionAdapter.add(section);
                    }
                };
                fragmentManager.setFragmentResultListener(HomeActivity.KEY_REQUEST,
                        SectionsFragment.this,
                        onAddSectionDialogResult);
            }
        };
        fabNewSection.setOnClickListener(newSectionHandler);

        // Set up a onCleanSelection listener for visually clearing the views.
        OnSelectionClearListener onSelectionClearListener = new OnSelectionClearListener() {
            @Override
            public void onSelectionClear(List<Integer> selectedItemPositions) {
                for (Integer position: selectedItemPositions) {
                    // For each selected view, visually clear its selection.
                    View selectedView = rvSections.getLayoutManager().findViewByPosition(position);
                    MaterialCardView mcvCategory = selectedView.findViewById(R.id.mcvCategory);
                    mcvCategory.setStrokeWidth(0);
                    startSectionsFragmentToolbar();
                }
            }
        };
        selectionClearer.setOnCleanSelectionListener(onSelectionClearListener);
    }

    public void startSectionsFragmentToolbar() {
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.tbHome);
        int currentToolbarColor = HomeActivity.getToolbarColor(toolbar);
        toolbar.getMenu().clear();
        HomeActivity.setToolbarText(toolbar, parentSubject.getDescription(), "");
        HomeActivity.setToolbarColor(toolbar, currentToolbarColor
                                    ,getResources().getColor(R.color.design_default_color_primary));
    }
}