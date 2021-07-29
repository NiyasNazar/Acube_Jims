package com.acube.jims.Presentation.HomePage.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.HomeFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.FragmentTransition {

    private HomeViewModel mViewModel;
    HomeFragmentBinding binding;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.home_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mViewModel.init();
        mViewModel.getHomeMenu(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), AppConstants.HomeMenuAppName, LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserRole));
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<HomeData>>() {
            @Override
            public void onChanged(List<HomeData> homeData) {
                if (homeData != null)
                    binding.recyvhomemenu.setAdapter(new HomeAdapter(getActivity(), homeData, HomeFragment.this::replaceFragment));
            }
        });

        return binding.getRoot();

    }


    @Override
    public void replaceFragment() {
        FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
    }
}