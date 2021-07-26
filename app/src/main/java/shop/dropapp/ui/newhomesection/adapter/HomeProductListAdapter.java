package shop.dropapp.ui.newhomesection.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.R;
import shop.dropapp.databinding.ProductListItemBinding;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;
import shop.dropapp.utils.UpdateImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeProductListAdapter extends RecyclerView.Adapter<HomeProductListAdapter.HomeProductListViewHolder> {

    LayoutInflater layoutInflater;
    private Context context;
    private JSONArray jsonArray;


    public HomeProductListAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public HomeProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new HomeProductListViewHolder(view);*/

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ProductListItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.product_list_item,parent,false);
        return new HomeProductListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductListViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            /*Glide.with(context)
                    .load(jsonObject.getString("product_image"))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.productImage);*/

            UpdateImage.showImage(context,jsonObject.getString("product_image"),R.drawable.placeholder,holder.binding.productImage);
            if (jsonObject.getString("Inwishlist").equals("IN")) {
                holder.binding.wishlistIcon.setImageResource(R.drawable.wishred);
            } else {
                holder.binding.wishlistIcon.setVisibility(View.GONE);
            }
            holder.binding.productName.setText(jsonObject.getString("product_name"));
            if (jsonObject.has("offer")) {
                holder.binding.offerText.setText(jsonObject.getString("offer") + "% off");
            } else {
                holder.binding.offerText.setVisibility(View.GONE);
            }
            if (jsonObject.has("special_price")) {
                holder.binding.regularPrice.setText(jsonObject.getString("special_price"));
            } else {
                holder.binding.regularPrice.setVisibility(View.GONE);
            }
            if (jsonObject.has("regular_price")) {
                holder.binding.specialPrice.setText(jsonObject.getString("regular_price"));
                holder.binding.specialPrice.setPaintFlags(holder.binding.specialPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.binding.specialPrice.setVisibility(View.GONE);
            }

            if (jsonObject.getString("special_price").equalsIgnoreCase("no_special")) {
                holder.binding.specialPrice.setVisibility(View.GONE);
                holder.binding.regularPrice.setText(jsonObject.getString("regular_price"));

            }

            holder.binding.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(context, Ced_NewProductView.class);
                        intent.putExtra("product_id", jsonArray.getJSONObject(position).getString("product_id"));
                        context.startActivity(intent);
                        ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                 }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class HomeProductListViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public ProductListItemBinding binding;
        public HomeProductListViewHolder(ProductListItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
/*
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.productImage.getId()) {
                try {
                    Intent intent = new Intent(context, Ced_NewProductView.class);
                    intent.putExtra("product_id", jsonArray.getJSONObject(getAdapterPosition()).getString("product_id"));
                    context.startActivity(intent);
                    ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

    }
}
