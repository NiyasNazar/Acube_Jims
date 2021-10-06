package com.acube.jims.Presentation.Report.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FoundFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FoundFragment extends Fragment {

    private FoundViewModel mViewModel;
    List<Found> dataset;

    public static FoundFragment newInstance(List<Found> dataset) {

        return new FoundFragment(dataset);
    }

    FoundFragmentBinding binding;

    public FoundFragment(List<Found> dataset) {
        this.dataset = dataset;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.found_fragment, container, false);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.tvTotaldata.setText("Total Items Found : "+dataset.size());
        binding.recyvfound.setAdapter(new Foundadapter(getActivity(), dataset));


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FoundViewModel.class);
        // TODO: Use the ViewModel
    }


}