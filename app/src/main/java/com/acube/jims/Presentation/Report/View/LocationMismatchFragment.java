package com.acube.jims.Presentation.Report.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.Presentation.Report.adapter.LocationMismatchAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FoundFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
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
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyvfound.setAdapter(new LocationMismatchAdapter(getActivity(), datasetlocationmismatch));


        return binding.getRoot();
    }


}