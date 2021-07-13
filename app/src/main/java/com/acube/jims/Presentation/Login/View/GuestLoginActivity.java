package com.acube.jims.Presentation.Login.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acube.jims.Presentation.Login.ViewModel.CheckCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityGuestLoginBinding;
import com.acube.jims.datalayer.models.Login.ResponseCheckCustomer;

public class GuestLoginActivity extends AppCompatActivity {
    ActivityGuestLoginBinding binding;
    private CheckCustomerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_login);
        viewModel = ViewModelProviders.of(this).get(CheckCustomerViewModel.class);

        ((EditText) findViewById(R.id.ed_mobile)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // When focus is lost check that the text field has valid values.

                if (!hasFocus) {
                    {
                        // Validate youredittext
                        Log.d("onFocusChange", "onFocusChange: ");
                        String vaphone = binding.edMobile.getText().toString();
                        viewModel.CheckUserExists(vaphone);

                    }
                }
            }


        });
        viewModel.getCustomerLiveData().observe(this, new Observer<ResponseCheckCustomer>() {
            @Override
            public void onChanged(ResponseCheckCustomer responseCheckCustomer) {
                if (responseCheckCustomer != null) {
                    binding.edEmail.setText(responseCheckCustomer.getEmailID());
                    binding.edGuestname.setText(responseCheckCustomer.getCustomerName());
                }

            }
        });
    }
}