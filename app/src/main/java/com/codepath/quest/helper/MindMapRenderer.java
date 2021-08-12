package com.codepath.quest.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.fragment.MindMapFragment;
import com.codepath.quest.model.Section;
import com.codepath.quest.model.Subject;

import java.util.List;

import me.jagar.mindmappingandroidlibrary.Listeners.OnItemClicked;
import me.jagar.mindmappingandroidlibrary.Views.ConnectionTextMessage;
import me.jagar.mindmappingandroidlibrary.Views.Item;
import me.jagar.mindmappingandroidlibrary.Views.ItemLocation;
import me.jagar.mindmappingandroidlibrary.Views.MindMappingView;

/**
 * Class responsible for rendering the all the items of the mind map.
 */
public class MindMapRenderer {
    final private String SUBJECT_ROOT_DESCRIPTION = "CLICK A SUBJECT";
    final private int RENDERED_DISTANCE_FROM_PARENT_ITEM = 200;
    final private int ITEM_MARGIN = 10;

    private Context context;
    private MindMappingView mindMap;


    public MindMapRenderer(Context context, MindMappingView mindMap) {
        this.context = context;
        this.mindMap = mindMap;
    }

    public Item createSubjectItem(Subject subject) {
        Item subjectItem = new Item(context, subject.getDescription(), null, false);
        Resources resources = context.getResources();

        // Customize the category background.
        Drawable subjectBackground
                = ResourcesCompat.getDrawable(resources
                                                , R.drawable.background_mind_map_category
                                                , null);
        subjectItem.setBackground(subjectBackground);

        // Customize the category title color and size.
        TextView title = subjectItem.getTitle();
        title.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null));
        title.setTextSize(18);

        // Create an on click listener for each subject item for navigation.
        subjectItem.setClickable(true);
        subjectItem.bringToFront();
        subjectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the mind map.
                MindMapFragment.clearMindMap(mindMap);

                // Recreate the mind map with the subject as the root.
                Section.querySections(MindMapRenderer.this, subject);
            }
        });
        return subjectItem;
    }

    public Item createSectionItem(String sectionName) {
        Item sectionItem = new Item(context, sectionName, null, false);
        Resources resources = context.getResources();

        // Customize the category background.
        Drawable sectionBackground
                = ResourcesCompat.getDrawable(resources
                , R.drawable.background_mind_map_category
                , null);
        sectionItem.setBackground(sectionBackground);

        // Customize the category title color and size.
        TextView title = sectionItem.getTitle();
        title.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null));
        title.setTextSize(18);

        // Create an on click listener for each section item for navigation.
        sectionItem.bringToFront();
        return sectionItem;

    }

    public void renderSubjects(List<Subject> subjects) {
        // Create the root node.
        Item root = new Item(context, SUBJECT_ROOT_DESCRIPTION, null, true);
        Resources resources = context.getResources();
        Drawable background
                = ResourcesCompat.getDrawable(resources
                , R.drawable.background_mind_map_category
                , null);
        root.setBackground(background);
        root.getTitle().setTextColor(HomeActivity.getThemeColor(context, R.attr.colorSecondary));
        mindMap.addCentralItem(root, false);

        // The API allows us to render an child item north, east, south and west of
        // the parent item. To render in all directions, we will use a location counter
        // that will be modulo-ed to 4 to select the direction and incremented by one
        // for each subject.
        int locationCounter = 0;

        for (Subject subject: subjects) {
            Item subjectItem = createSubjectItem(subject);
            int direction = -1;

            // Cyclically assign the direction.
            if (locationCounter % 4 == 0) {
                direction = ItemLocation.TOP;
            } else if (locationCounter % 4 == 1) {
                direction = ItemLocation.RIGHT;
            } else if (locationCounter % 4 == 2) {
                direction = ItemLocation.BOTTOM;
            } else {
                direction = ItemLocation.LEFT;
            }

            locationCounter++;

            // Render an invisible connection.
            ConnectionTextMessage connectionTextMessage = new ConnectionTextMessage(context);
            connectionTextMessage.setVisibility(View.INVISIBLE);

            mindMap.addItem(subjectItem, root, RENDERED_DISTANCE_FROM_PARENT_ITEM, ITEM_MARGIN
                            , direction, false, connectionTextMessage);

        }
    }

    public void renderSections(List<Section> sections, Subject parentSubject) {
        // Create the root subject node.
        Item rootSubject = new Item(context, parentSubject.getDescription(), null, true);
        Resources resources = context.getResources();
        Drawable background
                = ResourcesCompat.getDrawable(resources
                , R.drawable.background_mind_map_category
                , null);
        rootSubject.setBackground(background);
        rootSubject.getTitle().setTextColor(HomeActivity.getThemeColor(context, R.attr.colorSecondary));
        mindMap.addCentralItem(rootSubject, false);

        int locationCounter = 0;

        // Create each section item.
        for (Section section: sections) {
            Item sectionItem = createSectionItem(section.getDescription());
            int direction = -1;

            // Cyclically assign the direction.
            if (locationCounter % 4 == 0) {
                direction = ItemLocation.TOP;
            } else if (locationCounter % 4 == 1) {
                direction = ItemLocation.RIGHT;
            } else if (locationCounter % 4 == 2) {
                direction = ItemLocation.BOTTOM;
            } else {
                direction = ItemLocation.LEFT;
            }

            locationCounter++;

            mindMap.addItem(sectionItem, rootSubject, RENDERED_DISTANCE_FROM_PARENT_ITEM, ITEM_MARGIN
                    , direction, false, null);
        }
    }


}
