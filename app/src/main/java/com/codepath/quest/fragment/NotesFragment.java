package com.codepath.quest.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.quest.BuildConfig;
import com.codepath.quest.R;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.NotesAdapter;
import com.codepath.quest.model.Answer;
import com.codepath.quest.model.Constants;
import com.codepath.quest.model.Page;
import com.codepath.quest.model.Question;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    private RecyclerView rvNotes;
    private NotesAdapter notesAdapter;
    private List<Question> questionsList;
    private Page parentPage;

    // Required empty public constructor
    public NotesFragment() {}

    /**
     * Creates a new instance of the notes fragment.
     *
     * @param parentPage the parent page for the questions to refer to
     * @return A new instance of fragment NotesFragment.
     */
    public static NotesFragment newInstance(Page parentPage) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_PAGE, parentPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentPage = getArguments().getParcelable(Constants.KEY_PAGE);
        }

        // Let HomeActivity know that what the current page is.
        HomeActivity.setCurrentPage(parentPage);

        // Initialize the adapter.
        questionsList = new ArrayList<>();
        notesAdapter = new NotesAdapter(getContext(), questionsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the toolbar.
        startNotesFragmentToolbar();

        // Set up recycler view.
        rvNotes = view.findViewById(R.id.rvNotes);
        rvNotes.setAdapter(notesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));


        // Let the Home Activity know what the current page is.
        HomeActivity.setCurrentPage(parentPage);

        // Fill the recycler view with questions.
        try {
            Question.queryQuestions(notesAdapter, parentPage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void startNotesFragmentToolbar() {
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.tbHome);
        int currentToolbarColor = HomeActivity.getToolbarColor(toolbar);
        toolbar.getMenu().clear();

        // Add the "Add question" menu item.
        toolbar.inflateMenu(R.menu.menu_notes);
        Menu menu = toolbar.getMenu();

        //Set up an on click listener for adding a new question.
        MenuItem addQAndAItem = menu.findItem(R.id.iAddQuestionAndAnswer);
        MenuItem.OnMenuItemClickListener addQAndAHandler = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startAddQDialog(getContext(), R.layout.dialog_add_q_and_a);
                return true;
            }
        };
        addQAndAItem.setOnMenuItemClickListener(addQAndAHandler);

        // Set up an on click listener for exporting the page as a pdf.
        MenuItem pdfExportItem = menu.findItem(R.id.iExportPDF);
        MenuItem.OnMenuItemClickListener pdfExportHandler = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startPDFExportDialog(getContext(), R.layout.dialog_export_pdf);
                return false;
            }
        };
        pdfExportItem.setOnMenuItemClickListener(pdfExportHandler);



        // Set the action bar title and subtitle.
        String subjectDescription = HomeActivity.getCurrentSubject().getDescription();
        String sectionDescription = HomeActivity.getCurrentSection().getDescription();
        String pageDescription = parentPage.getDescription();
        HomeActivity.setToolbarText(toolbar
                ,subjectDescription + " | " + sectionDescription
                ,pageDescription );

        // Animate the tool bar color.
        HomeActivity.setToolbarColor(toolbar, currentToolbarColor
                ,getResources().getColor(R.color.design_default_color_primary));
    }


    public void startAddQDialog(Context context, int dialogId) {
        Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(dialogId);

        // Set an on click listener for the submit button.
        Button submit = dialog.findViewById(R.id.btnSubmitQAndA);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the question and answer views.
                EditText etQuestionDescription = dialog.findViewById(R.id.etAddQ);
                String questionDescription = etQuestionDescription.getText().toString();
                EditText etAnswerDescription = dialog.findViewById(R.id.etAddAns);
                String answerDescription = etAnswerDescription.getText().toString();

                // Parse does not accept an empty string of length 0 as acceptable
                // value. Thus, we set the description to a space character if nothing
                // was typed in for the answer.
                if (answerDescription.equals("")) {
                    answerDescription = " ";
                }
                createQAndA(questionDescription, answerDescription);

                // Hide the dialog.
                dialog.hide();
            }
        });
        dialog.show();
    }

    /**
     * Creates a new question and answer in the database and updates the adapter.
     * If no answer is provided, the answer description will be
     * set to " " (a space character).
     *
     * @param questionDescription the description of the question
     * @param answerDescription the description of the answer; can be ""
     */
    public Question createQAndA(String questionDescription, String answerDescription) {
        // Create the question in the database and set it's attributes.
        ParseUser user = ParseUser.getCurrentUser();

        Question question = new Question();
        question.setDescription(questionDescription);
        question.setParent(parentPage);
        question.setUser(user);

        // We want a new answer object attached to the question
        // even if the answer is empty.
        Answer answer = new Answer();
        answer.setDescription(answerDescription);
        answer.setParent(question);
        answer.setUser(user);
        answer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // The answer to a question that is currently
                // saving will always be null since a processor
                // speeds are always faster than network latency.
                // To be safe, we want to set the answer only after
                // the answer is done saving.

                question.setAnswer(answer);
                question.saveInBackground();

                // Render the question to the adapter.
                notesAdapter.add(question);
                int lastIndex = notesAdapter.getQuestions().size() - 1;
                rvNotes.scrollToPosition(lastIndex);
            }
        });
        return question;
    }

    public void startPDFExportDialog(Context context, int dialogId) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(dialogId);
        dialog.show();

        // Set content to the note page in case the user doesn't want to
        // type in anything.
        EditText etFileName = dialog.findViewById(R.id.etFileName);
        String fileName = replaceIllegalCharacters(parentPage.getDescription());
        etFileName.setText(fileName);

        // Set up an on click listener for the export pdf button.
        Button btnExport = dialog.findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportDataToPDF(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.hide();
            }
        });
    }

    private String replaceIllegalCharacters (String fileName) {
        String result = "";
        for (int i = 0; i < fileName.length(); i++) {
            Character currentChar = fileName.charAt(i);
            if (Constants.ILLEGAL_FILE_NAME_CHARS_SET.contains(currentChar)) {
                // If the character is illegal, replace it with an underscore.
                result += "_";
            } else {
                result += currentChar;
            }
        }
        return result;
    }

    private void exportDataToPDF(String fileName) throws IOException {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(path, fileName + ".pdf");
        PdfWriter writer = null;
        try {
            //writer = new PdfWriter(path );
            writer = new PdfWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Write question & answer descriptions to the document.
        write(document);

        document.close();

        openPDF(file);
    }

    public void write(Document document) {
        // Write the page title.
        Paragraph pageTitle = new Paragraph(parentPage.getDescription());
        pageTitle.setFontColor(ColorConstants.BLUE).setFontSize(24.0f);
        document.add(pageTitle);

        // Write the questions and answers.
        for (Question question: notesAdapter.getQuestions()) {
            Table table = new Table(2);
            table.addCell(writeCell(question.getDescription(), TextAlignment.LEFT).setFontColor(ColorConstants.BLUE));
            table.addCell(writeCell(question.getAnswer().getDescription(), TextAlignment.RIGHT));
            document.add(table);
        }
    }

    public Cell writeCell(String description, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(description));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public void openPDF(File pdfFile) {
        Uri uriPath = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uriPath, "application/pdf");
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}

