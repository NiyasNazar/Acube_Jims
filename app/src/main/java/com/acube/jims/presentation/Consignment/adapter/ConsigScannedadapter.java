package com.acube.jims.presentation.Consignment.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.acube.jims.R;
import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

public class ConsigScannedadapter extends RecyclerView.Adapter<ConsigScannedadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;

    private List<ConsignmentLine> dataset;

    PassId passId;


    public ConsigScannedadapter(Context mCtx, List<ConsignmentLine> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_consignment, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ConsignmentLine responseAuditDownload = dataset.get(position);
        holder.textViewSerialNo.setText(responseAuditDownload.getSerialNumber());
        int status = responseAuditDownload.getStatus() == null ? 0 : responseAuditDownload.getStatus();
        if (status == 40) {
            holder.textViewstatus.setText("Item Found");

        } else if (status == 50) {
            holder.textViewstatus.setText("Item Missing");

        } else if (status == 60) {
            holder.textViewstatus.setText("Item Sold");

        } else if (status == 70) {
            holder.textViewstatus.setText("Item Unknown");

        } else {
            holder.textViewstatus.setText("");

        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewstatus, textViewLoccode, textViewSerialNo, tvbranchname, tvkarat, tvcategory;
        ImageView imageView;
        CheckBox comparecheckbox;

        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);


            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);

            textViewstatus = itemView.findViewById(R.id.tv_status);
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    passId.passid(dataset.get(getAbsoluteAdapterPosition()).getConsignmentId(), dataset.get(getAbsoluteAdapterPosition()).getSerialNumber());
                }
            });
        }
    }

    public interface PassId {
        void passid(String id, String serial);
    }


}