package com.acube.jims.Presentation.Report.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.R;
import com.acube.jims.databinding.FoundFragmentBinding;

public class FoundFragment extends Fragment {

    private FoundViewModel mViewModel;

    public static FoundFragment newInstance() {
        return new FoundFragment();
    }

    FoundFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.found_fragment, container, false);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FoundViewModel.class);
        // TODO: Use the ViewModel
    }

}