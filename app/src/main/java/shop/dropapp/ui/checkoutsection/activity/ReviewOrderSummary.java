package shop.dropapp.ui.checkoutsection.activity;

import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.JsonObject;

import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.databinding.ActivityReviewOrderSummaryBinding;
import shop.dropapp.databinding.FooterpricelayoutForreviewpageBinding;
import shop.dropapp.databinding.MagenativeActivityNoModuleBinding;
import shop.dropapp.databinding.MagenativeEmptyCartBinding;
import shop.dropapp.ui.cartsection.adapter.Ced_CartListAdapter;
import shop.dropapp.ui.cartsection.viewmodel.CartViewModel;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.Keys.*;


@AndroidEntryPoint
public class ReviewOrderSummary extends Ced_NavigationActivity {

    ActivityReviewOrderSummaryBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    CartViewModel cartViewModel;
    static boolean isHavingdownloadable = false;
    HashMap<String, HashMap<String, String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String, String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    ArrayList<String> IDS;
    Ced_CartListAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_review_order_summary, content, true);
        cartViewModel = new ViewModelProvider(ReviewOrderSummary.this, viewModelFactory).get(CartViewModel.class);
        showtootltext(getResources().getString(R.string.ordersummary));
        showbackbutton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String paymentcode = getIntent().getStringExtra("paymentcode");
        RecyclerView cart_recycler_ = binding.cartRecycler;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReviewOrderSummary.this, RecyclerView.VERTICAL, false);
        cart_recycler_.setLayoutManager(linearLayoutManager);
        cart_recycler_.setNestedScrollingEnabled(false);
        //--------------------------------------------------------
        IDS = new ArrayList<>();
        bundledata = new HashMap<>();
        configdata = new HashMap<>();
        finalconfigdata = new HashMap<>();
        ProductData = new ArrayList<>();
        set_regular_font_fortext(binding.MageNativeCount);
        set_regular_font_forButton(binding.MageNativeCheckout);

        binding.MageNativeCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.errorLayout.getVisibility() == View.VISIBLE) {
                    final Animation animShake = AnimationUtils.loadAnimation(ReviewOrderSummary.this, R.anim.shake2);
                    binding.errorLayout.startAnimation(animShake);
                } else {
                    if (paymentcode.equals("stripe")) {
                        createStripePopUp();
                    } else if (paymentcode.equals("Razorpay")) {
                        Intent intent = new Intent(ReviewOrderSummary.this, PlacedOrderRazorPay.class);
                        intent.putExtra("email", session.getUserDetails().get("Email"));
                        intent.putExtra("paymentcode", "apppayment");
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else {
                        Intent intent = new Intent(ReviewOrderSummary.this, ProcessOrder.class);
                        intent.putExtra("paymentcode", paymentcode);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                }
            }
        });
        request();
    }

    private void createStripePopUp() {

    }


    public void request() {
        try {
            JsonObject cartlist = new JsonObject();
            if (session.isLoggedIn()) {
                cartlist.addProperty("customer_id", session.getCustomerid());
                if (cedSessionManagement.getStoreId() != null) {
                    cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                if (cedSessionManagement.getCartId() != null) {
                    cartlist.addProperty("cart_id", cedSessionManagement.getCartId());
                }
                ProductData = new ArrayList<>();
                // request();
                proceed(cartlist);
            } else {
                if (cedSessionManagement.getCartId() != null) {
                    cartlist.addProperty("cart_id", cedSessionManagement.getCartId());
                    if (cedSessionManagement.getStoreId() != null) {
                        cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                    ProductData = new ArrayList<>();
                    // request();
                    proceed(cartlist);
                } else {
                    MagenativeEmptyCartBinding magenativeEmptyCartBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_cart, content, true);
                    magenativeEmptyCartBinding.conti.setOnClickListener(v -> {
                        Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("exceptfromhome", "true");
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    });
                }
            }
        } catch (Exception e) {
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void proceed(JsonObject cartlist) {
        cartViewModel.viewCart(ReviewOrderSummary.this, cedSessionManagement.getCurrentStore(),cartlist).observe(ReviewOrderSummary.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(apiResponse.data);
                        if (object.has("header") && object.getString("header").equals("false")) {
                            MagenativeActivityNoModuleBinding magenativeActivityNoModuleBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_un_authourized_request_error, content, true);
                            magenativeActivityNoModuleBinding.conti.setOnClickListener(v -> request());
                        } else {
                            if (object.has("success")) {
                                String status = object.getString("success");
                                if (status.equals("false")) {
                                    Ced_MainActivity.latestcartcount = "0";
                                    changecartcount();
                                    MagenativeEmptyCartBinding magenativeEmptyCartBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_cart, content, true);
                                    magenativeEmptyCartBinding.conti.setOnClickListener(v -> {
                                        Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                        intent.putExtra("exceptfromhome", "true");
                                        startActivity(intent);
                                        finishAffinity();
                                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                        finish();
                                    });
                                    if (object.has("message")) {
                                        showmsg(object.getString("message"));
                                    }

                                }
                            } else {
                                applydata(apiResponse.data);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    public void applydata(String Jstring) {
        try {
            binding.main.setVisibility(View.VISIBLE);
            JSONObject jsonObj = new JSONObject(Jstring);
            JSONObject data = jsonObj.getJSONObject("data");
            String items_count = String.valueOf(data.getInt("items_count"));
            Ced_MainActivity.latestcartcount = items_count;
            changecartcount();
            binding.MageNativeCount.setText(getResources().getString(R.string.totalitems) + items_count.toUpperCase());

            if (data.has("cart_error")) {
                String cart_error = data.getString("cart_error");
                if (!cart_error.equals("") && cart_error != null) {
                    binding.cartErrorTxt.setText(cart_error);
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            } else {
                binding.errorLayout.setVisibility(View.GONE);
            }

            if (data.has("segments")) {
                JSONArray segments = data.getJSONArray("segments");
                for (int k = 0; k < segments.length(); k++) {
                    FooterpricelayoutForreviewpageBinding footerpricelayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.footerpricelayout_forreviewpage, null, false);
                    set_regular_font_fortext(footerpricelayoutBinding.key);
                    set_regular_font_fortext(footerpricelayoutBinding.value);
                    JSONObject object = segments.getJSONObject(k);
                    if (object.getString("label").contains("Grand") || object.getString("label").equalsIgnoreCase("Grand Total")) {
                        binding.grandamount.setText(getResources().getString(R.string.total_) + " " + object.getString("value"));
                    }
                    footerpricelayoutBinding.key.setText(object.getString("label") + " " + ":");
                    footerpricelayoutBinding.value.setText(object.getString("value"));
                    binding.pricelayout.addView(footerpricelayoutBinding.getRoot());
                }
            }
           /*
            String is_discount = data.getString("is_discount");
           String allowed_guest_checkout = data.getString("allowed_guest_checkout");*/

            JSONArray products = data.getJSONArray("products");
            for (int i = 0; i < products.length(); i++) {
                JSONObject c = null;
                c = products.getJSONObject(i);
                String id = c.getString(PRODUCT_ID);
                // IDS.add(id);
                String item_id = c.getString(ITEM_ID);
                String name = c.getString(PRODUCT_NAME);
                String image = c.getString(PRODUCT_IMAGE);
                String price = c.getString(SUB_TOTAL);
                int Quantity = c.getInt(QUANTITY);
                String product_type = c.getString(PRODUCT_TYPE);
                HashMap<String, String> productdata = new HashMap<>();
                productdata.put(PRODUCT_ID, id);
                productdata.put(PRODUCT_NAME, name);
                productdata.put(PRODUCT_IMAGE, image);
                productdata.put(ITEM_ID, item_id);
                productdata.put(PRICE, price);
                productdata.put(QUANTITY, String.valueOf(Quantity));

                productdata.put("Product_type", product_type);
                if (c.has("item_error")) {
                    JSONArray item_error = c.getJSONArray("item_error");
                    productdata.put("item_error", item_error.toString());
                }
                if (product_type.equals("bundle")) {
                    JSONObject object = c.getJSONObject("bundle_options");
                    JSONArray option_names = object.names();
                    JSONArray option_values = object.toJSONArray(option_names);
                    ArrayList<String> data1 = new ArrayList<>();
                    for (int option = 0; option < Objects.requireNonNull(option_values).length(); option++) {
                        JSONObject jsonObject = option_values.getJSONObject(option);
                        String label = jsonObject.getString("label");
                        JSONArray array = jsonObject.getJSONArray("value");
                        for (int value = 0; value < array.length(); value++) {
                            JSONObject object1 = array.getJSONObject(value);
                            String title = object1.getString("title");
                            String qty = object1.getString("qty");
                            String optionprice = object1.getString("price");
                            String datatoshow = label + "#" + title + "#" + qty + "#" + optionprice;
                            data1.add(datatoshow);
                        }
                    }
                    bundledata.put(item_id, data1);
                }
                if (product_type.equals("configurable")) {
                    JSONArray object = c.getJSONArray("options_selected");
                    HashMap<String, String> config_data = new HashMap<>();
                    for (int option = 0; option < object.length(); option++) {
                        JSONObject jsonObject = object.getJSONObject(option);
                        String label = jsonObject.getString("label");
                        String value = jsonObject.getString("value");
                        config_data.put(value, label);
                    }
                    finalconfigdata.put(item_id, config_data);
                }
                if (product_type.equals("downloadable")) {
                    isHavingdownloadable = true;
                }
                ProductData.add(productdata);
                System.out.println("ProductData:" + ProductData);
            }

            if (finalconfigdata.size() > 0 && bundledata.size() <= 0) {
                Log.i("ProductData", "In");
                Log.i("ProductData", "In" + finalconfigdata);
                listViewAdapter = new Ced_CartListAdapter(this, ProductData, finalconfigdata, false);
            } else {
                if (bundledata.size() > 0 && finalconfigdata.size() <= 0) {
                    Log.i("ProductData", "In2");
                    listViewAdapter = new Ced_CartListAdapter(this, ProductData, bundledata, "Bundle", false);
                } else {
                    if (bundledata.size() > 0 && finalconfigdata.size() > 0) {
                        Log.i("ProductData", "In3");
                        listViewAdapter = new Ced_CartListAdapter(this, ProductData, bundledata, finalconfigdata, false);
                    } else {
                        Log.i("ProductData", "In4");
                        listViewAdapter = new Ced_CartListAdapter(this, ProductData, false);
                    }
                }
            }

            binding.cartRecycler.setAdapter(listViewAdapter);
            listViewAdapter.notifyDataSetChanged();
        } catch (Exception w) {
            w.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    public void changecartcount() {
        invalidateOptionsMenu();
    }
}