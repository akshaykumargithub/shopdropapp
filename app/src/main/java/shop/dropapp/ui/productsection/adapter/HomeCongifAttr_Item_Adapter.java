package shop.dropapp.ui.productsection.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shop.dropapp.R;
import shop.dropapp.databinding.SingleItemBinding;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeCongifAttr_Item_Adapter extends RecyclerView.Adapter<HomeCongifAttr_Item_Adapter.ViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    LayoutInflater layoutInflater;
   private String row_id;
   private String row_position;
   private JSONObject attribute_index;
   private RecyclerView thisrecyler;

    public HomeCongifAttr_Item_Adapter(Context context, JSONArray jsonArray, String row_id, String row_position, JSONObject attribute_index,RecyclerView thisrecyler) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.row_id = row_id;
        this.row_position = row_position;
        this.attribute_index = attribute_index;
        this.thisrecyler = thisrecyler;
    }

    @NonNull
    @Override
    public HomeCongifAttr_Item_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(context).inflate(R.layout.others_category_list_item, parent, false);
        return new ViewHolder(view);*/
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SingleItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.single_item,parent,false);
        return new HomeCongifAttr_Item_Adapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCongifAttr_Item_Adapter.ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String id=jsonObject.getString("id");
            String label=jsonObject.getString("label");
            String value=jsonObject.getString("value");
            if(value.contains("#"))
            {
                holder.binding.text.setText("");

                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                Log.d("REpo", "colorr: "+value);
                gradientDrawable.setColor(Color.parseColor(value));
                holder.binding.text.setBackground(gradientDrawable);

            }
            else
            {
                holder.binding.text.setText(label);
            }

           holder.binding.card.setBackground(context.getResources().getDrawable(R.drawable.cardcorner));
           holder.binding.card.setCardElevation(2);
           holder.binding.card.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(context instanceof Ced_NewProductView){
                       ((Ced_NewProductView)context).unselectallselectedimages(thisrecyler);
                   }
                   holder.binding.card.setBackground(context.getResources().getDrawable(R.drawable.border_image_gallery_selected));
                   Ced_NewProductView.selected_row_image_id.remove(Integer.valueOf(row_position));
                   Ced_NewProductView.selected_row_image_id.put(Integer.valueOf(row_position),row_id+"#"+id);
                   try {
                       //-remove old selected index value---
                       Ced_NewProductView.selected_config_row_values.remove(row_id);
                       Ced_NewProductView.selected_config_row_values.put((row_id),id);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   if((Ced_NewProductView.totalconfig_attributes==Ced_NewProductView.selected_row_image_id.size()) && context instanceof Ced_NewProductView)
                   {
                       String imageindex_key="";
                       for(int k=0;k<Ced_NewProductView.selected_row_image_id.size();k++)
                       {
                           if(k==0)
                           {
                               imageindex_key+=Ced_NewProductView.selected_row_image_id.get(k);
                           }
                           else
                           {
                               imageindex_key+="#"+Ced_NewProductView.selected_row_image_id.get(k);
                           }
                           Log.d("REpo", "imageindex_key: "+imageindex_key);
                       }

                       ((Ced_NewProductView)context).SearchImage2(imageindex_key,attribute_index);
                   }
               }
           });

           if(position==0)
           {
               holder.binding.card.setBackground(context.getResources().getDrawable(R.drawable.border_image_gallery_selected));
               holder.binding.card.callOnClick();
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {

        public SingleItemBinding binding;
        public ViewHolder(SingleItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
