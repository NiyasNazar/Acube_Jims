package com.acube.jims.Presentation.Compare;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.acube.jims.Presentation.Compare.adapter.CompareItemsAdapter;
import com.acube.jims.Presentation.Compare.adapter.ViewPagerAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.SpanningGridLayoutManager;
import com.acube.jims.databinding.CompareFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }

    CompareFragmentBinding binding;
    String AuthToken;
    List<String> compareparams;
    String commaseparatedlist;
    LinearLayoutManager linearLayoutManager;
    CompareItemsAdapter compareItemsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.compare_fragment, container, false);

        binding.toolbar.tvFragname.setText("Compare");
        binding.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                binding.viewPager.getWidth();
                Log.d("onGlobalLayout", "onGlobalLayout: " + binding.viewPager.getWidth());//height is ready
            }
        });
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);

        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        mViewModel.init();
        compareparams = new ArrayList<>();
        compareparams = getList("compare");
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


        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serialNumber", commaseparatedlist);
        mViewModel.getcompareItems(AppConstants.Authorization + AuthToken, jsonObject);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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

        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCompare>>() {
            @Override
            public void onChanged(List<ResponseCompare> responseCompares) {
                if (responseCompares != null) {
                    compareItemsAdapter = new CompareItemsAdapter(getActivity(), responseCompares, binding.viewPager.getWidth());
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
        return binding.getRoot();
    }

    public List<String> getList(String name) {
        List<String> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getActivity(), name);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }


}