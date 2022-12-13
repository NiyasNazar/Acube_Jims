package com.acube.jims;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoast.StyleableToast;

public abstract class BaseFragment extends Fragment {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    protected ProgressDialog mProgressDialog;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dialog = new Dialog(requireActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (new AppUtility(getActivity()).isTablet(getActivity())) {
          //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }
    public void showsuccess(String text) {

        new StyleableToast
                .Builder(requireActivity())
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#3CB371"))
                .show();
    }

    public void showerror(String title) {
        new StyleableToast
                .Builder(requireActivity())
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



    public void customSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();


    }
    public String getPrefix() {
        return LocalPreferences.retrieveStringPreferences(requireActivity(), "Prefix");
    }

    public String getSuffix() {
        return LocalPreferences.retrieveStringPreferences(requireActivity(), "Suffix");
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

}