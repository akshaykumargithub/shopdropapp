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

package shop.dropapp.ui.orderssection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.databinding.MagenativeOrderplacedListBinding;
import shop.dropapp.ui.orderssection.activity.Ced_Orderview;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;

import shop.dropapp.R;
import shop.dropapp.utils.UpdateImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Developer on 2/1/2016.
 */
public class Ced_OrderViewAdapter extends RecyclerView.Adapter<Ced_OrderViewAdapter.OrderViewHolder> {
    private Context act;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private HashMap<String, HashMap<String, String>> value_config;
    private boolean config_flag = false;
    private int count_config = 1;

    public Ced_OrderViewAdapter(Context orderview, ArrayList<HashMap<String, String>> orderinfo) {
        act = orderview;
        data = orderinfo;
    }

    public Ced_OrderViewAdapter(Context orderview, ArrayList<HashMap<String, String>> orderinfo, HashMap<String, HashMap<String, String>> finalconfigdata) {
        act = orderview;
        data = orderinfo;
        value_config = finalconfigdata;
    }

    /*@Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }*/

    @NonNull
    @Override
    public Ced_OrderViewAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MagenativeOrderplacedListBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.magenative_orderplaced_list, parent, false);
        return new OrderViewHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Ced_OrderViewAdapter.OrderViewHolder holder, int position) {
        HashMap<String, String> order_list = data.get(position);

        holder.listBinding.MageNativeProdName.setOnClickListener(v -> {
            ArrayList<String> IDS = new ArrayList<String>();
            Intent prod_link = new Intent(act, Ced_NewProductView.class);
            prod_link.putExtra("product_id", holder.listBinding.MageNativeProdId.getText().toString());
            act.startActivity(prod_link);
        });

        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeProdName);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeProdPrice);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeProdQuantity);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeProdRowSubtotal);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeQtyText);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeAmountTxt);
        ((Ced_NavigationActivity)act).set_regular_font_fortext(holder.listBinding.MageNativeQtyText);


        holder.listBinding.MageNativeProdName.setText(order_list.get(Ced_Orderview.KEY_PRODUCT_NAME));
        holder.listBinding.MageNativeProdType.setText(order_list.get(Ced_Orderview.KEY_PRODUCT_TYPE));
        holder.listBinding.MageNativeProdPrice.setText(order_list.get(Ced_Orderview.KEY_PRODUCT_PRICE));
        holder.listBinding.MageNativeProdQuantity.setText(order_list.get(Ced_Orderview.KEY_PRODUCT_QTY));
        holder.listBinding.MageNativeProdRowSubtotal.setText(order_list.get(Ced_Orderview.KEY_ROW_SUB_TOTAL));
        holder.listBinding.MageNativeProdId.setText(order_list.get(Ced_Orderview.KEY_PRODUCT_ID));

      /*  Glide.with(act)
                .load(order_list.get(Ced_Orderview.KEY_PRODUCT_IMAGE))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .override(120, 120)
                // optional// optional
                .into(holder.listBinding.MageNativeProdImg);*/

        UpdateImage.showImage(act,order_list.get(Ced_Orderview.KEY_PRODUCT_IMAGE),R.drawable.placeholder,holder.listBinding.MageNativeProdImg);

        if (holder.listBinding.MageNativeProdType.getText().toString().equals("configurable") ||
                holder.listBinding.MageNativeProdType.getText().toString().equals("bundle") ||
                holder.listBinding.MageNativeProdType.getText().toString().equals("downloadable")) {
            holder.listBinding.MageNativeConfioption.setVisibility(View.VISIBLE);
            HashMap<String, String> config_data = value_config.get(order_list.get(Ced_Orderview.KEY_PRODUCT_ID));
            if (config_flag) {
                holder.listBinding.MageNativeConfioption.removeAllViewsInLayout();
            }

            Iterator iterator = config_data.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                String key = String.valueOf(pair.getKey());
                String value = (String) pair.getValue();

                LinearLayout layout = new LinearLayout(act);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView label = new TextView(act);
                label.setTextSize(12);
                ((Ced_NavigationActivity)act).set_regular_font_fortext(label);
                label.setTextColor(act.getResources().getColor(R.color.black));
                label.setText(value);
                label.setPadding(0, 0, 12, 0);
                layout.addView(label, 0);

                TextView optionvalue = new TextView(act);
                optionvalue.setText(key);
                optionvalue.setTextSize(12);
                ((Ced_NavigationActivity)act).set_regular_font_fortext(optionvalue);
                optionvalue.setTextColor(act.getResources().getColor(R.color.black));
                layout.addView(optionvalue, 1);
                layout.setPadding(0, 0, 0, 8);

                holder.listBinding.MageNativeConfioption.addView(layout);
                config_flag = true;
            }
            count_config++;
        }


        holder.listBinding.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prod_link = new Intent(act, Ced_NewProductView.class);
                prod_link.putExtra("product_id", holder.listBinding.MageNativeProdId.getText().toString());
                act.startActivity(prod_link);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        MagenativeOrderplacedListBinding listBinding;

        public OrderViewHolder(@NonNull MagenativeOrderplacedListBinding listBinding) {
            super(listBinding.getRoot());

            this.listBinding = listBinding;
        }
    }
}