package com.codepath.quest.helper;

import android.util.Log;

import com.codepath.quest.adapter.SearchAdapter;
import com.codepath.quest.model.Constants;
import com.codepath.quest.model.Question;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SearchEngine {
    public static void searchQuestions(SearchAdapter adapter, String target) {
        // Query for questions.
        ParseQuery<Question> query = ParseQuery.getQuery(Question.class);
        query.include(Constants.KEY_ANSWER);
        query.include(Constants.KEY_PARENT);

        // Filters the query to find questions under the current user.
        query.whereEqualTo(Constants.KEY_USER, ParseUser.getCurrentUser());

        // Preprocess the target text by lower casing.
        String processedTarget = target.toLowerCase();

        FindCallback<Question> queryCallBack = new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null) {
                    // Add the filtered questions to the adapter.
                    adapter.addAll(filter(questions, processedTarget));
                } else {
                    Log.e("Question", "Failure in search query.", e);
                }
            }
        };
        query.findInBackground(queryCallBack);
    }

    /**
     * Filters questions by:
     * 1) Removing the need for case sensitivity.
     * 2) Ranking search results by how many times the tokens (from the user input)
     *    appear inside the question & answers.
     * 3) Searching only what matters by removing insignificant
     *    words such as "is, the, I".
     *
     * @param questions the questions to filter.
     * @param target what to search
     * @return the filtered list of questions.
     */
    public static List<Question> filter(List<Question> questions, String target) {
        List<Question> results = new ArrayList<>();
        List<String> tokens = Arrays.asList(target.split("[ -.!?]"));

        // Search the words that tokens that matter.
        tokens = removeInsignificantTokens(tokens);

        for (Question question: questions) {
            // Preprocess the question description and answer description
            // by lower casing them.
            String questionDescription = question.getDescription()
                    .toLowerCase();
            String answerDescription = question.getAnswer().getDescription()
                    .toLowerCase();
            // The ranking system works as follows:
            // 1) Any question with a token match gets + 1 to their rank.
            // 2) Any answer with a token match gets + 1 to their rank.
            question.setRank(0);

            // Search each token of the target with the question
            // description and answer description.
            for (int i = 0; i < tokens.size(); i++) {
                // For example if the question's description is "when did world war II end?"
                // and the answer's description is "world war II ended in 1945."
                // and the target is "world war", then the rank for this question would be 2
                // and added to the results to be rendered.
                if (kmpSearch(tokens.get(i), questionDescription)) {
                    question.setRank(question.getRank() + 1);
                    results.add(question);
                }
                if (kmpSearch(tokens.get(i), answerDescription)) {
                    question.setRank(question.getRank() + 1);
                    results.add(question);
                }
            }
        }

        // Undoubtedly, there will be duplicate questions. We don't want to render
        // duplicates.
        removeDuplicates(results);

        // Sort them according to their rank.
        Collections.sort(results, new Comparator<Question>() {
            @Override
            public int compare(Question q1, Question q2) {
                return q2.getRank() - q1.getRank();
            }
        });

        return results;
    }


    /**
     * Returns true if the target is found in the search space and
     * false otherwise. Search implementation is based off of the
     * Knuth-Morris-Pratt pattern recognition algorithm.
     *
     * @param target what to search
     * @param searchSpace the text to search through
     * @return true if the target is contained in the search space
     *          , false otherwise
     */
    // target = box
    // searchspace = bowl


    //               000123
    // index:
    // search space: balxballballalxoonball
    // target:       allall
    //                allall
    //                  allall
    //                   allall
    //                    allall
    //                       allall
    //                        allall
    //                           allall
    //                                 allball
    //
    //
    //

    public static boolean kmpSearch(String target, String searchSpace) {
        if (target.isEmpty() || searchSpace.isEmpty()) {
            return false;
        }
        List<Integer> failureTable = buildFailureTable(target);
        int targetIndex = 0;
        int searchSpaceIndex = 0;
        int searchSpacePivotIndex = searchSpaceIndex;

        // The end condition ensures that the algorithm doesn't make unnecessary
        // comparisons at the end of the search space.
        while (searchSpacePivotIndex + target.length() <= searchSpace.length()) {
            if (target.charAt(targetIndex) == searchSpace.charAt(searchSpaceIndex)) {
                // CASE 1: There's a character match. See if there is a target match.
                if (targetIndex == target.length() - 1) { return true; }

                targetIndex += 1;
                searchSpaceIndex += 1;
            } else {
                // CASE 2: There's a character mismatch. Target index is 0.
                if (targetIndex == 0) {
                    searchSpaceIndex += 1;
                } else {
                    // CASE 3: There's a character mistmatch. Target index is non-zero.
                    //         Target index will go to its matching prefix in the failure
                    //         table rather than the brute force setting the target index
                    //         to 0.
                    targetIndex = failureTable.get(targetIndex - 1);
                }
                searchSpacePivotIndex = searchSpaceIndex;
            }
        }
        return false;
    }




    /**
     * The failure table tells the KMP pattern recognition
     * algorithm how much to shift when there is a mismatch (failure).
     *
     * @param pattern the search target for the search space
     * @return the length of the longest prefix for each suffix
     *         ex: loveCANlove will return
     *             l o v e C A N l o v e
     *             0 0 0 0 0 0 0 1 2 3 4
     *         The longest prefix for suffix "love" (end of pattern) is "love".
     */

    // ababx
    // 00120
    public static List<Integer> buildFailureTable(String pattern) {
        List<Integer> failureTable = new ArrayList<>(pattern.length());
        failureTable.add(0, 0);
        int i = 0;
        int j = 1;
        while (j < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                // CASE 1: There's a match.
                i += 1;
                failureTable.add(j, i);
                j += 1;
            } else {
                if (i == 0) {
                    // CASE 2: There's a mismatch. Index i is 0.
                    failureTable.add(j, 0);
                    j += 1;
                } else {
                    // Case 3: There's a mistmatch. Index i is greater than 0.
                    i = failureTable.get(i - 1);
                }
            }
        }
        return failureTable;
    }


    public static List<String> removeInsignificantTokens(List<String> tokens) {
        List<String> result = new ArrayList<>();
        for (String token: tokens) {
            if (!Constants.INSIGNIFICANT_WORDS_SET.contains(token)) {
                result.add(token);
            }
        }
        return result;
    }


    /**
     * Removes the duplicates of the argument by reference.
     *
     * @param questions the list of questions to remove duplicates from
     */
    private static void removeDuplicates(List<Question> questions) {
        Set<Question> result = new LinkedHashSet<>(questions);
        questions.clear();
        questions.addAll(result);
    }

}
