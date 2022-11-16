package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.databinding.ItemCategoryBinding;
import com.acube.jims.databinding.ItemSubMaincategoryBinding;
import com.acube.jims.datalayer.api.ResponseLiveCategory;
import com.acube.jims.datalayer.api.ResponseLiveSubCategory;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainSubCategoryAdapter extends RecyclerView.Adapter<MainSubCategoryAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<ResponseLiveSubCategory> dataset;
    FragmentTransition fragmenttransition;
    private int lastSelectedPosition = -1;
    private int lastsel = 0;

    public MainSubCategoryAdapter(Context mCtx, List<ResponseLiveSubCategory> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ItemSubMaincategoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item__sub_maincategory, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseLiveSubCategory responseLiveCategory = dataset.get(position);

        holder.binding.tvItemName.setText(responseLiveCategory.getSubCategoryName());
            Glide.with(mCtx)
                    .load(responseLiveCategory.getImageUrl())
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
                    .into(holder.binding.imageView);
        }







    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemSubMaincategoryBinding binding;
        ImageView imageView;

        public ProductViewHolder(ItemSubMaincategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getBindingAdapterPosition();
                    lastSelectedPosition = getBindingAdapterPosition();
                    lastsel = getAdapterPosition();
                    fragmenttransition.passData(dataset.get(position).getId());
                    notifyDataSetChanged();
                }
            });


        }
    }

    public interface FragmentTransition {
        void passData(int ID);

        void passlist(List<Integer> dataset);

        void scanaction(int docID, String toBeAuditedOn, String remark);

    }

    public String formatDate(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        return fmtOut.format(date);
    }

}
