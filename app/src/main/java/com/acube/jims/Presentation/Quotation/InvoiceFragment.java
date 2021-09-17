package com.acube.jims.Presentation.Quotation;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.Presentation.Quotation.adapter.InvoiceAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.InvoiceFragmentBinding;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
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

public class InvoiceFragment extends Fragment implements InvoiceAdapter.DiscountSum {

    private InvoiceViewModel mViewModel;
    RecyclerView mRecyinvoice;

    public static InvoiceFragment newInstance() {
        return new InvoiceFragment();
    }

    InvoiceFragmentBinding binding;
    double total = 0.0;

    double totalItemtax = 0.0;
    double netamount = 0.0;
    double discount = 0.0;
    List<ResponseInvoiceList> dataset;
    double labourchargewithtax = 0.0;
    double labourchargetax = 0.0;
    double labourcharge = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.invoice_fragment, container, false);


        binding.recyvinvoiceitems.setLayoutManager(new LinearLayoutManager(getActivity()));
        String Customername = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerName");
        String CustomerMobile = LocalPreferences.retrieveStringPreferences(getContext(), "CustomerMobile");
        binding.tvcustomername.setText("Customer Name : " + Customername);
        binding.tvcustomercontactnumber.setText("Contact Number: " + CustomerMobile);
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        binding.tvdate.setText(date);
        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PdfCreatorExampleActivity.class));
                // FragmentHelper.replaceFragment(getActivity(),R.id.content,new PdfCreatorExampleActivity());
            }
        });

        return binding.getRoot();
    }

    private void getSum() {
        class SavePlan extends AsyncTask<Void, Void, Void> {


            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    discount = DatabaseClient
                            .getInstance(getActivity())
                            .getAppDatabase()
                            .discountItemsDao()

                            .getdiscountsum();
                    LocalPreferences.storeStringPreference(getActivity(), "discount", String.valueOf(discount));
                    binding.tvDiscount.setText("Discount : " + discount);
                    double totalamount = DatabaseClient
                            .getInstance(getActivity())
                            .getAppDatabase()
                            .discountItemsDao()
                            .gettotalpayable();
                    double discountedtotal = netamount - discount;
                    binding.tvtotal.setText("Total " + discountedtotal);
                    LocalPreferences.storeStringPreference(getActivity(), "total", String.valueOf(discountedtotal));


                    Log.d("dsicountsum", "doInBackground: " + netamount);
                } catch (Exception e) {
                    Log.d("exception", "doInBackground: " + discount);
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
    }

    private void total() {
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        mViewModel.init();
        ;
        JsonArray jsonObject = new JsonArray();
        String[] datset = {"10001", "10002"};
        List<CartDetail> cartdata = getList();
        List<String> filteredvalue = new ArrayList<>();
        for (int i = 0; i < cartdata.size(); i++) {
            filteredvalue.add(cartdata.get(i).getSerialNumber());

        }


        String[] strArray = filteredvalue.toArray(new String[filteredvalue.size()]);
        // jsonObject
        mViewModel.FetchInvoice(LocalPreferences.getToken(getActivity()), strArray);
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseInvoiceList>>() {
            @Override
            public void onChanged(List<ResponseInvoiceList> responseInvoiceLists) {
                dataset = new ArrayList();
                dataset = responseInvoiceLists;
                setList("invoicelist", dataset);
                if (responseInvoiceLists != null) {
                    for (int i = 0; i < responseInvoiceLists.size(); i++) {

                        total += responseInvoiceLists.get(i).getPriceWithoutTax();
                        totalItemtax += (responseInvoiceLists.get(i).getPriceWithoutTax() / 100.0f) * responseInvoiceLists.get(i).getItemTax();
                        labourchargetax += (responseInvoiceLists.get(i).getLabourCharge() / 100.0f) * responseInvoiceLists.get(i).getLabourTax();
                        labourcharge += responseInvoiceLists.get(i).getLabourCharge();
                        labourchargewithtax += (responseInvoiceLists.get(i).getLabourCharge() / 100.0f) * responseInvoiceLists.get(i).getLabourTax() + responseInvoiceLists.get(i).getLabourCharge();
                    }
                    double tot = total + labourcharge;
                    binding.tvsalesamount.setText("Total without Tax " + tot);
                    LocalPreferences.storeStringPreference(getActivity(), "pricewithouttax", String.valueOf(tot));
                    double totaltax = totalItemtax + labourchargetax;
                    LocalPreferences.storeStringPreference(getActivity(), "totaltax", String.valueOf(totaltax));
                    binding.tvtax.setText("Total Tax " + totaltax);
                    binding.tvDiscount.setText("Discount : " + discount);
                    netamount = total + totalItemtax + labourchargewithtax;
                    LocalPreferences.storeStringPreference(getActivity(), "total", String.valueOf(netamount));
                    Log.d("bbms", "onBindViewHolder: " + totalItemtax);
                    Log.d("bbms", "onBindViewHolder: " + labourchargetax);
                     binding.tvtotal.setText("Total " + netamount);
                    binding.recyvinvoiceitems.setAdapter(new InvoiceAdapter(getActivity(), responseInvoiceLists, InvoiceFragment.this));


                }
            }
        });
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getActivity(), key, json);
    }

    public List<CartDetail> getList() {
        List<CartDetail> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getActivity(), "cartitem");
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