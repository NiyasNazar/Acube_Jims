package com.acube.jims.utils;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {

    private long lastClickTime = 0;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        onSingleClick(v);
    }
}
