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
package shop.dropapp.ui.sellersection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;

import shop.dropapp.Keys;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.databinding.MagenativeListViewBinding;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.productsection.activity.Ced_New_Product_Listing;
import shop.dropapp.ui.sellersection.viewmodel.SellerShopViewModel;
import shop.dropapp.utils.Status;
import shop.dropapp.utils.UpdateImage;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.utils.Status.SUCCESS;

@AndroidEntryPoint
public class Ced_Seller_Shop extends Ced_NavigationActivity {
    MagenativeListViewBinding magenativeListViewBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SellerShopViewModel sellerShopViewModel;
    private JSONObject json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magenativeListViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_list_view, content, true);
        sellerShopViewModel = new ViewModelProvider(Ced_Seller_Shop.this, viewModelFactory).get(SellerShopViewModel.class);
        JsonObject params = new JsonObject();
        params.addProperty(Keys.VENDOR_ID, getIntent().getStringExtra("ID"));
        sellerShopViewModel.getSellerCategories(this,cedSessionManagement.getCurrentStore(), params).observe(this, this::getDetails);

    }

    private void getDetails(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    if (apiResponse.status == SUCCESS) {
                        json = new JSONObject(apiResponse.data.toString());
                        if (json.has(Keys.SUCCESS) && json.getBoolean(Keys.SUCCESS)) {
                            if (json.has(Keys.VENDOR_PROFILE)) {
                                JSONObject vendorProfObj = json.getJSONObject(Keys.VENDOR_PROFILE);
                                if (vendorProfObj.has(Keys.PUBLIC_NAME)) {
                                    magenativeListViewBinding.vendorPublicName.setVisibility(View.VISIBLE);
                                    magenativeListViewBinding.vendorPublicName.setText(getString(R.string.namee) + ": " + vendorProfObj.getString(Keys.PUBLIC_NAME));
                                }
                                if (vendorProfObj.has(Keys.EMAIL)) {
                                    magenativeListViewBinding.vendorEmail.setVisibility(View.VISIBLE);
                                    magenativeListViewBinding.vendorEmail.setText(getString(R.string.email) + ": " + vendorProfObj.getString(Keys.EMAIL));
                                }
                                if (vendorProfObj.has(Keys.COMPANY_ADDRESS)) {
                                    magenativeListViewBinding.vendorAddress.setVisibility(View.VISIBLE);
                                    magenativeListViewBinding.vendorAddress.setText(getString(R.string.address) + ": " + vendorProfObj.getString(Keys.COMPANY_ADDRESS));
                                }
                                if (vendorProfObj.has(Keys.SUPPORT_NUMBER)) {
                                    magenativeListViewBinding.mobileNumber.setVisibility(View.VISIBLE);
                                    magenativeListViewBinding.mobileNumber.setText(getString(R.string.number) + ": " + vendorProfObj.getString(Keys.SUPPORT_NUMBER));
                                }
                                if (vendorProfObj.has(Keys.PROFILE_PICTURE)) {
                                    UpdateImage.showImage(Ced_Seller_Shop.this, vendorProfObj.getString(Keys.PROFILE_PICTURE), R.drawable.placeholder, magenativeListViewBinding.vendorProfileImage);
                                }
                                if (vendorProfObj.has(Keys.BANNER_URL)) {
                                    UpdateImage.showImage(Ced_Seller_Shop.this, vendorProfObj.getString(Keys.BANNER_URL), R.drawable.placeholder, magenativeListViewBinding.vendorBanner);
                                }
                                if (json.has(Keys.CATEGORIES) && json.getJSONArray(Keys.CATEGORIES).length() > 0) {
                                    createNestedCategories(json.getJSONArray(Keys.CATEGORIES));
                                } else {
                                    magenativeListViewBinding.catNotFoundMsg.setVisibility(View.VISIBLE);
                                    magenativeListViewBinding.categoriesList.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.sellerShopNotFound), Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.sellerShopNotFound), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void createNestedCategories(JSONArray jstring) {
        for (int i = 0; i < jstring.length(); i++) {
            try {
                JSONObject jsonObject = jstring.getJSONObject(i);
                View view = getLayoutInflater().inflate(R.layout.base_item_cat, null);
                final RelativeLayout maincat_section = (RelativeLayout) view.findViewById(R.id.maincat_section);
                final TextView cat_id = (TextView) view.findViewById(R.id.cat_id);
                // cat_id.setVisibility(View.VISIBLE);
                final TextView json = (TextView) view.findViewById(R.id.json);
                final TextView cat_name = (TextView) view.findViewById(R.id.cat_name);
                ImageView cat_icon = (ImageView) view.findViewById(R.id.cat_icon);
                final ImageView rotateimage = (ImageView) view.findViewById(R.id.rotateimage);
                final LinearLayout subcats_section = (LinearLayout) view.findViewById(R.id.subcats_section);
                final boolean[] zArr = new boolean[]{false};

                cat_name.setText(jsonObject.getString("category_label"));
                cat_id.setText(jsonObject.getString("id"));
                if (!jsonObject.getBoolean("has_child")) {
                    rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
                }
                json.setText(jsonObject.toString());

                magenativeListViewBinding.categoriesList.addView(view);

                maincat_section.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (subcats_section.getChildCount() > 0) {
                            if (zArr[0]) {
                                collapse(subcats_section);
                                zArr[0] = false;
                                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.plusicon));
                            } else {
                                expand(subcats_section);
                                zArr[0] = true;
                                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.minusicon));
                            }
                        } else {
                            try {
                                JSONObject cat_section = new JSONObject(json.getText().toString());
                                if (cat_section.getBoolean("has_child")) {
                                    JSONArray subcatnames = cat_section.getJSONArray("sub_cats");
                                    for (int j = 0; j < subcatnames.length(); j++) {
                                        JSONObject jsonObject2 = subcatnames.getJSONObject(j);
                                        createsubcats(subcats_section, jsonObject2);

                                    }
                                    expand(subcats_section);
                                    zArr[0] = true;
                                    rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.minusicon));
                                } else {
                                    Intent intent = new Intent(Ced_Seller_Shop.this, Ced_New_Product_Listing.class);
                                    Log.i("intent189", "onClick: " + cat_id.getText().toString());
                                    intent.putExtra("ID", cat_id.getText().toString());
                                    intent.putExtra("vendorId", getIntent().getStringExtra("ID"));
                                    intent.putExtra("vendor", true);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createsubcats(final LinearLayout subcat_section, final JSONObject jsonObject2) {
        try {
            final View view = View.inflate(this, R.layout.base_item_subcat, null);
            RelativeLayout maincatsection = view.findViewById(R.id.maincatsection);
            ImageView cat_icon = (ImageView) view.findViewById(R.id.cat_icon);
            final ImageView rotateimage = (ImageView) view.findViewById(R.id.rotateimage);
            final TextView json2 = (TextView) view.findViewById(R.id.json2);
            if (!jsonObject2.getBoolean("has_child")) {
                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
            }
            json2.setText(jsonObject2.toString());
            final RelativeLayout maincat_section = (RelativeLayout) view.findViewById(R.id.mainsub_section);
            final TextView cat_id = (TextView) view.findViewById(R.id.cat_id);
            final TextView cat_name = (TextView) view.findViewById(R.id.cat_name);
            final LinearLayout subcats_section = (LinearLayout) view.findViewById(R.id.subcats_section);
            cat_name.setText(Html.fromHtml(jsonObject2.getString("category_label").replace("&amp;", "&")));
            cat_id.setText(jsonObject2.getString("id"));
            subcat_section.addView(view);

            final boolean[] show = new boolean[]{false};
            maincat_section.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if (subcats_section.getChildCount() > 0) {
                            if (show[0]) {
                                collapse(subcats_section);
                                show[0] = false;
                                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.plusicon));
                            } else {
                                expand(subcats_section);
                                show[0] = true;
                                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.minusicon));
                            }
                        } else {
                            TextView json = (TextView) maincat_section.getChildAt(0);
                            JSONObject jsonObject = new JSONObject(json.getText().toString());
                            if (jsonObject.getBoolean("has_child")) {
                                JSONArray subcatnames = jsonObject.getJSONArray("sub_cats");
                                for (int j = 0; j < subcatnames.length(); j++) {
                                    createsubcats(subcats_section, subcatnames.getJSONObject(j));
                                }
                                expand(subcats_section);
                                show[0] = true;
                                rotateimage.setImageDrawable(getResources().getDrawable(R.drawable.minusicon));
                            } else {
                                Intent intent = new Intent(Ced_Seller_Shop.this, Ced_New_Product_Listing.class);
                                Log.i("intent253", "onClick: " + cat_id.getText().toString());
                                intent.putExtra("ID", cat_id.getText().toString());
                                intent.putExtra("vendorId", getIntent().getStringExtra("ID"));
                                intent.putExtra("vendor", true);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}