package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Filter.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class FilterMasterAdapter extends RecyclerView.Adapter<FilterMasterAdapter.ProductViewHolder> {


    private Context mCtx;
    int selectedPosition = 0;
    ReplaceFregment replaceFregment;

    private List<String> dataset;
    List<SubCategory> sublist;


    public FilterMasterAdapter(Context mCtx, List<String> dataset,ReplaceFregment replaceFregment) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.replaceFregment=replaceFregment;
        sublist = new ArrayList<>();

    }

    public FilterMasterAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_sub_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(dataset.get(position));
        // holder.imageView.setImageResource(homeData.getImage());
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F8FAFF"));
            holder.textViewItemName.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
            holder.textViewItemName.setTextColor(Color.parseColor("#666666"));

        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView arrow;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            arrow = itemView.findViewById(R.id.arrow);
            recyclerView = itemView.findViewById(R.id.sublist);
            recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                 /*   sublist = dataset.get(pos).getSubCategories();
                    recyclerView.setAdapter(new FilterChildAdapter(mCtx, sublist));
                    if (recyclerView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(recyclerView,
                                new AutoTransition());
                        recyclerView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    } else {
                        arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);

                        recyclerView.setVisibility(View.VISIBLE);

                    }
*/
                    notifyDataSetChanged();
                    selectedPosition = pos;
                    replaceFregment.replace(pos);

                }
            });

        }
    }

    public  interface ReplaceFregment{
        void replace(int Id);
    }
}