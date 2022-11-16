package com.acube.jims.presentation.Report.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.databinding.LaytComparisonBinding;
import com.acube.jims.databinding.LaytComparisonDateBinding;
import com.acube.jims.datalayer.models.missingcomp.AuditDate;
import com.acube.jims.datalayer.models.missingcomp.MissinDetail;
import com.acube.jims.datalayer.models.missingcomp.MissinSerialNo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MissingDateAdapter extends RecyclerView.Adapter<MissingDateAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<AuditDate> dataset;
    FragmentTransition fragmenttransition;

    public MissingDateAdapter(Context mCtx, List<AuditDate> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_inbound_item, parent, false);*/
        LaytComparisonDateBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layt_comparison_date, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        AuditDate responseInbound = dataset.get(position);
        holder.binding.tvserial.setText("" + formatDate(responseInbound.getToBeAuditedOn()));







        /*try {
            holder.binding.requestDateTextView.setText(formatDate(responseInbound.getToBeAuditedOn()));

        }catch (Exception e){

        }*/
        // holder.binding.tvBinRef.setText("" + responseInbound.getb);
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        LaytComparisonDateBinding binding;
        ImageView imageView;

        public ProductViewHolder(LaytComparisonDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

        void passlist(List<Integer> dataset);

        void scanaction(String auditId, String toBeAuditedOn, String remark);

    }

    public String formatDate(String dateString) {
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
