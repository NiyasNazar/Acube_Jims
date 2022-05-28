package com.acube.jims.presentation.Analytics.adapter;

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

import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Analytics.AnalyticsCustomersServed;

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
        holder.tvemployeename.setText(analyticsCustomersServed.getEmployeeName());



    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCount, tvemployeename, textViewDate;
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
            tvemployeename = itemView.findViewById(R.id.tvemployeename);


        }
    }

    public interface PassId {
        void passid(String id);
    }


}