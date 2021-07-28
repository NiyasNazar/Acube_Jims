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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class FilterParentAdapter extends RecyclerView.Adapter<FilterParentAdapter.ProductViewHolder> {


    private Context mCtx;


    private  List<String> dataset;
    List<String> sublist;


    public FilterParentAdapter(Context mCtx, List<String> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;

    }
    public FilterParentAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist=new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_parent_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

       // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        //   holder.textViewItemName.setText(responseCatalogueListing.getItemName());
        // holder.imageView.setImageResource(homeData.getImage());
        sublist.add("Gold");
        sublist.add("Diamond");
        holder.recyclerView.setAdapter(new FilterChildAdapter(mCtx, sublist));


    }


    @Override
    public int getItemCount() {
        return 5;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView imageView;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            //imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.sublist);
            recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recyclerView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(recyclerView,
                                new AutoTransition());
                        recyclerView.setVisibility(View.GONE);
                       // arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                    }


                }
            });

        }
    }
}