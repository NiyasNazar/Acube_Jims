package com.acube.jims.presentation.Report.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.presentation.Report.adapter.LocationMismatchAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.FoundFragmentBinding;
import com.acube.jims.datalayer.models.Audit.LocationMismatch;

import java.util.List;

public class LocationMismatchFragment extends Fragment {

    private FoundViewModel mViewModel;
    List<LocationMismatch> datasetlocationmismatch;

    public static LocationMismatchFragment newInstance(List<LocationMismatch> datasetlocationmismatch) {
        return new LocationMismatchFragment(datasetlocationmismatch);
    }

    FoundFragmentBinding binding;

    LocationMismatchFragment(List<LocationMismatch> datasetlocationmismatch) {
        this.datasetlocationmismatch = datasetlocationmismatch;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.found_fragment, container, false);
        binding.recyvfound.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recyvfound.setAdapter(new LocationMismatchAdapter(getActivity(), datasetlocationmismatch));

        binding.tvTotaldata.setText("Total Items Location Mismatch : " + datasetlocationmismatch.size());

        return binding.getRoot();
    }


}