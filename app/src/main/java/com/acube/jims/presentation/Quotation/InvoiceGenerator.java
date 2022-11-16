package com.acube.jims.presentation.Quotation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.acube.jims.R;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.nio.file.FileSystem;

public class InvoiceGenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_generator);
       // PdfWriter.getInstance()
        createPDF();
    }

    private void createPDF() {
        File file = new File("","");

       // PdfDocument pdfDocument=new PdfDocument();

    }
}