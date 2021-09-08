package com.acube.jims.Presentation.Quotation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ProductViewHolder> {


    private Context mCtx;
    int itemquantity = 1;
    List<ResponseInvoiceList> dataset;
    UpdateQuantity updateQuantity;
    DeleteProduct deleteProduct;
    double pricewithtax = 0.0;
    double labourchargewithtax = 0.0;
    double totalamountwithalltax = 0.0;
    double minpercentage = 0.0;
    double maxpercentage = 0.0;


    public InvoiceAdapter(Context mCtx, List<ResponseInvoiceList> dataset, UpdateQuantity updateQuantity, DeleteProduct deleteProduct) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.updateQuantity = updateQuantity;
        this.deleteProduct = deleteProduct;
    }

    public InvoiceAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    public InvoiceAdapter(Context mCtx, List<ResponseInvoiceList> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_invoice_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ResponseInvoiceList cartDetail = dataset.get(position);
        holder.textViewitemName.setText(cartDetail.getItemName());
        holder.textViewSerialno.setText(cartDetail.getSerialNumber());
        labourchargewithtax = (cartDetail.getLabourCharge() / 100.0f) * cartDetail.getLabourTax() + cartDetail.getLabourCharge();
        Log.d("Labourcharge", "onBindViewHolder: " + labourchargewithtax);

        pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
        totalamountwithalltax = pricewithtax + labourchargewithtax;
        minpercentage = cartDetail.getLabourChargeMin();
        maxpercentage = cartDetail.getLabourChargeMax();
        holder.textViewPrice.setText("SAR " + totalamountwithalltax);
        holder.textViewlabourcharge.setText("SAR " + cartDetail.getLabourCharge());

        holder.textViewTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase("")) {
                    double enteredvalue = Double.parseDouble(s.toString());
                    if (enteredvalue > maxpercentage) {
                        Toast.makeText(mCtx, "Discount Percentage Exceeded", Toast.LENGTH_SHORT).show();
                    } else if (enteredvalue < minpercentage) {
                        Toast.makeText(mCtx, "Discount Percentage Minimum", Toast.LENGTH_SHORT).show();
                    } else {
                        double labrchrgdiscount = (cartDetail.getLabourCharge() / 100.0f) *enteredvalue;
                        double lbr = cartDetail.getLabourCharge() - labrchrgdiscount;
                        double  labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
                        double   totalamountwithalltax = pricewithtax + labourchargewithtax;
                        holder.textViewPrice.setText("SAR " + totalamountwithalltax);
                    }
                }else{
                    labourchargewithtax = (cartDetail.getLabourCharge() / 100.0f) * cartDetail.getLabourTax() + cartDetail.getLabourCharge();
                    Log.d("Labourcharge", "onBindViewHolder: " + labourchargewithtax);

                    pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
                    totalamountwithalltax = pricewithtax + labourchargewithtax;
                    minpercentage = cartDetail.getLabourChargeMin();
                    maxpercentage = cartDetail.getLabourChargeMax();
                    holder.textViewPrice.setText("SAR " + totalamountwithalltax);
                }

                // TODO Auto-generated method stub
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewQuantity, textViewitemName, textViewWeight, textViewStoneweight, textViewSerialno, textViewPrice, textViewlabourcharge;
        ImageView imageViewadd, imageViewremove, ItemImage, imageviewdelete;
        EditText textViewTax;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewitemName = itemView.findViewById(R.id.tv_itemname);
            textViewSerialno = itemView.findViewById(R.id.tv_serialnumber);


            textViewPrice = itemView.findViewById(R.id.tvprice);
            textViewlabourcharge = itemView.findViewById(R.id.tvlabourcharge);

            textViewTax = itemView.findViewById(R.id.tvtax);



          /*  imageviewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                   // deleteProduct.removefromcart(String.valueOf(dataset.get(pos).getItemID()), String.valueOf(dataset.get(pos).getQty()), dataset.get(pos).getSerialNumber());

                }
            });*/

            /*imageViewadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemquantity = Integer.parseInt(textViewQuantity.getText().toString());
                    itemquantity = itemquantity + 1;
                    textViewQuantity.setText(String.valueOf(itemquantity));
                    updateQuantity.updatevalue(String.valueOf(dataset.get(getAdapterPosition()).getItemID()), String.valueOf(itemquantity));

                }
            });
            imageViewremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemquantity = Integer.parseInt(textViewQuantity.getText().toString());
                    if (itemquantity > 1) {
                        itemquantity = itemquantity - 1;
                        textViewQuantity.setText(String.valueOf(itemquantity));
                        updateQuantity.updatevalue(String.valueOf(dataset.get(getAdapterPosition()).getItemID()), String.valueOf(itemquantity));
                    }

                }
            });*/


        }
    }

    public interface UpdateQuantity {
        void updatevalue(String itemid, String quantity);
    }

    public interface DeleteProduct {
        void removefromcart(String itemid, String quantity, String serialno);

    }
}