package com.acube.jims.presentation.ItemRequest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    AddtoList addtoList;
    private List<ResponseCatalogueListing> dataset;
    private final Context context;
    replaceFregment replaceFregment;
    private boolean isLoadingAdded = false;
    boolean isselectclicked = false;

    public ItemRequestAdapter(Context context, replaceFregment replaceFregment, AddtoList addtoList) {
        this.context = context;
        this.replaceFregment = replaceFregment;
        this.addtoList = addtoList;
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
        View view1 = inflater.inflate(R.layout.layout_itemrequest_item, parent, false);
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
                String myString = responseCatalogueListing.getItemName();
                String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase();

                catalogVH.textViewItemName.setText(myString);
                catalogVH.textViewSerialnumber.setText("" + responseCatalogueListing.getSerialNumber());
                catalogVH.textViewKarat.setText("" + responseCatalogueListing.getKaratName());
                catalogVH.textViewPrice.setText("SAR " + responseCatalogueListing.getMrp());

                if (isselectclicked) {
                    catalogVH.checkboxlayout.setVisibility(View.VISIBLE);
                } else {
                    catalogVH.checkboxlayout.setVisibility(View.INVISIBLE);
                }
                if (responseCatalogueListing.isItemRequested()){
                    catalogVH.checkboxlayout.setVisibility(View.VISIBLE);
                    ((CatalogVH) holder).selectcheckbox.setChecked(true);
                }else{

                }
                // holder.imageView.setImageResource(homeData.getImage());
                if (responseCatalogueListing.getItemSubList().size() > 0) {
                    Glide.with(context)
                            .load(responseCatalogueListing.getItemSubList().get(0).getImageFilePath())
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
        TextView textViewItemName, textViewStoneWeight,
                textViewGrossWeight, textViewSerialnumber, textViewPrice,
                textViewKarat, textViewStock;
        ImageView imageView;
        CheckBox selectcheckbox;
        RelativeLayout checkboxlayout;

        public CatalogVH(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            textViewStock = itemView.findViewById(R.id.tvstock);
            selectcheckbox = itemView.findViewById(R.id.select_checkbox);
            checkboxlayout = itemView.findViewById(R.id.checkboxlayt);
            textViewSerialnumber = itemView.findViewById(R.id.tv_serialnumber);
            textViewGrossWeight = itemView.findViewById(R.id.tvgrossweight);
            textViewPrice = itemView.findViewById(R.id.tvprice);
            textViewKarat = itemView.findViewById(R.id.tvkarat);
            imageView = itemView.findViewById(R.id.imageView);

            selectcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        int pos=getBindingAdapterPosition();
                        int itemID=dataset.get(pos).getId();
                        String serialNo=dataset.get(pos).getSerialNumber();
                        addtoList.addtopicklist(itemID,serialNo,0);
                    }
                    if (!isChecked){
                        int pos=getBindingAdapterPosition();
                        int itemID=dataset.get(pos).getId();
                        String serialNo=dataset.get(pos).getSerialNumber();
                        addtoList.removefrompicklist(itemID);
                    }

                }
            });

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

    public interface AddtoList {
        void addtopicklist(int itemID,String serialNumber, int deviceID);
        void removefrompicklist(int itemID);
    }

    public void selectAll() {
        isselectclicked = true;


        notifyDataSetChanged();
    }

    public void unselectall() {

        notifyDataSetChanged();
    }
}