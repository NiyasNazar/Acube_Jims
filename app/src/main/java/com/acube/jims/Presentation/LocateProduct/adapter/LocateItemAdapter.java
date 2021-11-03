package com.acube.jims.Presentation.LocateProduct.adapter;

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
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class LocateItemAdapter extends RecyclerView.Adapter<LocateItemAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<LocateItem> dataset;
    FragmentTransition fragmenttransition;


    public LocateItemAdapter(Context mCtx, List<LocateItem> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_locate_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.textViewTitle.setText(dataset.get(position).getSerialnumber());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_serialnumber);


        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

    }
}
