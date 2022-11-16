package com.acube.jims.presentation.Compare;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Compare.adapter.CompareItemsAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.CompareFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareFragment extends BaseActivity {

    private CompareViewModel mViewModel;


    CompareFragmentBinding binding;
    String AuthToken;
    List<String> compareparams;
    String commaseparatedlist;
    LinearLayoutManager linearLayoutManager;
    CompareItemsAdapter compareItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.compare_fragment);
        initToolBar(binding.toolbarApp.toolbar, "Compare", true);


        binding.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.viewPager.getWidth();
                Log.d("onGlobalLayout", "onGlobalLayout: " + binding.viewPager.getWidth());//height is ready
            }
        });
        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        mViewModel.init();


        DatabaseClient.getInstance(CompareFragment.this).getAppDatabase().scannedItemsDao().getFromSmarttool().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d("comapreItems", "onChanged: " + strings.size());
              /*  compareparams = new ArrayList<>();
                compareparams = strings;
                if (compareparams != null) {
                    StringBuilder str = new StringBuilder("");
                    for (String eachstring : compareparams) {
                        str.append(eachstring).append(",");
                    }
                    commaseparatedlist = str.toString();
                    if (commaseparatedlist.length() > 0)
                        commaseparatedlist
                                = commaseparatedlist.substring(
                                0, commaseparatedlist.length() - 1);


                }*/

                String[] strArray = strings.toArray(new String[strings.size()]);
                JSONArray jsonArray = new JSONArray(Arrays.asList(strArray));

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serialNo", jsonArray);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());
                    mViewModel.getcompareItems(AppConstants.Authorization + AuthToken, gsonObject,getApplicationContext());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.viewPager.setHasFixedSize(true);
        binding.viewPager.setLayoutManager(linearLayoutManager);
        //  DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        //  binding.viewPager.addItemDecoration(itemDecor);
        //   binding.viewPager.setLayoutManager(new SpanningGridLayoutManager(getActivity(), 2));
        binding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisible = linearLayoutManager.findFirstVisibleItemPosition() - 1;
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition() + 1;

                if (lastVisible <= compareItemsAdapter.getItemCount()) {
                    linearLayoutManager.scrollToPosition(lastVisible);
                }
            }
        });
        binding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisible = linearLayoutManager.findFirstVisibleItemPosition() - 1;
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition() + 1;

                if (lastVisible <= compareItemsAdapter.getItemCount()) {
                    linearLayoutManager.scrollToPosition(firstVisible);
                }
            }
        });

        mViewModel.getLiveData().observe(this, new Observer<List<ResponseCompare>>() {
            @Override
            public void onChanged(List<ResponseCompare> responseCompares) {
                if (responseCompares != null) {
                    compareItemsAdapter = new CompareItemsAdapter(getApplicationContext(), responseCompares, binding.viewPager.getWidth());
                    binding.viewPager.setAdapter(compareItemsAdapter);
                }

            }
        });

        //  binding.viewPager.setAdapter(new ViewPagerAdapter(getActivity()));

//        int CurrenePosition = ((LinearLayoutManager) binding.viewPager.getLayoutManager()).findFirstVisibleItemPosition();

   /*     binding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrenePosition < binding.viewPager.getAdapter().getItemCount() - 1)
                    binding.viewPager.scrollToPosition(CurrenePosition + 1);

            }
        });
        binding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrenePosition > 0)
                    binding.viewPager.scrollToPosition(CurrenePosition - 1);
            }
        });*/
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