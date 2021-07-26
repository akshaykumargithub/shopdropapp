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
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */

package shop.dropapp.ui.orderssection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import shop.dropapp.databinding.MagenativeShoworderPageBinding;
import shop.dropapp.databinding.NoorderBinding;
import shop.dropapp.ui.loginsection.activity.Ced_Login;
import shop.dropapp.ui.orderssection.adapter.OrderRecyclerAdapter;
import shop.dropapp.ui.orderssection.viewmodel.OrderViewModel;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by developer on 9/27/2015.
 */
@AndroidEntryPoint
public class Ced_ShowOrder extends Ced_NavigationActivity {
    MagenativeShoworderPageBinding orderPageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    OrderViewModel orderViewModel;
    JsonObject hashmap;
    String Url = "";
    static final String KEY_ITEM = "data";
    static final String KEY_NAME = "orderdata";
    static final String KEY_TOTAL_AMOUNT = "total_amount";
    static final String KEY_ID = "order_id";
    static final String KEY_DATE = "date";
    static final String KEY_SHIP_TO = "ship_to";
    static final String KEY_ORDER_ID = "number";
    static final String KEY_ORDER_STATUS = "order_status";
    static final String KEY_STATUS = "status";
    RecyclerView OrderList;
    ArrayList<HashMap<String, String>> Orderinfo;
    int current = 1;
    private boolean loading = false;
    HashMap<HashMap<String, String>, HashMap<String, String>> orderdata;
    HashMap<String, HashMap<String, String>> order_data_map;
    ArrayList<String> order_data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderViewModel = new ViewModelProvider(Ced_ShowOrder.this, viewModelFactory).get(OrderViewModel.class);
        hashmap = new JsonObject();
        Orderinfo = new ArrayList<>();
        orderdata = new HashMap<>();
        order_data_map = new HashMap<>();
        order_data_list = new ArrayList<>();

        if (session.isLoggedIn()) {
            orderPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_showorder_page, content, true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
            OrderList = orderPageBinding.MageNativeOrderlist;
            try {
                hashmap.addProperty("customer_id", session.getCustomerid());
                hashmap.addProperty("page", current);
                if (cedSessionManagement.getStoreId() != null) {
                    hashmap.addProperty("store_id", cedSessionManagement.getStoreId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            OrderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !loading) {
                        Log.i("onScrollStateChanged", "loadmore");
                        loading = true;
                        //-------------------
                        current = current + 1;
                        hashmap.addProperty("page", current);
                        orderViewModel.getOrdersData(Ced_ShowOrder.this, cedSessionManagement.getCurrentStore(),hashmap,session.getHahkey()).observe(Ced_ShowOrder.this, Ced_ShowOrder.this::Response);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            orderViewModel.getOrdersData(Ced_ShowOrder.this, cedSessionManagement.getCurrentStore(),hashmap,session.getHahkey()).observe(Ced_ShowOrder.this,Ced_ShowOrder.this::Response);

        } else {
            Intent goto_login = new Intent(getApplicationContext(), Ced_Login.class);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            finish();
            startActivity(goto_login);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void Response(ApiResponse apiResponse) {
            switch (apiResponse.status){
                case SUCCESS:
                    applydata(apiResponse.data);
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }

    }

    private void applydata(String Jstring) {
        try {
            JSONObject jsonObject = new JSONObject(Jstring);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("no_order") && current==1) {
                    NoorderBinding noorderBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.noorder, content,true);
                    noorderBinding.conti.setOnClickListener(v -> finish());
                } else {
                   /* orderdetail = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME);
                    for (int i = 0; i < orderdetail.length(); i++) {
                        HashMap<String, String> data2 = new HashMap<>();
                        JSONObject c = null;
                        c = orderdetail.getJSONObject(i);
                        String total_amount, date, id, ship_to, order_id, order_status = null;
                        total_amount = c.getString(KEY_TOTAL_AMOUNT);
                        date = c.getString(KEY_DATE);
                        ship_to = c.getString(KEY_SHIP_TO);
                        order_id = c.getString(KEY_ORDER_ID);
                        order_status = c.getString(KEY_ORDER_STATUS);
                        data2.put("date", date);
                        data2.put("ship_to", ship_to);
                        data2.put("total_amount", total_amount);
                        data2.put("order_status", order_status);
                        data2.put("qty_ordered", order_id);
                        data2.put("order_id", order_id);
                        Orderinfo.add(data2);
                    }*/
                    OrderRecyclerAdapter recyclerAdapter = new OrderRecyclerAdapter(Ced_ShowOrder.this, jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME));
                    OrderList.setLayoutManager(new LinearLayoutManager(Ced_ShowOrder.this, RecyclerView.VERTICAL, false));
                    OrderList.setAdapter(recyclerAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
//        OrderList.setIndicatorBounds(OrderList.getRight() - 40, OrderList.getWidth());
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        OrderList.setIndicatorBounds(OrderList.getRight() - 40, OrderList.getWidth());
    }

}