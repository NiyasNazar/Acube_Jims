package com.acube.jims.presentation.Filters.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;

import java.util.List;

public class AppliedSubcategoryAdapter extends RecyclerView.Adapter<AppliedSubcategoryAdapter.ProductViewHolder> {


    private Context mCtx;
    Passfilter passfilter;

    //  private final List<ResponseCatalogueListing> dataset;
    List<String> dataset;


    public AppliedSubcategoryAdapter(Context mCtx, List<String> dataset, Passfilter passfilter) {
        this.mCtx = mCtx;
        this.dataset = dataset;

        this.passfilter = passfilter;

    }

    public AppliedSubcategoryAdapter(Context mCtx, List<String> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_appliedsub_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(dataset.get(position));


    }


    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvname);


        }

    }

    public interface Passfilter {
        void PassId(Boolean flag, String id);
    }
}