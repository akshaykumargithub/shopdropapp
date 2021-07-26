package shop.dropapp.ui.orderssection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.ui.orderssection.activity.Ced_Orderview;
import shop.dropapp.R;
import shop.dropapp.databinding.ListGroupBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {
    private Context context;
    private JSONArray data;

    /* public OrderRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> data){
         this.context = context;
         this.data = data;

     }*/
    public OrderRecyclerAdapter(Context context, JSONArray data){
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListGroupBinding listGroupBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group, parent, false);
        return new OrderViewHolder(listGroupBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        try {
            JSONObject order = data.getJSONObject(position);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.ordertext);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderId);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.placedontext);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderDate);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.itemsText);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.itemsTextValue);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.amountTextValue);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.shipToText);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.shipToTextValue);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderStatusText);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderStatusTextValue);

            holder.listGroupBinding.orderId.setText(order.getString("order_id"));
            holder.listGroupBinding.orderDate.setText(order.getString("date"));

//        holder.listGroupBinding.itemsTextValue.setText(order.getString("qty_ordered"));
            holder.listGroupBinding.amountTextValue.setText(order.getString("total_amount"));
            holder.listGroupBinding.shipToTextValue.setText(order.getString("ship_to"));
            holder.listGroupBinding.orderStatusTextValue.setText(order.getString("order_status"));
            holder.listGroupBinding.itemsTextValue.setText(order.getString("order_id"));

            holder.listGroupBinding.orderInfo.setOnClickListener(v -> {
                Intent orderview_intent = new Intent(context, Ced_Orderview.class);
                orderview_intent.putExtra("orderid", holder.listGroupBinding.itemsTextValue.getText().toString());
                context.startActivity(orderview_intent);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ListGroupBinding listGroupBinding;

        public OrderViewHolder(@NonNull ListGroupBinding listGroupBinding) {
            super(listGroupBinding.getRoot());

            this.listGroupBinding = listGroupBinding;
        }
    }
}


/*
public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> data;

    public OrderRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> data){
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListGroupBinding listGroupBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group, parent, false);
        return new OrderViewHolder(listGroupBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        HashMap<String, String> order = data.get(position);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.ordertext);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderId);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.placedontext);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderDate);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.itemsText);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.itemsTextValue);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.amountTextValue);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.shipToText);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.shipToTextValue);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderStatusText);
        ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.listGroupBinding.orderStatusTextValue);

        holder.listGroupBinding.orderId.setText(order.get("order_id"));
        holder.listGroupBinding.orderDate.setText(order.get("date"));

        holder.listGroupBinding.amountTextValue.setText(order.get("total_amount"));
        holder.listGroupBinding.shipToTextValue.setText(order.get("ship_to"));
        holder.listGroupBinding.orderStatusTextValue.setText(order.get("order_status"));
        holder.listGroupBinding.itemsTextValue.setText(order.get("order_id"));

        holder.listGroupBinding.orderInfo.setOnClickListener(v -> {
            Intent orderview_intent = new Intent(context, Ced_Orderview.class);
            orderview_intent.putExtra("orderid", holder.listGroupBinding.orderId.getText().toString());
            context.startActivity(orderview_intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ListGroupBinding listGroupBinding;

        public OrderViewHolder(@NonNull ListGroupBinding listGroupBinding) {
            super(listGroupBinding.getRoot());

            this.listGroupBinding = listGroupBinding;
        }
    }
}
*/
