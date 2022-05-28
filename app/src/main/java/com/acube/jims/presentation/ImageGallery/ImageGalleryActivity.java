package com.acube.jims.presentation.ImageGallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityImageGalleryBinding;
import com.acube.jims.datalayer.models.Catalogue.ItemSub;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sung2063.sliders.exception.SlideNullPointerException;
import com.sung2063.sliders.exception.SlideOutOfBoundException;
import com.sung2063.sliders.slideshow.SlideshowView;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends BaseActivity {

    List<ItemSub> dataset;
    ActivityImageGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);

        Intent mIntent = getIntent();
        dataset = new ArrayList<>();

        dataset = mIntent.getParcelableArrayListExtra("DATA");

        SlideshowView slideShowView = findViewById(R.id.slideshow_view);
        initToolBar(binding.toolbarApp.toolbar, "", true);


        List<ViewGroup> slideshowLayouts = new ArrayList<>();

        for (int k = 0; k < dataset.size(); k++) {
            SlideshowAdapter introSlide = new SlideshowAdapter(getApplicationContext(), dataset.get(k).getImageFilePath());
            slideshowLayouts.add(introSlide.getRootView());
        }
        try {
            slideShowView.setSlideList(slideshowLayouts);

            slideShowView.launch();
        } catch (SlideOutOfBoundException e) {
            Log.e("TAG", e.toString());
        } catch (SlideNullPointerException e) {
            Log.e("TAG", e.toString());
        }


    }

    public void back(View view) {
        onBackPressed();
    }

    public List<String> getListCity() {
        List<String> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "imagearray");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }
}