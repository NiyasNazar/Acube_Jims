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

import java.util.List;

public class LocationMismatchAdapter extends RecyclerView.Adapter<LocationMismatchAdapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;

    private final List<Found> dataset;

    PassId passId;


    public LocationMismatchAdapter(Context mCtx, List<Found> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;




    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_found_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Found found = dataset.get(position);
        holder.textViewItemName.setText(found.getSystemLocationName());
        holder.textViewLoccode.setText(found.getScanLocationName());
        holder.textViewDate.setText(found.getSerialNumber());








    }





    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode,textViewDate;
        ImageView imageView;
        CheckBox comparecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvlocationname);
            selection = itemView.findViewById(R.id.layoutparent);
            tableRow4=itemView.findViewById(R.id.tableRow4);

            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            textViewDate = itemView.findViewById(R.id.tv_serialnumber);



        }
    }
    public interface PassId{
        void passid(String id);
    }


}