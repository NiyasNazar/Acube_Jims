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
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.acube.jims.datalayer.models.Audit.LocationMismatchApproved;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class LocationApprovedAdapter extends RecyclerView.Adapter<LocationApprovedAdapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;

    private final List<LocationMismatchApproved> dataset;

    PassId passId;


    public LocationApprovedAdapter(Context mCtx, List<LocationMismatchApproved> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;




    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_approved_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        LocationMismatchApproved approved = dataset.get(position);
        holder.tvlocationname.setText("System Location : " + approved.getSystemLocationName());
        holder.textViewItemName.setText("Item : " + approved.getItemName());
        holder.textViewLoccode.setText("Scanned Location : " + approved.getScanLocationName());
        holder.textViewSerialNo.setText("Sl No. : " + approved.getSerialNumber());
        try {
            if(approved.getKaratCode()!=null){
                DecimalFormat format = new DecimalFormat("0.#");

                holder.tvkarat.setText("Karat: " + format.format(approved.getKaratCode()));
            }else{
                holder.tvkarat.setText("Karat: " + "N/A");
            }

        } catch (NumberFormatException e) {

        }

        holder.tvcategory.setText("Category : " + approved.getCategoryName());
        Glide.with(mCtx).load(approved.getItemImagePath()).into(holder.imageView);








    }





    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewSerialNo, tvlocationname, tvkarat, tvcategory;
        ImageView imageView;
        CheckBox comparecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvlocationname = itemView.findViewById(R.id.tvlocationname);
            textViewItemName = itemView.findViewById(R.id.tvitemname);
            selection = itemView.findViewById(R.id.layoutparent);
            tableRow4=itemView.findViewById(R.id.tableRow4);

            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            tvkarat = itemView.findViewById(R.id.tvkarat);
            tvcategory = itemView.findViewById(R.id.tvcategory);
            imageView = itemView.findViewById(R.id.imvitemimage);



        }
    }
    public interface PassId{
        void passid(String id);
    }


}