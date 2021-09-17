package com.acube.jims.Presentation.Report;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Audit.AuditFragment;
import com.acube.jims.Presentation.Favorites.View.FavoritesFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.Report.View.FoundFragment;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.databinding.ReportFragmentBinding;

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    ReportFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.report_fragment, container, false);

        binding.toolbar.tvFragname.setText("Report");
        binding.tabfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new FoundFragment());

            }
        });

        binding.tabmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new FavoritesFragment());
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replace(new FoundFragment());

            }
        });

        return binding.getRoot();


    }
public void  replace(Fragment fragment){
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment).commit();
}

}