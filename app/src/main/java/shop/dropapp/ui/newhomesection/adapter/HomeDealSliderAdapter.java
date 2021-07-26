package shop.dropapp.ui.newhomesection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.R;
import shop.dropapp.databinding.HomeDealItemBinding;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.utils.UpdateImage;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeDealSliderAdapter extends RecyclerView.Adapter<HomeDealSliderAdapter.HomecategoryListViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;

    public HomeDealSliderAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public HomecategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        HomeDealItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_deal_item,parent,false);
        return new HomecategoryListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomecategoryListViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String link_to=jsonObject.getString("link_to");
            String product_id=jsonObject.getString("product_id");
            holder.binding.image.getLayoutParams().width= (Magenative_HomePageNewTheme.device_width/(2));
            holder.binding.image.getLayoutParams().height= (Magenative_HomePageNewTheme.device_width/(3));
           /* Glide.with(context)
                    .load(jsonObject.getString("deal_image_name"))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.image);*/

            UpdateImage.showImage(context,jsonObject.getString("deal_image_name"),R.drawable.placeholder,holder.binding.image);
            if(context instanceof Magenative_HomePageNewTheme)
            {
                ((Magenative_HomePageNewTheme)context).BannerIntent(holder.binding.image,link_to,product_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class HomecategoryListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public HomeDealItemBinding binding;
        public HomecategoryListViewHolder(HomeDealItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
