package com.acube.jims.presentation.ItemRequest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.ItemRequest.ResponseItemRequestDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class PicklistDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    AddtoFavorites addtoFavorites;
    private List<ResponseItemRequestDetails> dataset;
    private final Context context;
    replaceFregment replaceFregment;
    private boolean isLoadingAdded = false;

    public PicklistDetailsAdapter(Context context, List<ResponseItemRequestDetails> dataset) {
        this.context = context;

        this.dataset = dataset;
    }


    @Override
    public CatalogVH onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_itemdetails_item, parent, false);
        return new CatalogVH(view);
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ResponseItemRequestDetails result = dataset.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final CatalogVH catalogVH = (CatalogVH) holder;
                ResponseItemRequestDetails responseCatalogueListing = dataset.get(position);
                String myString = responseCatalogueListing.getItemName();
                String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase();

                catalogVH.textViewItemName.setText(myString);
                catalogVH.textViewSerialnumber.setText("" + responseCatalogueListing.getSerialNumber());


                    Glide.with(context)
                            .load(responseCatalogueListing.getImageFilePath())
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
                            .into(catalogVH.imageView);



                break;

            case LOADING:

                break;
        }

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }





    protected class CatalogVH extends RecyclerView.ViewHolder {
        TextView textViewItemName, textViewStoneWeight,
                textViewGrossWeight, textViewSerialnumber, textViewPrice,
                textViewKarat, textViewStock;
        ImageView imageView;
        LikeButton mlikebtn;

        public CatalogVH(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewStock = itemView.findViewById(R.id.tvstock);
            mlikebtn = itemView.findViewById(R.id.fav_button);

            textViewSerialnumber = itemView.findViewById(R.id.tv_serialnumber);
            textViewGrossWeight = itemView.findViewById(R.id.tvgrossweight);
            textViewPrice = itemView.findViewById(R.id.tvprice);

            textViewKarat = itemView.findViewById(R.id.tvkarat);


            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    String ID = String.valueOf(dataset.get(pos).getSerialNumber());
                    replaceFregment.replace(ID);
                }
            });
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public interface replaceFregment {
        void replace(String Id);
    }

    public interface AddtoFavorites {
        void addtofav(String id, String serialno);
    }


}