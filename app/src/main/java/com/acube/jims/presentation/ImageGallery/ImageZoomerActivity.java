package com.acube.jims.presentation.ImageGallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityImageZoomerBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class ImageZoomerActivity extends BaseActivity {
    String imageResId;
    ActivityImageZoomerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_zoomer);

        initToolBar(binding.toolbar.toolbar, "", true);
        imageResId = getIntent().getStringExtra("imageResId");
        ZoomageView imageViewZoom = findViewById(R.id.imagezoom);
        Glide.with(ImageZoomerActivity.this)
                .load(imageResId)

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
                .into(imageViewZoom);
    }
}