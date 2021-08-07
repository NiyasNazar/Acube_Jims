package com.acube.jims.Presentation.HomePage.adapter;

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
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ProductViewHolder> {


    private Context mCtx;


    private final List<ResponseCustomerListing> customerListings;


    public CustomerListAdapter(Context mCtx, List<ResponseCustomerListing> customerListings) {
        this.mCtx = mCtx;
        this.customerListings = customerListings;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_customer_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ResponseCustomerListing responseCustomerListing = customerListings.get(position);
        holder.textViewContact.setText(responseCustomerListing.getCustomerName());
        holder.textViewEmail.setText(responseCustomerListing.getEmailID());
        holder.textViewEmail.setText(responseCustomerListing.getContactNumber());


    }


    @Override
    public int getItemCount() {
        return customerListings.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCustomername,textViewEmail,textViewContact;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewCustomername = itemView.findViewById(R.id.tv_customername);
            textViewEmail = itemView.findViewById(R.id.tv_email);

            textViewContact = itemView.findViewById(R.id.textViewContact);



        }
    }


}
