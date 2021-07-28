package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;

import java.util.ArrayList;
import java.util.List;

public class FilterChildAdapter extends RecyclerView.Adapter<FilterChildAdapter.ProductViewHolder> {


    private Context mCtx;


    //  private final List<ResponseCatalogueListing> dataset;
    List<String> dataset;
    List<String> sublist;

    public FilterChildAdapter(Context mCtx, List<String> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
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
        holder.textViewItemName.setText(dataset.get(position));
        // holder.imageView.setImageResource(homeData.getImage());
         sublist.add("Ring");
         sublist.add("Bangle");
       // holder.recyclerView.setAdapter(new FilterChildAdapter(mCtx, sublist));


    }


    @Override
    public int getItemCount() {
        return dataset.size();
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

        }
    }
}