package shop.dropapp.ui.newhomesection.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.R;
import shop.dropapp.databinding.HomeCategoryListItemBinding;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.ui.productsection.activity.Ced_New_Product_Listing;
import shop.dropapp.ui.sellersection.activity.Ced_Seller_Shop;
import shop.dropapp.utils.UpdateImage;
import shop.dropapp.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeCategoryListAdapter extends RecyclerView.Adapter<HomeCategoryListAdapter.HomecategoryListViewHolder> {

    private final Context context;
    private final JSONArray jsonArray;
    private LayoutInflater layoutInflater;
    private final String category_type;
    private final String category_shape;

    public HomeCategoryListAdapter(Context context, JSONArray jsonArray, String category_type, String category_shape) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.category_type = category_type;
        this.category_shape = category_shape;
    }

    @NonNull
    @Override
    public HomecategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        HomeCategoryListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.home_category_list_item, parent, false);
        return new HomecategoryListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomecategoryListViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            switch (category_shape) {
                case "circular":
                    if (category_type.equalsIgnoreCase("grid")) {
                        holder.binding.circularImage.getLayoutParams().width = (int) ((Magenative_HomePageNewTheme.device_width / 3) - 25);
                        holder.binding.circularImage.getLayoutParams().height = (int) ((Magenative_HomePageNewTheme.device_width / 3) - 25);
                    }
                    holder.binding.circularImage.setVisibility(View.VISIBLE);
                    holder.binding.categoryImageCard.setVisibility(View.GONE);
                    UpdateImage.showImage(context, jsonObject.getString("image"), R.drawable.placeholder, holder.binding.circularImage);
                    holder.binding.circularImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                                intent.putExtra("ID", jsonArray.getJSONObject(position).getString("id"));
                                context.startActivity(intent);
                                ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case "square":
                    if (category_type.equalsIgnoreCase("grid")) {
                        holder.binding.categoryImageCard.getLayoutParams().width = (int) ((Magenative_HomePageNewTheme.device_width / 3));
                        holder.binding.categoryImageCard.getLayoutParams().height = (int) ((Magenative_HomePageNewTheme.device_width / 3));
                    }
                    holder.binding.circularImage.setVisibility(View.GONE);
                    holder.binding.categoryImageCard.setVisibility(View.VISIBLE);

                    if (jsonObject.has("company_logo") && jsonObject.getString("company_logo") != null) {
                        UpdateImage.showImage(context, jsonObject.getString("company_banner"), R.drawable.placeholder, holder.binding.categoryImage);
                    } else if (jsonObject.has("image") && jsonObject.getString("image") != null) {
                        UpdateImage.showImage(context, jsonObject.getString("image"), R.drawable.placeholder, holder.binding.categoryImage);
                    }
                    holder.binding.categoryImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = null;
                                if (jsonArray.getJSONObject(position).has("vendor_id")) {
                                    Log.i("123414", "onClick: "+ jsonArray.getJSONObject(position).getString("vendor_id"));
                                    intent = new Intent(context, Ced_Seller_Shop.class);
                                    intent.putExtra("ID", jsonArray.getJSONObject(position).getString("vendor_id"));
                                    context.startActivity(intent);
                                    ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                } else {
                                    intent = new Intent(context, Ced_New_Product_Listing.class);
                                    intent.putExtra("ID", jsonArray.getJSONObject(position).getString("id"));
                                    context.startActivity(intent);
                                    ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                default:
                    holder.binding.circularImage.setVisibility(View.GONE);
                    holder.binding.categoryImageCard.setVisibility(View.GONE);
                    break;
            }
            if (jsonObject.has("name")) {
                holder.binding.categoryName.setText(jsonObject.getString("name"));
            } else if (jsonObject.has("public_name")) {
                holder.binding.categoryName.setText(jsonObject.getString("public_name"));
            } else {
                Log.i(Urls.TAG, "onBindViewHolder: " + jsonObject);
            }
            Log.d(Urls.TAG, "onBindViewHolder: " + holder.binding.categoryImage.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class HomecategoryListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public HomeCategoryListItemBinding binding;

        public HomecategoryListViewHolder(HomeCategoryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

