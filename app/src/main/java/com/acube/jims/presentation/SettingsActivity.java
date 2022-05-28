package com.acube.jims.presentation;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivitySettingsBinding;
import com.acube.jims.presentation.Login.View.LoginActivity;

public class SettingsActivity extends BaseActivity {
    ActivitySettingsBinding binding;
    boolean languageUpdated;
    String locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        binding.edUrl.setText(LocalPreferences.getBaseUrl(getApplicationContext()));
  binding.edUrl.setText("http://jmsapi.acubeinfotech.ae/jwl/");
        languageUpdated = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "langUpdated");
        if (!languageUpdated) {
            binding.rdenglish.setChecked(true);
        } else {
            locale = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "locale");
            Log.d("locale", "onCreate: " + locale);
            if (locale.equalsIgnoreCase("en")) {
                binding.rdenglish.setChecked(true);
                binding.rdarabic.setChecked(false);

            } else {
                binding.rdenglish.setChecked(false);
                binding.rdarabic.setChecked(true);
            }

        }
        binding.radiogrplanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {

                    case R.id.rdenglish:
                        LocalPreferences.storeStringPreference(getApplicationContext(), "locale", "en");
                        LocalPreferences.storeBooleanPreference(getApplicationContext(), "langUpdated", true);

                        break;
                    case R.id.rdarabic:
                        LocalPreferences.storeStringPreference(getApplicationContext(), "locale", "ar");
                        LocalPreferences.storeBooleanPreference(getApplicationContext(), "langUpdated", true);

                        break;


                }
            }


        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edUrl.setError(null);
                String vaBaseUrl = binding.edUrl.getText().toString();
                if (vaBaseUrl.equalsIgnoreCase("")) {
                    customSnackBar(binding.parent, "Required Field");

                } else {
                    LocalPreferences.setbaseUrl(getApplicationContext(), vaBaseUrl);
                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "urlupdated", true);
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }


            }
        });

    }
}