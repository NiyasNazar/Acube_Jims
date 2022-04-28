package com.acube.jims.presentation.Report.adapter;

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
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class LocationMismatchAdapter extends RecyclerView.Adapter<LocationMismatchAdapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;

    private final List<LocationMismatch> dataset;

    PassId passId;


    public LocationMismatchAdapter(Context mCtx, List<LocationMismatch> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;




    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_locationsmissmatch_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        LocationMismatch locationMismatch = dataset.get(position);
        holder.tvlocationname.setText("System Location : " + locationMismatch.getSystemLocationName());
        holder.textViewItemName.setText("Item : " + locationMismatch.getItemName());
        holder.textViewLoccode.setText("Scanned Location : " + locationMismatch.getScanLocationName());
        holder.textViewSerialNo.setText("Sl No. : " + locationMismatch.getSerialNumber());
        try {
            if(locationMismatch.getKaratCode()!=null){
                DecimalFormat format = new DecimalFormat("0.#");

                holder.tvkarat.setText("Karat: " + format.format(locationMismatch.getKaratCode()));
            }else{
                holder.tvkarat.setText("Karat: " + "N/A");
            }

        } catch (NumberFormatException e) {

        }

        holder.tvcategory.setText("Category : " + locationMismatch.getCategoryName());
        Glide.with(mCtx).load(locationMismatch.getItemImagePath()).into(holder.imageView);






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
            tvkarat = itemView.findViewById(R.id.tvkarat);
            tvcategory = itemView.findViewById(R.id.tvcategory);
            imageView = itemView.findViewById(R.id.imvitemimage);
            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);



        }
    }
    public interface PassId{
        void passid(String id);
    }


}