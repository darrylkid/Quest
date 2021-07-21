package com.codepath.quest.helper;

import java.util.List;

/**
 * Visually clears the selection.
 */
public class SelectionClearer {
    private OnSelectionClearListener onSelectionClearListener;

    public void startClearSelection(List<Integer> selectedPostions) {
        onSelectionClearListener.onSelectionClear(selectedPostions);
    }

    public void setOnCleanSelectionListener(OnSelectionClearListener listener ) {
        this.onSelectionClearListener = listener;
    }


}