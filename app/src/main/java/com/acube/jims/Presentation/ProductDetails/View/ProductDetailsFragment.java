package com.acube.jims.Presentation.ProductDetails.View;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.ViewModel.ItemDetailsViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.databinding.ProductDetailsFragmentBinding;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;

public class ProductDetailsFragment extends Fragment {

    private ItemDetailsViewModel mViewModel;

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    ProductDetailsFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.product_details_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);
        mViewModel.init();
        mViewModel.FetchItemDetails("");
        binding.toolbar.tvFragname.setText("Catalogue");
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
            }
        });
       /* binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
                    @Override
                    public void handleOnBackPressed() {
                        // Handle the back button event
                        Toast.makeText(getActivity(),"OnBackPressed",Toast.LENGTH_LONG).show();
                        FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());

                    }
                };
                requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),callback);
            }
        });*/
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseCatalogDetails>() {
            @Override
            public void onChanged(ResponseCatalogDetails responseCatalogDetails) {
                if (responseCatalogDetails != null) {

                }
            }
        });

        return binding.getRoot();

    }


}