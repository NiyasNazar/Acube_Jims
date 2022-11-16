package com.acube.jims.presentation.CustomerManagment.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.CustomerManagment.ItemViewHistory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;

import java.text.DecimalFormat;
import java.util.List;

public class LastViewedAdapterAll extends RecyclerView.Adapter<LastViewedAdapterAll.ProductViewHolder> {


    private Context mCtx;

    ReplaceFragment replaceFragment;
    private final List<ItemViewHistory> itemViewHistoryList;


    public LastViewedAdapterAll(Context mCtx, List<ItemViewHistory> itemViewHistoryList, ReplaceFragment replaceFragment) {
        this.mCtx = mCtx;
        this.itemViewHistoryList = itemViewHistoryList;
        this.replaceFragment = replaceFragment;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_lastviewed_itemall, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ItemViewHistory responseListing = itemViewHistoryList.get(position);
        holder.textViewItemName.setText(responseListing.getItemName());
        holder.textViewSerialnumber.setText(responseListing.getSerialNumber());
        holder.textViewCode.setText(responseListing.getItemCode());
        //  holder.tv_description.setText(responseListing.getItemDesc());
        //  holder.textViewPrice.setText(""+responseListing.getMrp());
        Glide.with(mCtx)
                .load(responseListing.getImagePath())
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
                .into(holder.imageView);
        holder.textViewWeight.setText("" + getValueOrDefault(responseListing.getGoldWeight(),"")+ "g");

    }


    @Override
    public int getItemCount() {
        return itemViewHistoryList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewWarehouse,
                textViewGrossWeight, textViewSerialnumber, textViewPrice,
                textViewKarat, textViewWeight, textViewCode, textviewDesc;
        ImageView imageView;
        LikeButton mlikebtn;
        CheckBox selectionlayout;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewWarehouse = itemView.findViewById(R.id.tv_warehouse);
            textViewWeight = itemView.findViewById(R.id.tv_item_weight);
            textViewCode = itemView.findViewById(R.id.tv_item_code);
            textViewSerialnumber = itemView.findViewById(R.id.tv_serialnumber);
            textViewGrossWeight = itemView.findViewById(R.id.tvgrossweight);
            textViewPrice = itemView.findViewById(R.id.tvprice);
            textviewDesc = itemView.findViewById(R.id.tv_description);
            textViewKarat = itemView.findViewById(R.id.tvkarat);
            textviewDesc = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.imageView);
            selectionlayout = itemView.findViewById(R.id.fav_button);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                  //  LocalPreferences.storeStringPreference(mCtx, "GuestCustomerID", String.valueOf(customerListings.get(pos).getId()));


                  replaceFragment.replacefragments(itemViewHistoryList.get(pos).getSerialNumber());
                }
            });


        }
    }

    public interface ReplaceFragment {
        void replacefragments(String id);
    }
    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
