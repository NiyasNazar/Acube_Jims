package com.acube.jims.presentation.HomePage.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DecimalFormat;
import java.util.List;

public class GoldRateDapter extends RecyclerView.Adapter<GoldRateDapter.ProductViewHolder> {


    private Context mCtx;


    private final List<KaratPrice> homemenuList;
    FragmentTransition fragmenttransition;

    public GoldRateDapter(Context mCtx, List<KaratPrice> homemenuList) {
        this.mCtx = mCtx;
        this.homemenuList = homemenuList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_goldrate_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        KaratPrice karatPrice = homemenuList.get(position);
        Double karat = Double.valueOf(karatPrice.getKaratCode());
        DecimalFormat format = new DecimalFormat("0.#");

        holder.textViewTitle.setText(""+format.format(karat)+" K - "+karatPrice.getKaratPrice());

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

            textViewTitle = itemView.findViewById(R.id.tvgoldrate);


        }
    }

    public interface FragmentTransition {
        void replaceFragment(String menuname);

    }
}
