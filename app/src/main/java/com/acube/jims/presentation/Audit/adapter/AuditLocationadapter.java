package com.acube.jims.presentation.Audit.adapter;

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

import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;

import java.util.List;

public class AuditLocationadapter extends RecyclerView.Adapter<AuditLocationadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;

    private final List<ResponseLocationList> dataset;

    PassId passId;


    public AuditLocationadapter(Context mCtx, List<ResponseLocationList> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_auditdetails, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ResponseLocationList responseLocationList = dataset.get(position);
        holder.textViewItemName.setText(responseLocationList.getCandidateLocationName());
        holder.textViewLoccode.setText(responseLocationList.getCandidateLocationCode());
        holder.textViewDate.setText(responseLocationList.getToBeAuditedOn());
        holder.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                LocalPreferences.storeStringPreference(mCtx, "locationID", String.valueOf(responseLocationList.getCandidateLocationID()));
                passId.passid(String.valueOf(responseLocationList.getCandidateLocationID()));
                LocalPreferences.storeStringPreference(mCtx, "auditID", responseLocationList.getAuditID());
                notifyDataSetChanged();
            }
        });


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (row_index == position) {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.textViewItemName.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border_selected));
                holder.textViewLoccode.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border_selected));
                holder.textViewDate.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border_selected));
                holder.textViewItemName.setTextColor(Color.parseColor("#666666"));
                holder.textViewLoccode.setTextColor(Color.parseColor("#666666"));

            } else {
                holder.textViewItemName.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border_selected));
                holder.textViewLoccode.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border_selected));
                holder.textViewItemName.setTextColor(Color.parseColor("#ffffff"));
                holder.textViewLoccode.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.textViewItemName.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border));
                holder.textViewLoccode.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border));
                holder.textViewDate.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border));
                holder.textViewItemName.setTextColor(Color.parseColor("#666666"));
                holder.textViewLoccode.setTextColor(Color.parseColor("#666666"));
            } else {
                holder.textViewItemName.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border));
                holder.textViewLoccode.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.table_item_border));
                holder.textViewItemName.setTextColor(Color.parseColor("#666666"));
                holder.textViewLoccode.setTextColor(Color.parseColor("#666666"));
            }
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewDate;
        ImageView imageView;
        CheckBox comparecheckbox;
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