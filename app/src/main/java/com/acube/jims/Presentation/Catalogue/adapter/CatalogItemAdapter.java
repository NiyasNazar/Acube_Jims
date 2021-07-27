package com.acube.jims.Presentation.Catalogue.adapter;

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

import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<ResponseCatalogueListing> dataset;


    public CatalogItemAdapter(Context mCtx, List<ResponseCatalogueListing> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_catalog_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(responseCatalogueListing.getItemName());
        // holder.imageView.setImageResource(homeData.getImage());
        if (responseCatalogueListing.getItemSubList().size()>0) {
            Glide.with(mCtx)
                    .load(responseCatalogueListing.getItemSubList().get(0).getImageFilePath())
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


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}