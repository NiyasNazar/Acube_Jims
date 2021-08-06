package com.acube.jims.Presentation.ProductDetails.View;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ProductDetailsFragment extends Fragment {

    private ItemDetailsViewModel mViewModel;
    String Id;

    public static ProductDetailsFragment newInstance(String Id) {
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

        Bundle args = new Bundle();
        args.putString("Id", Id);
        productDetailsFragment.setArguments(args);
        return productDetailsFragment;
    }

    ProductDetailsFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.product_details_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);
        mViewModel.init();
        if (getArguments() != null) {
            String Id = getArguments().getString("Id");
            mViewModel.FetchItemDetails(Id);
        }


        binding.toolbar.tvFragname.setText("Catalogue");
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
            }
        });

      /* OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Toast.makeText(getActivity(), "OnBackPressed", Toast.LENGTH_LONG).show();
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.handleOnBackPressed();


            }
        });*/
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseCatalogDetails>() {
            @Override
            public void onChanged(ResponseCatalogDetails responseCatalogDetails) {
                if (responseCatalogDetails != null) {
                    if (responseCatalogDetails.getItemSubList().size() > 0) {
                        Glide.with(getActivity())
                                .load(responseCatalogDetails.getItemSubList().get(0).getImageFilePath())
                                //  .placeholder(R.drawable.placeholder)
                                //  .error(R.drawable.imagenotfound)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // log exception
                                        Log.e("TAG", "Error loading image", e);
                                        return false; // important to return false so the error placeholder can be placed
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(binding.imvsingleitemimage);
                    }


                    binding.tvMrp.setText(  responseCatalogDetails.getMrp()+" SAR ");
                    binding.tvItemName.setText(responseCatalogDetails.getItemName());
                    binding.tvbrandname.setText(responseCatalogDetails.getItemBrandName());
                    binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
                    binding.tvItemcode.setText(responseCatalogDetails.getItemCode());
                    binding.tvGender.setText(responseCatalogDetails.getGender());
                    binding.tvmakingchargemin.setText("" + responseCatalogDetails.getMakingChargeMin());
                    binding.tvMakingchrgmax.setText("" + responseCatalogDetails.getMakingChargeMax());
                    binding.tvKaratname.setText(responseCatalogDetails.getKaratName());
                    binding.tvColor.setText(responseCatalogDetails.getColorName());
                    binding.tvgroupname.setText(responseCatalogDetails.getItemGroupName());
                    binding.tvgrossweight.setText("" + responseCatalogDetails.getGrossWeight() + " g");
                    binding.tvcategory.setText(responseCatalogDetails.getCategoryName());
                    binding.tvSubcategory.setText(responseCatalogDetails.getSubCategoryName());


                    if (responseCatalogDetails.getStoneWeight() != null) {
                        binding.tvItemStoneweight.setText("Stone Weight: " + responseCatalogDetails.getStoneWeight() + " g");
                    } else {
                        binding.tvItemStoneweight.setText("Stone Weight: N/A");
                    }
                    if (responseCatalogDetails.getGrossWeight() != null) {
                        binding.tvItemweight.setText(responseCatalogDetails.getKaratName() + "- Weight: " + responseCatalogDetails.getGrossWeight() + " g");
                    } else {
                        binding.tvItemweight.setText("Stone Weight: N/A");
                    }


                }
            }
        });

        return binding.getRoot();

    }


}