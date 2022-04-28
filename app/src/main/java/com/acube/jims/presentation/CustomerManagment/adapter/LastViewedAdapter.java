package com.acube.jims.presentation.CustomerManagment.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.DecimalFormat;
import java.util.List;

public class LastViewedAdapter extends RecyclerView.Adapter<LastViewedAdapter.ProductViewHolder> {


    private Context mCtx;

    ReplaceFragment replaceFragment;
    private final List<ItemViewHistory> itemViewHistoryList;


    public LastViewedAdapter(Context mCtx, List<ItemViewHistory> itemViewHistoryList, ReplaceFragment replaceFragment) {
        this.mCtx = mCtx;
        this.itemViewHistoryList = itemViewHistoryList;
        this.replaceFragment = replaceFragment;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_lastviewed_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ItemViewHistory responseListing = itemViewHistoryList.get(position);
        if (responseListing.getImagePath()!=null){
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
        }
        holder.textViewSerialNo.setText("" + responseListing.getSerialNumber());
        holder.textViewName.setText(responseListing.getItemName());
        DecimalFormat format = new DecimalFormat("0.#");
        holder.textViewWeight.setText("" + format.format(Double.parseDouble(responseListing.getGoldWeight())) + " gm");

    }


    @Override
    public int getItemCount() {
        return itemViewHistoryList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWeight, textViewSerialNo, textViewName;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imvitemimage);
            textViewName = itemView.findViewById(R.id.tv_itemname);

            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            textViewWeight = itemView.findViewById(R.id.tv_weight);


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


}
