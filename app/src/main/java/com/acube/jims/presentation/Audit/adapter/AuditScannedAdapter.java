package com.acube.jims.presentation.Audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.acube.jims.R;
import com.acube.jims.databinding.LayoutAuditScannedItemBinding;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;

import java.util.ArrayList;
import java.util.List;

public class AuditScannedAdapter extends RecyclerView.Adapter<AuditScannedAdapter.ProductViewHolder> {


    private Context mCtx;

    List<Integer> downloadList;
    private final List<AuditSnapShot> dataset;
    FragmentTransition fragmenttransition;
    int locationID;

    public AuditScannedAdapter(Context mCtx, List<AuditSnapShot> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    public AuditScannedAdapter(Context mCtx, List<AuditSnapShot> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        downloadList = new ArrayList<>();


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_inbound_item, parent, false);*/
        LayoutAuditScannedItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_audit_scanned_item, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        AuditSnapShot responseStockDetails = dataset.get(position);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        int Status = responseStockDetails.getStatus();
        int scannedlocID = getValueOrDefault(responseStockDetails.getScannedLocationId(), 0);
        if (scannedlocID != 0) {

            if (locationID == scannedlocID) {
                if (responseStockDetails.getLocationId() != 0) {
                    if (responseStockDetails.getLocationId() != scannedlocID) {
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            holder.binding.rellayout.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_mismatch : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                        } else {
                            holder.binding.rellayout.setBackground(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_mismatch : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                        }
                    } else {
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            holder.binding.rellayout.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                        } else {
                            holder.binding.rellayout.setBackground(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                        }
                    }
                } else {
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.binding.rellayout.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                    } else {
                        holder.binding.rellayout.setBackground(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                    }
                }


            } else {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.binding.rellayout.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                } else {
                    holder.binding.rellayout.setBackground(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
                }
            }
        } else {
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.binding.rellayout.setBackgroundDrawable(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
            } else {
                holder.binding.rellayout.setBackground(ContextCompat.getDrawable(mCtx, Status == 0 ? R.drawable.border_notscanned : Status == 1 ? R.drawable.border_scanned : Status == 2 ? R.drawable.border_unknown : R.drawable.border_mismatch));
            }
        }


        holder.binding.tvScannedserialnumber.setText(responseStockDetails.getSerialNumber());
        holder.binding.tvLoc.setText(responseStockDetails.getWeight()+" g");
        holder.binding.tvProductname.setText(responseStockDetails.getItemCode());
        holder.binding.tvCategory.setText(responseStockDetails.getSubCategoryName());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        LayoutAuditScannedItemBinding binding;
        ImageView imageView;

        public ProductViewHolder(LayoutAuditScannedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

        void passlist(List<Integer> dataset);

        void scanaction(int docID);

    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
