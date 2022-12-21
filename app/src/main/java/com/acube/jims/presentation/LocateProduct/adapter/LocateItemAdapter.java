package com.acube.jims.presentation.LocateProduct.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.presentation.reading.LocateScanActivity;
import com.acube.jims.utils.OnSingleClickListener;

import java.util.List;

public class LocateItemAdapter extends RecyclerView.Adapter<LocateItemAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<LocateItem> dataset;
    FragmentTransition fragmenttransition;


    public LocateItemAdapter(Context mCtx, List<LocateItem> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;

    }

    public LocateItemAdapter(Context mCtx, List<LocateItem> dataset, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_locate_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.textViewTitle.setText(dataset.get(position).getSerialnumber());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_serialnumber);
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    fragmenttransition.replaceFragment(dataset.get(getAbsoluteAdapterPosition()).getSerialnumber());

                }
            });


        }
    }

    public interface FragmentTransition {
        void replaceFragment(String serial);

    }
}
