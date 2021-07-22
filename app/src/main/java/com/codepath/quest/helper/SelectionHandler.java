package com.codepath.quest.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Visually clears the selection.
 */
public class SelectionHandler {
    private List<OnSelectionListener> onSelectionListenerList = null;

    public void setOnSelectionListener(OnSelectionListener listener) {
        if (onSelectionListenerList == null) {
            onSelectionListenerList = new ArrayList<>();
        }
        this.onSelectionListenerList.add(listener);
    }

    /** Methods responsible for visually clearing the selection **/

    public void startSelectionClear(List<Integer> selectedPostions) {
        for (OnSelectionListener listener: onSelectionListenerList) {
            listener.onSelectionClear(selectedPostions);
        }
    }

    /** Methods responsible for deleting the selected items **/

    public void startSelectionDelete(List<Integer> selectedPositions) {
        for (OnSelectionListener listener: onSelectionListenerList) {
            listener.onSelectionDelete(selectedPositions);
        }
    }
}