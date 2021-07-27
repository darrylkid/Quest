package com.codepath.quest.helper;

import java.util.List;

public interface OnSelectionListener {
    void onSelectionClear(List<Integer> selectedItemPositions);
    void onSelectionDelete(List<Integer> selectedItemPositions);
}
