package com.acube.jims;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.acube.jims.utils.AppUtility;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.Favorites.View.Favorites;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected ProgressDialog mProgressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#BF8F3A"));
        }
      /*  if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appmaincolor)); //status bar or the time bar at the top (see example image1)

             // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }*/
        dialog = new Dialog(BaseActivity.this, R.style.AnimDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.setContentView(R.layout.dialog_loading);



        if (new AppUtility(this).isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }
    public void showcart(){
        startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
    }
    public void showfavorites(){
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
        if (dialog != null&&!dialog.isShowing()) {
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
    public void openCart(){
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
}