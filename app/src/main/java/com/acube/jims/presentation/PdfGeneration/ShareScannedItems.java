package com.acube.jims.presentation.PdfGeneration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.Compare.CompareViewModel;
import com.acube.jims.R;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityShareItemsScreenBinding;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegaintentbuilder.downloader.DownloadCallback;
import com.omega_r.libs.omegaintentbuilder.handlers.ContextIntentHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShareScannedItems extends BaseActivity {
    CompareViewModel mViewModel;
    ActivityShareItemsScreenBinding binding;
    String commaseparatedlist;
    List<String> imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_items_screen);
        showProgressDialog();
        initToolBar(binding.toolbarApp.toolbar, "Share", true);
        int customerId = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        binding.recycartitems.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        imgurl = new ArrayList<>();
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        mViewModel.init();

        DatabaseClient.getInstance(ShareScannedItems.this).getAppDatabase().scannedItemsDao().getFromSmarttool().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d("comapreItems", "onChanged: " + strings.size());
                String[] strArray = strings.toArray(new String[strings.size()]);

                JSONArray jsonArray = new JSONArray(Arrays.asList(strArray));

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serialNo", jsonArray);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
                    mViewModel.getcompareItems(LocalPreferences.getToken(getApplicationContext()), gsonObject,getApplicationContext());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mViewModel.getLiveData().observe(this, new Observer<List<ResponseCompare>>() {
            @Override
            public void onChanged(List<ResponseCompare> responseCompares) {
                hideProgressDialog();
                if (responseCompares != null) {
                    for (int i = 0; i < responseCompares.size(); i++) {
                        imgurl.add(responseCompares.get(i).getImagePath());
                    }

                    binding.recycartitems.setAdapter(new ScannedAdapterForSharing(getApplicationContext(), responseCompares));
                }
            }
        });


    }

    public void share(View view) {

   /*     OmegaIntentBuilder.from(ShareScannedItems.this)
                .share()


                .text("ASDDAD")


                .filesUrls(imgurl)
                .download(new DownloadCallback() {
                    @Override
                    public void onDownloaded(boolean success, @NotNull ContextIntentHandler contextIntentHandler) {
                        contextIntentHandler.startActivity();
                    }
                });*/

      PdfGenerator.getBuilder()
                .setContext(ShareScannedItems.this)
                .fromViewSource()
                .fromView(binding.recycartitems)
                .setFileName("Test-PDF")
                .setFolderName("Test-PDF-folder")
                .openPDFafterGeneration(true)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        showProgressDialog();

                    }

                    @Override
                    public void onFinishPDFGeneration() {

                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        hideProgressDialog();
                    }
                });
    }

    public List<String> getList(String name) {
        List<String> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getApplicationContext(), name);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

}