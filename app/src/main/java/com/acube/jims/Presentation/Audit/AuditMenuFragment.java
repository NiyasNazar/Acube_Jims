package com.acube.jims.Presentation.Audit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.acube.jims.Presentation.Report.ReportFragment;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.databinding.ReportMenuBinding;

public class AuditMenuFragment extends Fragment {

    private ReportViewModel mViewModel;

    public static AuditMenuFragment newInstance() {
        return new AuditMenuFragment();
    }

    ReportMenuBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.report_menu, container, false);

        binding.layoutreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new ReportFragment());
            }
        });
        binding.layoutaudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new AuditFragment());
            }
        });
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


        return binding.getRoot();

    }



}