package com.acube.jims;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Report.View.reports.MisiingReport;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.Favorites.View.Favorites;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoast.StyleableToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Objects;

import am.leon.LeonImageView;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected ProgressDialog mProgressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

      if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appmaincolor)); //status bar or the time bar at the top (see example image1)

             // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
        dialog = new Dialog(BaseActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);

        if (new AppUtility(this).isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }

    public void showcart() {
        startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
    }

    public void showfavorites() {
        startActivity(new Intent(getApplicationContext(), Favorites.class));
    }

    public void showsuccess(String text) {

        new StyleableToast
                .Builder(getApplicationContext())
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#3CB371"))
                .show();
    }

    public void showerror(String title) {
        new StyleableToast
                .Builder(getApplicationContext())
                .text(title)
                .textColor(Color.WHITE)
                .iconStart(R.drawable.ic_error)
                .font(R.font.regular)
                .gravity(Gravity.BOTTOM)
                .length(Toast.LENGTH_LONG).solidBackground()
                .backgroundColor(Color.RED)
                .show();

    }

    protected void showProgressDialog() {
        //  dialog.show();
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }


    }

    protected void hideProgressDialog() {
        //  dialog.dismiss();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        hideProgressDialog();
    }

    public void customSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();


    }

    public void initToolBar(Toolbar toolbar, String title, Boolean back) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(back);
        getSupportActionBar().setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void openCart() {
        startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
    }

    public String ConvertToString(String hexString) {
        String result = "";
        /*   if (IsValid(hexString)) {*/
        result += padLeftZeros(hexToDecimal(hexString.substring(0, 2)), 2);
        result += padLeftZeros(hexToDecimal(hexString.substring(2, 4)), 3);
        result += padLeftZeros(hexToDecimal(hexString.substring(4, 10)), 7);
        result += padLeftZeros(hexToDecimal(hexString.substring(10, 16)), 6);
        result += padLeftZeros(hexToDecimal(hexString.substring(16, 22)), 7);
        result += padLeftZeros(hexToDecimal(hexString.substring(22, 24)), 2);
        // }
        return result;
    }

    public static String padLeftZeros(String str, int length) {
        return String.format("%1$" + length + "s", str).replace(' ', '0');
    }

    public String hexToDecimal(String hexnum) {
        int num = Integer.parseInt(hexnum, 16);
        String dec = String.valueOf(num);
        return dec;
    }

    public String HexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        System.out.println(output.toString().trim());
        return output.toString().trim();
    }

    public static boolean isHexNumber(String str) {
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            char cc = str.charAt(i);
            if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4'
                    || cc == '5' || cc == '6' || cc == '7' || cc == '8'
                    || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C'
                    || cc == 'D' || cc == 'E' || cc == 'F' || cc == 'a'
                    || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd'
                    || cc == 'e' || cc == 'f') {
                flag = true;
            }
        }
        return flag;
    }

    public void showDialog(String imageResId, Context activity) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;
        int windowHeight = size.y;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        Dialog alertDialog = new Dialog(BaseActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.custom_zoom_dialog);
//        Activity activity = unwrap(alertDialog.getContext());


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.5f);
        int dialogWindowHeight = (int) (displayHeight * 0.5f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
        ImageViewZoom expandedImageView = alertDialog.findViewById(R.id.myZoomageView);

        ImageView close = alertDialog.findViewById(R.id.imvclose);
        close.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                alertDialog.dismiss();
            }
        });


        //   expandedImageView.loadImage(imageResId);
        Glide.with(activity)
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


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void DeleteTemp() {
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().deleteTemp();

        }).start();
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private static Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }

    public String getPrefix() {
        return LocalPreferences.retrieveStringPreferences(getApplicationContext(), "Prefix");
    }

    public String getSuffix() {
        return LocalPreferences.retrieveStringPreferences(getApplicationContext(), "Suffix");
    }


}