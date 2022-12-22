package com.acube.jims.presentation.reading;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseFragment;
import com.acube.jims.R;
import com.acube.jims.databinding.FragmentBlankBinding;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.presentation.ImageGallery.ImageZoomerActivity;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;


public class SingleReadingView extends BaseFragment {

    ResponseCatalogDetails responseCatalogDetails;
    String value;

    public SingleReadingView() {
        // Required empty public constructor
    }

    public static SingleReadingView newInstance(String describable) {
        SingleReadingView fragment = new SingleReadingView();
        Bundle bundle = new Bundle();
        bundle.putString("DESCRIBABLE_KEY", describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    FragmentBlankBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_blank, container, false);

        if (getArguments() != null) {

            value = getArguments().getString("DESCRIBABLE_KEY");
            Gson gson = new Gson();
            responseCatalogDetails = gson.fromJson(value,
                    ResponseCatalogDetails.class);

            binding.tvitemname.setText(responseCatalogDetails.getItemName());
            binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
            binding.tvItemcode.setText(responseCatalogDetails.getSerialNumber());
            binding.tvGender.setText(responseCatalogDetails.getGender());
            binding.tvmakingchargemin.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMin(), ""));
            binding.tvMakingchrgmax.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMax(), ""));
            binding.tvKaratname.setText(getValueOrDefault(responseCatalogDetails.getKaratName(), ""));
            binding.tvColor.setText(getValueOrDefault(responseCatalogDetails.getColorName(), ""));
            binding.tvuom.setText(responseCatalogDetails.getUomName());
            binding.tvgrossweight.setText("" + responseCatalogDetails.getGrossWeight() + " g");
            binding.tvcategory.setText(responseCatalogDetails.getCategoryName());
            binding.tvSubcategory.setText(responseCatalogDetails.getSubCategoryName());
            Glide.with(requireActivity())
                    .load(responseCatalogDetails.getImageFilePath())
                    .placeholder(R.drawable.jwimage)
                    .error(R.drawable.jwimage)
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
                    .into(binding.glideimg);
            binding.laytimage.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    startActivity(new Intent(requireActivity(), ImageZoomerActivity.class).putExtra("imageResId", responseCatalogDetails.getImageFilePath()));
                }
            });

        }
        return binding.getRoot();
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}