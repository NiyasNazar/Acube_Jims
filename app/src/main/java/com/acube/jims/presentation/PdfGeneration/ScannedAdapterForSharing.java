package com.acube.jims.presentation.PdfGeneration;

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
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class ScannedAdapterForSharing extends RecyclerView.Adapter<ScannedAdapterForSharing.ProductViewHolder> {


    private Context mCtx;
    int itemquantity = 1;
    List<ResponseCompare> dataset;
    UpdateQuantity updateQuantity;
    DeleteProduct deleteProduct;

    public ScannedAdapterForSharing(Context mCtx, List<ResponseCompare> dataset, UpdateQuantity updateQuantity, DeleteProduct deleteProduct) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.updateQuantity = updateQuantity;
        this.deleteProduct = deleteProduct;
    }

    public ScannedAdapterForSharing(Context mCtx, List<ResponseCompare> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_cart_item_share, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseCompare cartDetail = dataset.get(position);
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



        }
    }

    public interface UpdateQuantity {
        void updatevalue(String itemid, String quantity);
    }

    public interface DeleteProduct {
        void removefromcart(String itemid, String quantity, String serialno);

    }
}