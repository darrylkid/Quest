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
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    private RecyclerView rvNotes;
    private CategoryAdapter notesAdapter;
    private Page parentPage;

    // Required empty public constructor
    public NotesFragment() {}

    /**
     * Creates a new instance of the notes fragment.
     *
     * @param parentPage the parent page for the questions to refer to
     * @return A new instance of fragment NotesFragment.
     */
    public static NotesFragment newInstance(Page parentPage) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable(HomeActivity.KEY_PAGE, parentPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentPage = getArguments().getParcelable(HomeActivity.KEY_PAGE);
        }

        // Let HomeActivity know that what the current page is.
        HomeActivity.setCurrentPage(parentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the action bar title and subtitle.
        String subjectDescription = HomeActivity.getCurrentSubject().getDescription();
        String sectionDescription = HomeActivity.getCurrentSection().getDescription();
        String pageDescription = parentPage.getDescription();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        HomeActivity.setActionBarText(actionBar
                                     ,subjectDescription + " | " + sectionDescription
                                     ,pageDescription );

        // Set up recycler view.
        rvNotes = view.findViewById(R.id.rvNotes);
        rvNotes.setAdapter(notesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}