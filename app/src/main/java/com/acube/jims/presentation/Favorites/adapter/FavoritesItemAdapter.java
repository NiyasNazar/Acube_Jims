package com.acube.jims.presentation.Favorites.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FavoritesItemAdapter extends RecyclerView.Adapter<FavoritesItemAdapter.ProductViewHolder> {


    private Context mCtx;
    int itemquantity = 1;
    List<ResponseFavorites> dataset;
    DeleteProduct deleteProduct;
    List<String> comparelist;
    Comaparelist comaparelist;
    boolean isSelectedAll;
    public FavoritesItemAdapter(Context mCtx, List<ResponseFavorites> dataset, DeleteProduct deleteProduct,Comaparelist comaparelist) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.deleteProduct = deleteProduct;
        comparelist = new ArrayList<>();
        this.comaparelist=comaparelist;
    }

    public FavoritesItemAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_favorites_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseFavorites cartDetail = dataset.get(position);
        holder.textViewitemName.setText(cartDetail.getItemName());
        holder.textViewSerialnos.setText(cartDetail.getSerialNumber());


        Glide.with(mCtx)
                .load(cartDetail.getImagePath())
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
                .into(holder.ItemImage);


        if (cartDetail.getStoneWeight() != null) {
            holder.textViewStoneweight.setText("Stone Weight: " + cartDetail.getStoneWeight() + " g");
        } else {
            holder.textViewStoneweight.setText("Stone Weight: N/A");
        }
        if (cartDetail.getGrossWeight() != null) {
            holder.textViewWeight.setText("Weight: " + cartDetail.getGrossWeight() + " g");
        } else {
            holder.textViewWeight.setText("Stone Weight: N/A");
        }
        if (!isSelectedAll){
            holder.comparecheckbox.setChecked(false);
            comparelist=new ArrayList<>();
        }
        else {
            holder.comparecheckbox.setChecked(true);
            comparelist.add(String.valueOf(dataset.get(position).getSerialNumber()));
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewQuantity, textViewitemName, textViewWeight, textViewStoneweight,textViewSerialnos;
        ImageView imageViewadd, imageViewremove, ItemImage, imageviewdelete;
        CheckBox comparecheckbox;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewitemName = itemView.findViewById(R.id.tv_itemname);
            textViewSerialnos = itemView.findViewById(R.id.tv_serialnumber);

            textViewWeight = itemView.findViewById(R.id.tv_weight);
            textViewStoneweight = itemView.findViewById(R.id.tv_stoneweight);
            imageviewdelete = itemView.findViewById(R.id.imvdelete);
            ItemImage = itemView.findViewById(R.id.item_image);
            comparecheckbox = itemView.findViewById(R.id.comparecheckbox);
            imageviewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    deleteProduct.removefromcart(String.valueOf(dataset.get(pos).getItemID()), dataset.get(pos).getSerialNumber());

                }
            });
            comparecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        comparelist.add(String.valueOf(dataset.get(getAdapterPosition()).getSerialNumber()));
                        comaparelist.compareitems(comparelist);


                    } else if (!isChecked) {
                        comparelist.remove(String.valueOf(dataset.get(getAdapterPosition()).getSerialNumber()));
                        comaparelist.compareitems(comparelist);
                    }
                }
            });


        }
    }

    public interface DeleteProduct {
        void removefromcart(String itemid, String serialno);

    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(mCtx, key, json);
    }

    public interface Comaparelist {
        void compareitems(List<String> comparelist);
    }

    public void selectAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        notifyDataSetChanged();
    }

}