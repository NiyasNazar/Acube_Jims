package com.acube.jims.presentation.Compare.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.acube.jims.R;
import com.acube.jims.databinding.LayoutCompareItemBinding;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CompareItemsAdapter extends RecyclerView.Adapter<CompareItemsAdapter.ProductViewHolder> {


    private Context mCtx;

    List<ResponseCompare> dataset;
    int width;


    public CompareItemsAdapter(Context mCtx, List<ResponseCompare> dataset, int width) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.width = width;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        LayoutCompareItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_compare_item, parent, false);

        //  binding = inflater.inflate(R.layout.layout_compare_item, parent, false);
        if (dataset.size() > 3) {
            binding.parent.getLayoutParams().width = width / 2;
        } else {
            binding.parent.getLayoutParams().width = width /2;
        }
        /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseCompare responseCompare = dataset.get(position);
        holder.bind(responseCompare);


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        LayoutCompareItemBinding binding;

        TextView textViewItemName, textViewDescription, textViewSerialNo;
        ImageView imageView;

        public ProductViewHolder(LayoutCompareItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

        public void bind(ResponseCompare responseCompare) {
            binding.tvbrandname.setText(responseCompare.getItemName());
            binding.tvDescription.setText(responseCompare.getItemDesc());
            binding.tvSerialnumber.setText(responseCompare.getSerialNumber());
            binding.tvgrossweight.setText("" + getValueOrDefault(responseCompare.getGrossWeight() + " g", ""));
            binding.tvKaratname.setText("" + getValueOrDefault(responseCompare.getKaratName(), ""));
            binding.tvGender.setText(responseCompare.getGender());
            binding.tvmakingchargemin.setText("" + getValueOrDefault(responseCompare.getMakingChargeMin(), ""));
            binding.tvMakingchrgmax.setText("" + getValueOrDefault(responseCompare.getMakingChargeMax(), ""));
            binding.tvuom.setText(responseCompare.getUomName());
            binding.tvcategory.setText(responseCompare.getCategoryName());
            binding.tvSubcategory.setText(responseCompare.getSubCategoryName());

            Glide.with(mCtx)
                    .load(responseCompare.getImagePath())
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
                    .into(binding.imvsingleitemimage);


        }
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}