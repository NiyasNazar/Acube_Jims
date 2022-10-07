package com.acube.jims.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.acube.jims.R;

public class Utils {
    public static void alert(Activity act, int titleInt, String message, int iconInt, DialogInterface.OnClickListener positiveListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setTitle(titleInt);
            builder.setMessage(message);
            builder.setIcon(iconInt);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            if (positiveListener != null) {
                builder.setPositiveButton("Ok", positiveListener);
            }
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alert(Activity act, String title, View view, int iconInt, DialogInterface.OnClickListener positiveListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setTitle(title);
            builder.setView(view);
            builder.setIcon(iconInt);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            if (positiveListener != null) {
                builder.setPositiveButton("Ok", positiveListener);
            }
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alert(Activity act, int titleInt, View view, int iconInt, DialogInterface.OnClickListener positiveListener) {
        alert(act, act.getString(titleInt), view, iconInt, positiveListener);
    }
}
