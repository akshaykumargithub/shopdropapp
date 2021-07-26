package shop.dropapp.ui.homesection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import shop.dropapp.R;
import shop.dropapp.ui.websection.Ced_Weblink;
import shop.dropapp.databinding.DealLayoutBinding;
import shop.dropapp.ui.homesection.activity.Ced_New_home_page;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;
import shop.dropapp.ui.productsection.activity.Ced_New_Product_Listing;
import shop.dropapp.utils.UpdateImage;

import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class DealsBannerAdapter extends SliderViewAdapter<DealsBannerAdapter.DealsBannerViewHolder> {
    private Context context;
    private JSONArray content;

    public DealsBannerAdapter(Context context, JSONArray content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public DealsBannerViewHolder onCreateViewHolder(ViewGroup parent) {
        DealLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.deal_layout, parent, false);
        return new DealsBannerViewHolder(layoutBinding);
    }

    @Override
    public void onBindViewHolder(DealsBannerViewHolder viewHolder, int position) {
        try{
            JSONObject deal = content.getJSONObject(position);

            viewHolder.layoutBinding.linkTo.setText(deal.getString("deal_type"));

            if(deal.getString("deal_type").equals("1")){
                viewHolder.layoutBinding.id.setText(deal.getString("product_link"));
            }
            if(deal.getString("deal_type").equals("2")){
                viewHolder.layoutBinding.id.setText(deal.getString("category_link"));
            }
            if(deal.getString("deal_type").equals("3")){
                viewHolder.layoutBinding.id.setText(deal.getString("static_link"));
            }

            viewHolder.layoutBinding.offer.setText(deal.getString("offer_text"));

          /*  Glide.with(context)
                    .load(deal.getString("deal_image_name"))
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(viewHolder.layoutBinding.MageNativeBannerimage);*/

            UpdateImage.showImage(context,deal.getString("deal_image_name"),R.drawable.bannerplaceholder,viewHolder.layoutBinding.MageNativeBannerimage);

            viewHolder.layoutBinding.MageNativeBannerimage.setOnClickListener(v -> {
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("2")) {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(intent);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("1")) {
                    Intent prod_link = new Intent(context, Ced_NewProductView.class);
                    prod_link.putExtra("product_id", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(prod_link);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("3")) {
                    Intent weblink = new Intent(context, Ced_Weblink.class);
                    weblink.putExtra("link", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(weblink);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return content.length();
    }

    public class DealsBannerViewHolder extends SliderViewAdapter.ViewHolder {
        DealLayoutBinding layoutBinding;

        public DealsBannerViewHolder(DealLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());

            this.layoutBinding = layoutBinding;
        }
    }
}
