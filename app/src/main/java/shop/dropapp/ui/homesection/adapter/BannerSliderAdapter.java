package shop.dropapp.ui.homesection.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import shop.dropapp.R;
import shop.dropapp.ui.websection.Ced_Weblink;
import shop.dropapp.databinding.MagenativeBannerLayoutBinding;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;
import shop.dropapp.ui.productsection.activity.Ced_New_Product_Listing;
import shop.dropapp.utils.UpdateImage;

import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class BannerSliderAdapter extends SliderViewAdapter<BannerSliderAdapter.SliderViewHolder> {
    private Context context;
    private JSONArray images;

    public BannerSliderAdapter(Context context, JSONArray images){
        this.context = context;
        this.images = images;
    }

    @Override
    public BannerSliderAdapter.SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        MagenativeBannerLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.magenative_banner_layout, parent, false);
        return new SliderViewHolder(layoutBinding);
    }

    @Override
    public void onBindViewHolder(BannerSliderAdapter.SliderViewHolder viewHolder, int position) {
        try{
            JSONObject bannerImage = images.getJSONObject(position);

            viewHolder.layoutBinding.linkTo.setText(bannerImage.getString("link_to"));
            viewHolder.layoutBinding.id.setText(bannerImage.getString("id"));

/*            Glide.with(context)
                    .load(bannerImage.getString("banner_image"))
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(viewHolder.layoutBinding.MageNativeBannerimage);*/

            UpdateImage.showImage(context,bannerImage.getString("banner_image"),R.drawable.bannerplaceholder,viewHolder.layoutBinding.MageNativeBannerimage);

            viewHolder.layoutBinding.MageNativeBannerimage.setOnClickListener(view -> {
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("category")) {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("product")) {
                    Intent prod_link = new Intent(context, Ced_NewProductView.class);
                    prod_link.putExtra("product_id", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(prod_link);
                    ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (viewHolder.layoutBinding.linkTo.getText().toString().equals("website")) {
                    Intent weblink = new Intent(context, Ced_Weblink.class);
                    weblink.putExtra("link", viewHolder.layoutBinding.id.getText().toString());
                    context.startActivity(weblink);
                    ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return images.length();
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        MagenativeBannerLayoutBinding layoutBinding;

        public SliderViewHolder(MagenativeBannerLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());

            this.layoutBinding = layoutBinding;
        }
    }
}
