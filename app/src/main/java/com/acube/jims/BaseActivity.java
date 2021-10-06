package com.acube.jims;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.acube.jims.Presentation.Login.ViewModel.LoginViewModel;
import com.acube.jims.Utils.AppUtility;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected ProgressDialog mProgressDialog;
    Dialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dialog = new Dialog(BaseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (new AppUtility(this).isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    protected void showProgressDialog() {
      //  dialog.show();
       if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(this, "", "Please wait...");
    }

    protected void hideProgressDialog() {
      //  dialog.dismiss();
       if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
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

}