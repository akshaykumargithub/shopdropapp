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
package shop.dropapp.ui.sellersection.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.utils.UpdateImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_SellerGridAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public Ced_SellerGridAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.magenative_sellerinfo, null);

        TextView Shopname = vi.findViewById(R.id.magenative_ShopName);
        TextView Shopid = vi.findViewById(R.id.magenative_ShopId);
        ImageView ShopImage = vi.findViewById(R.id.magenative_shopImage);
        TextView address = vi.findViewById(R.id.magenative_address);
        TextView averagerating = vi.findViewById(R.id.magenative_averagerating);
        TextView reviewcount = vi.findViewById(R.id.magenative_reviewcount);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(Shopname);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(address);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(reviewcount);
        ((Ced_NavigationActivity)activity).set_bold_font_fortext(averagerating);
        HashMap<String, String> song;
        song = data.get(position);

        if (song.containsKey("citycountrey")) {
            address.setText(song.get("citycountrey"));
        }

        if (Objects.requireNonNull(song.get("review")).equals("no_review")) {
            averagerating.setVisibility(View.INVISIBLE);
        } else {
            averagerating.setVisibility(View.VISIBLE);
            averagerating.setText(song.get("review"));
            Float aFloat = Float.valueOf(averagerating.getText().toString());
            if (aFloat < 2) {
                averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner_red));
            }
            if (aFloat >= 2 && aFloat < 4) {
                averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner_yellow));
                averagerating.setTextColor(activity.getResources().getColor(R.color.black));
            }
            if (aFloat >= 4) {
                averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner));
            }
        }

        if (Objects.requireNonNull(song.get("reviewcount")).equals("no_count")) {
            reviewcount.setVisibility(View.INVISIBLE);
        } else {
            reviewcount.setVisibility(View.VISIBLE);
            reviewcount.setText(song.get("reviewcount") + " Reviews");
        }

        Shopname.setText(song.get("public_name"));
        Shopid.setText(song.get("entity_id"));

        if (Objects.requireNonNull(song.get("company_logo")).equals("false")) {
            ShopImage.setImageResource(R.drawable.bannerplaceholder);
        } else {
            /*Glide.with(activity)
                    .load(song.get("company_logo"))

                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(ShopImage);*/

            UpdateImage.showImage(activity,song.get("company_logo"),R.drawable.bannerplaceholder,ShopImage);
        }

        return vi;
    }

}