package com.acube.jims.presentation.DashBoard.adapter;

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
import com.acube.jims.datalayer.models.Dashboard.DashboardEmployeeAnalytic;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class DashBoardPieChartadapter extends RecyclerView.Adapter<DashBoardPieChartadapter.ProductViewHolder> {


    private Context mCtx;

    private final List<DashboardEmployeeAnalytic> dataset;




    public DashBoardPieChartadapter(Context mCtx, List<DashboardEmployeeAnalytic> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;



    }
    public DashBoardPieChartadapter(Context mCtx, List<DashboardEmployeeAnalytic> dataset) {
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
        DashboardEmployeeAnalytic analyticsCustomersServed=dataset.get(position);
        holder.circleProgressView.setValue(analyticsCustomersServed.getPerformancePercentage());
        holder.textViewCount.setText(""+analyticsCustomersServed.getTotalValue());
        holder.textViewEmployeeName.setText(analyticsCustomersServed.getEmployeeName());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCount, textViewEmployeeName, textViewDate;
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
            textViewEmployeeName = itemView.findViewById(R.id.tvemployeename);


        }
    }

    public interface PassId {
        void passid(String id);
    }


}