package com.acube.jims.presentation.HomePage.adapter;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<HomeData> homemenuList;
    FragmentTransition fragmenttransition;

    public HomeAdapter(Context mCtx, List<HomeData> homemenuList, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.homemenuList = homemenuList;
        this.fragmenttransition = fragmenttransition;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_home_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        HomeData homeData = homemenuList.get(position);
        holder.textViewTitle.setText(homeData.getMenuText());
        // holder.imageView.setImageResource(homeData.getImage());
        if (homeData.getMenuName().equalsIgnoreCase("Dashboard")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.dashboard);

        } else if (homeData.getMenuName().equalsIgnoreCase("Analytics")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.analytics);


        } else if (homeData.getMenuName().equalsIgnoreCase("ItemRequest")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.itemreq);

        } else if (homeData.getMenuName().equalsIgnoreCase("ItemCatalog")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.catalogue);

        } else if (homeData.getMenuName().equalsIgnoreCase("Scan")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.scan);

        } else if (homeData.getMenuName().equalsIgnoreCase("AndroidReports")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.graph_report_gold);

        } else if (homeData.getMenuName().equalsIgnoreCase("Customer")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.itemreq);

        } else if (homeData.getMenuName().equalsIgnoreCase("InventoryAudit")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.audit);

        }else if (homeData.getMenuName().equalsIgnoreCase("deviceregistration")) {
            ((ProductViewHolder) holder).bindImage(holder.imageView, R.drawable.auditic);

        }

    }


    @Override
    public int getItemCount() {
        return homemenuList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvhomemenu);

            imageView = itemView.findViewById(R.id.imvhomeicon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmenttransition.replaceFragment(homemenuList.get(getAbsoluteAdapterPosition()).getMenuName());

                }
            });
        }

        private void bindImage(ImageView imageView, int imagepath) {
            Glide.with(mCtx)
                    .load(imagepath)
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
                    .into(imageView);

        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

    }
}
