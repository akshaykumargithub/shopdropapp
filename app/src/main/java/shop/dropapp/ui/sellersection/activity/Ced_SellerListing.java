/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license   http://cedcommerce.com/license-agreement.txt
 *
 */
package shop.dropapp.ui.sellersection.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.databinding.MagenativeSellerListingBinding;
import shop.dropapp.databinding.MagenativeSellerSearchLayoutBinding;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.sellersection.adapter.Ced_SellerGridAdapter;
import shop.dropapp.ui.sellersection.utils.Ced_MapComparator;
import shop.dropapp.ui.sellersection.utils.Ced_MapComprator_des;
import shop.dropapp.ui.sellersection.viewmodel.SellerListViewModel;
import shop.dropapp.utils.UpdateImage;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.Keys.*;

@AndroidEntryPoint
public class Ced_SellerListing extends Ced_NavigationActivity {
    public static boolean loadfrombottom = false;
    MagenativeSellerListingBinding magenativeSellerListingBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SellerListViewModel sellerListViewModel;
    String Jstring;
    String marketurl = "";
    ListView sellergrid;
    JSONArray sellerjsonarray;
    Ced_SellerGridAdapter sellerGridAdapter;
    ImageView marketplacebanner;
    ArrayList<HashMap<String, String>> finalsellerlist;
    String status;
    boolean load = false;
    boolean flag = false;
    int current = 1;
    LinearLayout search;
    Dialog listDialog;
    List<String> countrylabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    List<String> statelabellist;
    HashMap<String, String> codecountry;
    String getcountry = "";
    String country_code = "";
    String state_code = "";
    String country_label = "";
    String state_label = "";
    JsonObject searchdata;
    View view;
    ArrayList<String> arrayList;
    String sellersearch = "";
    HashMap<String, String> label_code;
    LinearLayout sorting;
    LinearLayout find;
    boolean a_z = false;
    ImageView sort;
    LinearLayout sortsection;
    ViewStub MageNative_list;
    Ced_SessionManagement ced_sessionManagement;
    Boolean nostates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magenativeSellerListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_seller_listing, content, true);
        sellerListViewModel = new ViewModelProvider(Ced_SellerListing.this, viewModelFactory).get(SellerListViewModel.class);
        sortsection = magenativeSellerListingBinding.sortsection;
        countrylabellist = new ArrayList<String>();
        countrycodelist = new ArrayList<String>();
        searchdata = new JsonObject();
        ced_sessionManagement = new Ced_SessionManagement(Ced_SellerListing.this);
        searchdata.addProperty("page", String.valueOf(current));
        try {
           // JsonObject locationObject = new JsonObject();
          //  JsonObject extensionAttribute = new JsonObject();
         //   searchdata.addProperty(LONGITUDE, ced_sessionManagement.getlongitude());
           // searchdata.addProperty(LATITUDE, ced_sessionManagement.getlatitude());
            searchdata.addProperty(CITY, ced_sessionManagement.getcity());
            searchdata.addProperty(STATE, ced_sessionManagement.getstate());
            searchdata.addProperty(COUNTRY_ID, ced_sessionManagement.getcountry());
            searchdata.addProperty(POSTCODE, ced_sessionManagement.getpostcode());
          //  extensionAttribute.add(LOCATION, locationObject);
          //  searchdata.add(EXTENSION_ATTRIBUTES, extensionAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        label_code = new HashMap<String, String>();
        sorting = magenativeSellerListingBinding.sorting;
        find = magenativeSellerListingBinding.find;
        sort = magenativeSellerListingBinding.sort;
        sellergrid = magenativeSellerListingBinding.MagenatibeSellerlistview;
        try {
            view = View.inflate(getApplicationContext(), R.layout.magenative_headeratsellers, null);
            marketplacebanner = (ImageView) view.findViewById(R.id.magenative_marketplacebanner);
            marketplacebanner.setOnClickListener(null);
            sellergrid.addHeaderView(view);
            finalsellerlist = new ArrayList<HashMap<String, String>>();
            codecountry = new HashMap<String, String>();
            getcountries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent seller = getIntent();
        if (seller.getExtras() != null) {
            if (seller.hasExtra("seller")) {
                if (seller.getStringExtra("seller").equals("out")) {
                    searchdata.addProperty("seller_search", seller.getStringExtra("sellersearch"));
                    Log.i("sdf", "" + sellersearch);
                    sellersearch = seller.getStringExtra("sellersearch");
                } else {
                    searchdata.addProperty("seller_search", "no_data");
                    sellersearch = "";
                }
            } else {
                searchdata.addProperty("seller_search", "no_data");
                sellersearch = "";
            }
            if (seller.getStringExtra("seller").equals("out")) {
                searchdata.addProperty("seller_search", seller.getStringExtra("sellersearch"));
                sellersearch = seller.getStringExtra("sellersearch");
            } else {
                searchdata.addProperty("seller_search", "no_data");
                sellersearch = "";
            }
        }
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showsearchpopup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a_z) {
                    a_z = false;
                    loadfrombottom = true;
                    sort.setImageResource(R.drawable.sort_a_z);
                    Collections.sort(finalsellerlist, new Ced_MapComparator("public_name"));
                    sellerGridAdapter = new Ced_SellerGridAdapter(Ced_SellerListing.this, finalsellerlist);
                    sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
                    sellergrid.setDividerHeight(0);
                    sellergrid.setAdapter(sellerGridAdapter);
                    load = true;
                } else {
                    a_z = true;
                    loadfrombottom = true;
                    sort.setImageResource(R.drawable.sort_z_to_a);
                    Collections.sort(finalsellerlist, new Ced_MapComprator_des("public_name"));
                    sellerGridAdapter = new Ced_SellerGridAdapter(Ced_SellerListing.this, finalsellerlist);
                    sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
                    sellergrid.setDividerHeight(0);
                    sellergrid.setAdapter(sellerGridAdapter);
                    load = true;
                }
            }
        });
        /*sortbyname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(sortbyname.getSelectedItem().equals("Name:A To Z"))
                {
                    loadfrombottom=true;
                    Collections.sort(finalsellerlist, new Ced_MapComparator("public_name"));
                    sellerGridAdapter = new Ced_SellerGridAdapter(Ced_SellerListing.this, finalsellerlist);
                    sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
                    sellergrid.setDividerHeight(10);
                    sellergrid.setAdapter(sellerGridAdapter);
                    load = true;
                }
                if(sortbyname.getSelectedItem().equals("Name:Z To A"))
                {
                    loadfrombottom=true;
                    Collections.sort(finalsellerlist, new Ced_MapComprator_des("public_name"));
                    sellerGridAdapter = new Ced_SellerGridAdapter(Ced_SellerListing.this, finalsellerlist);
                    sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
                    sellergrid.setDividerHeight(7);
                    sellergrid.setAdapter(sellerGridAdapter);
                    load = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        sellergrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ShopId = (TextView) view.findViewById(R.id.magenative_ShopId);
                Intent sellershop = new Intent(Ced_SellerListing.this, Ced_Seller_Shop.class);
                if (!ShopId.getText().toString().isEmpty()) {
                    sellershop.putExtra("ID", ShopId.getText().toString());
                }

                startActivity(sellershop);
            }
        });

        sellergrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    sortsection.setVisibility(View.VISIBLE);
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    sortsection.setVisibility(View.INVISIBLE);
                }
                if (scrollState == SCROLL_STATE_FLING) {
                    sortsection.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if ((firstVisibleItem + visibleItemCount) != 0 && (firstVisibleItem + visibleItemCount) > 1) {

                    if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                        current = current + 1;
                        searchdata.addProperty("page", String.valueOf(current));
                        load = false;
                        sellerListViewModel.getSellerListData(Ced_SellerListing.this, ced_sessionManagement.getCurrentStore(),searchdata)
                                .observe(Ced_SellerListing.this, Ced_SellerListing.this::consumePaginationResponse);
                    }
                }
            }
        });
        sellerListViewModel.getSellerListData(this, ced_sessionManagement.getCurrentStore(),searchdata).observe(this, this::consumeResponse);
    }

    private void consumePaginationResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:

                    try {
                        applydata2_(apiResponse.data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void applydata2_(String Jstring) throws JSONException {
        JSONObject object = new JSONObject(Jstring);
        if (object.getJSONObject("data").getString("success").equals("false")) {
            load = false;
        } else {
            loadfrombottom = false;
            marketplacebanner.setVisibility(View.VISIBLE);
            status = object.getJSONObject("data").getString("success");
            if (status.equals("true")) {
                sellerjsonarray = object.getJSONObject("data").getJSONArray("sellers");
                for (int i = 0; i < sellerjsonarray.length(); i++) {
                    JSONObject sellerobject = sellerjsonarray.getJSONObject(i);
                    HashMap<String, String> seller = new HashMap<String, String>();
                    seller.put("entity_id", sellerobject.getString("entity_id"));
                    seller.put("public_name", sellerobject.getString("public_name"));
                    if (sellerobject.has("vendor_review")) {
                        if (sellerobject.getString("vendor_review").equals("false")) {
                            seller.put("review", "no_review");
                        } else {
                            seller.put("review", sellerobject.getString("vendor_review"));
                        }

                    } else {
                        seller.put("review", "no_review");
                    }
                    if (sellerobject.has("vendor_review_count")) {
                        seller.put("reviewcount", sellerobject.getString("vendor_review_count"));
                    } else {
                        seller.put("reviewcount", "no_count");
                    }
                    if (sellerobject.has("city") && sellerobject.has("country_id")) {
                        if (sellerobject.getString("city").isEmpty()) {
                            seller.put("citycountrey", "no info regarding address");
                        } else {
                            if (codecountry.containsKey(sellerobject.get("country_id"))) {
                                seller.put("citycountrey", sellerobject.get("city") + ", " + codecountry.get(sellerobject.get("country_id")));
                            } else {
                                seller.put("citycountrey", "no info regarding address");
                            }
                        }
                    } else {
                        seller.put("citycountrey", "no info regarding address");
                    }
                    if (sellerobject.has("company_banner")) {
                        seller.put("company_logo", sellerobject.getString("company_banner"));
                    } else {
                        seller.put("company_logo", "false");
                    }

                    finalsellerlist.add(seller);


                }

                sellerGridAdapter = new Ced_SellerGridAdapter(this, finalsellerlist);
                int cp = sellergrid.getFirstVisiblePosition();
                sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
                sellergrid.setDividerHeight(10);
                sellergrid.setAdapter(sellerGridAdapter);
                sellergrid.setSelection(cp + 1);
                sellerGridAdapter.notifyDataSetChanged();
                load = true;

            }
        }
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:

                    try {
                        applydata(Objects.requireNonNull(apiResponse.data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void applydata(String Jstring) throws JSONException {
        loadfrombottom = false;
        marketplacebanner.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject(Jstring);
        status = object.getJSONObject("data").getString("success");
        if (status.equals("true")) {
            String bannerurl = object.getJSONObject("data").getString("banner_url");
            sellerjsonarray = object.getJSONObject("data").getJSONArray("sellers");
            for (int i = 0; i < sellerjsonarray.length(); i++) {
                JSONObject sellerobject = sellerjsonarray.getJSONObject(i);
                HashMap<String, String> seller = new HashMap<String, String>();
                seller.put("entity_id", sellerobject.getString("entity_id"));
                seller.put("public_name", sellerobject.getString("public_name"));
                if (sellerobject.has("vendor_review")) {
                    if (sellerobject.getString("vendor_review").equals("false")) {
                        seller.put("review", "no_review");
                    } else {
                        seller.put("review", sellerobject.getString("vendor_review"));
                    }
                } else {
                    seller.put("review", "no_review");
                }
                if (sellerobject.has("vendor_review_count")) {
                    seller.put("reviewcount", sellerobject.getString("vendor_review_count"));
                } else {
                    seller.put("reviewcount", "no_count");
                }
                if (sellerobject.has("city") && sellerobject.has("country_id")) {
                    if (sellerobject.getString("city").isEmpty()) {
                        seller.put("citycountrey", "no info regarding address");
                    } else {
                        if (codecountry.containsKey(sellerobject.get("country_id"))) {
                            seller.put("citycountrey", sellerobject.get("city") + ", " + codecountry.get(sellerobject.get("country_id")));
                        } else {
                            seller.put("citycountrey", "no info regarding address");
                        }
                    }
                } else {
                    seller.put("citycountrey", "no info regarding address");
                }
                if (sellerobject.has("company_banner")) {
                    seller.put("company_logo", sellerobject.getString("company_banner"));
                } else {
                    seller.put("company_logo", "false");
                }
                finalsellerlist.add(seller);
            }
            /*Glide.with(Ced_SellerListing.this)
                    .load(bannerurl)
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(marketplacebanner);*/
            UpdateImage.showImage(Ced_SellerListing.this,bannerurl,R.drawable.bannerplaceholder,marketplacebanner);

            sellerGridAdapter = new Ced_SellerGridAdapter(this, finalsellerlist);
            sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
            sellergrid.setDividerHeight(10);
            sellergrid.setAdapter(sellerGridAdapter);
            load = true;
        } else {
            if (status.equals("false")) {
                View view = View.inflate(getApplicationContext(), R.layout.magenative_novendor, null);
                TextView cancel = (TextView) view.findViewById(R.id.magenative_canceldata);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(sellersearch.isEmpty())) {
                            sellersearch = "";
                            Intent intent = new Intent(getApplicationContext(), Ced_SellerListing.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            finish();
                        }
                    }
                });
                showmsg(object.getJSONObject("data").getString("message"));
                load = false;
                setContentView(view);
            }
        }

    }

    public void applydata2() throws JSONException {
        loadfrombottom = false;
        marketplacebanner.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject(Jstring);
        status = object.getJSONObject("data").getString("success");
        if (status.equals("true")) {
            sellerjsonarray = object.getJSONObject("data").getJSONArray("sellers");
            for (int i = 0; i < sellerjsonarray.length(); i++) {
                JSONObject sellerobject = sellerjsonarray.getJSONObject(i);
                HashMap<String, String> seller = new HashMap<String, String>();
                seller.put("entity_id", sellerobject.getString("entity_id"));
                seller.put("public_name", sellerobject.getString("public_name"));
                if (sellerobject.has("vendor_review")) {
                    if (sellerobject.getString("vendor_review").equals("false")) {
                        seller.put("review", "no_review");
                    } else {
                        seller.put("review", sellerobject.getString("vendor_review"));
                    }

                } else {
                    seller.put("review", "no_review");
                }
                if (sellerobject.has("vendor_review_count")) {
                    seller.put("reviewcount", sellerobject.getString("vendor_review_count"));
                } else {
                    seller.put("reviewcount", "no_count");
                }
                if (sellerobject.has("city") && sellerobject.has("country_id")) {
                    if (sellerobject.getString("city").isEmpty()) {
                        seller.put("citycountrey", "no info regarding address");
                    } else {
                        if (codecountry.containsKey(sellerobject.get("country_id"))) {
                            seller.put("citycountrey", sellerobject.get("city") + ", " + codecountry.get(sellerobject.get("country_id")));
                        } else {
                            seller.put("citycountrey", "no info regarding address");
                        }
                    }

                } else {
                    seller.put("citycountrey", "no info regarding address");
                }
                if (sellerobject.has("company_banner")) {
                    seller.put("company_logo", sellerobject.getString("company_banner"));
                } else {
                    seller.put("company_logo", "false");
                }

                finalsellerlist.add(seller);


            }

            sellerGridAdapter = new Ced_SellerGridAdapter(this, finalsellerlist);
            int cp = sellergrid.getFirstVisiblePosition();
            sellergrid.setDivider(new ColorDrawable(getResources().getColor(R.color.ebebebe)));
            sellergrid.setDividerHeight(10);
            sellergrid.setAdapter(sellerGridAdapter);
            sellergrid.setSelection(cp + 1);
            sellerGridAdapter.notifyDataSetChanged();
            load = true;

        }
    }

    private void showsearchpopup() throws JSONException {
        listDialog = new Dialog(this, R.style.PauseDialog);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MagenativeSellerSearchLayoutBinding binding = DataBindingUtil.inflate(li, R.layout.magenative_seller_search_layout, null, false);
        final EditText country = binding.magenativeCountry;
        final EditText state = binding.magenativeState;
        final EditText dropdown_state = binding.magenativeDropdownState;
        final TextInputLayout state_dropdown_layout = binding.stateDropdownLayout;
        final TextInputLayout state_layout =binding.stateLayout;
        final EditText zip = binding.magenativeZip;
        final EditText city = binding.magenativeCity;
        final EditText vendorname = binding.magenativeVendorname;
        final Button submit = binding.magenativeSubmit;

        country.requestFocus();
        country.callOnClick();

        if (!(sellersearch.isEmpty())) {
            JSONObject object = new JSONObject(sellersearch);
            if (codecountry.containsKey(object.getString("country"))) {
                country.setText(codecountry.get(object.getString("country")));
            }
            if (object.has("region_id")) {
                state_code = object.getString("region_id");
                state_label = object.getString("region_label");
                state.setText(state_label);
                dropdown_state.setText(state_label);
            } else {
                state.setText(object.getString("state"));
                dropdown_state.setText(object.getString("state"));
            }
            zip.setText(object.getString("zip"));
            vendorname.setText(object.getString("vendorname"));
            city.setText(object.getString("estimate_city"));
        }

        dropdown_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statecodelist != null) {
                    if (statecodelist.size() > 0 && statelabellist.size() > 0) {
                        nostates = false;
                        /*state.setEnabled(false);*/
                        final CharSequence[] arrayOfInt = (CharSequence[]) statecodelist.toArray(new CharSequence[statecodelist.size()]);
                        final CharSequence[] arrayOfInt2 = (CharSequence[]) statelabellist.toArray(new CharSequence[statelabellist.size()]);
                        Dialog levelDialog1 = new Dialog(Ced_SellerListing.this);
                       new MaterialAlertDialogBuilder(Ced_SellerListing.this,R.style.SingleChoiceRadioStyle)
                        .setTitle("Select Your  State")
                        .setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int postion) {
                                        state_code = (String) arrayOfInt[postion];
                                        state_label = (String) arrayOfInt2[postion];
                                        state.setText(state_label);
                                        dropdown_state.setText(state_label);
                                        dialog.dismiss();
                                    }
                                })
                       .show();
                    } else {
                        /*state.setEnabled(true);*/
                        nostates = true;
                    }
                } else {
                    if (country.getText().toString().equals("")) {
                        showmsg("Please select country first..");
                    }

                }
            }
        });


        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] arrayOfInt = (CharSequence[]) countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
                final CharSequence[] arrayOfInt2 = (CharSequence[]) countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
                Dialog levelDialog1 = new Dialog(Ced_SellerListing.this);
                new MaterialAlertDialogBuilder(Ced_SellerListing.this, R.style.SingleChoiceRadioStyle)
                .setTitle("Select Your  Country")
                .setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int postion) {
                                country_code = (String) arrayOfInt[postion];
                                country_label = (String) arrayOfInt2[postion];
                                country.setText(country_label);
                                state.setText("");
                                dropdown_state.setText("");
                                getState(country_code, state, dropdown_state, state_layout, state_dropdown_layout);
                                dialog.dismiss();
                            }
                        })
                .show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject seacrhseller = new JSONObject();
                try {
                    listDialog.dismiss();
                    seacrhseller.put("country", country_code);
                    if (state_code.isEmpty()) {
                        seacrhseller.put("state", state.getText().toString());
                    } else {
                        if (nostates) {
                            seacrhseller.put("region_label", state_label);
                        } else {
                            seacrhseller.put("region_id", state_code);
                        }

                    }
                    seacrhseller.put("zip", zip.getText().toString());
                    seacrhseller.put("vendorname", vendorname.getText().toString());
                    seacrhseller.put("estimate_city", city.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Ced_SellerListing.this, Ced_SellerListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("seller", "out");
                intent.putExtra("sellersearch", seacrhseller.toString());
                startActivity(intent);

            }
        });
        listDialog.setTitle(getResources().getString(R.string.app_name));
        listDialog.setContentView(binding.getRoot());
        listDialog.setCancelable(true);
        listDialog.show();
    }

    private void getcountries() {
        try {
            sellerListViewModel.getCountriesData(Ced_SellerListing.this,ced_sessionManagement.getCurrentStore()).observe(Ced_SellerListing.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countrycodelist.add(object.getString("value"));
                                countrylabellist.add(object.getString("label"));
                                codecountry.put(object.getString("value"), object.getString("label"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getState(String country_code, EditText state, final EditText dropdown_state, TextInputLayout state_layout, TextInputLayout state_dropdown_layout) {
        sellerListViewModel.getStates(this,ced_sessionManagement.getCurrentStore(), String.valueOf(country_code)).observe(Ced_SellerListing.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {

                            JSONObject object = new JSONObject(apiResponse.data);
                            Boolean status = object.getBoolean("success");
                            if (status.equals(true) && object.getJSONArray("states").length() > 0) {
                                JSONArray jsonArray = object.getJSONArray("states");
                                statelabellist = new ArrayList<String>();
                                statecodelist = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    statecodelist.add(c.getString("region_id"));
                                    statelabellist.add(c.getString("default_name"));
                                }
                                // state.setHint("select your state here...");
                                state.setEnabled(false);
                                state_layout.setVisibility(View.GONE);
                                state_dropdown_layout.setVisibility(View.VISIBLE);
                                state.setText("");
                                dropdown_state.setText("");
                                dropdown_state.requestFocus();
                                state.callOnClick();
                            } else {
                                //  state.setHint("State");
                                state.setEnabled(true);
                                state_layout.setVisibility(View.VISIBLE);
                                state_dropdown_layout.setVisibility(View.GONE);
                                state.requestFocus();
                            }


                        ;
                    } catch (JSONException e) {
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
}

