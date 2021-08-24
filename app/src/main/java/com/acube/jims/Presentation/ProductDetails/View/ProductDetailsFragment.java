package com.acube.jims.Presentation.ProductDetails.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.Presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.ViewModel.ItemDetailsViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ProductDetailsFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;

public class ProductDetailsFragment extends BaseFragment {

    private ItemDetailsViewModel mViewModel;
    String Id;
    BackHandler backHandler;
    AddtoCartViewModel addtoCartViewModel;
    String AuthToken;
    String mSerialno;
    String CartId;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    String GuestCustomerID, UserId;

    public static ProductDetailsFragment newInstance(String Id) {
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

        Bundle args = new Bundle();
        args.putString("Id", Id);
        productDetailsFragment.setArguments(args);
        return productDetailsFragment;
    }

    ProductDetailsFragmentBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            backHandler = (BackHandler) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.product_details_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        addtoFavoritesViewModel.init();
        mViewModel.init();
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        CartId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.CartID);
        GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);

        binding.favButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, GuestCustomerID, UserId, Id, "add", "",mSerialno);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });


        if (getArguments() != null) {
            String Id = getArguments().getString("Id");
            mViewModel.FetchItemDetails(AppConstants.Authorization + AuthToken, Id);
        }

        addtoFavoritesViewModel.getLiveData().observe(getActivity(), new Observer<JsonObject>() {

            @Override
            public void onChanged(JsonObject jsonObject) {
                Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                if (jsonObject != null) {

                }
            }
        });


        binding.toolbar.tvFragname.setText("View Details-Single item");
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
            }
        });
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHandler.backpress();
            }
        });
        binding.btnAddTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();


                addtoCartViewModel.AddtoCart(CartId, AppConstants.Authorization + AuthToken, GuestCustomerID, UserId, Id, "add", "0", mSerialno);

            }
        });
        addtoCartViewModel.getLiveData().observe(getActivity(), new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                }
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

                    mSerialno = responseCatalogDetails.getSerialNumber();
                    Id = String.valueOf(responseCatalogDetails.getId());
                    binding.tvMrp.setText(responseCatalogDetails.getMrp() + " SAR ");
                    binding.tvItemName.setText(responseCatalogDetails.getItemName());
                    binding.tvbrandname.setText(responseCatalogDetails.getItemBrandName());
                    binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
                    binding.tvItemcode.setText(responseCatalogDetails.getSerialNumber());
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


    public interface BackHandler {
        void backpress();
    }


}