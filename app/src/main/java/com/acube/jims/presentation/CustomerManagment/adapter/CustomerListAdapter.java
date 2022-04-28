package com.acube.jims.presentation.CustomerManagment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ProductViewHolder> {


    private Context mCtx;

    ReplaceFragment replaceFragment;
    private final List<ResponseCustomerListing> customerListings;


    public CustomerListAdapter(Context mCtx, List<ResponseCustomerListing> customerListings, ReplaceFragment replaceFragment) {
        this.mCtx = mCtx;
        this.customerListings = customerListings;
        this.replaceFragment = replaceFragment;
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
        holder.textViewCustomername.setText(responseCustomerListing.getCustomerName());
        holder.textViewEmail.setText(responseCustomerListing.getEmailID());
        holder.textViewContact.setText(responseCustomerListing.getContactNumber());


    }


    @Override
    public int getItemCount() {
        return customerListings.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCustomername, textViewEmail, textViewContact;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewCustomername = itemView.findViewById(R.id.tv_customername);
            textViewEmail = itemView.findViewById(R.id.tv_email);

            textViewContact = itemView.findViewById(R.id.textViewContact);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    LocalPreferences.storeIntegerPreference(mCtx, "GuestCustomerID", customerListings.get(pos).getId());
                    LocalPreferences.storeStringPreference(mCtx, "custname", customerListings.get(pos).getCustomerName());
                    LocalPreferences.storeStringPreference(mCtx, "custcode", customerListings.get(pos).getCustomerCode());
                    replaceFragment.replacefragments();
                }
            });


        }
    }

    public interface ReplaceFragment {
        void replacefragments();
    }


}
