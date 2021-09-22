package com.acube.jims.Presentation.Report.adapter;

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
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;

import java.util.List;

public class ItemHistoryadapter extends RecyclerView.Adapter<ItemHistoryadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;
    boolean isSelectedAll;
    private final List<ResponseScanHistory> dataset;

    PassId passId;


    public ItemHistoryadapter(Context mCtx, List<ResponseScanHistory> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_history_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ResponseScanHistory responseScanHistory = dataset.get(position);
        holder.textViewItemName.setText(responseScanHistory.getScanLocationName());
        holder.textViewLoccode.setText(responseScanHistory.getScanLocationCode());
        holder.textViewDate.setText(responseScanHistory.getScannedDate());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewDate;
        ImageView imageView;
        CheckBox locatecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvlocationname);
            selection = itemView.findViewById(R.id.layoutparent);
            tableRow4 = itemView.findViewById(R.id.tableRow4);

            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            textViewDate = itemView.findViewById(R.id.tv_date);


        }
    }

    public interface PassId {
        void passid(String id);
    }


}