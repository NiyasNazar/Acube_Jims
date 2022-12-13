package com.acube.jims.presentation.Report.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityItemWiseReportBinding;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.models.report.ResponseItemWiseReport;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Report.MissingComparison;
import com.acube.jims.presentation.Report.View.reports.MisiingReport;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.presentation.Report.adapter.ItemWiseadapter;
import com.acube.jims.presentation.Report.adapter.Missingadapter;
import com.acube.jims.presentation.reading.FindMissingReadingActivity;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ortiz.touchview.TouchImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemWiseReport extends BaseActivity implements ItemWiseadapter.PassId {
    ActivityItemWiseReportBinding binding;
    private ReportViewModel mViewModel;
    ItemWiseadapter reportadapter;
    String companyID, warehouseID, AuditID, Employeename;
    AuditUploadViewModel auditUploadViewModel;
    List<String> compareitemlist;
    String commaseparatedlist;
    int systemLocationID, storeID, categoryId, itemID,subcatcategoryId;
    String remark = "", status;
    private Animator currentAnimator;
    private int shortAnimationDuration;
    String username, date;
    List<com.acube.jims.datalayer.models.report.ItemWiseReport> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_wise_report);
        binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));


        AuditID = getIntent().getStringExtra("auditId");
        status = getIntent().getStringExtra("status");
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        subcatcategoryId=getIntent().getIntExtra("subCategoryId",0);

        itemID = getIntent().getIntExtra("itemID", 0);
        username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
        dataset = new ArrayList<>();
        initToolBar(binding.toolbarApp.toolbar, status, true);
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        if (status.equalsIgnoreCase("missing")) {
            binding.toolbarApp.chkselectall.setVisibility(View.VISIBLE);
            binding.fabcomparison.setVisibility(View.VISIBLE);

        } else {
            binding.toolbarApp.chkselectall.setVisibility(View.GONE);
            binding.fabcomparison.setVisibility(View.GONE);

        }
        new Thread(() -> {
            DatabaseClient.getInstance(ItemWiseReport.this).getAppDatabase().auditDownloadDao().deleteItemWiseReport();


        }).start();
        binding.fabcomparison.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                startActivity(new Intent(getApplicationContext(), MissingComparison.class)
                        .putExtra("status", "")
                        .putExtra("categoryId", categoryId)
                        .putExtra("storeID", storeID)
                        .putExtra("systemLocationID", systemLocationID));
            }
        });

        binding.toolbarApp.chkselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < dataset.size(); i++)
                        reportadapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    binding.toolbarApp.chkselectall.setText("Unselect All");

                  update();

                } else {
                    isChecked = false;
                  removeall();

                    //If button text is Deselect All remove check from all items
                    reportadapter.removeSelection();

                    //After checking all items change button text
                    binding.toolbarApp.chkselectall.setText("Select All");
                }



            /*    if (isChecked) {

                    reportadapter.selectAll();
                    binding.toolbarApp.chkselectall.setText("Unselect All");


                } else {
                    reportadapter.unselectall();
                    binding.toolbarApp.chkselectall.setText("Select All");
                }*/

            }
        });


        companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        Log.d("AuditID", "onCreate: " + AuditID);
        showProgressDialog();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("warehouseId", storeID);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryId", subcatcategoryId);
        jsonObject.addProperty("locationId", systemLocationID);
        RetrofitInstance.getApiService(getApplicationContext()).GetItemWiseReport(LocalPreferences.getToken(getApplicationContext()), jsonObject).enqueue(new Callback<ResponseItemWiseReport>() {
            @Override
            public void onResponse(Call<ResponseItemWiseReport> call, Response<ResponseItemWiseReport> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    binding.tvNotfound.setVisibility(View.GONE);

                    hideProgressDialog();
                    ResponseItemWiseReport responseItemWiseReport = response.body();
                    dataset = responseItemWiseReport.getResult();
                    new Thread(() -> {
                        DatabaseClient.getInstance(ItemWiseReport.this).getAppDatabase().auditDownloadDao().insertItemWiseReport(dataset);



                    }).start();
                    binding.qty.setText("" + responseItemWiseReport.getTotalQty());
                    binding.weight.setText("" + responseItemWiseReport.getTotalWeight() + " g");

                    reportadapter = new ItemWiseadapter(getApplicationContext(), dataset, true, ItemWiseReport.this, status);
                    binding.recyvfound.setAdapter(reportadapter);
                } else {
                    binding.tvNotfound.setVisibility(View.VISIBLE);
                }
                removeall();
            }

            @Override
            public void onFailure(Call<ResponseItemWiseReport> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


    }


    @Override
    public void passid(String id, Integer locid) {

    }

    @Override
    public void enlargeImage(View imageView, String imageUrl) {

        showDialog(imageUrl, ItemWiseReport.this);
    }

    @Override
    public void compareitems(String serial, boolean ischecked) {


        new Thread(() -> {
            if (ischecked) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateselection(1, serial);

            } else {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateselection(0, serial);

            }


        }).start();
    }
    public void update() {


        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateselection(1);


        }).start();
    }

    public void removeall() {

        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().updateselection(0);


        }).start();

    }

}