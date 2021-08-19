package com.acube.jims.Presentation.Catalogue.View;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.acube.jims.Presentation.Login.View.GuestLoginActivity;
import com.acube.jims.R;

public class CatalogueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
    }

    public void launch(View view) {

        ComponentName componetName = new ComponentName(
                "com.example.acubetest",  //This is the package name of another application
                "com.example.acubetest.MainActivity");   //This parameter is the full pathname of the Activity to start

        try {
            Intent res = new Intent();
            String mPackage = "com.example.acubetest";
            String mClass = ".MainActivity";
            res.setComponent(new ComponentName(mPackage,mPackage+mClass));



            someActivityResultLauncher.launch(res);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "You can prompt the user here that the application is not found, or do other operations!", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        Log.d("onActivityResult", "onActivityResult: "+data.getStringExtra("result"));
                        //doSomeOperations();
                    }
                }
            });
}