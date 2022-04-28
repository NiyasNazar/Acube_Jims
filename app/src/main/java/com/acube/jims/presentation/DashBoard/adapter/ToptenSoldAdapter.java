package com.acube.jims.presentation.DashBoard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Dashboard.DashboardItemWiseSold;

import java.util.List;

public class ToptenSoldAdapter extends RecyclerView.Adapter<ToptenSoldAdapter.ProductViewHolder> {


    private Context mCtx;

    private final List<DashboardItemWiseSold> dataset;


    public ToptenSoldAdapter(Context mCtx, List<DashboardItemWiseSold> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    public ToptenSoldAdapter(Context mCtx, List<DashboardItemWiseSold> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_itemwissold, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        DashboardItemWiseSold analyticsItemWiseViewed = dataset.get(position);
        holder.textViewItemName.setText(analyticsItemWiseViewed.getItemName());
        holder.textViewCount.setText(String.valueOf(analyticsItemWiseViewed.getTotalCount()));
        holder.textViewValue.setText(String.valueOf(analyticsItemWiseViewed.getTotalValue()+" SAR"));

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewCount, textViewValue;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvproductname);
            textViewValue = itemView.findViewById(R.id.tvamount);
            textViewValue.setVisibility(View.VISIBLE);
            textViewCount = itemView.findViewById(R.id.tvcount);

        }
    }

    public interface PassId {
        void passid(String id);
    }


}