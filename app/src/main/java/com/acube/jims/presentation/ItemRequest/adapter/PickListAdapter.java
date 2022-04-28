package com.acube.jims.presentation.ItemRequest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.ItemRequest.ResponseFetchPickList;
import com.acube.jims.presentation.ItemRequest.view.ItemRequestDetailActivity;
import com.acube.jims.presentation.ItemRequest.view.ItemRequestPickListDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class PickListAdapter extends RecyclerView.Adapter<PickListAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<ResponseFetchPickList> pickLists;
    FragmentTransition fragmenttransition;

    public PickListAdapter(Context mCtx, List<ResponseFetchPickList> pickLists, FragmentTransition fragmenttransition) {
        this.mCtx = mCtx;
        this.pickLists = pickLists;
        this.fragmenttransition = fragmenttransition;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_picklist_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseFetchPickList responseFetchPickList = pickLists.get(position);

        holder.textViewPicklistNo.setText("Picklist No : " + responseFetchPickList.getItemRequestNo());
        holder.TrayID.setText("Tray ID : " + responseFetchPickList.getTrayID());

    }


    @Override
    public int getItemCount() {
        return pickLists.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPicklistNo, TrayID;
        Button btnview;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewPicklistNo = itemView.findViewById(R.id.tv_picklistno);
            TrayID = itemView.findViewById(R.id.tv_trayID);

            btnview = itemView.findViewById(R.id.btnview);
            btnview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmenttransition.replaceFragment(pickLists.get(getAbsoluteAdapterPosition()).getItemRequestNo());


                }
            });
        }
    }

    public interface FragmentTransition {
        void replaceFragment(String id);

    }
}
