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
import com.acube.jims.datalayer.models.Audit.Missing;

import java.util.List;

public class MissingadapterforFragment extends RecyclerView.Adapter<MissingadapterforFragment.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;
    boolean isSelectedAll;
    private final List<Missing> dataset;

    PassId passId;


    public MissingadapterforFragment(Context mCtx, List<Missing> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId=passId;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_missing_item_frag, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Missing missing = dataset.get(position);
        holder.textViewItemName.setText(missing.getSystemLocationName());
        holder.textViewLoccode.setText(missing.getScanLocationName());
        holder.textViewSerialNo.setText(missing.getSerialNumber());





    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewSerialNo;
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
            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            locatecheckbox = itemView.findViewById(R.id.checkBox);
            textViewSerialNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passId.passid(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(),dataset.get(getBindingAdapterPosition()).getScanLocationID());
                }
            });


        }
    }

    public interface PassId {
        void passid(String id,Integer locid);
    }


}