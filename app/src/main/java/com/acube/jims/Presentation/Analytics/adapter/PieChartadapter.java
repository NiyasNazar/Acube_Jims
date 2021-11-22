package com.acube.jims.Presentation.Analytics.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Analytics.AnalyticsCustomersServed;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class PieChartadapter extends RecyclerView.Adapter<PieChartadapter.ProductViewHolder> {


    private Context mCtx;

    private final List<AnalyticsCustomersServed> dataset;




    public PieChartadapter(Context mCtx, List<AnalyticsCustomersServed> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;



    }
    public PieChartadapter(Context mCtx, List<AnalyticsCustomersServed> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;



    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_piechart,parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        AnalyticsCustomersServed analyticsCustomersServed=dataset.get(position);
        holder.circleProgressView.setValue(analyticsCustomersServed.getPerformancePercentage());
        holder.textViewCount.setText("Total "+analyticsCustomersServed.getTotalCount());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCount, textViewLoccode, textViewDate;
        ImageView imageView;
        CheckBox comparecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;
        CircleProgressView circleProgressView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            circleProgressView = itemView.findViewById(R.id.circleView);
            textViewCount = itemView.findViewById(R.id.tvcount);



        }
    }

    public interface PassId {
        void passid(String id);
    }


}