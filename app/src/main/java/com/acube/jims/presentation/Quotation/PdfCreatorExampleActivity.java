package com.acube.jims.presentation.Quotation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFFooterView;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfCreatorExampleActivity extends PDFCreatorActivity {
    List<ResponseInvoiceList> dataset;
    double total = 0.0;
    PermissionListener permissionlistener;

    double totalItemtax = 0.0;
    double netamount = 0.0;
    double discount = 0.0;
    double labourchargewithtax = 0.0;
    double labourchargetax = 0.0;
    double labourcharge = 0.0;
    File fileToShare;
    List<KaratPrice> karatPriceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dataset = new ArrayList<>();

        dataset = getList();
        createPDF("Invoice " + getCurrentDateAndTime(), new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PdfCreatorExampleActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted  () {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
    }

    public static String getCurrentDateAndTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
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
        String[] textInTable = {"Item Name", "Karat", "Serial No.", "Gold Weight", "Making Charge", "Price without tax", "Price with tax"};
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

        PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());
        ArrayList<String> goldweight = null;

        goldweight = new ArrayList<>();
        goldweight.add(dataset.get(0).getItemName());
        goldweight.add(dataset.get(0).getKaratCode());
        goldweight.add(dataset.get(0).getSerialNumber());
        goldweight.add(String.valueOf(dataset.get(0).getGoldWeight()));
        goldweight.add(String.valueOf(dataset.get(0).getLabourCharge()));
        goldweight.add(String.valueOf(dataset.get(0).getPriceWithoutTax()));
        goldweight.add(String.valueOf(dataset.get(0).getPriceWithoutTax()));


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

            total += dataset.get(i).getPriceWithoutTax();
            totalItemtax += (dataset.get(i).getPriceWithoutTax() / 100.0f) * dataset.get(i).getItemTax();
            labourchargetax += (dataset.get(i).getLabourCharge() / 100.0f) * dataset.get(i).getLabourTax();
            labourcharge += dataset.get(i).getLabourCharge();
            labourchargewithtax += (dataset.get(i).getLabourCharge() / 100.0f) * dataset.get(i).getLabourTax() + dataset.get(i).getLabourCharge();
            double totals = dataset.get(i).getPriceWithoutTax() + (dataset.get(i).getPriceWithoutTax() / 100.0f) * dataset.get(i).getItemTax() + (dataset.get(i).getLabourCharge() / 100.0f) * dataset.get(i).getLabourTax() + dataset.get(i).getLabourCharge();
            newdata = new ArrayList<>();
            newdata.add(dataset.get(i).getItemName());
            newdata.add(dataset.get(i).getKaratCode());
            newdata.add(dataset.get(i).getSerialNumber());
            newdata.add(String.valueOf(dataset.get(i).getGoldWeight()));
            newdata.add(String.valueOf(dataset.get(i).getLabourCharge()));
            newdata.add(String.valueOf(dataset.get(i).getPriceWithoutTax()));
            newdata.add(String.valueOf(totals));
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
        pdfTextViewtotal.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewtotal.getView().setGravity(Gravity.END);
        LocalPreferences.retrieveStringPreferences(getApplicationContext(), "discount");
        pdfTextViewtotal.setText("Total : " + LocalPreferences.retrieveStringPreferences(getApplicationContext(), "total"));
        PDFTextView pdfTextViewDiscount = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        PDFTextView pdfTexttotalwithouttax = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

        pdfTextViewDiscount.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewDiscount.getView().setGravity(Gravity.END);
        pdfTexttotalwithouttax.setText("\n\n Price without tax : " + LocalPreferences.retrieveStringPreferences(getApplicationContext(), "pricewithouttax"));

        pdfTexttotalwithouttax.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTexttotalwithouttax.getView().setGravity(Gravity.END);
        pdfTextViewDiscount.setText("Discount : " + LocalPreferences.retrieveStringPreferences(getApplicationContext(), "discount"));
        PDFTextView totaltax = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        LocalPreferences.retrieveStringPreferences(getApplicationContext(), "totaltax");
        totaltax.setText("Total Tax :" + LocalPreferences.retrieveStringPreferences(getApplicationContext(), "totaltax"));
        totaltax.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        totaltax.getView().setGravity(Gravity.END);

        pdfTextViewDiscount.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        pdfTextViewDiscount.getView().setGravity(Gravity.END);

        //goldrateview
        PDFTextView rateheader = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

        rateheader.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        rateheader.getView().setGravity(Gravity.END);
        rateheader.setText("Today's Rate \n");
        footerView.addView(rateheader);
        for (int i = 0; i < dataset.size(); i++) {
            karatPriceList = new ArrayList<>();
            karatPriceList = dataset.get(i).getKaratPriceList();
        }
        for (int i = 0; i < karatPriceList.size(); i++) {
            PDFTextView karatone = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
            karatone.setLayout(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0));
            karatone.getView().setGravity(Gravity.END);
            karatone.setText(karatPriceList.get(i).getKaratCode() + " K: " + karatPriceList.get(i).getKaratPrice());
            footerView.addView(karatone);
        }

       /* PDFTextView karatone = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);

        karatone.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        karatone.getView().setGravity(Gravity.END);
        karatone.setText(dataset.get(0).getKaratPriceList().get(0).getKaratCode()+" : " + dataset.get(0).getKaratPriceList().get(0).getKaratPrice());
*/
      /*  PDFTextView karattwo = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        karattwo.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        karattwo.getView().setGravity(Gravity.END);
        karattwo.setText(dataset.get(1).getKaratPriceList().get(1).getKaratCode()+" : " + dataset.get(1).getKaratPriceList().get(1).getKaratPrice());
*/
 /*       PDFTextView karatthree = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
        karattwo.setLayout(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        karatthree.getView().setGravity(Gravity.END);
        karatthree.setText(dataset.get(2).getKaratPriceList().get(2).getKaratCode()+" : " + dataset.get(2).getKaratPriceList().get(2).getKaratPrice());*/

       /* footerView.addView(karatone);
        footerView.addView(karattwo);
        footerView.addView(karatthree);*/
        footerView.addView(pdfTexttotalwithouttax);
        footerView.addView(pdfTextViewDiscount);
        footerView.addView(totaltax);
        footerView.addView(pdfTextViewtotal);
        footerView.addView(pdfTextViewPage);

        return footerView;
    }

    @Nullable
    @Override
    protected PDFImageView getWatermarkView(int forPage) {
       /* PDFImageView pdfImageView = new PDFImageView(getApplicationContext());
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                200, Gravity.CENTER);
        pdfImageView.setLayout(childLayoutParams);

        pdfImageView.setImageResource(n);
        pdfImageView.setImageScale(ImageView.ScaleType.FIT_CENTER);
        pdfImageView.getView().setAlpha(0.3F);*/

        return null;
    }

    @Override
    protected void onNextClicked(final File savedPDFFile) {
        Uri pdfUri = Uri.fromFile(savedPDFFile);
        fileToShare = new File(pdfUri.getPath());
        Uri apkURI = FileProvider.getUriForFile(
                getApplicationContext(),
                getApplicationContext()
                        .getPackageName() + ".provider", fileToShare);
       /* Intent intentPdfViewer = new Intent(PdfCreatorExampleActivity.this, PdfViewerExampleActivity.class);
        intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri);
        startActivity(intentPdfViewer);*/
        showPopupWindow(apkURI);

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

    public void showPopupWindow(Uri pdf) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_share, null);
        CardView whatsapp = alertLayout.findViewById(R.id.cdvsharewhatsapp);
        CardView email = alertLayout.findViewById(R.id.cdvshareemail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rony8652@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n Please find the attachment for invoice");
                emailIntent.putExtra(Intent.EXTRA_STREAM, pdf);
                emailIntent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
            }
        });


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(PdfCreatorExampleActivity.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //   sendTextMsgOnWhatsApp("+919747337738",emailbody);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Please check the invoice");
                shareIntent.putExtra(Intent.EXTRA_STREAM, pdf);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),"Install Whatsapp",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}