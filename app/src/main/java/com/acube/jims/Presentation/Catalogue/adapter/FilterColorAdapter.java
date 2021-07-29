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
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class FilterColorAdapter extends RecyclerView.Adapter<FilterColorAdapter.ProductViewHolder> {


    private Context mCtx;


    private List<Karatresult> dataset;
    List<SubCategory> sublist;


    public FilterColorAdapter(Context mCtx, List<Karatresult> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();

    }

    public FilterColorAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_color_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
     //   holder.textViewItemName.setText(dataset.get(position).getKaratName());
        // holder.imageView.setImageResource(homeData.getImage());


    }


    @Override
    public int getItemCount() {
        return 2;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView imageView;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

           /* textViewItemName = itemView.findViewById(R.id.tv_item_name);
            //imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.sublist);
            recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  *//*  int pos = getAdapterPosition();
                    sublist = dataset.get(pos).getSubCategories();
                    recyclerView.setAdapter(new FilterChildAdapter(mCtx, sublist));
                    if (recyclerView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(recyclerView,
                                new AutoTransition());
                        recyclerView.setVisibility(View.GONE);
                        // arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    } else {

                        recyclerView.setVisibility(View.VISIBLE);

                    }
*//*

                }
            });*/

        }
    }
}