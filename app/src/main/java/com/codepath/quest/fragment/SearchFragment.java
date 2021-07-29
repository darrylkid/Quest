package com.codepath.quest.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.SearchAdapter;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.helper.SearchEngine;
import com.codepath.quest.model.Constants;
import com.codepath.quest.model.Question;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment responsible for searching questions & answers.
 */
public class SearchFragment extends Fragment {
    private SearchAdapter searchAdapter;
    private List<Question> results;
    private RecyclerView rvSearch;

    // Required empty public constructor
    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set up the adapter.
        results = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), results);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the recycler view.
        rvSearch = view.findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearch.setAdapter(searchAdapter);

        // Set the tool bar title and color.
        Toolbar toolbar = ((HomeActivity)getContext()).findViewById(R.id.tbHome);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home_top_with_logout);
        HomeActivity.setToolbarText(toolbar, getString(R.string.search),"");

        // Set up the character sequence listener for the search view.
        SearchView search = ((HomeActivity) getContext()).findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Not in use.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchEngine.searchQuestions(searchAdapter, newText);
                return true;
            }
        });
    }

}