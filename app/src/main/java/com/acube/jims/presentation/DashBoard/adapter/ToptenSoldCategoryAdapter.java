package com.acube.jims.presentation.DashBoard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Dashboard.DashboardCategoryWiseSold;

import java.util.List;

public class ToptenSoldCategoryAdapter extends RecyclerView.Adapter<ToptenSoldCategoryAdapter.ProductViewHolder> {


    private Context mCtx;

    private final List<DashboardCategoryWiseSold> dataset;


    public ToptenSoldCategoryAdapter(Context mCtx, List<DashboardCategoryWiseSold> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    public ToptenSoldCategoryAdapter(Context mCtx, List<DashboardCategoryWiseSold> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_tocategory_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        DashboardCategoryWiseSold dashboardCategoryWiseSold = dataset.get(position);
        holder.textViewItemName.setText(dashboardCategoryWiseSold.getCategoryName());
        holder.textViewCount.setText(String.valueOf(dashboardCategoryWiseSold.getTotalCount()));
        holder.textViewValue.setText(String.valueOf(dashboardCategoryWiseSold.getTotalValue()+" SAR"));
        holder.textViewsubcategory.setText(dashboardCategoryWiseSold.getSubCategoryName());

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewCount, textViewValue,textViewsubcategory;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvproductname);
            textViewValue = itemView.findViewById(R.id.tvamount);
            textViewCount = itemView.findViewById(R.id.tvcount);
            textViewsubcategory = itemView.findViewById(R.id.tvsubcategory);

        }
    }

    public interface PassId {
        void passid(String id);
    }


}