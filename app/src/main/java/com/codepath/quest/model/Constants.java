package com.codepath.quest.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    // Constants for the data models and fragments to use.
    public static final String KEY_PARENT = "parent";
    public static final String KEY_SUBJECT = "Subject";
    public static final String KEY_SECTION = "Section";
    public static final String KEY_PAGE = "Page";
    public static final String KEY_QUESTION = "Question";
    public static final String KEY_USER = "user";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DIALOG = "dialog";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_OBJECT_ID = "objectId";


    // Constants for the search engine.
    public static final String[] INSIGNIFICANT_WORDS =
            {"a", "about", "above", "after", "again", "against", "ain", "all"
            , "am", "an", "and", "any", "are", "aren", "aren't", "as", "at", "be"
            , "because", "been", "before", "being", "below", "between", "both", "but"
            , "by", "can", "couldn", "couldn't", "d", "did", "didn", "didn't", "do"
            , "does", "doesn", "doesn't", "doing", "don", "don't", "down", "during"
            , "each", "few", "for", "from", "further", "had", "hadn", "hadn't", "has"
            , "hasn", "hasn't", "have", "haven", "haven't", "having", "he", "her", "here"
            , "hers", "herself", "him", "himself", "his", "how", "i", "if", "in"
            , "into", "is", "isn", "isn't", "it", "it's", "its", "itself", "just"
            , "ll", "m", "ma", "me", "mightn", "mightn't", "more", "most", "mustn"
            , "mustn't", "my", "myself", "needn", "needn't", "no", "nor", "not", "now"
            , "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours"
            , "ourselves", "out", "over", "own", "re", "s", "same", "shan", "shan't"
            , "she", "she's", "should", "should've", "shouldn", "shouldn't", "so", "some"
            , "such", "t", "than", "that", "that'll", "the", "their", "theirs", "them"
            , "themselves", "then", "there", "these", "they", "this", "those", "through"
            , "to", "too", "under", "until", "up", "ve", "very", "was", "wasn", "wasn't"
            , "we", "were", "weren", "weren't", "while", "whom", "will", "with", "won", "won't"
            , "y", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself"
            , "yourselves", "could", "he'd", "he'll", "he's", "here's", "how's", "i'd", "i'll"
            , "i'm", "i've", "let's", "ought", "she'd", "she'll", "that's", "there's", "they'd"
            , "they'll", "they're", "they've", "we'd", "we'll", "we're", "we've", "what's"
            , "when's", "where's", "who's", "why's", "would", "wouldn", "wouldn't"};
    public static final Set<String> INSIGNIFICANT_WORDS_SET
            = new HashSet<>(Arrays.asList(INSIGNIFICANT_WORDS));



}
