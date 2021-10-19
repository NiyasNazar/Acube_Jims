package com.acube.jims.Presentation.ProductDetails.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.Presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.ViewModel.ItemDetailsViewModel;
import com.acube.jims.Presentation.Quotation.InvoiceFragment;
import com.acube.jims.Presentation.Quotation.SaleFragment;
import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ProductDetailsFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ProductDetailsFragment extends BaseFragment {

    private ItemDetailsViewModel mViewModel;
    String Id;
    BackHandler backHandler;
    AddtoCartViewModel addtoCartViewModel;
    String AuthToken;
    String mSerialno;
    String CartId;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    String GuestCustomerID, UserId;
    String emailbody, emailsubject;
    String imageurl;
    PermissionListener permissionlistener;

    public static ProductDetailsFragment newInstance(String Id) {
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

        Bundle args = new Bundle();
        args.putString("Id", Id);
        productDetailsFragment.setArguments(args);
        return productDetailsFragment;
    }

    ProductDetailsFragmentBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            backHandler = (BackHandler) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.product_details_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        addtoFavoritesViewModel.init();
        mViewModel.init();
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        CartId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.CartID);
        GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                sendTextMsgOnWhatsApp("45", emailbody);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        binding.imvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);


                /*    try {



                 *//*   Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rony8652@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailbody);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(binding.imvsingleitemimage));
                    emailIntent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));*//*
                    Uri uri = Uri.parse("smsto:" + "919747337738");
                    Intent share = new Intent(Intent.ACTION_SENDTO, uri);
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_TEXT, emailbody);

                    //share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(binding.imvsingleitemimage));
                    share.setPackage("com.whatsapp");
                    startActivity(share);
                } catch (ActivityNotFoundException e) {


                } catch (Exception e) {
                    e.printStackTrace();

                }*/


            }
        });


        binding.favButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, GuestCustomerID, UserId, Id, "add", "", mSerialno);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });


        if (getArguments() != null) {
            String Id = getArguments().getString("Id");
            showProgressDialog();
            mViewModel.FetchItemDetails(AppConstants.Authorization + AuthToken, Id);
        }

        addtoFavoritesViewModel.getLiveData().observe(getActivity(), new Observer<JsonObject>() {

            @Override
            public void onChanged(JsonObject jsonObject) {
                Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                if (jsonObject != null) {

                }
            }
        });


        binding.toolbar.tvFragname.setText("View Details-Single item");
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
            }
        });
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHandler.backpress();
            }
        });
        binding.btnAddTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                JsonObject items = new JsonObject();

                items.addProperty("cartListNo", CartId);
                items.addProperty("customerID", GuestCustomerID);
                items.addProperty("employeeID", UserId);
                items.addProperty("serialNumber", mSerialno);
                items.addProperty("itemID", Id);
                items.addProperty("qty", 0);
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(items);


                addtoCartViewModel.AddtoCart(AppConstants.Authorization + AuthToken, "add", jsonArray);

            }
        });
        addtoCartViewModel.getLiveData().observe(getActivity(), new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                }
            }
        });

      /* OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Toast.makeText(getActivity(), "OnBackPressed", Toast.LENGTH_LONG).show();
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.handleOnBackPressed();


            }
        });*/
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseCatalogDetails>() {
            @Override
            public void onChanged(ResponseCatalogDetails responseCatalogDetails) {
                hideProgressDialog();
                if (responseCatalogDetails != null) {
                    if (responseCatalogDetails.getItemSubList().size() > 0) {
                        Glide.with(getActivity())
                                .load(responseCatalogDetails.getItemSubList().get(0).getImageFilePath())
                                //  .placeholder(R.drawable.placeholder)
                                //  .error(R.drawable.imagenotfound)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // log exception
                                        Log.e("TAG", "Error loading image", e);
                                        return false; // important to return false so the error placeholder can be placed
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(binding.imvsingleitemimage);
                    }

                    mSerialno = responseCatalogDetails.getSerialNumber();
                    Id = String.valueOf(responseCatalogDetails.getId());
                    if (!GuestCustomerID.equalsIgnoreCase("")) {
                        CustomerHistory customerHistory = new CustomerHistory();
                        customerHistory.setCustomerID(Integer.valueOf(GuestCustomerID));
                        customerHistory.setItemID(Integer.valueOf(Id));
                        customerHistory.setSerialNo(responseCatalogDetails.getSerialNumber());
                        SaveHistory(customerHistory);
                    }

                    binding.tvMrp.setText(Math.round(responseCatalogDetails.getMrp())+ " SAR ");
                    binding.tvItemName.setText(responseCatalogDetails.getItemName());
                    binding.tvbrandname.setText(responseCatalogDetails.getItemBrandName());
                    binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
                    binding.tvItemcode.setText(responseCatalogDetails.getSerialNumber());
                    binding.tvGender.setText(responseCatalogDetails.getGender());
                    binding.tvmakingchargemin.setText("" + responseCatalogDetails.getMakingChargeMin());
                    binding.tvMakingchrgmax.setText("" + responseCatalogDetails.getMakingChargeMax());
                    binding.tvKaratname.setText(responseCatalogDetails.getKaratName());
                    binding.tvColor.setText(responseCatalogDetails.getColorName());
                    binding.tvgroupname.setText(responseCatalogDetails.getItemGroupName());
                    binding.tvgrossweight.setText("" + responseCatalogDetails.getGrossWeight() + " g");
                    binding.tvcategory.setText(responseCatalogDetails.getCategoryName());
                    binding.tvSubcategory.setText(responseCatalogDetails.getSubCategoryName());
                    emailbody = "Hey \n" + "Check this new item " + responseCatalogDetails.getItemName() + " ," + responseCatalogDetails.getGrossWeight() + "g  " + responseCatalogDetails.getKaratName()+" for "+responseCatalogDetails.getMrp();

                    imageurl = responseCatalogDetails.getItemSubList().get(0).getImageFilePath();
                    if (responseCatalogDetails.getStoneWeight() != null) {
                        binding.tvItemStoneweight.setText("Stone Weight: " + responseCatalogDetails.getStoneWeight() + " g");
                    } else {
                        binding.tvItemStoneweight.setText("Stone Weight: N/A");
                    }
                    if (responseCatalogDetails.getGrossWeight() != null) {
                        binding.tvItemweight.setText(responseCatalogDetails.getKaratName() + "- Weight: " + responseCatalogDetails.getGrossWeight() + " g");
                    } else {
                        binding.tvItemweight.setText("Stone Weight: N/A");
                    }


                }
            }
        });

        return binding.getRoot();

    }


    public interface BackHandler {
        void backpress();
    }

    private void SaveHistory(CustomerHistory history) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .customerHistoryDao()
                        .insert(history);
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

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = getBitmapFromDrawable(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public Uri getBitmapFromDrawable(Bitmap bmp) {

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            bmpUri = FileProvider.getUriForFile(getActivity(), "com.acube.jims.provider", file);  // use this version for API >= 24

            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void sendTextMsgOnWhatsApp(String sContactNo, String sMessage) {



        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sMessage);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(binding.imvsingleitemimage));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),"Install Whatsapp",Toast.LENGTH_SHORT).show();
        }
    }

    private String contactIdByPhoneNumber(String phoneNumber) {
        String contactId = null;
        if (phoneNumber != null && phoneNumber.length() > 0) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

            Cursor cursor = contentResolver.query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                }
                cursor.close();
            }
        }
        return contactId;
    }

    public String hasWhatsApp(String contactID) {
        String rowContactId = null;
        boolean hasWhatsApp;

        String[] projection = new String[]{ContactsContract.RawContacts._ID};
        String selection = ContactsContract.RawContacts.CONTACT_ID + " = ? AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?";
        String[] selectionArgs = new String[]{contactID, "com.whatsapp"};
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null) {
            hasWhatsApp = cursor.moveToNext();
            if (hasWhatsApp) {
                rowContactId = cursor.getString(0);
            }
            cursor.close();
        }
        return rowContactId;
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_share, null);
        CardView whatsapp = alertLayout.findViewById(R.id.cdvsharewhatsapp);
        CardView email = alertLayout.findViewById(R.id.cdvshareemail);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //   sendTextMsgOnWhatsApp("+919747337738",emailbody);
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                        .check();

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rony8652@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailbody);
                emailIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(binding.imvsingleitemimage));
                emailIntent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
            }
        });


    }
}