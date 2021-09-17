package com.acube.jims.Presentation.Quotation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
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
    double calucalteddiscount = 0.0;
    double labrchrgdiscount = 0.0;
    List<DiscountItem> discountdata;
    DiscountSum discountSum;

    public InvoiceAdapter(Context mCtx, List<ResponseInvoiceList> dataset, UpdateQuantity updateQuantity, DeleteProduct deleteProduct) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.updateQuantity = updateQuantity;
        this.deleteProduct = deleteProduct;

    }

    public InvoiceAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    public InvoiceAdapter(Context mCtx, List<ResponseInvoiceList> dataset, DiscountSum discountSum) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.discountSum = discountSum;
        discountdata = new ArrayList<>();
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
        holder.Pricetvwithouttax.setText("" + cartDetail.getPriceWithoutTax());
        holder.textViewWeight.setText("" + cartDetail.getGoldWeight() + " g");
        labourchargewithtax = (cartDetail.getLabourCharge() / 100.0f) * cartDetail.getLabourTax() + cartDetail.getLabourCharge();
        Log.d("Labourcharge", "onBindViewHolder: " + labourchargewithtax);

        pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
        totalamountwithalltax = pricewithtax + labourchargewithtax;
        minpercentage = cartDetail.getLabourChargeMin();
        maxpercentage = cartDetail.getLabourChargeMax();
        holder.textViewPrice.setText("SAR " + totalamountwithalltax);
        holder.textViewlabourcharge.setText("SAR " + cartDetail.getLabourCharge());
       /* DiscountItem discountItem=new DiscountItem();
        discountItem.setItemserial(cartDetail.getSerialNumber());
        discountItem.setAmt(labrchrgdiscount);
        discountItem.setTotalwithtax(totalamountwithalltax);
        SaveItems(discountItem);*/

    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView Pricetvwithouttax, textViewitemName, textViewWeight, textViewStoneweight, textViewSerialno, textViewPrice, textViewlabourcharge;
        ImageView imageViewadd, imageViewremove, ItemImage, imageviewdelete;
        EditText textViewTax;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewitemName = itemView.findViewById(R.id.tv_itemname);
            textViewSerialno = itemView.findViewById(R.id.tv_serialnumber);
            Pricetvwithouttax = itemView.findViewById(R.id.tvwithouttax);
            textViewWeight = itemView.findViewById(R.id.tv_goldweight);

            textViewPrice = itemView.findViewById(R.id.tvprice);
            textViewlabourcharge = itemView.findViewById(R.id.tvlabourcharge);

            textViewTax = itemView.findViewById(R.id.tvtax);
            textViewTax.addTextChangedListener(new TextWatcher() {
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
                    ResponseInvoiceList cartDetail = dataset.get(getAbsoluteAdapterPosition());
                    if (!s.toString().equalsIgnoreCase("")) {
                        double enteredvalue = Double.parseDouble(s.toString());
                        // if (enteredvalue > maxpercentage) {
                        //    Toast.makeText(mCtx, "Discount Percentage Exceeded", Toast.LENGTH_SHORT).show();
                        // } else if (enteredvalue < minpercentage) {
                        //  Toast.makeText(mCtx, "Discount Percentage Minimum", Toast.LENGTH_SHORT).show();
                        //   } else {
                        labrchrgdiscount = (cartDetail.getLabourCharge() / 100.0f) * enteredvalue;
                        double lbr = cartDetail.getLabourCharge() - labrchrgdiscount;
                        double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
                        double totalamountwithalltax = pricewithtax + labourchargewithtax;
                        textViewPrice.setText("SAR " + totalamountwithalltax);
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setAmt(labrchrgdiscount);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);
                        discountSum.somofdiscount(labrchrgdiscount);

                        Log.d("afterTextChanged", "removed: " + labrchrgdiscount);


                        // }
                    } else {
                        labrchrgdiscount = (cartDetail.getLabourCharge() / 100.0f) * 0;
                        double lbr = cartDetail.getLabourCharge() - labrchrgdiscount;
                        double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double pricewithtax = (cartDetail.getPriceWithoutTax() / 100.0f) * cartDetail.getItemTax() + cartDetail.getPriceWithoutTax();
                        double totalamountwithalltax = pricewithtax + labourchargewithtax;
                        minpercentage = cartDetail.getLabourChargeMin();
                        maxpercentage = cartDetail.getLabourChargeMax();
                        textViewPrice.setText("SAR " + totalamountwithalltax);
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setAmt(0.0);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);
                        discountSum.somofdiscount(0.0);
                    }

                    // TODO Auto-generated method stub
                }
            });


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

    public interface DiscountSum {
        void somofdiscount(double sum);
    }

    private void SaveItems(DiscountItem items) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .discountItemsDao()
                        .insert(items);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    private void Delete(DiscountItem items) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .discountItemsDao()
                        .delete(items);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }
}