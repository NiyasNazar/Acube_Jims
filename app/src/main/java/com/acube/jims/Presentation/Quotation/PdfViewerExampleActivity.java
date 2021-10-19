package com.acube.jims.Presentation.Quotation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.acube.jims.R;
import com.gun0912.tedpermission.normal.TedPermission;
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity;

import java.io.File;
import java.net.URLConnection;

public class PdfViewerExampleActivity  extends PDFViewerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pdf Viewer");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.colorTransparentBlack)));
        }
        File fileToShare = getPdfFile();
        if (fileToShare == null || !fileToShare.exists()) {
            //Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();

        }

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        Uri apkURI = FileProvider.getUriForFile(
                getApplicationContext(),
                getApplicationContext()
                        .getPackageName() + ".provider", fileToShare);
        intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileToShare.getName()));
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);





    }



   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menuPrintPdf) {
            File fileToPrint = getPdfFile();
            if (fileToPrint == null || !fileToPrint.exists()) {
                Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
            printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
            printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

            PDFUtil.printPdf(PdfViewerExampleActivity.this, fileToPrint, printAttributeBuilder.build());
        } else if (item.getItemId() == R.id.menuSharePdf) {
            File fileToShare = getPdfFile();
            if (fileToShare == null || !fileToShare.exists()) {
                Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", fileToShare);
            intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileToShare.getName()));
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    apkURI);

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
        return super.onOptionsItemSelected(item);
    }*/
}