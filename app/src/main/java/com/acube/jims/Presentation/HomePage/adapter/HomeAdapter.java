package com.acube.jims.Presentation.HomePage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<HomeData> homemenuList;


    public HomeAdapter(Context mCtx, List<HomeData> homemenuList) {
        this.mCtx = mCtx;
        this.homemenuList = homemenuList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_home_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        HomeData homeData = homemenuList.get(position);
        holder.textViewTitle.setText(homeData.getName());
        holder.imageView.setImageResource(homeData.getImage());

    }


    @Override
    public int getItemCount() {
        return homemenuList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvhomemenu);

            imageView = itemView.findViewById(R.id.imvhomeicon);
        }
    }
}
