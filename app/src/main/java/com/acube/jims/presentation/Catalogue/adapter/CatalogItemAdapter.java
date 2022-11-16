package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.ProductViewHolder> {


    private Context mCtx;
    Datalist datalist;

    private final List<ResponseItems> dataset;
    List<ResponseItems> list;


    public CatalogItemAdapter(Context mCtx, List<ResponseItems> dataset, Datalist datalist) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.datalist = datalist;
        list = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_scanned_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ResponseItems responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(responseCatalogueListing.getItemName());
        holder.textViewPrice.setText("" + Math.round(responseCatalogueListing.getMrp()));
        holder.textViewSerialNo.setText(responseCatalogueListing.getSerialNumber());
        holder.textViewKarat.setText("" + responseCatalogueListing.getKaratName());

        // holder.imageView.setImageResource(homeData.getImage());

        Glide.with(mCtx)
                .load(responseCatalogueListing.getImagePath())
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
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewPrice, textViewSerialNo, textViewKarat, textViewStoneWeight, textViewGrossweight;
        ImageView imageView;
        CheckBox comparecheckbox;
        ResponseItems responseItems;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewPrice = itemView.findViewById(R.id.tvprice);
            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            textViewKarat = itemView.findViewById(R.id.tvkarat);
            textViewGrossweight = itemView.findViewById(R.id.tvgrossweight);
            imageView = itemView.findViewById(R.id.imageView);
            comparecheckbox = itemView.findViewById(R.id.comparecheckbox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datalist.replace(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber());
                }
            });
            comparecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        responseItems = new ResponseItems();
                        responseItems.setSerialNumber(dataset.get(getAdapterPosition()).getSerialNumber());
                        responseItems.setItemID(dataset.get(getAdapterPosition()).getItemID());
                        list.add(responseItems);
                        datalist.datalist(list);
                        Log.d("TAG", "onCheckedChanged: " + list.size());
                    } else if (!isChecked) {

                        list.remove(responseItems);
                        Log.d("TAG", "onCheckedChanged: " + responseItems.getSerialNumber());
                        Log.d("TAG", "onCheckedChanged: " + list.size());
                        datalist.datalist(list);
                        if (list.size() > 1) {


                        } else {

                        }


                    }
                }
            });
        }
    }


    public interface Datalist {
        void datalist(List<ResponseItems> comparelist);

        void replace(String Id);
    }
}