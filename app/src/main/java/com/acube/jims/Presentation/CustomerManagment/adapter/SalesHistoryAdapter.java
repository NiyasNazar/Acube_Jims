package com.acube.jims.Presentation.CustomerManagment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.CustomerManagment.ItemViewHistory;
import com.acube.jims.datalayer.models.CustomerManagment.SalesHistory;
import com.bumptech.glide.Glide;

import java.util.List;

public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.ProductViewHolder> {


    private Context mCtx;

    ReplaceFragment replaceFragment;
    private final List<SalesHistory> itemViewHistoryList;


    public SalesHistoryAdapter(Context mCtx, List<SalesHistory> itemViewHistoryList, ReplaceFragment replaceFragment) {
        this.mCtx = mCtx;
        this.itemViewHistoryList = itemViewHistoryList;
        this.replaceFragment = replaceFragment;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_lastviewed_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        SalesHistory responseListing = itemViewHistoryList.get(position);
        Glide.with(mCtx).load(responseListing.getImagePath()).into(holder.imageView);
        holder.textViewSerialNo.setText("Sl No. " + responseListing.getSerialNumber());
        holder.textViewName.setText(responseListing.getItemName());

        holder.textViewWeight.setText("Weight " + responseListing.getGoldWeight() + " g");

    }


    @Override
    public int getItemCount() {
        return itemViewHistoryList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWeight, textViewSerialNo, textViewName;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imvitemimage);
            textViewName = itemView.findViewById(R.id.tv_itemname);

            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            textViewWeight = itemView.findViewById(R.id.tv_weight);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //  LocalPreferences.storeStringPreference(mCtx, "GuestCustomerID", String.valueOf(customerListings.get(pos).getId()));


                    replaceFragment.replacefragments(itemViewHistoryList.get(pos).getSerialNumber());
                }
            });


        }
    }

    public interface ReplaceFragment {
        void replacefragments(String id);
    }


}
