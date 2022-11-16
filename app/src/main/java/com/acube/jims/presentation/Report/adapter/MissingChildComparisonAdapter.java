package com.acube.jims.presentation.Report.adapter;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.databinding.ItemChildBinding;
import com.acube.jims.databinding.ItemChildOneBinding;
import com.acube.jims.databinding.LaytComparisonBinding;
import com.acube.jims.datalayer.models.missingcomp.MissinDetail;
import com.acube.jims.datalayer.models.missingcomp.MissingDetail;
import com.acube.jims.datalayer.models.missingcomp.MissingStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MissingChildComparisonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mCtx;
    int LAYOUT_ONE = 1, LAYOUT_TWO = 2;

    List<Integer> downloadList;
    private final List<MissinDetail> dataset;
    FragmentTransition fragmenttransition;

    public MissingChildComparisonAdapter(Context mCtx, List<MissinDetail> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        downloadList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return LAYOUT_TWO;
        else
            return LAYOUT_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == LAYOUT_ONE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_one, parent, false);
            viewHolder = new ProductViewHolder2(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
            viewHolder = new ProductViewHolder(view);
        }

        return viewHolder;


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MissinDetail responseInbound = dataset.get(position);

        if (holder.getItemViewType() == LAYOUT_ONE) {
            ProductViewHolder2 vaultItemHolder2 = (ProductViewHolder2) holder;
            vaultItemHolder2.textView.setText(responseInbound.getSerialNo());
            if (responseInbound.getStatus() == 0) {

                vaultItemHolder2.imageView.setImageResource(R.drawable.ic_error_svgrepo_com);

            } else {
                vaultItemHolder2.imageView.setImageResource(R.drawable.ic_right_svgrepo_com);

            }

        } else {
            ProductViewHolder vaultItemHolder = (ProductViewHolder) holder;

            if (responseInbound.getStatus() == 0) {

                vaultItemHolder.imageView.setImageResource(R.drawable.ic_error_svgrepo_com);

            } else {
                vaultItemHolder.imageView.setImageResource(R.drawable.ic_right_svgrepo_com);

            }
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemChildBinding binding;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imvstatus);

        }
    }

    class ProductViewHolder2 extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ProductViewHolder2(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvserial);
            imageView = itemView.findViewById(R.id.imvstatus);


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
