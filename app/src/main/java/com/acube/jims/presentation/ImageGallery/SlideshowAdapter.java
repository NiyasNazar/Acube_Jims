package com.acube.jims.presentation.ImageGallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.acube.jims.R;
import com.acube.jims.utils.ZoomableImageView;
import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;


public class SlideshowAdapter {

    // =============================================================================================
    // Variables
    // =============================================================================================
    private Context context;
    private ViewGroup vgContainer;
    private ZoomableImageView ivImage;

    // =============================================================================================
    // Constructor
    // =============================================================================================
    public SlideshowAdapter(Context context, String  url) {
        this.context = context;
        initViews();
        populateData(url);
    }
    public SlideshowAdapter(Context context) {
        this.context = context;
        initViews();

    }
    // =============================================================================================
    // Methods
    // =============================================================================================
    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup rootLayout = (ViewGroup) inflater.inflate(R.layout.image_template_layout, null);

        vgContainer = rootLayout.findViewById(R.id.cl_container);
        ivImage = rootLayout.findViewById(R.id.iv_image);
    }

    private void populateData(String drawableImageId) {
        Glide.with(context).load(drawableImageId).into(ivImage);
    }

    public ViewGroup getRootView() {
        return vgContainer;
    }

}