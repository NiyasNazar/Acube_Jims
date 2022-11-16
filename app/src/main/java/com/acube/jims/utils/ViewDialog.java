package com.acube.jims.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.acube.jims.R;

public class ViewDialog {
    Dialog dialog;

    public void showDialog(Activity activity, String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_bluetooth);
        TextView text = (TextView) dialog.findViewById(R.id.macaddress);
        Button close = (Button) dialog.findViewById(R.id.btn_dialog);
        text.setText(msg);
        close.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dismiss();
            }
        });


        dialog.show();

    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }


    }
}