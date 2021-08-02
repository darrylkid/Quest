package com.codepath.quest.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.codepath.quest.BuildConfig;
import com.codepath.quest.activity.HomeActivity;
import com.codepath.quest.adapter.NotesAdapter;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextToPDF {
    public static void exportDataToPDF(Context context, NotesAdapter adapter, String fileName) throws IOException {
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
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

        // Write question & answer descriptions to the document.
        write(adapter, document);

        document.close();

        openPDF(context, file);
    }

    private static void write(NotesAdapter adapter, Document document) {
        // Write the page title.
        Paragraph pageTitle = new Paragraph(HomeActivity.getCurrentPage().getDescription());
        pageTitle.setFontColor(ColorConstants.BLUE).setFontSize(24.0f);
        document.add(pageTitle);

        // Write the questions and answers.
        for (Question question: adapter.getQuestions()) {
            Table table = new Table(2);
            table.addCell(writeCell(question.getDescription(), TextAlignment.LEFT).setFontColor(ColorConstants.BLUE));
            table.addCell(writeCell(question.getAnswer().getDescription(), TextAlignment.RIGHT));
            document.add(table);
        }
    }

    private static Cell writeCell(String description, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(description));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private static void openPDF(Context context, File pdfFile) {
        Uri uriPath = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uriPath, "application/pdf");
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
