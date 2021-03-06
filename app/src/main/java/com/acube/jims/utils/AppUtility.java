package com.acube.jims.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtility {
    private static final int MY_PERMISSIONS_CONSTANT = 12356;
    private Activity activity;


    public AppUtility(Activity activity) {
        this.activity = activity;
    }

    //Internet checking
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkInternet() {
        if (isInternetConnected())
            return true;
        else {
            // customToast(activity.getResources().getString(R.string.no_internet));
            // customSnackBar("");
            return false;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isInternetConnected() {


        ConnectivityManager conMgr = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = conMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        /** to get info of mobile N/W : */
        final android.net.NetworkInfo mobile = conMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi.isAvailable() && wifi.isConnected())
                || (mobile.isAvailable() && mobile.isConnected())) {
            return true;
        } else if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;

    }


    //Mobile or Phone number validation
    public boolean isValidMobile(String phone) {
        return !phone.trim().equals("") && Pattern.matches("\\d+", phone) && !(phone.length() < 10 || phone.length() > 13);
    }
    public  boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    //email address validation
    public boolean isValidMail(String email) {
        if (email.trim().equals(""))
            return false;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        return m.matches();
    }


}

