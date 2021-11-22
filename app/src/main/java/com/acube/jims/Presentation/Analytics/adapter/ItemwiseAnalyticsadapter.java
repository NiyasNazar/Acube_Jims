package com.acube.jims.Presentation.Analytics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Analytics.AnalyticsCustomersServed;
import com.acube.jims.datalayer.models.Analytics.AnalyticsItemWiseViewed;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class ItemwiseAnalyticsadapter extends RecyclerView.Adapter<ItemwiseAnalyticsadapter.ProductViewHolder> {


    private Context mCtx;

    private final List<AnalyticsItemWiseViewed> dataset;


    public ItemwiseAnalyticsadapter(Context mCtx, List<AnalyticsItemWiseViewed> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    public ItemwiseAnalyticsadapter(Context mCtx, List<AnalyticsItemWiseViewed> dataset) {
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
        AnalyticsItemWiseViewed analyticsItemWiseViewed = dataset.get(position);
        holder.textViewItemName.setText(analyticsItemWiseViewed.getItemName());
        holder.textViewViews.setText(String.valueOf(analyticsItemWiseViewed.getTotalCount()));


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewViews, textViewDate;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvproductname);
            textViewViews = itemView.findViewById(R.id.tvcount);


        }
    }

    public interface PassId {
        void passid(String id);
    }


}