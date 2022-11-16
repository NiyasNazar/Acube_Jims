package com.acube.jims.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityReadingRangeSettingsBinding;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.android.material.slider.Slider;

public class ReadingRangeSettings extends BaseActivity {
    ActivityReadingRangeSettingsBinding binding;
    float handheldvalue, blevalue, trayrange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reading_range_settings);
        initToolBar(binding.toolbar.toolbar, "Range Settings", true);
        handheldvalue = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "handheldvalue");
        blevalue = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "handheldble");

        trayrange = LocalPreferences.retrieveReadFloatPreferences(getApplicationContext(), "trayrange");

        binding.handheldSlider.setValue(handheldvalue);
        binding.handheldble.setValue(blevalue);
        binding.trayrange.setValue(trayrange);
        binding.btnsave.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
        binding.handheldSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                LocalPreferences.storeReadfloatPreference(getApplicationContext(), "handheldvalue", slider.getValue());


            }
        });

        binding.handheldble.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                LocalPreferences.storeReadfloatPreference(getApplicationContext(), "handheldble", slider.getValue());


            }
        });

        binding.trayrange.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                LocalPreferences.storeReadfloatPreference(getApplicationContext(), "trayrange", slider.getValue());


            }
        });


    }
}