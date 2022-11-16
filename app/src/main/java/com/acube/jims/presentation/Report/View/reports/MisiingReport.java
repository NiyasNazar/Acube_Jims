package com.acube.jims.presentation.Report.View.reports;

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
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.presentation.Report.adapter.Missingadapter;
import com.acube.jims.R;
import com.acube.jims.presentation.reading.FindMissingReadingActivity;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jsibbold.zoomage.ZoomageView;
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

import am.leon.LeonImageView;

public class MisiingReport extends BaseActivity implements Missingadapter.PassId {
    ActivityReportViewbycatBinding binding;
    private ReportViewModel mViewModel;
    Missingadapter reportadapter;
    String companyID, warehouseID, AuditID, Employeename;
    AuditUploadViewModel auditUploadViewModel;
    List<String> compareitemlist;
    String commaseparatedlist;
    int systemLocationID, storeID, categoryId, itemID;
    String remark = "";
    private Animator currentAnimator;
    private int shortAnimationDuration;
    String username,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_viewbycat);
        if (new AppUtility(MisiingReport.this).isTablet(MisiingReport.this)) {
            binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        } else {
            binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        AuditID = getIntent().getStringExtra("auditID");
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        username = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
        initToolBar(binding.toolbarApp.toolbar, "Missing", true);
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        Employeename = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "EmployeeName");
        Log.d("AuditID", "onCreate: " + AuditID);
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", systemLocationID);
        binding.btnfound.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showAlert();
            }
        });

        binding.cdvlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindMissingReadingActivity.class)
                        .putExtra("systemLocationID", systemLocationID)
                        .putExtra("storeID", storeID)
                        .putExtra("categoryId", categoryId)
                        .putExtra("itemID", itemID)
                        .putExtra("url", AppConstants.BASE_URL)
                        .putExtra("auditID", AuditID));

            }
        });
        binding.checkBoxselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    reportadapter.selectAll();
                    binding.checkBoxselectall.setText("Unselect All");


                } else {
                    reportadapter.unselectall();
                    binding.checkBoxselectall.setText("Select All");
                }

            }
        });

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getMissing(AuditID, 0, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<List<SelectionHolder>>() {
            @Override
            public void onChanged(List<SelectionHolder> auditReportItems) {
                hideProgressDialog();
                if (auditReportItems != null) {
                    binding.tvTotaldata.setText("Total Items : " + auditReportItems.size());
                    reportadapter = new Missingadapter(getApplicationContext(), auditReportItems, MisiingReport.this);
                    binding.recyvfound.setAdapter(reportadapter);
                    if (reportadapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNotfound.setVisibility(View.GONE);
                    }


                }
            }
        });


    }

    private void showAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MisiingReport.this);
        builder.setTitle("Mark as found");
        builder.setMessage("Do you want to move the selectected items to found?");

// Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter Remarks");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remark = input.getText().toString();
                updateRemark(remark);
            }

            private void updateRemark(String remark) {

                new Thread(() -> {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().markasFound(AuditID, systemLocationID, categoryId, itemID, storeID, remark,date,username);

                }).start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    @Override
    public void passid(String id, Integer locid) {

    }

    @Override
    public void enlargeImage(View imageView, String imageUrl) {

        showDialog(imageUrl, MisiingReport.this);
    }

    @Override
    public void compareitems(String serial, boolean ischecked) {
        SelectionHolder selectionHolder = new SelectionHolder();
        selectionHolder.setSerialNumber(serial);
        new Thread(() -> {
            if (ischecked) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertMissingselection(selectionHolder);

            } else {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().DeleteTemporarySerialSelectionHolder(selectionHolder);

            }


        }).start();

    }


    private void PerformScan() {
        String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "TrayMacAddress");

        StringBuilder str = new StringBuilder("");
        for (String eachstring : compareitemlist) {
            str.append(eachstring).append(",");
        }
        commaseparatedlist = str.toString();
        if (commaseparatedlist.length() > 0)
            commaseparatedlist
                    = commaseparatedlist.substring(
                    0, commaseparatedlist.length() - 1);
        Log.d(TAG, "PerformScan: " + commaseparatedlist);


        try {
            Intent res = new Intent();
            String mPackage = "com.acube.smarttray";// package name
            String mClass = ".SmartTrayReading";//the activity name which return results*/
            //   String mPackage = "com.example.acubetest";// package name
            //    String mClass = ".Audit";//the activity name which return results
            res.putExtra("type", "MISSING");
            res.putExtra("url", AppConstants.BASE_URL);
            res.putExtra("macAddress", TrayMacAddress);
            res.putExtra("jsonSerialNo", "json");
            res.setComponent(new ComponentName(mPackage, mPackage + mClass));
            someActivityResultLauncher.launch(res);
        } catch (Exception e) {
            Log.d("TAG", "replaceFragment: ");
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        new Thread(() -> {
                            Intent data = result.getData();
                            if (data != null) {
                                //    String filepath = data.getStringExtra("jsonSerialNo");
                                //  Log.d("ResponseFile", "onActivityResult: " + filepath);

                                String filepath = "/storage/emulated/0/Download/AcubeJimsLocate.json";
                                File file = new File(filepath);
                                if (file.exists()) {
                                    FileReader fileReader = null;
                                    try {
                                        fileReader = new FileReader(filepath);
                                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        String line = bufferedReader.readLine();
                                        while (line != null) {
                                            stringBuilder.append(line).append("\n");
                                            line = bufferedReader.readLine();
                                        }
                                        bufferedReader.close();
                                        String response = stringBuilder.toString();
                                        //    AppDatabase.getInstance(getApplicationContext()).auditDownloadDao().inserdummy(tempSerials);
                                        JSONArray obj = new JSONArray(response);
                                        ArrayList<TemDataSerial> dataset = new Gson().fromJson(obj.toString(), new TypeToken<List<TemDataSerial>>() {
                                        }.getType());
                                        CheckSerialnumExist(dataset);
                                        Log.d("TAG", "onActivityResult: " + dataset.size());

                                    } catch (FileNotFoundException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());

                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        Log.d("TAG", "filenotexist: " + e.getMessage());


                                        e.printStackTrace();
                                    }

                                } else {


                                }
                                Log.d("aspdata", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));


                            }


                            runOnUiThread(() -> {
                                hideProgressDialog();


                            });
                        }).start();
                        // Here, no request code


                    }
                }
            });

    private void CheckSerialnumExist(ArrayList<TemDataSerial> dataset) {


        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().insertTemporarySerialNos(dataset);
            runOnUiThread(this::checkexists);
        }).start();

    }

    private void checkexists() {
        int systemID = 0;
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().checkauditscan(AuditID, systemLocationID);


            runOnUiThread(this::hideProgressDialog);
        }).start();
    }

    private void zoomImageFromThumb(final View thumbView, String imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        Glide.with(MisiingReport.this)
                .load(imageResId)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(expandedImageView);


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                                .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    public void showDialog(String imageResId) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;
        int windowHeight = size.y;
        Dialog alertDialog = new Dialog(MisiingReport.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.custom_zoom_dialog);

        TouchImageView expandedImageView = alertDialog.findViewById(R.id.myZoomageView);
        ImageView close = alertDialog.findViewById(R.id.imvclose);
        close.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                alertDialog.dismiss();
            }
        });


        //   expandedImageView.loadImage(imageResId);
        Glide.with(MisiingReport.this)
                .load(R.drawable.dummy_item_image)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(expandedImageView);


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}