package com.acube.jims.presentation.LocateProduct.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.presentation.LocateProduct.adapter.LocateItemAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.LocateProductFragmentBinding;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.datalayer.remote.db.DatabaseClient;

import java.util.Arrays;
import java.util.List;

public class LocateProduct extends Fragment {

    private LocateProductViewModel mViewModel;

    public static LocateProduct newInstance() {
        return new LocateProduct();
    }

    LocateProductFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.locate_product_fragment, container, false);
        binding.recylocateitems.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set title
                alertDialogBuilder.setTitle("Clear All Data?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to clear all data?")
                        .setCancelable(false)
                        .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteAll();

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edLocate.setError(null);
                String serialnos = binding.edLocate.getText().toString();
                if (serialnos.equalsIgnoreCase("")) {
                    binding.edLocate.setError("Field Empty");

                } else {
                    List<String> convertedList = Arrays.asList(serialnos.split(",", -1));
                    Log.d("TAG", "onClick: " + convertedList);
                    LocateItem locateItem;
                    for (int i = 0; i < convertedList.size(); i++) {
                        locateItem = new LocateItem();
                        locateItem.setSerialnumber(convertedList.get(i));
                        SaveItems(locateItem);

                    }

                }
            }
        });

        return binding.getRoot();
    }

    private void getItems() {
        class GetTasks extends AsyncTask<Void, Void, List<LocateItem>> {

            @Override
            protected List<LocateItem> doInBackground(Void... voids) {
                List<LocateItem> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .locateItemsDao().getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<LocateItem> responseItems) {
                super.onPostExecute(responseItems);
                Log.d("markItemSale", "markItemSale: " + responseItems.size());
                binding.recylocateitems.setAdapter(new LocateItemAdapter(getActivity(), responseItems));


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void DeleteAll() {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .locateItemsDao()
                        .deleteall();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getItems();

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    public void SaveItems(LocateItem items) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .locateItemsDao()
                        .insert(items);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getItems();

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LocateProductViewModel.class);
        // TODO: Use the ViewModel
    }

}