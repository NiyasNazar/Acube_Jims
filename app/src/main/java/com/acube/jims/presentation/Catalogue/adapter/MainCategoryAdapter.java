package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.acube.jims.R;
import com.acube.jims.databinding.ItemCategoryBinding;
import com.acube.jims.datalayer.api.ResponseLiveCategory;
import com.acube.jims.utils.OnSingleClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<ResponseLiveCategory> dataset;
    FragmentTransition fragmenttransition;
    private int lastSelectedPosition = -1;
    private int lastsel = 0;

    public MainCategoryAdapter(Context mCtx, List<ResponseLiveCategory> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ItemCategoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_category, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseLiveCategory responseCategory = dataset.get(position);

        holder.binding.tvusername.setText(responseCategory.getCategoryName());

        if (lastSelectedPosition == position) {
            holder.binding.cdv.setBackgroundColor(Color.parseColor("#BF8F3A"));
            holder.binding.tvusername.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (position == lastsel) {
            fragmenttransition.replaceFragment(dataset.get(position).getId());
            holder.binding.cdv.setBackgroundColor(Color.parseColor("#BF8F3A"));
            holder.binding.tvusername.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.binding.cdv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.binding.tvusername.setTextColor(Color.parseColor("#000000"));

        }

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;
        ImageView imageView;

        public ProductViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.parent.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getAdapterPosition();
                    lastSelectedPosition = getAdapterPosition();
                    lastsel = getAdapterPosition();
                    fragmenttransition.replaceFragment(dataset.get(position).getId());
                    binding.tvusername.setTextColor(dataset.get(position).isSelected() ? Color.parseColor("#BF8F3A") : Color.parseColor("#000000"));
                    notifyDataSetChanged();
                }
            });


        }
    }

    public interface FragmentTransition {
        void replaceFragment(int ID);

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
