package com.acube.jims.presentation.Audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.acube.jims.R;
import com.acube.jims.databinding.ItemAssetAuditBinding;
import com.acube.jims.datalayer.remote.db.LocalAudit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<LocalAudit> dataset;
    FragmentTransition fragmenttransition;

    public AuditAdapter(Context mCtx, List<LocalAudit> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_inbound_item, parent, false);*/
        ItemAssetAuditBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_asset_audit, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        LocalAudit responseInbound = dataset.get(position);
        holder.binding.requestByValueTextView.setText("" + responseInbound.getAuditID());
        holder.binding.tvremark.setText("" + responseInbound.getRemark());
        try {
            holder.binding.requestDateTextView.setText(formatDate(responseInbound.getToBeAuditedOn()));

        }catch (Exception e){

        }
        // holder.binding.tvBinRef.setText("" + responseInbound.getb);
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemAssetAuditBinding binding;
        ImageView imageView;

        public ProductViewHolder(ItemAssetAuditBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmenttransition.scanaction(dataset.get(getAbsoluteAdapterPosition()).getAuditID(),"","");

                }
            });

        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

        void passlist(List<Integer> dataset);

        void scanaction(String auditId, String toBeAuditedOn,String remark);

    }
    public  String formatDate(String dateString){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        return fmtOut.format(date);
    }

}
