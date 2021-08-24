package com.acube.jims.Presentation.Compare;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Compare.adapter.CompareItemsAdapter;
import com.acube.jims.Presentation.Compare.adapter.ViewPagerAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.CompareFragmentBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }

    CompareFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.compare_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        binding.viewPager.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
       binding.viewPager.setAdapter(new CompareItemsAdapter(getActivity()));
    //  binding.viewPager.setAdapter(new ViewPagerAdapter(getActivity()));

        int CurrenePosition = ((LinearLayoutManager)binding.viewPager.getLayoutManager()).findFirstVisibleItemPosition();

        binding.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrenePosition < binding.viewPager.getAdapter().getItemCount()-1)
                    binding.viewPager.scrollToPosition(CurrenePosition + 1);

            }
        });
        binding.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrenePosition > 0)
                    binding.viewPager.scrollToPosition(CurrenePosition - 1);
            }
        });
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