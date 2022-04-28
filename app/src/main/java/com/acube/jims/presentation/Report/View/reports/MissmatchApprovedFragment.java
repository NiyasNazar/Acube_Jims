package com.acube.jims.presentation.Report.View.reports;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.presentation.Report.adapter.LocationApprovedAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.FragmentMissmatchApprovedBinding;
import com.acube.jims.datalayer.models.Audit.LocationMismatchApproved;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissmatchApprovedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissmatchApprovedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentMissmatchApprovedBinding binding;

    public MissmatchApprovedFragment() {
        // Required empty public constructor
    }

    List<LocationMismatchApproved> locationMismatchApprovedList;

    public static MissmatchApprovedFragment newInstance(List<LocationMismatchApproved> locationMismatchApprovedList) {


        return new MissmatchApprovedFragment(locationMismatchApprovedList);
    }

    public MissmatchApprovedFragment(List<LocationMismatchApproved> datasetlocationmismatch) {
        this.locationMismatchApprovedList = datasetlocationmismatch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(

                inflater, R.layout.fragment_missmatch_approved, container, false);

        binding.recyvapproved.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recyvapproved.setAdapter(new LocationApprovedAdapter(getActivity(),locationMismatchApprovedList));
        binding.tvTotaldata.setText("Total Mismatch Approved Items : " + locationMismatchApprovedList.size());
        return binding.getRoot();
    }
}