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
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;
import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagesFragment extends Fragment {
    private Section parentSection;
    private RecyclerView rvPages;
    private CategoryAdapter pageAdapter;

    // Required empty public constructor
    public PagesFragment() {}

    /**
     * Constructor for a new PagesFragment object
     *
     * @param parentSection the parent section for page objects to reference
     * @return A new instance of fragment PagesFragment.
     */
    public static PagesFragment newInstance(Section parentSection) {
        PagesFragment fragment = new PagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(HomeActivity.KEY_PARENT, parentSection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentSection = getArguments().getParcelable(HomeActivity.KEY_PARENT);
        }

        // Set up the adapter.
        List<ParseObject> pageList = new ArrayList<>();
        pageAdapter = new CategoryAdapter(getContext(), pageList);

        // Let HomeActivity know that what the current section is.
        HomeActivity.setCurrentSection(parentSection);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the action bar title and subtitle.
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        HomeActivity.setActionBarText(actionBar
                       ,HomeActivity.getCurrentSubject().getDescription()
                       ,parentSection.getDescription() );

        // Set up recycler view.
        rvPages = view.findViewById(R.id.rvPages);
        rvPages.setAdapter(pageAdapter);
        rvPages.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load up the sections under the current user and parent subject.
        if (pageAdapter.getCategoryList().size() == 0) {
            Page.queryPages(pageAdapter, parentSection);
        }

    }
}