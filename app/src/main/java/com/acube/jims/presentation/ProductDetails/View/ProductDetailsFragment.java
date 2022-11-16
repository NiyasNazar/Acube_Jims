package com.acube.jims.presentation.ProductDetails.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseLiveStore;
import com.acube.jims.datalayer.models.Catalogue.ItemSub;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.presentation.Consignment.ConsignmentActivity;
import com.acube.jims.presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerLogoutViewModel;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.ImageGallery.ImageGalleryActivity;
import com.acube.jims.presentation.ProductDetails.ViewModel.ItemDetailsViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ProductDetailsFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.acube.jims.presentation.ProductDetails.adapter.ViewPagerAdapter;
import com.acube.jims.utils.SearchableSpinners;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegaintentbuilder.downloader.DownloadCallback;
import com.omega_r.libs.omegaintentbuilder.handlers.ContextIntentHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends BaseActivity implements ViewPagerAdapter.Viewimage {

    private ItemDetailsViewModel mViewModel;
    String Id;
    BackHandler backHandler;
    AddtoCartViewModel addtoCartViewModel;
    String AuthToken;
    String mSerialno;
    String CartId;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    String UserId;
    String emailbody, emailsubject;
    String imageurl;
    PermissionListener permissionlistener;
    boolean outofstock;
    List<ItemSub> dataset;
    ProductDetailsFragmentBinding binding;

    int GuestCustomerID;
    private int dotscount;
    private ImageView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    List<String> imgurl = new ArrayList<>();
    String warehouseId;
    CustomerLogoutViewModel customerLogoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(
                this, R.layout.product_details_fragment);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);

        customerLogoutViewModel = new ViewModelProvider(this).get(CustomerLogoutViewModel.class);
        initToolBar(binding.toolbarApp.toolbar, "Item Details", true);
        binding.toolbarApp.imvcart.setVisibility(View.VISIBLE);
        outofstock = getIntent().getBooleanExtra("outofstock", false);
        dataset = new ArrayList<>();
        binding.toolbarApp.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
            }
        });

        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        customerLogoutViewModel.init();
        addtoFavoritesViewModel.init();
        mViewModel.init();
        warehouseId = LocalPreferences.retrieveStringPreferences(ProductDetailsFragment.this, "warehouseId");


        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        CartId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.CartID);
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        boolean salesman = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "salesman");
        if (salesman) {
            binding.likelayout.setVisibility(View.INVISIBLE);
            binding.toolbarApp.imvcart.setVisibility(View.INVISIBLE);
        } else {
            binding.likelayout.setVisibility(View.VISIBLE);
            binding.toolbarApp.imvcart.setVisibility(View.VISIBLE);
        }

        String ItemId = getIntent().getStringExtra("Id");
        if (outofstock) {
            binding.btnAddTocart.setVisibility(View.INVISIBLE);
            binding.btnshare.setVisibility(View.INVISIBLE);
            binding.likelayout.setVisibility(View.INVISIBLE);

            mViewModel.FetchoutofsctockItemDetails(AppConstants.Authorization + AuthToken, ItemId, getApplicationContext());

        } else {
            mViewModel.FetchItemDetails(AppConstants.Authorization + AuthToken, ItemId, getApplicationContext());

        }


        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                sendTextMsgOnWhatsApp("45", emailbody);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };


        binding.favButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, String.valueOf(GuestCustomerID), UserId, Id, "add", "", mSerialno, getApplicationContext());

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });


        addtoFavoritesViewModel.getLiveData().observe(this, new Observer<JsonObject>() {

            @Override
            public void onChanged(JsonObject jsonObject) {
                Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                if (jsonObject != null) {

                }
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
                addtoCartViewModel.AddtoCart(AppConstants.Authorization + AuthToken, "add", jsonArray, getApplicationContext());

            }
        });
        addtoCartViewModel.getLiveData().observe(this, new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                }
            }
        });


        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        binding.btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);

            }
        });

        binding.laytimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mViewModel.getLiveData().observe(this, new Observer<ResponseCatalogDetails>() {
            @Override
            public void onChanged(ResponseCatalogDetails responseCatalogDetails) {
                hideProgressDialog();
                if (responseCatalogDetails != null) {
                    if (responseCatalogDetails.getItemSubList().size() > 0) {
                        dataset = responseCatalogDetails.getItemSubList();
                        if (dataset != null && dataset.size() > 0) {


                        }
                        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), responseCatalogDetails.getItemSubList(), ProductDetailsFragment.this);
                        binding.viewPager.setAdapter(viewPagerAdapter);
                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];
                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(ProductDetailsFragment.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);
                            binding.sliderDots.addView(dots[i], params);


                        }
                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        for (int i = 0; i < responseCatalogDetails.getItemSubList().size(); i++) {
                            imgurl.add(responseCatalogDetails.getItemSubList().get(i).getImageFilePath());

                        }

                        Glide.with(getApplicationContext())
                                .load(responseCatalogDetails.getItemSubList().get(0).getImageFilePath())
                                .placeholder(R.drawable.jwimage)
                                .error(R.drawable.jwimage)
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
                    if (GuestCustomerID != 0) {
                        CustomerHistory customerHistory = new CustomerHistory();
                        customerHistory.setCustomerID(GuestCustomerID);
                        customerHistory.setItemID(Integer.valueOf(Id));
                        customerHistory.setSerialNo(responseCatalogDetails.getSerialNumber());
                        SaveHistory(customerHistory);
                    }

                    JSONArray itemViewLIst = new JSONArray();

                    JSONObject parent = new JSONObject();
                    try {
                        JSONObject analyticsobject = new JSONObject();
                        analyticsobject.put("customerID", GuestCustomerID);
                        analyticsobject.put("itemID", Id);
                        analyticsobject.put("serialNumber", Id);
                        analyticsobject.put("trayID", 3);
                        analyticsobject.put("employeeID", UserId);
                        analyticsobject.put("WarehouseID", warehouseId);
                        itemViewLIst.put(analyticsobject);
                        parent.put("itemViewList", itemViewLIst);
                        JsonParser jsonParser = new JsonParser();
                        JsonObject gsonObject = new JsonObject();
                        gsonObject = (JsonObject) jsonParser.parse(parent.toString());
                        customerLogoutViewModel.CustomerLogout(LocalPreferences.getToken(ProductDetailsFragment.this), gsonObject, ProductDetailsFragment.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.tvitemname.setText(responseCatalogDetails.getItemName());
                    binding.tvproductname.setText(responseCatalogDetails.getItemName());
                    binding.tvsmalldesc.setText(responseCatalogDetails.getItemDesc());
                    binding.tvDescription.setText(responseCatalogDetails.getItemDesc());
                    binding.tvItemcode.setText(responseCatalogDetails.getSerialNumber());
                    binding.tvGender.setText(responseCatalogDetails.getGender());
                    binding.tvmakingchargemin.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMin(), ""));
                    binding.tvMakingchrgmax.setText("" + getValueOrDefault(responseCatalogDetails.getMakingChargeMax(), ""));
                    binding.tvKaratname.setText(getValueOrDefault(responseCatalogDetails.getKaratName(), ""));
                    binding.tvColor.setText(getValueOrDefault(responseCatalogDetails.getColorName(), ""));
                    binding.tvuom.setText(responseCatalogDetails.getUomName());
                    binding.tvgrossweight.setText("" + responseCatalogDetails.getGrossWeight() + " g");
                    binding.tvcategory.setText(responseCatalogDetails.getCategoryName());
                    binding.tvSubcategory.setText(responseCatalogDetails.getSubCategoryName());
                    emailbody = "Hey \n" + "Check this new item " + responseCatalogDetails.getItemName() + " ," + responseCatalogDetails.getGrossWeight() + "g  " + responseCatalogDetails.getKaratName() == null ? "" : responseCatalogDetails.getKaratName() + " for " + responseCatalogDetails.getMrp();
                    if (responseCatalogDetails.getItemSubList() != null && responseCatalogDetails.getItemSubList().size() != 0) {
                        imageurl = responseCatalogDetails.getItemSubList().get(0).getImageFilePath();

                    }


                }
            }
        });


    }

    @Override
    public void imagegallery() {
        startActivity(new Intent(getApplicationContext(), ImageGalleryActivity.class).putParcelableArrayListExtra("DATA", (ArrayList<? extends Parcelable>) dataset));

    }


    public interface BackHandler {
        void backpress();
    }

    private void SaveHistory(CustomerHistory history) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
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
            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
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
            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            bmpUri = FileProvider.getUriForFile(getApplicationContext(), "com.acube.jims.provider", file);  // use this version for API >= 24

            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void sendTextMsgOnWhatsApp(String sContactNo, String sMessage) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");

        OmegaIntentBuilder.from(ProductDetailsFragment.this)
                .share()


                .text(emailbody)

                .filesUrls(imgurl)
                .download(new DownloadCallback() {
                    @Override
                    public void onDownloaded(boolean success, @NotNull ContextIntentHandler contextIntentHandler) {
                        contextIntentHandler.startActivity();
                    }
                });


       /* Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sMessage);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(binding.imvsingleitemimage));
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Install Whatsapp", Toast.LENGTH_SHORT).show();
        }*/
    }

    private String contactIdByPhoneNumber(String phoneNumber) {
        String contactId = null;
        if (phoneNumber != null && phoneNumber.length() > 0) {
            ContentResolver contentResolver = getContentResolver();
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
        Cursor cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
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


        AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailsFragment.this);
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
                ShowEmailPopup();
                dialog.dismiss();
            }
            /*    try {
                    File rootSdDirectory = Environment.getExternalStorageDirectory();

                    File pictureFile = new File(rootSdDirectory, "attachment.jpg");
                    if (pictureFile.exists()) {
                        pictureFile.delete();
                    }
                    pictureFile.createNewFile();

                    FileOutputStream fos = new FileOutputStream(pictureFile);

                    URL url = new URL("http://acuberfid.fortiddns.com:4480/jmsqaapi/itemimages/27/8613.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();
                    InputStream in = connection.getInputStream();

                    byte[] buffer = new byte[1024];
                    int size = 0;
                    while ((size = in.read(buffer)) > 0) {
                        fos.write(buffer, 0, size);
                    }
                    fos.close();
                    dialog.dismiss();
                    Uri pictureUri = Uri.fromFile(pictureFile);

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rony8652@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailbody);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                    emailIntent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }*/
        });


    }

    public void ShowEmailPopup() {


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_email_dialog, null);
        final EditText edEmail = alertLayout.findViewById(R.id.ed_email);

        AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailsFragment.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", (dialog, which) ->
                Log.d(TAG, "ShowEmailPopup: "));

        alert.setPositiveButton("Proceed", (dialog, which) -> {
            String vaEmail = edEmail.getText().toString();
            OmegaIntentBuilder.from(ProductDetailsFragment.this)
                    .share()

                    .emailTo(vaEmail)
                    .subject("")
                    .text(emailbody)
                    .filesUrls(imgurl)
                    .download(new DownloadCallback() {
                        @Override
                        public void onDownloaded(boolean success, @NotNull ContextIntentHandler contextIntentHandler) {
                            contextIntentHandler.startActivity();
                        }
                    });

        });
        AlertDialog dialog = alert.create();
        dialog.show();


    }


    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }
}