package com.acube.jims.presentation.Quotation;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseActivity;
import com.acube.jims.BaseFragment;
import com.acube.jims.presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.presentation.Quotation.adapter.InvoiceAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.InvoiceFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.models.Invoice.SaleSuccess;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceFragment extends BaseActivity implements InvoiceAdapter.DiscountSum {

    private InvoiceViewModel mViewModel;
    RecyclerView mRecyinvoice;
    SaleViewModel saleViewModel;

    public static InvoiceFragment newInstance() {
        return new InvoiceFragment();
    }

    InvoiceFragmentBinding binding;
    double total = 0.0;

    double totalItemtax = 0.0;
    double netamount = 0.0;
    double discount = 0.0;
    //  List<ResponseInvoiceList> dataset;
    double labourchargewithtax = 0.0;
    double labourchargetax = 0.0;
    double labourcharge = 0.0;
    List<DiscountItem> datasetItems;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.invoice_fragment);

        datasetItems = new ArrayList<>();

        binding.recyvinvoiceitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerName");
        String CustomerMobile = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CustomerMobile");
        binding.tvcustomername.setText("Customer Name : " + Customername);
        binding.tvcustomercontactnumber.setText("Contact Number: " + CustomerMobile);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        saleViewModel.init();
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        binding.tvdate.setText(date);
//        dataset = new ArrayList();


        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), PdfCreatorExampleActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                // FragmentHelper.replaceFragment(getActivity(),R.id.content,new PdfCreatorExampleActivity());
            }
        });

        mViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        mViewModel.init();
        ;
        JsonArray jsonObject = new JsonArray();
        String[] datset = {"10001", "10002"};
        List<CartDetail> cartdata = getList();

        DatabaseClient.getInstance(InvoiceFragment.this).getAppDatabase().scannedItemsDao().getFromSmarttool().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {


                String[] strArray = strings.toArray(new String[strings.size()]);
                mViewModel.FetchInvoice(LocalPreferences.getToken(getApplicationContext()), strArray,getApplicationContext());

            }
        });

        // jsonObject
        saleViewModel.getLiveData().observe(this, new Observer<SaleSuccess>() {
            @Override
            public void onChanged(SaleSuccess jsonObject) {
                hideProgressDialog();
                startActivity(new Intent(getApplicationContext(), SaleSuccessActivity.class));
            }
        });
        mViewModel.getLiveData().observe(this, new Observer<List<ResponseInvoiceList>>() {
            @Override
            public void onChanged(List<ResponseInvoiceList> responseInvoiceLists) {

                // dataset = responseInvoiceLists;
                setList("invoicelist", responseInvoiceLists);
                if (responseInvoiceLists != null) {
                    for (int i = 0; i < responseInvoiceLists.size(); i++) {
                        if (responseInvoiceLists.get(i).getPriceWithoutTax() != null) {
                            double totalprice = responseInvoiceLists.get(i).getPriceWithoutTax() * responseInvoiceLists.get(i).getGoldWeight();
                            double newlabrchrg = responseInvoiceLists.get(i).getLabourCharge() * responseInvoiceLists.get(i).getGoldWeight();
                            total += totalprice;
                            totalItemtax += (totalprice / 100.0f) * responseInvoiceLists.get(i).getItemTax();
                            labourchargetax += (newlabrchrg / 100.0f) * responseInvoiceLists.get(i).getLabourTax();
                            labourcharge += newlabrchrg;
                            labourchargewithtax += (newlabrchrg / 100.0f) * responseInvoiceLists.get(i).getLabourTax() + (newlabrchrg);
                        }

                    }
                    double tot = total + labourcharge;
                    binding.tvsalesamount.setText("Total without Tax " + tot);
                    LocalPreferences.storeStringPreference(getApplicationContext(), "pricewithouttax", String.valueOf(tot));
                    double totaltax = totalItemtax + labourchargetax;
                    LocalPreferences.storeStringPreference(getApplicationContext(), "totaltax", String.valueOf(totaltax));
                    binding.tvtax.setText("Total Tax " + totaltax);
                    binding.tvDiscount.setText("Discount : " + discount);
                    netamount = total + totalItemtax + labourchargewithtax;
                    LocalPreferences.storeStringPreference(getApplicationContext(), "total", String.valueOf(netamount));
                    Log.d("bbms", "onBindViewHolder: " + totalItemtax);
                    Log.d("bbms", "onBindViewHolder: " + labourchargetax);
                    binding.tvtotal.setText("Total " + netamount);
                    binding.recyvinvoiceitems.setAdapter(new InvoiceAdapter(getApplicationContext(), responseInvoiceLists, InvoiceFragment.this));


                }

            }
        });


    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_invoice, null);
        CardView checkout = alertLayout.findViewById(R.id.cdvcheckout);
        // CardView compare = alertLayout.findViewById(R.id.cdvcompare);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getItems();


                //FragmentHelper.replaceFragment(getActivity(), R.id.content, new InvoiceFragment());
            }
        });


    }

    private void markItemSale() {
        showProgressDialog();
        int GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");

        String UserID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        JsonArray itemsarray = new JsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerID", GuestCustomerID);
        jsonObject.addProperty("remarks", "");
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("salesManID", UserID);
        jsonObject.addProperty("goldRate", 0);
        jsonObject.addProperty("saleType", "Sale");
        for (int i = 0; i < datasetItems.size(); i++) {
            JsonObject items = new JsonObject();
            items.addProperty("itemID", datasetItems.get(i).getItemID());
            items.addProperty("serialNumber", datasetItems.get(i).getItemserial());
            items.addProperty("qty", 1);
            items.addProperty("discountPercentage", datasetItems.get(i).getDiscountpercentage());
            items.addProperty("discountAmount", datasetItems.get(i).getDiscount());
            items.addProperty("itemVat", datasetItems.get(i).getItemVat());
            items.addProperty("itemVatAmount", datasetItems.get(i).getItemVatAmount());
            items.addProperty("labourVat", datasetItems.get(i).getLabourVat());
            items.addProperty("labourVatAmount", datasetItems.get(i).getLabourVatAmount());
            items.addProperty("finalAmount", datasetItems.get(i).getTotalwithtax());
            items.addProperty("goldWeight", datasetItems.get(i).getGoldweight());
            items.addProperty("stoneWeight", 0);
            items.addProperty("itemAmount", datasetItems.get(i).getTotaltax());
            items.addProperty("laborRate", datasetItems.get(i).getLaborRate());
            itemsarray.add(items);

        }

        jsonObject.add("saleSubMobile", itemsarray);
        Log.d("markItemSale", "markItemSale: " + jsonObject);
        Log.d("markItemSale", "markItemSale: " + datasetItems.size());

        saleViewModel.FetchInvoice(LocalPreferences.getToken(getApplicationContext()), jsonObject,getApplicationContext());

    }

    private void getItems() {
        class GetTasks extends AsyncTask<Void, Void, List<DiscountItem>> {

            @Override
            protected List<DiscountItem> doInBackground(Void... voids) {
                List<DiscountItem> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .discountItemsDao().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<DiscountItem> responseItems) {
                super.onPostExecute(responseItems);
                Log.d("markItemSale", "markItemSale: " + responseItems.size());

                datasetItems = responseItems;
                markItemSale();

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getSum() {

      /*  AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .interactionsDao()
                        .update(true, offlineId);
            }
        });*/

        class SavePlan extends AsyncTask<Void, Void, Void> {
            double totalamount, totaltax;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    total = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .discountItemsDao()

                            .totalwithouttax();

                    discount = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .discountItemsDao()

                            .getdiscountsum();
                    totaltax = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .discountItemsDao()
                            .gettotaltax();

                    totalamount = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .discountItemsDao()
                            .gettotalpayable();

                } catch (Exception e) {
                    Log.d("exception", "doInBackground: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                LocalPreferences.storeStringPreference(getApplicationContext(), "discount", String.valueOf(discount));
                binding.tvsalesamount.setText("Total without Tax " + total);

                double discountedtotal = netamount - discount;
                String stringdouble = Double.toString(totalamount);
                binding.tvtotal.setText("Total " + stringdouble);
                LocalPreferences.storeStringPreference(getApplicationContext(), "total", String.valueOf(discountedtotal));
                binding.tvDiscount.setText("Discount : " + discount);
                Log.d("dsicountsum", "doInBackground: " + totalamount);
                Log.d("exception", "doInBackground: " + discount);
                binding.tvtax.setText("Total Tax " + totaltax);
            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    public static String parseToCientificNotation(double result) {
        int cont = 0;
        java.text.DecimalFormat DECIMAL_FORMATER = new java.text.DecimalFormat("0.##");
        while (((int) result) != 0) {
            result /= 10;
            cont++;
        }
        return DECIMAL_FORMATER.format(result).replace(",", ".") + " x10^ -" + cont;
    }
/*    private void total() {
        class SavePlan extends AsyncTask<Void, Void, Void> {
            double totalamount = 0.0;

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    netamount = DatabaseClient
                            .getInstance(getActivity())
                            .getAppDatabase()
                            .discountItemsDao()
                            .gettotalpayable();
                    binding.tvDiscount.setText("Discount : " + discount);

                    binding.tvtotal.setText("Total " + totalamount);
                    LocalPreferences.storeStringPreference(getActivity(), "total", String.valueOf(netamount));

                    Log.d("dsicountsum", "doInBackground: " + discount);
                } catch (Exception e) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }*/


    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    public List<CartDetail> getList() {
        List<CartDetail> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "cartitem");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartDetail>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    @Override
    public void somofdiscount(double discount) {
        getSum();
        Log.d("mydiscount", "onBindViewHolder: " + discount);

       /* double discountedtotal=netamount-discount;
        binding.tvtotal.setText("Total " + discountedtotal);
        LocalPreferences.storeStringPreference(getActivity(),"total",String.valueOf(discountedtotal));
*/
    }
}