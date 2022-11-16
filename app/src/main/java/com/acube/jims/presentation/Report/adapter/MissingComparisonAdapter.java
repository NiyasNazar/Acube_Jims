package com.acube.jims.presentation.Report.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.databinding.ItemAssetAuditBinding;
import com.acube.jims.databinding.LaytComparisonBinding;
import com.acube.jims.datalayer.models.missingcomp.MissinDetail;
import com.acube.jims.datalayer.models.missingcomp.MissinSerialNo;
import com.acube.jims.datalayer.models.missingcomp.MissingCompResult;
import com.acube.jims.datalayer.models.missingcomp.MissingDetail;
import com.acube.jims.datalayer.models.missingcomp.MissingStatus;
import com.acube.jims.datalayer.models.missingcomp.ResponseMissingComp;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.acube.jims.utils.SpanningLinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MissingComparisonAdapter extends RecyclerView.Adapter<MissingComparisonAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<MissinSerialNo> dataset;
    FragmentTransition fragmenttransition;

    public MissingComparisonAdapter(Context mCtx, List<MissinSerialNo> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_inbound_item, parent, false);*/
        LaytComparisonBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layt_comparison, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        MissinSerialNo responseInbound = dataset.get(position);
        holder.binding.tvserial.setText("" + responseInbound.getSerialNo());
        List<MissinDetail> status = responseInbound.getMissinDetails();
        holder.binding.recyvcomp.setAdapter(new MissingChildComparisonAdapter(mCtx, status));







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
        LaytComparisonBinding binding;
        ImageView imageView;

        public ProductViewHolder(LaytComparisonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
          binding.recyvcomp.setLayoutManager(new GridLayoutManager(mCtx,5));


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
