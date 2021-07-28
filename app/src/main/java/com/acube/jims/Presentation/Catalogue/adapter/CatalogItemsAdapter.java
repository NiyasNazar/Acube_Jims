package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class CatalogItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<ResponseCatalogueListing> dataset;
    private final Context context;

    private boolean isLoadingAdded = false;

    public CatalogItemsAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }

    public List<ResponseCatalogueListing> getMovies() {
        return dataset;
    }

    public void setMovies(List<ResponseCatalogueListing> movieResults) {
        this.dataset = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View view1 = inflater.inflate(R.layout.layout_catalog_item, parent, false);
        viewHolder = new CatalogVH(view1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ResponseCatalogueListing result = dataset.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final CatalogVH catalogVH = (CatalogVH) holder;
                ResponseCatalogueListing responseCatalogueListing = dataset.get(position);

                catalogVH.textViewItemName.setText(responseCatalogueListing.getItemName());

                // holder.imageView.setImageResource(homeData.getImage());
                if (responseCatalogueListing.getItemSubList().size() > 0) {
                    Glide.with(context)
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
                            .into(catalogVH.imageView);
                }


                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dataset.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(ResponseCatalogueListing r) {
        dataset.add(r);
        notifyItemInserted(dataset.size() - 1);
    }

    public void addAll(List<ResponseCatalogueListing> moveResults) {
        for (ResponseCatalogueListing result : moveResults) {
            add(result);
        }
    }

    public void remove(ResponseCatalogueListing r) {
        int position = dataset.indexOf(r);
        if (position > -1) {
            dataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ResponseCatalogueListing());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataset.size() - 1;
        ResponseCatalogueListing result = getItem(position);

        if (result != null) {
            dataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ResponseCatalogueListing getItem(int position) {
        return dataset.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class CatalogVH extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        ImageView imageView;

        public CatalogVH(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}