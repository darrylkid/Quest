package com.codepath.quest.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.helper.MindMapRenderer;
import com.codepath.quest.helper.QuestToast;
import com.codepath.quest.model.Subject;

import org.jetbrains.annotations.NotNull;

import me.jagar.mindmappingandroidlibrary.Views.ConnectionTextMessage;
import me.jagar.mindmappingandroidlibrary.Views.Item;
import me.jagar.mindmappingandroidlibrary.Views.ItemLocation;
import me.jagar.mindmappingandroidlibrary.Views.MindMappingView;
import me.jagar.mindmappingandroidlibrary.Zoom.ZoomLayout;

public class MindMapFragment extends Fragment {
    static private View root;

    // Required empty public constructor
    public MindMapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mind_map, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.root = view;
        // Set the toolbar title.
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.tbHome);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home_top_with_logout);
        HomeActivity.setToolbarText(toolbar, getString(R.string.home), "");
        int oldToolbarColor = HomeActivity.getToolbarColor(toolbar);
        HomeActivity.setToolbarColor(toolbar, oldToolbarColor
                ,HomeActivity.getThemeColor(getContext(), R.attr.colorPrimary));


        // Set the action bar's logout button to be visible.
        MenuItem logoutIcon = toolbar.getMenu().findItem(R.id.iLogoutHome);
        if (logoutIcon != null) {
            logoutIcon.setVisible(true);
        }

        // Initialize the mind map.
        MindMappingView mindMap = view.findViewById(R.id.mindMap);
        MindMapRenderer renderer = new MindMapRenderer(getContext(), mindMap);
        Subject.querySubjects(renderer);

        //((Item)item.getChildAt(0)).setContent("This child's content has been changed by the parent");

    }

    public static void clearMindMap(MindMappingView mindMap) {
        mindMap.removeAllViews();
        ((ZoomLayout)root.findViewById(R.id.zoom)).removeAllViews();
    }


}