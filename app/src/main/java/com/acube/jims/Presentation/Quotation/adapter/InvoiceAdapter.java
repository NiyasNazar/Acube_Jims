package com.acube.jims.Presentation.Quotation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
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
    double calculatedlabourcharge = 0.0;
    double goldweighEnteredvalue = 0.0;

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
        if (cartDetail.getGoldWeight()==0){
            holder.edViewWeight.setEnabled(false);
        }else{
            holder.edViewWeight.setEnabled(true);

        }
        holder.textViewitemName.setText(cartDetail.getItemName());
        holder.textViewSerialno.setText(cartDetail.getSerialNumber());
        holder.Pricetvwithouttax.setText("" + cartDetail.getPriceWithoutTax());
        holder.edViewWeight.setText("" + cartDetail.getGoldWeight());
        double labourcharge = cartDetail.getLabourCharge() * cartDetail.getGoldWeight();
        labourchargewithtax = (labourcharge / 100.0f) * cartDetail.getLabourTax() + labourcharge;

        Log.d("Labourcharge", "onBindViewHolder: " + labourchargewithtax);
        double goldprice = ((cartDetail.getPriceWithoutTax() / 1000) * (cartDetail.getGoldWeight() * 1000));
        pricewithtax = (goldprice / 100.0f) * cartDetail.getItemTax() + goldprice;
        totalamountwithalltax = pricewithtax + labourchargewithtax;
        minpercentage = cartDetail.getLabourChargeMin();
        maxpercentage = cartDetail.getLabourChargeMax();
        holder.textViewPrice.setText("SAR " + totalamountwithalltax);
        holder.textViewItemKarat.setText(cartDetail.getKaratCode());
        holder.textViewlabourcharge.setText("SAR " + cartDetail.getLabourCharge() * cartDetail.getGoldWeight());
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

        TextView Pricetvwithouttax, textViewitemName, textViewItemKarat, textViewSerialno, textViewPrice, textViewlabourcharge;
        ImageView imageViewadd, imageViewremove, ItemImage, imageviewdelete;
        EditText edDiscount, edViewWeight;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewitemName = itemView.findViewById(R.id.tv_itemname);
            textViewSerialno = itemView.findViewById(R.id.tv_serialnumber);
            Pricetvwithouttax = itemView.findViewById(R.id.tvwithouttax);
            edViewWeight = itemView.findViewById(R.id.tv_goldweight);
            textViewItemKarat= itemView.findViewById(R.id.tv_itemkarat);
            textViewPrice = itemView.findViewById(R.id.tvprice);
            textViewlabourcharge = itemView.findViewById(R.id.tvlabourcharge);

            edDiscount = itemView.findViewById(R.id.tvdiscount);
            edDiscount.addTextChangedListener(new TextWatcher() {
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
                        if (cartDetail.getGoldWeight() == 0) {
                            double goldprice = (cartDetail.getPriceWithoutTax());

                            // if (enteredvalue > maxpercentage) {
                            //    Toast.makeText(mCtx, "Discount Percentage Exceeded", Toast.LENGTH_SHORT).show();
                            // } else if (enteredvalue < minpercentage) {
                            //  Toast.makeText(mCtx, "Discount Percentage Minimum", Toast.LENGTH_SHORT).show();
                            //   } else {
                            labrchrgdiscount = (goldprice / 100.0f) * enteredvalue;
                            double discountedprice = goldprice - labrchrgdiscount;
                           // double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;


                         //   double labourtax = (lbr / 100.0f) * cartDetail.getLabourTax();
                            double itemtax = (discountedprice / 100.0f) * cartDetail.getItemTax();
                          //  double totaltax = labourtax + itemtax;
                            double pricewithtax = (discountedprice / 100.0f) * cartDetail.getItemTax() + discountedprice;
                           // double totalamountwithalltax = pricewithtax ;

                            textViewPrice.setText("SAR " + pricewithtax);
                            Log.d("diamonf", "afterTextChanged: ");
                            DiscountItem discountItem = new DiscountItem();
                            discountItem.setItemserial(cartDetail.getSerialNumber());
                            discountItem.setDiscount(labrchrgdiscount);
                            discountItem.setTotaltax(itemtax);
                            discountItem.setItemID(cartDetail.getItemID());
                            discountItem.setDiscountpercentage(enteredvalue);
                            discountItem.setItemVat(cartDetail.getGoldVat());
                            discountItem.setLaborRate(0.0);
                            discountItem.setItemVatAmount(itemtax);
                            discountItem.setGoldweight(goldweighEnteredvalue);

                            discountItem.setLabourVat(cartDetail.getLaborVat());
                            discountItem.setLabourVatAmount(0.0);


                            discountItem.setAmt(discountedprice);
                            discountItem.setTotalwithtax(pricewithtax);
                            SaveItems(discountItem);
                            discountSum.somofdiscount(labrchrgdiscount);

                        } else {



                        calculatedlabourcharge = cartDetail.getLabourCharge() * goldweighEnteredvalue;
                        // if (enteredvalue > maxpercentage) {
                        //    Toast.makeText(mCtx, "Discount Percentage Exceeded", Toast.LENGTH_SHORT).show();
                        // } else if (enteredvalue < minpercentage) {
                        //  Toast.makeText(mCtx, "Discount Percentage Minimum", Toast.LENGTH_SHORT).show();
                        //   } else {
                        labrchrgdiscount = (calculatedlabourcharge / 100.0f) * enteredvalue;
                        double lbr = calculatedlabourcharge - labrchrgdiscount;
                        double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double goldprice = ((cartDetail.getPriceWithoutTax() / 1000) * (goldweighEnteredvalue * 1000));

                        double labourtax = (lbr / 100.0f) * cartDetail.getLabourTax();
                        double itemtax = (goldprice / 100.0f) * cartDetail.getItemTax();
                        double totaltax = labourtax + itemtax;
                        double pricewithtax = (goldprice / 100.0f) * cartDetail.getItemTax() + goldprice;
                        double totalamountwithalltax = pricewithtax + labourchargewithtax;

                        textViewPrice.setText("SAR " + totalamountwithalltax);
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setDiscount(labrchrgdiscount);
                        discountItem.setTotaltax(totaltax);
                        discountItem.setItemID(cartDetail.getItemID());
                        discountItem.setDiscountpercentage(enteredvalue);
                        discountItem.setItemVat(cartDetail.getGoldVat());
                        discountItem.setLaborRate(lbr);
                        discountItem.setItemVatAmount(itemtax);
                        discountItem.setGoldweight(goldweighEnteredvalue);

                        discountItem.setLabourVat(cartDetail.getLaborVat());
                        discountItem.setLabourVatAmount(labourtax);


                        discountItem.setAmt(goldprice + calculatedlabourcharge);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);
                        discountSum.somofdiscount(labrchrgdiscount);

                        // Log.d("niyastesting", "removed: " + s.toString());
                        Log.d("niyastesting", "disc: " + s.toString());
                    }
                        // }
                    } else {
                        double goldprice = ((cartDetail.getPriceWithoutTax() / 1000) * (goldweighEnteredvalue * 1000));
                        Log.d("niyastesting", "disc: " + s.toString());

                        calculatedlabourcharge = cartDetail.getLabourCharge() * goldweighEnteredvalue;
                        labrchrgdiscount = (calculatedlabourcharge / 100.0f) * 0;
                        double lbr = calculatedlabourcharge - labrchrgdiscount;
                        double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double pricewithtax = (goldprice / 100.0f) * cartDetail.getItemTax() + goldprice;
                        double totalamountwithalltax = pricewithtax + labourchargewithtax;
                        minpercentage = cartDetail.getLabourChargeMin();
                        maxpercentage = cartDetail.getLabourChargeMax();
                        double labourtax = (lbr / 100.0f) * cartDetail.getLabourTax();
                        double itemtax = (goldprice / 100.0f) * cartDetail.getItemTax();
                        double totaltax = labourtax + itemtax;
                        textViewPrice.setText("SAR " + totalamountwithalltax);
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setAmt(goldprice + calculatedlabourcharge);
                        discountItem.setTotaltax(totaltax);
                        discountItem.setDiscount(0.0);
                        discountItem.setItemVatAmount(itemtax);
                        discountItem.setGoldweight(goldweighEnteredvalue);
                        discountItem.setItemID(cartDetail.getItemID());
                        discountItem.setDiscountpercentage(0.0);
                        discountItem.setItemVat(cartDetail.getGoldVat());
                        discountItem.setLabourVat(cartDetail.getLaborVat());
                        discountItem.setLabourVatAmount(labourtax);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);
                        discountSum.somofdiscount(0.0);
                    }


                    // TODO Auto-generated method stub
                }
            });

            edViewWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    edViewWeight.setError(null);
                    ResponseInvoiceList cartDetail = dataset.get(getAbsoluteAdapterPosition());
                    if (!s.toString().equalsIgnoreCase("")) {
                        if (cartDetail.getGoldWeight()==0){

                            double calculation = (cartDetail.getPriceWithoutTax() );
                            Log.d("beforeTextChanged", "beforeTextChanged: " + calculation);
                            Log.d("niyastesting", "weight: " + s.toString());
                          //  calculatedlabourcharge = cartDetail.getLabourCharge() * goldweighEnteredvalue;
                          //  textViewlabourcharge.setText("SAR " + calculatedlabourcharge);
                            Log.d("calculatedlabourcharge", "calculatedlabourcharge: " + calculatedlabourcharge);
                          //  labrchrgdiscount = (calculatedlabourcharge / 100.0f) * 0;
                         ////   double lbr = calculatedlabourcharge - labrchrgdiscount;
                         //   double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                            double pricewithtax = (calculation / 100.0f) * cartDetail.getItemTax() + calculation;
                         //   double labourtax = (lbr / 100.0f) * cartDetail.getLabourTax();
                            double itemtax = (calculation / 100.0f) * cartDetail.getItemTax();
                        //    double totaltax = labourtax + itemtax;
                            double totalamountwithalltax = pricewithtax + labourchargewithtax;
                            minpercentage = cartDetail.getLabourChargeMin();
                            maxpercentage = cartDetail.getLabourChargeMax();
                            textViewPrice.setText("SAR " + totalamountwithalltax);
                            DiscountItem discountItem = new DiscountItem();
                            discountItem.setItemserial(cartDetail.getSerialNumber());
                            discountItem.setDiscount(labrchrgdiscount);
                            discountItem.setTotaltax(itemtax);
                            discountItem.setItemID(cartDetail.getItemID());
                            discountItem.setDiscountpercentage(0);
                            discountItem.setGoldweight(0.0);
                            discountItem.setItemVat(cartDetail.getGoldVat());
                            discountItem.setDiscount(0);
                            discountItem.setItemVatAmount(itemtax);
                            discountItem.setLabourVat(cartDetail.getLaborVat());
                            discountItem.setLabourVatAmount(0.0);


                            discountItem.setAmt(calculation + calculatedlabourcharge);
                            discountItem.setTotalwithtax(totalamountwithalltax);
                            SaveItems(discountItem);
                            discountSum.somofdiscount(labrchrgdiscount);
                        }else{



                        goldweighEnteredvalue = Double.parseDouble(s.toString());
                        double calculation = ((cartDetail.getPriceWithoutTax() / 1000) * (goldweighEnteredvalue * 1000));
                        Log.d("beforeTextChanged", "beforeTextChanged: " + calculation);
                        Log.d("niyastesting", "weight: " + s.toString());
                        calculatedlabourcharge = cartDetail.getLabourCharge() * goldweighEnteredvalue;
                        textViewlabourcharge.setText("SAR " + calculatedlabourcharge);
                        Log.d("calculatedlabourcharge", "calculatedlabourcharge: " + calculatedlabourcharge);
                        labrchrgdiscount = (calculatedlabourcharge / 100.0f) * 0;
                        double lbr = calculatedlabourcharge - labrchrgdiscount;
                        double labourchargewithtax = (lbr / 100.0f) * cartDetail.getLabourTax() + lbr;
                        double pricewithtax = (calculation / 100.0f) * cartDetail.getItemTax() + calculation;
                        double labourtax = (lbr / 100.0f) * cartDetail.getLabourTax();
                        double itemtax = (calculation / 100.0f) * cartDetail.getItemTax();
                        double totaltax = labourtax + itemtax;
                        double totalamountwithalltax = pricewithtax + labourchargewithtax;
                        minpercentage = cartDetail.getLabourChargeMin();
                        maxpercentage = cartDetail.getLabourChargeMax();
                        textViewPrice.setText("SAR " + totalamountwithalltax);
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setDiscount(labrchrgdiscount);
                        discountItem.setTotaltax(totaltax);
                        discountItem.setItemID(cartDetail.getItemID());
                        discountItem.setDiscountpercentage(0);
                        discountItem.setGoldweight(goldweighEnteredvalue);
                        discountItem.setItemVat(cartDetail.getGoldVat());
                        discountItem.setDiscount(0);
                        discountItem.setItemVatAmount(itemtax);
                        discountItem.setLabourVat(cartDetail.getLaborVat());
                        discountItem.setLabourVatAmount(labourtax);


                        discountItem.setAmt(calculation + calculatedlabourcharge);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);
                        discountSum.somofdiscount(labrchrgdiscount);
                        }

                    } else {
                        edViewWeight.setError("Weight cannot be Empty");
                        discountSum.somofdiscount(labrchrgdiscount);
                        double enteredvalue = 1.0;
                        double calculation = ((cartDetail.getPriceWithoutTax() / 1000) * (enteredvalue * 1000));
                        Log.d("beforeTextChanged", "removed: " + calculation);
                        double labourtax = (cartDetail.getLabourCharge() / 100.0f) * cartDetail.getLabourTax();
                        double itemtax = (calculation / 100.0f) * cartDetail.getItemTax();
                        double totaltax = labourtax + itemtax;
                        DiscountItem discountItem = new DiscountItem();
                        discountItem.setItemserial(cartDetail.getSerialNumber());
                        discountItem.setTotaltax(totaltax);
                        Log.d("niyastesting", "weights: " + s.toString());
                        discountItem.setAmt(calculation + calculatedlabourcharge);
                        discountItem.setDiscount(0.0);
                        discountItem.setTotalwithtax(totalamountwithalltax);
                        SaveItems(discountItem);

                    }
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