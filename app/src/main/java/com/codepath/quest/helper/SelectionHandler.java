package com.codepath.quest.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Visually clears the selection.
 */
public class SelectionHandler {
    private OnSelectionListener onSelectionListener = null;

    public void setOnSelectionListener(OnSelectionListener listener) {
        this.onSelectionListener = listener;
    }

    /** Methods responsible for visually clearing the selection **/

    public void startSelectionClear(List<Integer> selectedPostions) {
        onSelectionListener.onSelectionClear(selectedPostions);
    }

    /** Methods responsible for deleting the selected items **/

    public void startSelectionDelete(List<Integer> selectedPositions) {
        onSelectionListener.onSelectionDelete(selectedPositions);
    }
}