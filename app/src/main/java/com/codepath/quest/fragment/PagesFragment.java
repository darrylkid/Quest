package com.codepath.quest.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.codepath.quest.helper.OnSelectionListener;
import com.codepath.quest.helper.SelectionHandler;
import com.codepath.quest.model.Constants;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Section;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    private SelectionHandler selectionHandler;

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
        args.putParcelable(Constants.KEY_PARENT, parentSection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentSection = getArguments().getParcelable(Constants.KEY_PARENT);
        }

        // Set up the adapter.
        List<ParseObject> pageList = new ArrayList<>();
        selectionHandler = new SelectionHandler();
        pageAdapter = new CategoryAdapter(getContext(), pageList, selectionHandler);
        selectionHandler.setAdapter(pageAdapter);

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
        startPagesFragmentToolbar();

        // Set up recycler view.
        rvPages = view.findViewById(R.id.rvPages);
        rvPages.setAdapter(pageAdapter);
        rvPages.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load up the sections under the current user and parent subject.
        if (pageAdapter.getCategoryList().size() == 0) {
            Page.queryPages(pageAdapter, parentSection);
        }

        // Set up an onclick listener for the
        // "Add Page" floating action button
        FloatingActionButton fabNewPage = view.findViewById(R.id.fabNewPage);
        View.OnClickListener newPageHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a "New Page" dialog.
                AddCategoryDialogFragment fragment = AddCategoryDialogFragment.newInstance("Page");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment.show(fragmentManager,"Page");

                // Gets invoked when the user presses the "Create" option inside
                // the dialog and their input is valid.
                FragmentResultListener onAddPageDialogResult = new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NotNull String requestKey, @NonNull Bundle result) {
                        // When the user creates a new subject from the dialog,
                        // add it to the recycler view for the subject to be displayed.
                        String pageName = result.getString(Constants.KEY_DIALOG);
                        Page page = new Page();
                        page.save(pageName, ParseUser.getCurrentUser(), parentSection);
                        pageAdapter.add(page);
                    }
                };
                fragmentManager.setFragmentResultListener(Constants.KEY_REQUEST,
                        PagesFragment.this,
                        onAddPageDialogResult);
            }
        };
        fabNewPage.setOnClickListener(newPageHandler);

        // Set up a onCleanSelection listener for visually clearing the views.
        OnSelectionListener onSelectionListener = new OnSelectionListener() {
            @Override
            public void onSelectionClear(List<Integer> selectedItemPositions) {
                for (Integer position : selectedItemPositions) {
                    // For each selected view, visually clear its selection.
                    View selectedView = rvPages.getLayoutManager().findViewByPosition(position);
                    MaterialCardView mcvCategory = selectedView.findViewById(R.id.mcvCategory);
                    mcvCategory.setStrokeWidth(0);
                    startPagesFragmentToolbar();
                }
            }

            @Override
            public void onSelectionDelete(List<Integer> selectedItemPositions) {
                HomeActivity.deleteSelectedItems(getContext()
                                    , pageAdapter, selectedItemPositions);
            }
        };
        selectionHandler.setOnSelectionListener(onSelectionListener);
    }

    public void startPagesFragmentToolbar() {
        // Set the action bar title to the subject name.
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.tbHome);
        int currentToolbarColor = HomeActivity.getToolbarColor(toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home_top_without_logout);
        HomeActivity.setToolbarText(toolbar
                ,HomeActivity.getCurrentSubject().getDescription()
                ,parentSection.getDescription());
        HomeActivity.setToolbarColor(toolbar, currentToolbarColor
                ,getResources().getColor(R.color.design_default_color_primary));
    }
}