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
import java.util.List;

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
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyvfound.setAdapter(new Foundadapter(getActivity(), getList()));


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FoundViewModel.class);
        // TODO: Use the ViewModel
    }

    public List<Found> getList() {
        List<Found> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getActivity(), "datsetfound");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Found>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }
}