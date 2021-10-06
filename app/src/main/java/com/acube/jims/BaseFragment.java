package com.acube.jims;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.acube.jims.Utils.AppUtility;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    protected ProgressDialog mProgressDialog;
    Dialog dialog ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (new AppUtility(getActivity()).isTablet(getActivity())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }

    protected void showProgressDialog() {
       // dialog.show();
   if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait...");
    }

    protected void hideProgressDialog() {

      //  dialog.dismiss();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    public void customSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();


    }


}