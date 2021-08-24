package com.acube.jims.Presentation.Compare.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CompareItemsAdapter extends RecyclerView.Adapter<CompareItemsAdapter.ProductViewHolder> {


    private Context mCtx;




    public CompareItemsAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_compare_item, parent, false);
        view.getLayoutParams().width = (int) (getScreenWidth() / 3); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {



    }


    @Override
    public int getItemCount() {
        return 10;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
}