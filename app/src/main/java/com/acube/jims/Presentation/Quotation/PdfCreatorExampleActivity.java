package com.acube.jims.Presentation.Quotation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFFooterView;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFPageBreakView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PdfCreatorExampleActivity extends PDFCreatorActivity {
    List<ResponseInvoiceList> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        createPDF("test", new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected PDFHeaderView getHeaderView(int pageIndex) {
        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());

        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
        SpannableString word = new SpannableString("PROFORMA INVOICE");
        word.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pdfTextView.setText(word);
        pdfTextView.setLayout(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        pdfTextView.getView().setGravity(Gravity.CENTER_VERTICAL);
        pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);

        horizontalView.addView(pdfTextView);

        PDFImageView imageView = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 0);
        imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageLayoutParam.setMargins(0, 0, 10, 0);
        imageView.setLayout(imageLayoutParam);

        horizontalView.addView(imageView);

        headerView.addView(horizontalView);

        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        headerView.addView(lineSeparatorView1);

        return headerView;
    }

    @Override
    protected PDFBody getBodyViews() {
        PDFBody pdfBody = new PDFBody();

        PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
       // pdfCompanyNameView.setText("Company Name");
        pdfBody.addView(pdfCompanyNameView);
        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView1);
        PDFTextView pdfAddressView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        //    pdfAddressView.setText("Address Line 1\nCity, State - 123456");
        pdfBody.addView(pdfAddressView);

        PDFLineSeparatorView lineSeparatorView2 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        lineSeparatorView2.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                8, 0));
        pdfBody.addView(lineSeparatorView2);

        PDFLineSeparatorView lineSeparatorView3 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        pdfBody.addView(lineSeparatorView3);

        int[] widthPercent = {25, 25, 25, 25}; // Sum should be equal to 100%
        String[] textInTable = {"Item Name", "Gold Weight", "Making Charge", "Price without tax", "Price with tax"};
        PDFTextView pdfTableTitleView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        //pdfTableTitleView.setText("PROFORMA INVOICE");
        pdfBody.addView(pdfTableTitleView);
/*
        final PDFPageBreakView pdfPageBreakView = new PDFPageBreakView(getApplicationContext());
        pdfBody.addView(pdfPageBreakView);*/

        PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
        for (String s : textInTable) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("" + s);
            tableHeader.addToRow(pdfTextView);
        }
        dataset = new ArrayList<>();
        dataset = getList();
        PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());
        ArrayList<String> goldweight = null;

        goldweight = new ArrayList<>();
        goldweight.add(dataset.get(1).getItemName());
        goldweight.add(String.valueOf(dataset.get(1).getGoldWeight()));
        goldweight.add(String.valueOf(dataset.get(1).getLabourCharge()));
        goldweight.add(String.valueOf(dataset.get(1).getPriceWithoutTax()));
        goldweight.add(String.valueOf(dataset.get(1).getPriceWithoutTax()));


        String[] strArray = goldweight.toArray(new String[goldweight.size()]);

        for (String s : strArray) {
            PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView.setText("");
            tableRowView1.addToRow(pdfTextView);


        }


        PDFTableView tableView = new PDFTableView(getApplicationContext(), tableHeader, tableRowView1);
        ArrayList<String> newdata;
        for (int i = 0; i < dataset.size(); i++) {
            // Create 10 rows and add to table.
            newdata = new ArrayList<>();
            newdata.add(dataset.get(i).getItemName());
            newdata.add(String.valueOf(dataset.get(i).getGoldWeight()));
            newdata.add(String.valueOf(dataset.get(i).getLabourCharge()));
            newdata.add(String.valueOf(dataset.get(i).getPriceWithoutTax()));
            newdata.add(String.valueOf(dataset.get(i).getPriceWithoutTax()));
            String[] strArrays = newdata.toArray(new String[newdata.size()]);
            PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
            for (String s : strArrays) {
                PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
                pdfTextView.setText(s);
                tableRowView.addToRow(pdfTextView);
            }
            tableView.addRow(tableRowView);

        }
        pdfBody.addView(tableView);


        PDFLineSeparatorView lineSeparatorView4 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.BLACK);
        pdfBody.addView(lineSeparatorView4);

        PDFTextView pdfIconLicenseView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

        pdfBody.addView(pdfIconLicenseView);

        return pdfBody;
    }

    @Override
    protected PDFFooterView getFooterView(int pageIndex) {
        PDFFooterView footerView = new PDFFooterView(getApplicationContext());

        PDFTextView pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "Page: %d", pageIndex + 1));
        pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);
        PDFTextView pdfTextViewtotal = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        pdfTextViewtotal.setText("Discount : 3562566");
        pdfTextViewtotal.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewtotal.getView().setGravity(Gravity.END);

        PDFTextView pdfTextViewDiscount = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        pdfTextViewDiscount.setText("Total : 3562566");
        pdfTextViewDiscount.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewDiscount.getView().setGravity(Gravity.END);
        footerView.addView(pdfTextViewtotal);
        footerView.addView(pdfTextViewPage);
        footerView.addView(pdfTextViewDiscount);

        return footerView;
    }

    @Nullable
    @Override
    protected PDFImageView getWatermarkView(int forPage) {
        PDFImageView pdfImageView = new PDFImageView(getApplicationContext());
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200, Gravity.CENTER);
        pdfImageView.setLayout(childLayoutParams);

        pdfImageView.setImageResource(R.mipmap.ic_launcher);
        pdfImageView.setImageScale(ImageView.ScaleType.FIT_CENTER);
        pdfImageView.getView().setAlpha(0.3F);

        return pdfImageView;
    }

    @Override
    protected void onNextClicked(final File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);

       Intent intentPdfViewer = new Intent(PdfCreatorExampleActivity.this, PdfViewerExampleActivity.class);
       intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);

       startActivity(intentPdfViewer);




    }






    public List<ResponseInvoiceList> getList() {
        List<ResponseInvoiceList> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "invoicelist");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ResponseInvoiceList>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }
}