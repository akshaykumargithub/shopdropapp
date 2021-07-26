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
package shop.dropapp.ui.productsection.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonObject;

import shop.dropapp.Ced_MageNative_Scanner.Ced_Scanner;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.networkhandlea_activities.Ced_DownloadFileFromURL;
import shop.dropapp.R;
import shop.dropapp.ui.productsection.model.Ced_Data_Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.ui.product_review_section.activity.Ced_ReviewListing;
import shop.dropapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import shop.dropapp.databinding.MagenativeActivityScrollingBinding;
import shop.dropapp.databinding.MagenativeCheckboxlayoutBinding;
import shop.dropapp.databinding.MagenativeDatetyprlayoutBinding;
import shop.dropapp.databinding.MagenativeDropdownlayoutBinding;
import shop.dropapp.databinding.MagenativeFileAreaLayoutBinding;
import shop.dropapp.databinding.MagenativeRecyclerviewBinding;
import shop.dropapp.databinding.OneconfigattributeLayoutBinding;
import shop.dropapp.databinding.OneconfigrowBinding;
import shop.dropapp.databinding.WebviewBinding;
import shop.dropapp.ui.cartsection.activity.Ced_CartListing;
import shop.dropapp.ui.productsection.adapter.Ced_GroupProductAdapter;
import shop.dropapp.ui.productsection.adapter.Ced_ProductViewImagSlider_forswatches;
import shop.dropapp.ui.productsection.adapter.Ced_ProductViewSlider_ImageVideo;
import shop.dropapp.ui.productsection.adapter.Ced_RecyclerView_Adapter;
import shop.dropapp.ui.productsection.adapter.HomeCongifAttr_Item_Adapter;
import shop.dropapp.ui.productsection.viewmodel.ProductViewModel;
import shop.dropapp.ui.sellersection.activity.Ced_Seller_Shop;
import shop.dropapp.utils.UpdateImage;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static shop.dropapp.Keys.*;

@AndroidEntryPoint
public class Ced_NewProductView extends Ced_NavigationActivity {
    public String sourcebuynow = "frombuynow";
    public String sourceaddtocart = "fromaddtocart";
    @Inject
    ViewModelFactory viewModelFactory;
    ProductViewModel productViewModel;
    MagenativeActivityScrollingBinding productbinding;
    Boolean selectedSpecificationavailable = false;
    ViewPager pager;
    String URL;
    String product_id = " ";
    JSONArray productimages = null;
    ArrayList<String> list1;
    ArrayList<LinkedHashMap<String, String>> list3;
    LinearLayout offersection;
    LinearLayout custom_layout;
    TextView stocksection;
    RelativeLayout reviewsection;
    TextView normalprice;
    TextView specialprice;
    TextView toprice;
    TextView buynow;
    String productType = "";
    ArrayList<String> pricesforbundle;
    JSONObject option_price;
    JSONArray productprice = null;
    List<String> list2;
    boolean isspecialset = true;
    String baseprice;
    String currency_symbol;
    String offer;
    CardView customoptionsection;
    HashMap<String, String> selectedpricehash;
    HashMap<String, String> selectedoptiontypepricehash;
    HashMap<String, String> select_multi_tittle_id_hash;
    ArrayList<String> idsfordatecustomoptions;
    ArrayList<String> idsforselectcustomoptions;
    ArrayList<String> idsforradiocustomoptions;
    ArrayList<String> idsforfilecustomoptions;
    ArrayList<String> idsformulticustomoptions;
    HashMap<String, ArrayList<String>> hashmappricevalues;
    float main_value = 0;
    CardView bundleview;
    LinearLayout bundlelist;
    LinearLayout bundlelistmultiselect;
    HashMap<String, ArrayList<String>> configdata;
    HashMap<String, ArrayList<String>> configdata_dup;
    HashMap<String, HashMap<String, ArrayList<String>>> configavailability;
    TextView textView_spinner;
    Spinner spinner;
    LinearLayout view_6;
    HashMap<String, String> finalvaluestocart;
    HashMap<String, String> dependentvaluestoadd;
    JSONObject bundle_option_name;
    HashMap<String, HashMap<String, ArrayList<String>>> bundle_options;
    HashMap<String, String> bundle_types;
    HashMap<String, ArrayList<String>> bundleoptionandselection;
    HashMap<String, ArrayList<String>> selectedvaluewithoptions;
    HashMap<String, ArrayList<String>> checkboxids;
    HashMap<String, String> pricesforspinners;
    HashMap<String, String> bundle_option;
    HashMap<String, String> bundle_option_qty;
    RelativeLayout groupcard;
    RecyclerView grouplist;
    JSONArray groupedproducts;
    ArrayList<HashMap<String, String>> grouped_arraylist;
    CardView downloadproducts;
    LinearLayout productlinks;
    TextView linkstittle;
    String links_purchased_separately = "";
    JSONArray linksarray;
    HashMap<String, ArrayList<String>> linkshashmap;
    ArrayList<String> pricesoflinks;
    ArrayList<String> idsoflinks;
    JSONArray productdescription = null;
    JSONArray productdescription_2 = null;
    FloatingActionButton share;
    int writeresult;
    int readresult;
    String frontendurl = " ";
    ImageView relatedproduct;
    RelativeLayout buysection;
    RelativeLayout addtobagsection;
    String Inwishlist = "";
    String item_id = "";
    TextView atwl;
    TextView atc;
    ImageView wishlist;
    ImageView comparesection;/*
    RelativeLayout MageNative_wishlistsection;*/
    HashMap<String, String> selecthash;
    public static HashMap<String, String> groupdatatoaddtocart;
    String counter_string = "";
    LinearLayout quantitysection;
    ImageView sharesection;
    RelativeLayout specificrelative;
    RelativeLayout moreinfosection;
    ImageView drop;
    LinearLayout specificationlinear;
    boolean open = true;
    LinearLayout userreviewsection;
    RelativeLayout morereviewsection;
    LinearLayout view_4;
    static boolean isHavingdownloadable = false;
    boolean inwishlist = false;
    CardView sellersection;
    TextView vendor_id;
    ImageView vendor_logo;
    TextView vendor_name;
    final HashMap<String, String> configvalueidmap = new HashMap<>();
    String vendor_id_value = "";
    HashMap<String, String> attributelabel_attibutevalue;
    HashMap<String, String> attributevalue_attibutelabel;
    JSONObject config_attribute = null;
    HashMap<String, String> configurable_option_id_price = null;
    RelativeLayout additionalinforelative;
    RelativeLayout additionalinfosection;
    LinearLayout additionalinfolinear;
    ImageView additionalarrowinfo;
    RecyclerView relatedrecycler_view;
    RecyclerView crosssellrecycler_view;
    LinearLayout relatedprod_section;
    LinearLayout crosssellprod_section;
    RecyclerView upsellrecycler_view;
    LinearLayout upsellprod_section;
    boolean moreinfo_open = true;

    private HashMap<String, String> selected_attributes;

    //------------------
    /* public static String image_id = "";*/
    public static HashMap selected_row_image_id = new HashMap();
    public static JSONObject selected_config_row_values = new JSONObject();
    public static int totalconfig_attributes;

    //------------------
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productViewModel = new ViewModelProvider(this, viewModelFactory).get(ProductViewModel.class);
        try {
            productbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_scrolling, content, true);

            /*.............................. Initialization....................................*/
            init();
            counter_string = Ced_MainActivity.latestcartcount;
            setcount(counter_string);

            crosssellrecycler_view.setHasFixedSize(true);
            crosssellrecycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
            upsellrecycler_view.setHasFixedSize(true);
            upsellrecycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
            relatedrecycler_view.setHasFixedSize(true);
            relatedrecycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
            grouplist.setHasFixedSize(true);
            grouplist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

            morereviewsection.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), Ced_ReviewListing.class);
                intent.putExtra("Product_id", product_id);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });

            specificrelative.setOnClickListener(v -> {
                if (open) {
                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.magenative_rotatereverse);
                    sampleFadeAnimation.setRepeatCount(1);
                    sampleFadeAnimation.setFillAfter(true);
                    drop.startAnimation(sampleFadeAnimation);
                    collapse(specificationlinear);
                    open = false;
                } else {
                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.magenative_rotate);
                    sampleFadeAnimation.setRepeatCount(1);
                    sampleFadeAnimation.setFillAfter(true);
                    drop.startAnimation(sampleFadeAnimation);
                    expand(specificationlinear);
                    open = true;
                }
            });

            additionalinforelative.setOnClickListener(view -> {
                if (moreinfo_open) {
                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.magenative_rotatereverse);
                    sampleFadeAnimation.setRepeatCount(1);
                    sampleFadeAnimation.setFillAfter(true);
                    additionalarrowinfo.startAnimation(sampleFadeAnimation);
                    collapse(additionalinfolinear);
                    moreinfo_open = false;
                } else {
                    Animation sampleFadeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.magenative_rotate);
                    sampleFadeAnimation.setRepeatCount(1);
                    sampleFadeAnimation.setFillAfter(true);
                    additionalarrowinfo.startAnimation(sampleFadeAnimation);
                    expand(additionalinfolinear);
                    moreinfo_open = true;
                }
            });

            /*..................................................................*/
            JsonObject jsonObject1 = new JsonObject();
            try {
                if (getIntent().getStringExtra("product_id") != null) {
                    product_id = getIntent().getStringExtra("product_id");
                    jsonObject1.addProperty("prodID", product_id);
                } else {
                    if (getIntent().getStringExtra("ean") != null) {
                        jsonObject1.addProperty("ean", getIntent().getStringExtra("ean"));
                    } else {
                        String link = getIntent().getDataString();
                        String[] datavalue = Objects.requireNonNull(link).split("/");
                        String valueid = datavalue[datavalue.length - 1];
                        if (valueid.contains(getResources().getString(R.string.app_name).replace(" ", "_").trim())) {
                            String[] id = valueid.split("#");
                            product_id = id[0];
                        } else {
                            showmsg(getResources().getString(R.string.specifiedapp));
                            moveTaskToBack(true);
                            finish();
                        }
                    }
                }
                if (session.isLoggedIn()) {
                    jsonObject1.addProperty("customer_id", session.getCustomerid());
                }
                if (cedSessionManagement.getStoreId() != null) {
                    jsonObject1.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                if (getIntent().getStringExtra("vendor_id") != null) {
                    vendor_id_value = getIntent().getStringExtra("vendor_id");
                    jsonObject1.addProperty("vendor_id", vendor_id_value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            productViewModel.getProductViewData(Ced_NewProductView.this, cedSessionManagement.getCurrentStore(), jsonObject1).observe(Ced_NewProductView.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            if (apiResponse.data.equals("return")) {
                                showmsg(getResources().getString(R.string.NoProductFound));
                                Intent intent = new Intent(getApplicationContext(), Ced_Scanner.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                finish();
                            } else {
                                JSONObject jsonObject = new JSONObject(apiResponse.data);
                                if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                    Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    try {
                                        applydata(jsonObject.getJSONObject("data"));
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }
                                }
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
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        writeresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        readresult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        selected_attributes = new HashMap<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        pricesoflinks = new ArrayList<>();
        idsfordatecustomoptions = new ArrayList<>();
        idsforselectcustomoptions = new ArrayList<>();
        idsformulticustomoptions = new ArrayList<>();
        idsforradiocustomoptions = new ArrayList<>();
        idsforfilecustomoptions = new ArrayList<>();
        idsoflinks = new ArrayList<>();
        selectedpricehash = new HashMap<>();
        selectedoptiontypepricehash = new HashMap<>();
        select_multi_tittle_id_hash = new HashMap<>();
        hashmappricevalues = new HashMap<>();
        checkboxids = new HashMap<>();
        configdata = new HashMap<>();
        configdata_dup = new HashMap<>();
        bundle_option = new HashMap<>();
        bundle_option_qty = new HashMap<>();
        finalvaluestocart = new HashMap<>();
        linkshashmap = new HashMap<>();
        configavailability = new HashMap<>();
        dependentvaluestoadd = new HashMap<>();
        pricesforbundle = new ArrayList<>();
        bundle_options = new HashMap<>();
        bundle_types = new HashMap<>();
        pricesforspinners = new HashMap<>();
        grouped_arraylist = new ArrayList<>();
        selecthash = new HashMap<>();
        groupdatatoaddtocart = new HashMap<>();
        attributelabel_attibutevalue = new HashMap<>();
        attributevalue_attibutelabel = new HashMap<>();
        configurable_option_id_price = new HashMap<>();
        /*..............................Instance Initialization....................................*/
        sellersection = productbinding.sellersection;
        vendor_id = productbinding.vendorId;
        vendor_name = productbinding.vendorName;
        vendor_logo = productbinding.vendorLogo;
        pager = productbinding.MageNativeProductimages;
        normalprice = productbinding.MageNativeNormalprice;
        specialprice = productbinding.MageNativeSpecialprice;
        toprice = productbinding.MageNativeToprice;
        buynow = productbinding.MageNativeBuynow;
        offersection = productbinding.MageNativeOffersection;
        custom_layout = productbinding.MageNativeCustomLayout;
        stocksection = productbinding.MageNativeStocksection;
        reviewsection = productbinding.MageNativeReviewsection;
        customoptionsection = productbinding.MageNativeCustomoptionsection;
        spinner = productbinding.MageNativeConfiglist;
        textView_spinner = productbinding.MageNativeConfigtype;
        view_6 = productbinding.MageNativeView6;
        bundleview = productbinding.MageNativeBundleview;
        bundlelistmultiselect = productbinding.MageNativeBundlelistmultiselect;
        bundlelist = productbinding.MageNativeBundlelistsingleselect;
        groupcard = productbinding.MageNativeGroupproductlist;
        grouplist = productbinding.MageNativeGrouplist;
        downloadproducts = productbinding.MageNativeDownloadproducts;
        productlinks = productbinding.MageNativeProductlinks;
        linkstittle = productbinding.MageNativeLinkstittle;
        relatedproduct = productbinding.MageNativeRelatedproduct;
        buysection = productbinding.MageNativeBuysection;
        addtobagsection = productbinding.MageNativeAddtobagsection;
        atwl = productbinding.MageNativeAtwl;
        atc = productbinding.MageNativeAtc;
        /*wishlist = productbinding.MageNativeWishlist;*/
        wishlist = productbinding.MageNativeWishlist2;
        comparesection = productbinding.MageNativeComparesection2;
        quantitysection = productbinding.MageNativeQuantitysection;
        /*sharesection = productbinding.MageNativeSharesection;*/
        sharesection = productbinding.MageNativeSharesection2;
        specificrelative = productbinding.MageNativeSpecificrelative;
        moreinfosection = productbinding.MageNativeMoreinfosection;
        drop = productbinding.MageNativeDrop;
        specificationlinear = productbinding.MageNativeSpecificationlinear;
        userreviewsection = productbinding.MageNativeUserreviewsection;
        morereviewsection = productbinding.MageNativeMorereviewsection;
        view_4 = productbinding.MageNativeView4;
        additionalinforelative = productbinding.MageNativeAdditionalinforelative;
        additionalinfosection = productbinding.MageNativeAdditionalinfosection;
        additionalinfolinear = productbinding.MageNativeAdditionalinfolinear;
        additionalarrowinfo = productbinding.MageNativeAdditionalarrowinfo;
        relatedrecycler_view = productbinding.MageNativeRelatedrecyclerView;
        crosssellrecycler_view = productbinding.MageNativeCrosssellrecyclerView;
        relatedprod_section = productbinding.MageNativeRelatedprodSection;
        crosssellprod_section = productbinding.MageNativeCrosssellprodSection;
        upsellprod_section = productbinding.MageNativeUpsellprodSection;
        upsellrecycler_view = productbinding.MageNativeUpsellrecyclerView;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, 200, 200, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.AppTheme) : getResources().getColor(R.color.white);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 200, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void applydata(JSONObject data_) throws JSONException, WriterException {
        productbinding.maincontainer.setVisibility(VISIBLE);
        frontendurl = data_.getJSONArray("product-url").getString(0);
        String qr = " " + getResources().getString(R.string.hey) + " " + productbinding.MageNativeProductname.getText().toString() + " " + getResources().getString(R.string.on) + " " + getResources().getString(R.string.app_name) + "```\n" + frontendurl + "#" + getResources().getString(R.string.app_name).replace(" ", "_").trim();
        productbinding.Qrcode.setImageBitmap(TextToImageEncode(qr));
        /*........................Product Type........................*/
        productType = data_.getJSONArray("type").getString(0);
        /*........................Product Type........................*/
        /*......................................Product Name section..................................*/
        productbinding.MageNativeProductname.setText(data_.getJSONArray("product-name").getString(0));
        /*......................................Product Name section..................................*/
        /*......................................Product Curreny section..................................*/
        currency_symbol = data_.getString("currency_symbol");
        /*......................................Product currency section..................................*/
        /*......................................Product Price section..................................*/
        if (productType.equals("grouped")) {
            productbinding.pricesection.setVisibility(View.GONE);
            productbinding.MageNativeBundlepricesection.setVisibility(View.GONE);
        } else {
            productprice = data_.getJSONArray("price");
            for (int i = 0; i < productprice.length(); i++) {
                JSONObject obj3 = productprice.getJSONObject(i);
                JSONArray nameArray = obj3.names();
                JSONArray valArray = obj3.toJSONArray(nameArray);
                Log.i("prices", "" + nameArray);
                Log.i("prices", "" + valArray);
                for (int j = 0; j < valArray.length(); j++) {
                    String value = valArray.getString(j);
                    Gson converter = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> list = converter.fromJson(value, type);
                    String[] strArray = list.toArray(new String[0]);
                    for (int k = 0; k < strArray.length; k++) {
                        Log.i("prices", "" + strArray[k]);
                        list2.add(strArray[k]);

                    }
                }
            }
            String[] pricearray = list2.toArray(new String[2]);
            Log.i("prices", "" + pricearray[0]);
            Log.i("prices", "" + pricearray[1]);
            if (pricearray[0] == null) {
                isspecialset = false;
                baseprice = pricearray[1];
                normalprice.setText(currency_symbol + pricearray[1]);
                specialprice.setVisibility(View.GONE);
                offersection.setVisibility(View.GONE);
            } else {
                if (data_.has(getResources().getString(R.string.offer))) {
                    offer = data_.getString("offer");
                    offersection.setVisibility(GONE);
                    productbinding.MageNativeOfferpercent.setText(offer + getResources().getString(R.string.percent));
                }
                baseprice = pricearray[0];
                specialprice.setVisibility(View.VISIBLE);
                if (pricearray[1] != null) {
                    normalprice.setText(currency_symbol + pricearray[1]);
                    normalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    normalprice.setVisibility(View.VISIBLE);
                    //normalprice.setText(currency_symbol + pricearray[1]);
                } else {
                    normalprice.setVisibility(View.GONE);
                }

                if (pricearray[0] != null) {
                    specialprice.setText(currency_symbol + pricearray[0]);
                    specialprice.setTextColor(getResources().getColor(R.color.AppTheme));
                    specialprice.setVisibility(View.VISIBLE);
                } else {
                    specialprice.setVisibility(View.GONE);
                }
            }
        }
        /*......................................Product Price section..................................*/
        /*......................................Product Stock section..................................*/
        if (data_.getJSONArray("stock").getString(0).toLowerCase().equals("in stock")) {
            stocksection.setVisibility(View.GONE);
        } else {
            if (data_.getJSONArray("stock").getString(0).toLowerCase().equals("out of stock")) {
                stocksection.setVisibility(View.VISIBLE);
                addtobagsection.setEnabled(false);
                buysection.setEnabled(false);
            }
        }
        /*......................................Product Stock section..................................*/
        /*......................................Product quantity section..................................*/
        productbinding.MageNativeUpquant.setOnClickListener(v -> {
            int i = Integer.parseInt(buynow.getText().toString());
            String newcount = String.valueOf(i + 1);
            if ((i + 1) > 0) {
                buynow.setText(newcount);
            } else {
                buynow.setText("1");
            }
        });
        productbinding.MageNativeDownquant.setOnClickListener(v -> {
            int i = Integer.parseInt(buynow.getText().toString());
            if ((i - 1) > 0) {
                String newcount = String.valueOf(i - 1);
                buynow.setText(newcount);
            } else {
                buynow.setText("1");
            }
        });
        /*......................................Product quantity section..................................*/
        /**************************************custom_option*************************************/
        if (data_.has("custom_option")) {
            customoptionsection.setVisibility(VISIBLE);
            JSONArray custom_array = data_.getJSONArray("custom_option");
            if (custom_array.length() > 0) {
                for (int i = 0; i < custom_array.length(); i++) {
                    JSONObject custom_object = custom_array.getJSONObject(i);
                    String custom_option_type = custom_object.getString("type");
                    String is_require = custom_object.getString("is_require");
                    if (custom_option_type.equals("date")) {
                        MagenativeDatetyprlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_datetyprlayout, null, false);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeTypecustomoption.setText("date");
                        binding1.MageNativeDatetittle.setText("#" + custom_object.getString("title") + " +" + currency_symbol + custom_object.getString("price"));
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        set_regular_font_fortext(binding1.MageNativeDatetittle);
                        idsfordatecustomoptions.add(custom_object.getString("option_id"));
                        selectedpricehash.put(custom_object.getString("option_id"), custom_object.getString("price"));
                        custom_layout.addView(binding1.getRoot());
                        pricechangecustomoptions();
                    }
                    if (custom_option_type.equals("date_time")) {
                        MagenativeDatetyprlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_date_timetypelayout, null, false);
                        binding1.MageNativeTypecustomoption.setText("date_time");
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeDatetittle.setText("#" + custom_object.getString("title") + " +" + currency_symbol + custom_object.getString("price"));
                        set_regular_font_fortext(binding1.MageNativeDatetittle);
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        idsfordatecustomoptions.add(custom_object.getString("option_id"));
                        selectedpricehash.put(custom_object.getString("option_id"), custom_object.getString("price"));
                        custom_layout.addView(binding1.getRoot());
                        pricechangecustomoptions();
                    }
                    if (custom_option_type.equals("time")) {
                        MagenativeDatetyprlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_timetypelayout, null, false);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeTypecustomoption.setText("time");
                        binding1.MageNativeDatetittle.setText("#" + custom_object.getString("title") + " +" + currency_symbol + custom_object.getString("price"));
                        set_regular_font_fortext(binding1.MageNativeDatetittle);
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        idsfordatecustomoptions.add(custom_object.getString("option_id"));
                        selectedpricehash.put(custom_object.getString("option_id"), custom_object.getString("price"));
                        custom_layout.addView(binding1.getRoot());
                        pricechangecustomoptions();
                    }
                    if (custom_option_type.equals("drop_down")) {
                        MagenativeDropdownlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_dropdownlayout, null, false);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeSelecttittle.setText("#" + custom_object.getString("title"));
                        set_regular_font_fortext(binding1.MageNativeSelecttittle);
                        binding1.MageNativeTypecustomoption.setText("drop_down");
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        JSONArray options = custom_object.getJSONArray("option");
                        ArrayList<String> selectvalue = new ArrayList<>();
                        for (int d = 0; d < options.length(); d++) {
                            JSONObject object = options.getJSONObject(d);
                            selectedoptiontypepricehash.put(object.getString("option_type_id"), object.getString("price"));
                            select_multi_tittle_id_hash.put(object.getString("title") + " +" + currency_symbol + object.getString("price"), object.getString("option_type_id"));
                            selectvalue.add(object.getString("title") + " +" + currency_symbol + object.getString("price"));
                        }
                        ArrayAdapter<String> dropdown = new ArrayAdapter<>(getApplicationContext(), R.layout.magenative_textview, selectvalue);
                        binding1.MageNativeType.setAdapter(dropdown);
                        binding1.MageNativeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                LinearLayout layout = (LinearLayout) binding1.MageNativeType.getParent();
                                TextView OptionId = (TextView) layout.getChildAt(1);
                                selecthash.put(OptionId.getText().toString(), selectedoptiontypepricehash.get(select_multi_tittle_id_hash.get(binding1.MageNativeType.getSelectedItem().toString())));
                                pricechangecustomoptions();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        custom_layout.addView(binding1.getRoot());
                    }
                    if (custom_option_type.equals("radio")) {
                        MagenativeDropdownlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_dropdownlayout, null, false);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeSelecttittle.setText("#" + custom_object.getString("title"));
                        set_regular_font_fortext(binding1.MageNativeSelecttittle);
                        binding1.MageNativeTypecustomoption.setText("radio");
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        JSONArray options = custom_object.getJSONArray("option");
                        ArrayList<String> selectvalue = new ArrayList<>();

                        for (int d = 0; d < options.length(); d++) {
                            JSONObject object = options.getJSONObject(d);
                            selectedoptiontypepricehash.put(object.getString("option_type_id"), object.getString("price"));
                            select_multi_tittle_id_hash.put(object.getString("title") + " +" + currency_symbol + object.getString("price"), object.getString("option_type_id"));
                            selectvalue.add(object.getString("title") + " +" + currency_symbol + object.getString("price"));

                        }

                        ArrayAdapter<String> dropdown = new ArrayAdapter<>(getApplicationContext(), R.layout.magenative_textview, selectvalue);
                        binding1.MageNativeType.setAdapter(dropdown);

                        binding1.MageNativeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                LinearLayout layout = (LinearLayout) binding1.MageNativeType.getParent();
                                TextView OptionId = (TextView) layout.getChildAt(1);
                                selecthash.put(OptionId.getText().toString(), selectedoptiontypepricehash.get(select_multi_tittle_id_hash.get(binding1.MageNativeType.getSelectedItem().toString())));
                                pricechangecustomoptions();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        custom_layout.addView(binding1.getRoot());
                    }
                    if (custom_option_type.equals("checkbox") || custom_option_type.equals("multiple")) {
                        MagenativeCheckboxlayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_checkboxlayout, null, false);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeTypecustomoption.setText(custom_option_type);
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        JSONArray options = custom_object.getJSONArray("option");
                        for (int d = 0; d < options.length(); d++) {
                            JSONObject object = options.getJSONObject(d);
                            final CheckBox check = new CheckBox(getApplicationContext());
                            check.setButtonDrawable(checkbox_visibility);
                            selectedoptiontypepricehash.put(object.getString("option_type_id"), object.getString("price"));
                            select_multi_tittle_id_hash.put(object.getString("title") + " +" + currency_symbol + object.getString("price"), object.getString("option_type_id"));
                            check.setText(object.getString("title") + " +" + currency_symbol + object.getString("price"));
                            check.setTextColor(getResources().getColor(R.color.black));
                            check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    idsformulticustomoptions.add(select_multi_tittle_id_hash.get(check.getText().toString()));
                                    pricechangecustomoptions();
                                } else {
                                    idsformulticustomoptions.remove(select_multi_tittle_id_hash.get(check.getText().toString()));
                                    pricechangecustomoptions();
                                }
                            });
                            binding1.MageNativeMultiselectcheckbox.addView(check);
                        }
                        binding1.MageNativeMultiselecttittle.setText("#" + custom_object.getString("title"));
                        set_regular_font_fortext(binding1.MageNativeMultiselecttittle);
                        custom_layout.addView(binding1.getRoot());
                    }
                    if (custom_option_type.equals("field") || custom_option_type.equals("area")) {
                        MagenativeFileAreaLayoutBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_file_area_layout, null, false);
                        binding1.MageNativeTypecustomoption.setText(custom_option_type);
                        binding1.MageNativeRequired.setText(is_require);
                        binding1.MageNativeTextareatittle.setText("#" + custom_object.getString("title") + " +" + currency_symbol + custom_object.getString("price"));
                        binding1.MageNativeOptionid.setText(custom_object.getString("option_id"));
                        selectedpricehash.put(custom_object.getString("option_id"), custom_object.getString("price"));
                        binding1.MageNativeTextarea.addTextChangedListener(new TextWatcher() {

                            public void afterTextChanged(Editable s) {
                                LinearLayout linearLayout = (LinearLayout) binding1.MageNativeTextarea.getParent();
                                TextView optionid = (TextView) linearLayout.getChildAt(4);
                                if (binding1.MageNativeTextarea.getText().toString().isEmpty()) {
                                    if (idsfordatecustomoptions.contains(optionid.getText().toString())) {
                                        idsfordatecustomoptions.remove(optionid.getText().toString());
                                        pricechangecustomoptions();
                                    }
                                } else {
                                    if (!(idsfordatecustomoptions.contains(optionid.getText().toString()))) {
                                        idsfordatecustomoptions.add(optionid.getText().toString());
                                        pricechangecustomoptions();
                                    }
                                }
                            }

                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }
                        });
                        custom_layout.addView(binding1.getRoot());

                    }
                }
            } else {
                customoptionsection.setVisibility(GONE);
            }
        }
        /*****************************************custom_option**********************************/
        /*****************************************seller section**********************************/

        if (data_.has("vendor_info")) {
            sellersection.setVisibility(VISIBLE);
            vendor_id.setText(data_.getJSONObject("vendor_info").getString("vendor_id"));
            /*Glide.with(getApplicationContext())
                    .load(data_.getJSONObject("vendor_info").getString("company_logo"))
                    .dontTransform()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(vendor_logo);*/

            UpdateImage.showImage(getApplicationContext(), data_.getJSONObject("vendor_info").getString("company_logo"), R.drawable.placeholder, vendor_logo);

            vendor_name.setText(data_.getJSONObject("vendor_info").getString("vendor_name"));
            if (data_.getJSONObject("vendor_info").has("vendor_review")) {
                Float aFloat = Float.valueOf(data_.getJSONObject("vendor_info").getString("vendor_review"));
                if (aFloat < 2) {
                    productbinding.vendorRating.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                    productbinding.vendorRating.setText(aFloat.toString());
                }
                if (aFloat >= 3 && aFloat < 4) {
                    productbinding.vendorRating.setBackground(getResources().getDrawable(R.drawable.round_corner_yellow));
                    productbinding.vendorRating.setText(aFloat.toString());
                    productbinding.vendorRating.setTextColor(getResources().getColor(R.color.black));
                }
                if (aFloat >= 4) {
                    productbinding.vendorRating.setBackground(getResources().getDrawable(R.drawable.round_corner));
                    productbinding.vendorRating.setText(aFloat.toString());
                }
            } else {
                productbinding.reviewtag.setVisibility(GONE);
                productbinding.vendorRating.setVisibility(GONE);
            }
            sellersection.setOnClickListener(v -> {
                Intent intent = new Intent(Ced_NewProductView.this, Ced_Seller_Shop.class);
                intent.putExtra("ID", vendor_id.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });

        } else {
            sellersection.setVisibility(GONE);
        }
        /*****************************************seller section**********************************/
        /**********************************************ALL type of products****************************************/

        if (productType.equals("configurable")) {
           /* AtomicReference<String> image_id = new AtomicReference<>("");
            final String[] search_inarray = {""};
            ArrayList<LinearLayout> layout_id = new ArrayList<>();
            ArrayList<LinearLayout> size_layout_id = new ArrayList<>();
            AtomicBoolean isImageSelected = new AtomicBoolean(false);
            final boolean[] isSizeSelected = {false};
            ArrayList<Boolean> check_variant_selection = new ArrayList<>();

            LinearLayout ll = null;
            JSONArray indexes = null;*/

            if (data_.has("config-data")) {
                view_4.setVisibility(VISIBLE);
                /*LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 0);*/
                JSONObject config_data_ = data_.getJSONObject("config-data");
                final JSONObject attribute_index = config_data_.getJSONObject("index");
                final JSONArray attribute_codes = config_data_.getJSONArray("attributes");
                totalconfig_attributes = attribute_codes.length();
                ArrayList config_position = new ArrayList();
                for (int k = 0; k < attribute_codes.length(); k++) {
                    String row_position = attribute_codes.getJSONObject(k).getString("position");
                    config_position.add(row_position);
                }
                Collections.sort(config_position);
                for (int k = 0; k < attribute_codes.length(); k++) {
                    OneconfigrowBinding onerowbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.oneconfigrow, null, false);
                    String row_position = attribute_codes.getJSONObject(k).getString("position");
                    String row_id = attribute_codes.getJSONObject(k).getString("id");
                    String row_code = attribute_codes.getJSONObject(k).getString("code");
                    String row_label = attribute_codes.getJSONObject(k).getString("label");
                    onerowbinding.attribueHeading.setText(row_label);
                    JSONArray options = attribute_codes.getJSONObject(k).getJSONArray("options");
                    OneconfigattributeLayoutBinding oneconfigattributeLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.oneconfigattribute_layout, null, false);
                    oneconfigattributeLayoutBinding.attributeRecycler.setAdapter(new HomeCongifAttr_Item_Adapter(this, options, row_id, row_position, attribute_index, (oneconfigattributeLayoutBinding.attributeRecycler)));
                    onerowbinding.attributeRowLinear.addView(oneconfigattributeLayoutBinding.getRoot());
                    productbinding.configsection.addView(onerowbinding.getRoot());
                }
            }
        }
        if (productType.equals("bundle")) {
            String from = data_.getJSONObject("bundleddata").getString("from_price");
            String to = data_.getJSONObject("bundleddata").getString("to_price");
            //-----------
            productbinding.pricesection.setVisibility(GONE);
            set_regular_font_fortext(productbinding.MageNativeToprice);
            set_bold_font_fortext(productbinding.MageNativeTotag);
            set_bold_font_fortext(productbinding.MageNativeFromtag);
            set_regular_font_fortext(productbinding.MageNativeFromprice);
            //--------------------
            productbinding.MageNativeBundlepricesection.setVisibility(VISIBLE);
            specialprice.setVisibility(INVISIBLE);
            productbinding.MageNativeFromprice.setText(currency_symbol + from);
            toprice.setText(currency_symbol + to);
            bundle_option_name = data_.getJSONObject("bundleddata").getJSONObject("option name");
            JSONArray bundle_option_names = bundle_option_name.names();
            JSONArray bundle_option_selections = bundle_option_name.toJSONArray(bundle_option_names);
            for (int bundle = 0; bundle < bundle_option_selections.length(); bundle++) {
                String bon = bundle_option_names.getString(bundle);
                String bov = bundle_option_selections.getString(bundle);
                JSONObject selection_names = new JSONObject(bov);
                String type = selection_names.getString("type");
                String option_id = selection_names.getString("id");
                JSONObject selection_namesString = selection_names.getJSONObject("selection_name");
                JSONArray selection_names_option = selection_namesString.names();
                JSONArray selection_name_option_values = selection_namesString.toJSONArray(selection_names_option);
                HashMap<String, ArrayList<String>> bundleoptionnames = new HashMap<>();
                for (int select = 0; select < selection_name_option_values.length(); select++) {
                    String sno = selection_names_option.getString(select);
                    String snov = selection_name_option_values.getString(select);
                    JSONObject selected_option_values = new JSONObject(snov);
                    String default_qty = selected_option_values.getString("default_qty");
                    String user_can_change_qty = selected_option_values.getString("user_can_change_qty");
                    String selection_id = selected_option_values.getString("selection_id");
                    String selection_price = selected_option_values.getString("selection_price");
                    ArrayList<String> selectionvalues = new ArrayList<>();
                    selectionvalues.add("default_qty:" + default_qty);
                    selectionvalues.add("user_can_change_qty:" + user_can_change_qty);
                    selectionvalues.add("selection_price:" + selection_price);
                    selectionvalues.add("selection_id:" + selection_id);
                    String child_key = sno + "#" + selection_id;
                    bundleoptionnames.put(child_key, selectionvalues);
                    String key = bon + "#" + option_id;
                    bundle_options.put(key, bundleoptionnames);
                    bundle_types.put(key, type);
                }
            }
            bundleoptionandselection = new HashMap<>();
            selectedvaluewithoptions = new HashMap<>();
            Iterator iterator = bundle_options.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                String key = String.valueOf(pair.getKey());
                final HashMap<String, ArrayList<String>> list = (HashMap<String, ArrayList<String>>) pair.getValue();
                Iterator iterator1 = list.entrySet().iterator();
                ArrayList<String> selections = new ArrayList<>();
                while (iterator1.hasNext()) {
                    Map.Entry option = (Map.Entry) iterator1.next();
                    String optionname = String.valueOf(option.getKey());
                    selections.add(optionname);
                }
                bundleoptionandselection.put(key, selections);
            }
            Iterator selected = bundle_options.entrySet().iterator();
            while (selected.hasNext()) {
                Map.Entry pair = (Map.Entry) selected.next();
                final HashMap<String, ArrayList<String>> list = (HashMap<String, ArrayList<String>>) pair.getValue();
                Iterator iterator1 = list.entrySet().iterator();
                while (iterator1.hasNext()) {
                    Map.Entry option = (Map.Entry) iterator1.next();
                    String optionname = String.valueOf(option.getKey());
                    String parts[] = optionname.split("#");
                    selectedvaluewithoptions.put(parts[0], (ArrayList<String>) option.getValue());

                }
            }
            bundleview.setVisibility(VISIBLE);
            Iterator bundleiterate = bundleoptionandselection.entrySet().iterator();
            while (bundleiterate.hasNext()) {
                Map.Entry pair = (Map.Entry) bundleiterate.next();
                String key = String.valueOf(pair.getKey());
                String optionparts[] = key.split("#");
                ArrayList<String> valuestoselect = new ArrayList<>();
                ArrayList<String> value = (ArrayList<String>) pair.getValue();
                Iterator iterator1 = value.iterator();
                while (iterator1.hasNext()) {
                    String selectedvalues = (String) iterator1.next();
                    String selectedarray[] = selectedvalues.split("#");
                    valuestoselect.add(selectedarray[0]);
                }
                if (gettype(key).equals("checkbox") || gettype(key).equals("multi")) {
                    LinearLayout multilayerlayout = new LinearLayout(getApplicationContext());
                    multilayerlayout.setOrientation(LinearLayout.VERTICAL);
                    TextView option_name = new TextView(getApplicationContext());
                    set_regular_font_fortext(option_name);
                    option_name.setGravity(Gravity.START);
                    TextView option_id = new TextView(getApplicationContext());
                    option_id.setVisibility(GONE);
                    Iterator checkboxtoshow = valuestoselect.iterator();
                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    while (checkboxtoshow.hasNext()) {

                        final CheckBox checkBox = new CheckBox(getApplicationContext());
                        String text = (String) checkboxtoshow.next();
                        checkBox.setText(text);
                        checkBox.setTextColor(getResources().getColor(R.color.TextHintColor));
                        checkBox.setButtonDrawable(checkbox_visibility);
                        TextView default_qty = new TextView(getApplicationContext());
                        default_qty.setText(getResources().getString(R.string.Quantity) + getdefaultquantity(text));
                        set_regular_font_fortext(default_qty);
                        layout.addView(checkBox);
                        layout.addView(default_qty);
                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                LinearLayout checkboxparent = (LinearLayout) checkBox.getParent();
                                LinearLayout checkboxparentsParent = (LinearLayout) checkboxparent.getParent();
                                TextView optionID = (TextView) checkboxparentsParent.getChildAt(1);
                                ArrayList<String> arrayList = checkboxids.get(optionID.getText().toString());
                                arrayList.add(selectedid(checkBox.getText().toString()));
                                checkboxids.put(optionID.getText().toString(), arrayList);
                                pricesforbundle.add(selectedprice(checkBox.getText().toString()));
                                setPricesforbundle();
                            } else {
                                LinearLayout checkboxparent = (LinearLayout) checkBox.getParent();
                                LinearLayout checkboxparentsParent = (LinearLayout) checkboxparent.getParent();
                                TextView optionID = (TextView) checkboxparentsParent.getChildAt(1);
                                ArrayList<String> arrayList = checkboxids.get(optionID.getText().toString());
                                arrayList.remove(selectedid(checkBox.getText().toString()));
                                checkboxids.put(optionID.getText().toString(), arrayList);
                                pricesforbundle.remove(selectedprice(checkBox.getText().toString()));
                                setPricesforbundle();
                            }
                        });
                    }
                    option_name.setText(optionparts[0]);
                    ArrayList<String> arrayList = new ArrayList<>();
                    checkboxids.put(optionparts[1], arrayList);
                    option_id.setText(optionparts[1]);
                    multilayerlayout.addView(option_name, 0);
                    multilayerlayout.addView(option_id, 1);
                    multilayerlayout.addView(layout, 2);
                    bundlelistmultiselect.addView(multilayerlayout);
                } else if (gettype(key).equals("radio")) {

                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView optionname = new TextView(getApplicationContext());
                    set_regular_font_fortext(optionname);
                    optionname.setText(optionparts[0]);
                    TextView optionid = new TextView(getApplicationContext());
                    optionid.setText(optionparts[1]);
                    optionid.setVisibility(GONE);
                    final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                   /* ArrayAdapter<String> adp_2 = new ArrayAdapter<>(getApplicationContext(), R.layout.textview, valuestoselect);
                    spinner_dependent.setAdapter(adp_2);*/
                    for (int k = 0; k < valuestoselect.size(); k++) {
                        final RadioButton radiobtn = new RadioButton(getApplicationContext());
                        radiobtn.setText(valuestoselect.get(k));
                        radioGroup.addView(radiobtn);
                    }
                    TextView textView = new TextView(getApplicationContext());
                    EditText qty = new EditText(getApplicationContext());
                    qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                    qty.setTextColor(getResources().getColor(R.color.TextHintColor));
                    qty.setText("0");
                    textView.setText(getResources().getString(R.string.qty));
                    layout.addView(optionname, 0);
                    layout.addView(optionid, 1);
                    layout.addView(radioGroup, 2);
                    layout.addView(textView, 3);
                    layout.addView(qty, 4);
                    bundlelist.addView(layout);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup rd, int checkedId) {

                            for (int i = 0; i < rd.getChildCount(); i++) {
                                RadioButton radio_button = (RadioButton) rd.getChildAt(i);
                                int id = radio_button.getId();
                                if (radio_button.getId() == checkedId) {
                                   /* text = radio_button.getText().toString();
                                    flag=true;*/


                                    LinearLayout linearLayout = (LinearLayout) radioGroup.getParent();
                                    RadioGroup group = (RadioGroup) linearLayout.getChildAt(2);
                                    EditText editText = (EditText) linearLayout.getChildAt(4);
                                    TextView view1 = (TextView) linearLayout.getChildAt(1);
                                    TextView option_name = (TextView) linearLayout.getChildAt(0);
                                    set_regular_font_fortext(view1);
                                    set_regular_font_fortext(option_name);
                                    bundle_option.put(view1.getText().toString(), selectedid(radio_button.getText().toString()));
                                    pricesforspinners.put(option_name.getText().toString(), selectedprice(radio_button.getText().toString()));


                                    setPricesforbundle();
                                    if (checkusercanchange(radio_button.getText().toString())) {
                                        editText.setText(getdefaultquantity(radio_button.getText().toString()));
                                        editText.setEnabled(true);
                                    } else {
                                        editText.setText(getdefaultquantity(radio_button.getText().toString()));
                                        editText.setEnabled(false);
                                    }
                                    return;
                                }
                            }
                        }
                    });
                   /* spinner_dependent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            LinearLayout linearLayout = (LinearLayout) spinner_dependent.getParent();
                            Spinner spinner = (Spinner) linearLayout.getChildAt(2);
                            EditText editText = (EditText) linearLayout.getChildAt(4);
                            TextView view1 = (TextView) linearLayout.getChildAt(1);
                            TextView option_name = (TextView) linearLayout.getChildAt(0);
                            set_regular_font_fortext(view1);
                            set_regular_font_fortext(option_name);
                            bundle_option.put(view1.getText().toString(), selectedid((String) spinner.getSelectedItem()));
                            pricesforspinners.put(option_name.getText().toString(), selectedprice((String) spinner.getSelectedItem()));
                            setPricesforbundle();
                            if (checkusercanchange((String) spinner.getSelectedItem())) {
                                editText.setText(getdefaultquantity((String) spinner.getSelectedItem()));
                                editText.setEnabled(true);
                            } else {
                                editText.setText(getdefaultquantity((String) spinner.getSelectedItem()));
                                editText.setEnabled(false);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });*/
                } else {
                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView optionname = new TextView(getApplicationContext());
                    set_regular_font_fortext(optionname);
                    optionname.setText(optionparts[0]);
                    TextView optionid = new TextView(getApplicationContext());
                    optionid.setText(optionparts[1]);
                    optionid.setVisibility(GONE);
                    final Spinner spinner_dependent = new Spinner(getApplicationContext());
                    ArrayAdapter<String> adp_2 = new ArrayAdapter<>(getApplicationContext(), R.layout.textview, valuestoselect);
                    spinner_dependent.setAdapter(adp_2);
                    TextView textView = new TextView(getApplicationContext());
                    EditText qty = new EditText(getApplicationContext());
                    qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                    qty.setTextColor(getResources().getColor(R.color.TextHintColor));
                    qty.setText("0");
                    textView.setText(getResources().getString(R.string.qty));
                    layout.addView(optionname, 0);
                    layout.addView(optionid, 1);
                    layout.addView(spinner_dependent, 2);
                    layout.addView(textView, 3);
                    layout.addView(qty, 4);
                    bundlelist.addView(layout);
                    spinner_dependent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            LinearLayout linearLayout = (LinearLayout) spinner_dependent.getParent();
                            Spinner spinner = (Spinner) linearLayout.getChildAt(2);
                            EditText editText = (EditText) linearLayout.getChildAt(4);
                            TextView view1 = (TextView) linearLayout.getChildAt(1);
                            TextView option_name = (TextView) linearLayout.getChildAt(0);
                            set_regular_font_fortext(view1);
                            set_regular_font_fortext(option_name);
                            bundle_option.put(view1.getText().toString(), selectedid((String) spinner.getSelectedItem()));
                            pricesforspinners.put(option_name.getText().toString(), selectedprice((String) spinner.getSelectedItem()));
                            setPricesforbundle();
                            if (checkusercanchange((String) spinner.getSelectedItem())) {
                                editText.setText(getdefaultquantity((String) spinner.getSelectedItem()));
                                editText.setEnabled(true);
                            } else {
                                editText.setText(getdefaultquantity((String) spinner.getSelectedItem()));
                                editText.setEnabled(false);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        }
        if (productType.equals("grouped")) {
            quantitysection.setVisibility(GONE);
            groupcard.setVisibility(VISIBLE);
            groupedproducts = data_.getJSONArray("grouped_product");
            for (int group = 0; group < groupedproducts.length(); group++) {
                JSONObject groupdata = groupedproducts.getJSONObject(group);
                HashMap<String, String> grouped_hashmap = new HashMap<>();
                grouped_hashmap.put("group-prod-img", groupdata.getString("product_image"));
                grouped_hashmap.put("group-product-id", groupdata.getString("product_id"));
                grouped_hashmap.put("group-product-name", groupdata.getString("product_name"));
                grouped_hashmap.put("stock", groupdata.getString("stock"));
                grouped_hashmap.put("special_price", groupdata.getJSONObject("price").getJSONArray("special_price").getString(0));
                grouped_hashmap.put("regular_price", groupdata.getJSONObject("price").getJSONArray("regular_price").getString(0));
                grouped_arraylist.add(grouped_hashmap);
            }
            Ced_GroupProductAdapter cedGroupProductAdapter = new Ced_GroupProductAdapter(Ced_NewProductView.this, grouped_arraylist);
            grouplist.setAdapter(cedGroupProductAdapter);
            cedGroupProductAdapter.notifyDataSetChanged();
            //setListViewHeightBasedOnChildren(grouplist);
        }
        if (productType.equals("downloadable")) {
            downloadproducts.setVisibility(VISIBLE);
            linkstittle.setText(data_.getJSONObject("download-data").getString("link_title"));
            set_regular_font_fortext(linkstittle);
            links_purchased_separately = data_.getJSONObject("download-data").getString("links_purchased_separately");
            linksarray = data_.getJSONObject("download-data").getJSONArray("links");
            for (int link = 0; link < linksarray.length(); link++) {
                JSONObject link_data = linksarray.getJSONObject(link);
                ArrayList<String> link_id_price_url = new ArrayList<>();
                link_id_price_url.add("link_id:" + link_data.getString("link_id"));
                link_id_price_url.add("link_price:" + link_data.getString("link_price"));
                link_id_price_url.add("link_url=" + link_data.getString("link_url"));
                if (link_data.has("sample_link")) {
                    link_id_price_url.add("sample_link=" + link_data.getString("sample_link"));
                }
                linkshashmap.put(link_data.getString("link_title"), link_id_price_url);
            }
            if (links_purchased_separately.equals("1")) {
                Iterator iterator = linkshashmap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    String key = String.valueOf(pair.getKey());
                    LinearLayout link_tittles = new LinearLayout(getApplicationContext());
                    link_tittles.setOrientation(LinearLayout.HORIZONTAL);
                    final CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setText(key);
                    checkBox.setTextColor(getResources().getColor(R.color.TextHintColor));
                    checkBox.setButtonDrawable(checkbox_visibility);
                    final TextView sample = new TextView(getApplicationContext());
                    set_regular_font_fortext(sample);
                    sample.setText(getResources().getString(R.string.samples));
                    sample.setVisibility(GONE);
                    link_tittles.addView(checkBox, 0);
                    link_tittles.addView(sample, 1);
                    link_tittles.setPadding(2, 2, 2, 2);
                    productlinks.addView(link_tittles);
                    sample.setOnClickListener(v -> {
                        LinearLayout layout = (LinearLayout) sample.getParent();
                        CheckBox box = (CheckBox) layout.getChildAt(0);
                        String link = getlinkofproduct(box.getText().toString());
                        downloadfile(link, getsamplename(box.getText().toString()));
                    });
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            pricesoflinks.add(checkBox.getText().toString() + "#" + getPriceoflink(checkBox.getText().toString()));
                            idsoflinks.add(getIdoflink(checkBox.getText().toString()));
                            setPricefordownloadables();
                        } else {
                            pricesoflinks.remove(checkBox.getText().toString() + "#" + getPriceoflink(checkBox.getText().toString()));
                            idsoflinks.remove(getIdoflink(checkBox.getText().toString()));
                            setPricefordownloadables();
                        }
                    });

                }

            } else {
                Iterator iterator = linkshashmap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    String key = String.valueOf(pair.getKey());
                    LinearLayout link_tittles = new LinearLayout(getApplicationContext());
                    link_tittles.setOrientation(LinearLayout.HORIZONTAL);
                    link_tittles.setPadding(15, 0, 0, 0);
                    TextView text = new TextView(getApplicationContext());
                    set_regular_font_fortext(text);
                    text.setText(key);
                    final TextView sample = new TextView(getApplicationContext());
                    sample.setText(getResources().getString(R.string.samples));
                    sample.setVisibility(GONE);
                    text.setTextColor(getResources().getColor(R.color.red));
                    link_tittles.addView(text, 0);
                    link_tittles.addView(sample, 1);
                    productlinks.addView(link_tittles);
                    sample.setOnClickListener(v -> {
                        LinearLayout layout = (LinearLayout) sample.getParent();
                        TextView box = (TextView) layout.getChildAt(0);
                        String link = getlinkofproduct(box.getText().toString());
                        downloadfile(link, getsamplename(box.getText().toString()));

                    });

                }
            }
        }
        /*****************************************All type of products*********************************************/
        /*****************************************Description and detail and cms*********************************************/

        productdescription = data_.getJSONArray("description");
        productdescription_2 = data_.getJSONArray("short-description");

        if (productdescription.getString(0) != null) {
            WebviewBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.webview, null, false);
            binding.web.loadDataWithBaseURL(null, productdescription.getString(0), "text/html", "utf-8", null);
            specificationlinear.addView(binding.getRoot());
        }
        //  description.loadDataWithBaseURL(null, productdescription.getString(0), "text/html", "utf-8", null);

        moreinfosection.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(getApplicationContext(), Ced_ProductDescription.class);
                intent.putExtra("1", productdescription.getString(0));
                intent.putExtra("2", productdescription_2.getString(0));
                intent.putExtra("3", "Description");
                intent.putExtra("4", "Details");
                if (data_.has("cms-title")) {
                    String tittle = data_.getString("cms-title");
                    final String cms_content = data_.getString("cms-content");
                    intent.putExtra("5", tittle);
                    intent.putExtra("6", cms_content);
                    intent.putExtra("cms", true);
                } else {
                    intent.putExtra("cms", false);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        /*****************************************Description and detail and cms*********************************************/
        /*****************************************Share Section*********************************************/
        sharesection.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareString = Html.fromHtml(" " + getResources().getString(R.string.hey) + productbinding.MageNativeProductname.getText().toString() + "  " + getResources().getString(R.string.on) + getResources().getString(R.string.app_name) + "```\n" + frontendurl + "#" + getResources().getString(R.string.app_name).replace(" ", "_").trim()).toString();
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shareproduct)));
        });
        /*****************************************Share Section*********************************************/


        /****************************************Compare Section*********************************************/
        comparesection.setOnClickListener(v -> {

            if (session.isLoggedIn()) {
                JsonObject param = new JsonObject();
                param.addProperty("prodID", product_id);
                param.addProperty("customer_id", session.getCustomerid());
                productViewModel.addToCompare(this, cedSessionManagement.getCurrentStore(), param, session.getHahkey()).observe(this, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            try {
                                JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                JSONObject data = object.getJSONObject("data");
                                if (data.getString("status").equals("true")) {
                                    // incomparelist=true;
                                    comparesection.setImageDrawable(getResources().getDrawable(R.drawable.ic_compare_full));
                                }
                                if (object.has("message"))
                                    showmsg(object.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent main = new Intent(this, Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                            }
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });
            } else {
                showmsg(getResources().getString(R.string.loginfirsttocompare));
            }
        });
        /******************************************Compare Section*********************************************/


        /*****************************************Related Product Section*********************************************/

        relatedproduct.setOnClickListener(v -> {
            try {
                if (data_.has("related_product")) {
                    JSONArray related = data_.getJSONArray("related_product");
                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    ArrayList<String> arrayList3 = new ArrayList<>();
                    ArrayList<String> arrayList4 = new ArrayList<>();
                    ArrayList<String> arrayList5 = new ArrayList<>();
                    for (int r = 0; r < related.length(); r++) {
                        JSONObject object = related.getJSONObject(r);
                        HashMap<String, String> list = new HashMap<>();
                        arrayList.add(object.getString("related_product-name"));
                        arrayList2.add(object.getString("related_product_image"));
                        arrayList3.add(object.getString("related_product-id"));
                    }
                    Intent intent = new Intent(getApplicationContext(), Ced_RecyclerView_Activity.class);
                    String names[] = arrayList.toArray(new String[arrayList.size()]);
                    String images[] = arrayList2.toArray(new String[arrayList2.size()]);
                    String ids[] = arrayList3.toArray(new String[arrayList3.size()]);
                    String special_price[] = arrayList4.toArray(new String[arrayList4.size()]);
                    String normal_price[] = arrayList5.toArray(new String[arrayList5.size()]);
                    intent.putExtra("data_name", names);
                    intent.putExtra("data_image", images);
                    intent.putExtra("data_ids", ids);
                    MagenativeRecyclerviewBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_recyclerview, null, false);
                    binding1.MageNativeRecyclerView.setHasFixedSize(true);
                    binding1.MageNativeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    ArrayList<Ced_Data_Model> arrayListl = new ArrayList<>();
                    for (int i = 0; i < names.length; i++) {
                        arrayListl.add(new Ced_Data_Model(names[i], images[i], ids[i], special_price[i], normal_price[i]));
                    }
                    Ced_RecyclerView_Adapter adapter = new Ced_RecyclerView_Adapter(getApplicationContext(), arrayListl);
                    binding1.MageNativeRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                    TextView textView = layout.findViewById(R.id.snackbar_text);
                    textView.setVisibility(View.INVISIBLE);
                    layout.addView(binding1.getRoot(), 0);
                    layout.setBackgroundColor(getResources().getColor(R.color.white));
                    snackbar.show();
                } else {
                    showmsg(getResources().getString(R.string.nosimilarproductfound));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        });
        /*****************************************Related Product Section*********************************************/
        /*****************************************Add to wishlist *********************************************/
        //  MageNative_wishlistsection.setVisibility(VISIBLE);
        wishlist.setVisibility(VISIBLE);
        /***************************************product_id..........................................*/
        if (session.isLoggedIn()) {
            if (data_.has("Inwishlist")) {
                Inwishlist = data_.getJSONArray("Inwishlist").getString(0);
                if (Inwishlist.equals("IN")) {
                    wishlist.setVisibility(VISIBLE);
                    item_id = data_.getJSONArray("item_id").getString(0);
                    wishlist.setImageResource(R.drawable.wishred);
                    inwishlist = true;
                }
            }

        }
        wishlist.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                if (inwishlist) {
                    JsonObject removefrom_wishlist = new JsonObject();
                    try {
                        removefrom_wishlist.addProperty("item_id", item_id);
                        removefrom_wishlist.addProperty("customer_id", session.getCustomerid());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    productViewModel.removeFromWishList(this, cedSessionManagement.getCurrentStore(), removefrom_wishlist, session.getHahkey()).observe(this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                    String Status = object.getString("status");
                                    if (Status.equals("true")) {
                                        wishlist.setImageResource(R.drawable.wishlike);
                                        showmsg(object.getString("message"));
                                        inwishlist = false;

                                    }
                                    if (object.has("message"))
                                        showmsg(object.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Intent main = new Intent(this, Ced_MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);
                                }
                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                showmsg(getResources().getString(R.string.errorString));
                                break;
                        }
                    });

                } else {
                    JsonObject addtowishlist = new JsonObject();
                    addtowishlist.addProperty("prodID", product_id);
                    addtowishlist.addProperty("customer_id", session.getCustomerid());
                    productViewModel.addToWishList(this, cedSessionManagement.getCurrentStore(), addtowishlist, session.getHahkey()).observe((FragmentActivity) this, apiResponse ->
                    {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                try {

                                    JSONObject objec = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                    String Status = objec.getString("status");
                                    if (Status.equals("true")) {
                                        wishlist.setImageResource(R.drawable.wishred);
                                        inwishlist = true;
                                        showmsg(objec.getString("message"));
                                        item_id = objec.getString("wishlist-item-id");
                                    } else {
                                        if (Status.equals("false")) {
                                            inwishlist = false;
                                            showmsg(objec.getString("message"));

                                        }
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
                    });
                }
            } else {
                showmsg(getResources().getString(R.string.loginfirsttoaddwish));
            }
        });

        buysection.setOnClickListener(v -> {
            try {
                boolean checkingaddtocart = true;
                atwl.setText(getResources().getString(R.string.adding));
                buysection.setClickable(false);
                JsonObject addtocart = new JsonObject();
                addtocart.addProperty(PRODUCT_ID, product_id);
                addtocart.addProperty(TYPE, productType);
                addtocart.addProperty(QTY, buynow.getText().toString());
                addtocart.addProperty(POSTCODE, cedSessionManagement.getpostcode());
                addtocart.addProperty(LATITUDE, cedSessionManagement.getlatitude());
                addtocart.addProperty(LONGITUDE, cedSessionManagement.getlongitude());
                addtocart.addProperty(STATE, cedSessionManagement.getstate());
                addtocart.addProperty(COUNTRY_ID, cedSessionManagement.getcountry());
                addtocart.addProperty(CITY, cedSessionManagement.getcity());
                addtocart.addProperty(LOCATION, cedSessionManagement.getshipping_address());

                if (session.isLoggedIn()) {
                    addtocart.addProperty("customer_id", session.getCustomerid());
                } else {
                    if (cedSessionManagement.getCartId() != null) {
                        addtocart.addProperty("cart_id", cedSessionManagement.getCartId());
                    }
                }
                if (productType.equals("downloadable")) {
                    if (links_purchased_separately.equals("1")) {
                        int counter_links = 0;
                        JSONObject links_ids = new JSONObject();
                        Iterator iterator = idsoflinks.iterator();
                        while (iterator.hasNext()) {
                            if (counter_links < idsoflinks.size()) {
                                String id = (String) iterator.next();
                                try {
                                    links_ids.put(String.valueOf(counter_links), id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            counter_links++;
                        }
                        addtocart.addProperty("links", String.valueOf(links_ids));
                    } else {
                        int i = productlinks.getChildCount();
                        for (int j = 0; j < i; j++) {
                            if (j != 0) {
                                LinearLayout layout = (LinearLayout) productlinks.getChildAt(j);
                                TextView textView = (TextView) layout.getChildAt(0);
                                if (idsoflinks.size() <= 0) {
                                    idsoflinks.add(getIdoflink(textView.getText().toString()));
                                } else {
                                    if (idsoflinks.contains(getIdoflink(textView.getText().toString()))) {
                                    } else {
                                        idsoflinks.add(getIdoflink(textView.getText().toString()));
                                    }
                                }
                            }
                        }
                        int counter_links = 0;
                        JSONObject links_ids = new JSONObject();
                        Iterator iterator = idsoflinks.iterator();
                        while (iterator.hasNext()) {
                            if (counter_links < idsoflinks.size()) {
                                String id = (String) iterator.next();
                                try {
                                    links_ids.put(String.valueOf(counter_links), id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            counter_links++;
                        }
                        addtocart.addProperty("links", String.valueOf(links_ids));
                    }
                    isHavingdownloadable = true;
                }
                if (productType.equals("grouped")) {
                    try {
                        JSONObject groupobject = new JSONObject();
                        Iterator groupeddata = groupdatatoaddtocart.entrySet().iterator();
                        while (groupeddata.hasNext()) {
                            Map.Entry pair = (Map.Entry) groupeddata.next();
                            String key = String.valueOf(pair.getKey());
                            String value = (String) pair.getValue();
                            groupobject.put(key, value);
                        }
                        addtocart.addProperty("super_group", String.valueOf(groupobject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                }
                if (productType.equals("configurable")) {
                    configurabledata(selected_attributes, addtocart);
                    if (selected_config_row_values.length() < totalconfig_attributes/*search_inarray.equals("")*/) {
                        showmsg(getResources().getString(R.string.pleasechoose));
                        checkingaddtocart = false;
                        enabletext(atwl, buysection, sourcebuynow);
                    } else if (!selectedSpecificationavailable) {
                        enabletext(atwl, buysection, sourcebuynow);
                        checkingaddtocart = false;
                        showmsg(getResources().getString(R.string.selectedspecification_notavailable));
                    } else {
                        checkingaddtocart = true;
                        addtocart.addProperty("super_attribute", selected_config_row_values.toString());
                    }
                }
                if (productType.equals("bundle")) {
                    int tc = bundlelist.getChildCount();
                    for (int childcount = 0; childcount < tc; childcount++) {
                        LinearLayout linearLayout = (LinearLayout) bundlelist.getChildAt(childcount);
                        TextView qty = (TextView) linearLayout.getChildAt(4);
                        TextView optionid = (TextView) linearLayout.getChildAt(1);
                        bundle_option_qty.put(optionid.getText().toString(), qty.getText().toString());
                    }
                    try {
                        JSONObject jo = new JSONObject();
                        Iterator entryIterator = bundle_option.entrySet().iterator();
                        while (entryIterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) entryIterator.next();
                            String key = String.valueOf(pair.getKey());
                            String value = (String) pair.getValue();
                            jo.put(key, value);
                        }
                        Iterator checboxiterrator = checkboxids.entrySet().iterator();
                        while (checboxiterrator.hasNext()) {
                            Map.Entry pair = (Map.Entry) checboxiterrator.next();
                            String key = String.valueOf(pair.getKey());
                            ArrayList<String> value = (ArrayList<String>) pair.getValue();
                            JSONObject object = new JSONObject();
                            Iterator iterator = value.iterator();
                            int count = 0;
                            while (iterator.hasNext()) {
                                if (count < value.size()) {
                                    object.put(String.valueOf(count), iterator.next());
                                }
                                count++;
                            }
                            jo.put(key, object);
                        }
                        addtocart.addProperty("bundle_option", String.valueOf(jo));
                        JSONObject jo_qty = new JSONObject();
                        Iterator entryIterator_qty = bundle_option_qty.entrySet().iterator();
                        while (entryIterator_qty.hasNext()) {
                            Map.Entry pair = (Map.Entry) entryIterator_qty.next();
                            String key = String.valueOf(pair.getKey());
                            String value = (String) pair.getValue();
                            jo_qty.put(key, value);
                        }
                        addtocart.addProperty("bundle_option_qty", String.valueOf(jo_qty));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                }
                if (custom_layout.getChildCount() > 0) {
                    JSONObject custom_option_final_object = new JSONObject();
                    int childcount_custom_option = custom_layout.getChildCount();
                    JSONObject custom_option_object = new JSONObject();
                    for (int i = 0; i < childcount_custom_option; i++) {
                        LinearLayout layout = (LinearLayout) custom_layout.getChildAt(i);
                        TextView type_custom = (TextView) layout.getChildAt(0);
                        String type_string = type_custom.getText().toString();
                        if (type_string.equals("date")) {
                            TextView date_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                DatePicker datepicker = (DatePicker) layout.getChildAt(2);
                                JSONObject date_array = new JSONObject();
                                int day = datepicker.getDayOfMonth();
                                int month = datepicker.getMonth() + 1;
                                int year = datepicker.getYear();
                                date_array.put("month", month);
                                date_array.put("day", day);
                                date_array.put("year", year);
                                custom_option_object.put(date_option_id.getText().toString(), date_array);
                                addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                            }
                        }
                        if (type_string.equals("date_time")) {
                            TextView date_option_id = (TextView) layout.getChildAt(5);
                            TextView required = (TextView) layout.getChildAt(6);
                            if (required.getText().toString().equals("1")) {
                                DatePicker datepicker = (DatePicker) layout.getChildAt(2);
                                TimePicker timepicker = (TimePicker) layout.getChildAt(3);
                                JSONObject date_array = new JSONObject();
                                int day = datepicker.getDayOfMonth();
                                int month = datepicker.getMonth() + 1;
                                int year = datepicker.getYear();
                                date_array.put("month", month);
                                date_array.put("day", day);
                                date_array.put("year", year);
                                String format;
                                int hour = 0;
                                int min = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hour = timepicker.getHour();
                                    min = timepicker.getMinute();
                                } else {
                                    hour = timepicker.getCurrentHour();
                                    min = timepicker.getCurrentMinute();
                                }
                                if (hour == 0) {
                                    hour += 12;
                                    format = "am";
                                } else if (hour == 12) {
                                    format = "pm";
                                } else if (hour > 12) {
                                    hour -= 12;
                                    format = "pm";
                                } else {
                                    format = "am";
                                }
                                date_array.put("hour", hour);
                                date_array.put("minute", min);
                                date_array.put("day_part", format);
                                custom_option_object.put(date_option_id.getText().toString(), date_array);
                                addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                            }
                        }
                        if (type_string.equals("time")) {
                            TextView date_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                TimePicker timepicker = (TimePicker) layout.getChildAt(2);
                                JSONObject date_array = new JSONObject();
                                String format;
                                int hour = 0;
                                int min = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hour = timepicker.getHour();
                                    min = timepicker.getMinute();
                                } else {
                                    hour = timepicker.getCurrentHour();
                                    min = timepicker.getCurrentMinute();
                                }
                                if (hour == 0) {
                                    hour += 12;
                                    format = "am";
                                } else if (hour == 12) {
                                    format = "pm";
                                } else if (hour > 12) {
                                    hour -= 12;
                                    format = "pm";
                                } else {
                                    format = "am";
                                }
                                date_array.put("hour", hour);
                                date_array.put("minute", min);
                                date_array.put("day_part", format);
                                custom_option_object.put(date_option_id.getText().toString(), date_array);
                                addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                            }
                        }
                        if (type_string.equals("drop_down")) {
                            TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                Spinner spinner = (Spinner) layout.getChildAt(2);
                                custom_option_object.put(dropdown_option_id.getText().toString(), select_multi_tittle_id_hash.get(spinner.getSelectedItem().toString()));

                            }
                        }
                        if (type_string.equals("radio")) {
                            TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                Spinner spinner = (Spinner) layout.getChildAt(2);
                                custom_option_object.put(dropdown_option_id.getText().toString(), select_multi_tittle_id_hash.get(spinner.getSelectedItem().toString()));
                            }
                        }
                        if (type_string.equals("checkbox") || type_string.equals("multiple")) {
                            TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                LinearLayout checkboxlinear = (LinearLayout) layout.getChildAt(2);
                                JSONArray child = new JSONArray();
                                if (checkboxlinear.getChildCount() > 0) {
                                    for (int j = 0; j < checkboxlinear.getChildCount(); j++) {
                                        CheckBox checkBox = (CheckBox) checkboxlinear.getChildAt(j);
                                        if (checkBox.isChecked()) {
                                            child.put(select_multi_tittle_id_hash.get(checkBox.getText().toString()));
                                        }
                                    }
                                }
                                custom_option_object.put(dropdown_option_id.getText().toString(), child);
                            }
                        }
                        if (type_string.equals("field") || type_string.equals("area")) {
                            TextView text_option_id = (TextView) layout.getChildAt(4);
                            TextView required = (TextView) layout.getChildAt(5);
                            if (required.getText().toString().equals("1")) {
                                EditText field = (EditText) layout.getChildAt(2);
                                if (!(field.getText().toString().isEmpty())) {
                                    custom_option_object.put(text_option_id.getText().toString(), field.getText().toString());
                                }
                            }
                        }
                    }
                    custom_option_final_object.put("options", custom_option_object);
                    addtocart.addProperty("Custom", custom_option_final_object.toString());
                }
                try {
                    if (checkingaddtocart) {
                        addtocartfinally(addtocart, atwl, buysection, sourcebuynow);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);

            }
        });
        /*****************************************Add to WishList*********************************************/


        /*....................................Product Images section..................................*/
        productimages = data_.getJSONArray("product-image");
        String media_type = "";
        for (int i = 0; i < productimages.length(); i++) {
            if (productimages.get(i) instanceof JSONObject) {
                JSONObject img = productimages.getJSONObject(i);
                LinkedHashMap<String, String> img_param = new LinkedHashMap<String, String>();
                media_type = img.getString("media_type");
                if (media_type.equals("image")) {
                    img_param.put("image", "image");
                    Log.i("REpo", "proview " + media_type);
                } else if (media_type.equals("external-video")) {
                    img_param.put("image", "external-video");
                    img_param.put("image_url", img.getString("video_url"));
                    Log.i("REpo", "proview " + media_type);
                }
                list1.add(img.getString("url"));
                list3.add(img_param);
            } else {
                LinkedHashMap<String, String> img_param = new LinkedHashMap<String, String>();
                list1.add(productimages.getString(i));
                img_param.put("image", "image");
                list3.add(img_param);
            }
        }
        Log.i("productimagesurl", "" + list1);
        try {
            Ced_ProductViewSlider_ImageVideo productimagesSliderAdapter = new Ced_ProductViewSlider_ImageVideo(getSupportFragmentManager(), getApplicationContext(), list1, list3);
            pager.setAdapter(productimagesSliderAdapter);
            productbinding.MageNativeIndicator.setViewPager(pager);
        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        /*....................................Product Images section...................................*/


        /*****************************************Add to cart*********************************************/
        try {
            addtobagsection.setOnClickListener(v -> {
                try {
                    boolean checkingaddtocart = true;
                    //atc.setText("" + String.format(getResources().getString(R.string.adding)));

                    productbinding.MageNativeAtcadding.setVisibility(VISIBLE);
                    productbinding.MageNativeAtc.setVisibility(GONE);
                    //productbinding.MageNativeAtc.setText(getResources().getString(R.string.adding));
                    addtobagsection.setClickable(false);
                    JsonObject addtocart = new JsonObject();
                    addtocart.addProperty(PRODUCT_ID, product_id);
                    addtocart.addProperty(QTY, buynow.getText().toString());
                    addtocart.addProperty(TYPE, productType);

                    addtocart.addProperty(POSTCODE, cedSessionManagement.getpostcode());
                    addtocart.addProperty(LATITUDE, cedSessionManagement.getlatitude());
                    addtocart.addProperty(LONGITUDE, cedSessionManagement.getlongitude());
                    addtocart.addProperty(STATE, cedSessionManagement.getstate());
                    addtocart.addProperty(COUNTRY_ID, cedSessionManagement.getcountry());
                    addtocart.addProperty(CITY, cedSessionManagement.getcity());
                    addtocart.addProperty(LOCATION, cedSessionManagement.getshipping_address());


                    if (session.isLoggedIn()) {
                        addtocart.addProperty(CUSTOMER_ID, session.getCustomerid());
                    } else {
                        if (cedSessionManagement.getCartId() != null) {
                            addtocart.addProperty(CART_ID, cedSessionManagement.getCartId());
                        }
                    }
                    if (productType.equals("downloadable")) {
                        if (links_purchased_separately.equals("1")) {
                            int counter_links = 0;
                            JSONObject links_ids = new JSONObject();
                            Iterator iterator = idsoflinks.iterator();
                            while (iterator.hasNext()) {
                                if (counter_links < idsoflinks.size()) {
                                    String id = (String) iterator.next();
                                    try {
                                        links_ids.put(String.valueOf(counter_links), id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                counter_links++;
                            }
                            addtocart.addProperty("links", String.valueOf(links_ids));
                        } else {

                            int i = productlinks.getChildCount();
                            for (int j = 0; j < i; j++) {
                                if (j != 0) {
                                    LinearLayout layout = (LinearLayout) productlinks.getChildAt(j);
                                    TextView textView = (TextView) layout.getChildAt(0);
                                    if (idsoflinks.size() <= 0) {
                                        idsoflinks.add(getIdoflink(textView.getText().toString()));
                                    } else {
                                        if (idsoflinks.contains(getIdoflink(textView.getText().toString()))) {
                                        } else {
                                            idsoflinks.add(getIdoflink(textView.getText().toString()));
                                        }
                                    }
                                }
                            }
                            int counter_links = 0;
                            JSONObject links_ids = new JSONObject();
                            Iterator iterator = idsoflinks.iterator();
                            while (iterator.hasNext()) {
                                if (counter_links < idsoflinks.size()) {
                                    String id = (String) iterator.next();
                                    try {
                                        links_ids.put(String.valueOf(counter_links), id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                counter_links++;
                            }
                            addtocart.addProperty("links", String.valueOf(links_ids));
                        }
                    }
                    if (productType.equals("grouped")) {
                        try {
                            JSONObject groupobject = new JSONObject();
                            Iterator groupeddata = groupdatatoaddtocart.entrySet().iterator();
                            while (groupeddata.hasNext()) {
                                Map.Entry pair = (Map.Entry) groupeddata.next();
                                String key = String.valueOf(pair.getKey());
                                String value = (String) pair.getValue();
                                groupobject.put(key, value);
                            }
                            addtocart.addProperty("super_group", String.valueOf(groupobject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    }
                    if (productType.equals("configurable")) {
                        configurabledata(selected_attributes, addtocart);
                        if (selected_config_row_values.length() < totalconfig_attributes/*search_inarray.equals("")*/) {
                            showmsg((Ced_NewProductView.this).getResources().getString(R.string.pleasechoose));
                            checkingaddtocart = false;
                            enabletext(atc, addtobagsection, sourceaddtocart);
                        } else if (!selectedSpecificationavailable) {
                            checkingaddtocart = false;
                            enabletext(atc, addtobagsection, sourceaddtocart);
                            showmsg(getResources().getString(R.string.selectedspecification_notavailable));
                        } else {
                            checkingaddtocart = true;
                            addtocart.addProperty("super_attribute", selected_config_row_values.toString());
                        }
                    }
                    if (productType.equals("bundle")) {
                        int tc = bundlelist.getChildCount();
                        for (int childcount = 0; childcount < tc; childcount++) {
                            LinearLayout linearLayout = (LinearLayout) bundlelist.getChildAt(childcount);
                            TextView qty = (TextView) linearLayout.getChildAt(4);
                            TextView optionid = (TextView) linearLayout.getChildAt(1);
                            bundle_option_qty.put(optionid.getText().toString(), qty.getText().toString());
                        }
                        try {
                            JSONObject jo = new JSONObject();
                            Iterator entryIterator = bundle_option.entrySet().iterator();
                            while (entryIterator.hasNext()) {
                                Map.Entry pair = (Map.Entry) entryIterator.next();
                                String key = String.valueOf(pair.getKey());
                                String value = (String) pair.getValue();
                                jo.put(key, value);
                            }
                            Iterator checboxiterrator = checkboxids.entrySet().iterator();
                            while (checboxiterrator.hasNext()) {
                                Map.Entry pair = (Map.Entry) checboxiterrator.next();
                                String key = String.valueOf(pair.getKey());
                                ArrayList<String> value = (ArrayList<String>) pair.getValue();
                                JSONObject object = new JSONObject();
                                Iterator iterator = value.iterator();
                                int count = 0;
                                while (iterator.hasNext()) {
                                    if (count < value.size()) {
                                        object.put(String.valueOf(count), iterator.next());
                                    }
                                    count++;
                                }
                                jo.put(key, object);
                            }
                            addtocart.addProperty("bundle_option", String.valueOf(jo));
                            JSONObject jo_qty = new JSONObject();
                            Iterator entryIterator_qty = bundle_option_qty.entrySet().iterator();
                            while (entryIterator_qty.hasNext()) {
                                Map.Entry pair = (Map.Entry) entryIterator_qty.next();
                                String key = String.valueOf(pair.getKey());
                                String value = (String) pair.getValue();
                                jo_qty.put(key, value);
                            }
                            addtocart.addProperty("bundle_option_qty", String.valueOf(jo_qty));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    }
                    if (custom_layout.getChildCount() > 0) {
                        JSONObject custom_option_final_object = new JSONObject();
                        int childcount_custom_option = custom_layout.getChildCount();
                        JSONObject custom_option_object = new JSONObject();
                        for (int i = 0; i < childcount_custom_option; i++) {
                            LinearLayout layout = (LinearLayout) custom_layout.getChildAt(i);
                            TextView type_custom = (TextView) layout.getChildAt(0);
                            String type_string = type_custom.getText().toString();
                            if (type_string.equals("date")) {
                                TextView date_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    DatePicker datepicker = (DatePicker) layout.getChildAt(2);
                                    JSONObject date_array = new JSONObject();
                                    int day = datepicker.getDayOfMonth();
                                    int month = datepicker.getMonth() + 1;
                                    int year = datepicker.getYear();
                                    date_array.put("month", month);
                                    date_array.put("day", day);
                                    date_array.put("year", year);
                                    custom_option_object.put(date_option_id.getText().toString(), date_array);
                                    addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                                }
                            }
                            if (type_string.equals("date_time")) {
                                TextView date_option_id = (TextView) layout.getChildAt(5);
                                TextView required = (TextView) layout.getChildAt(6);
                                if (required.getText().toString().equals("1")) {
                                    DatePicker datepicker = (DatePicker) layout.getChildAt(2);
                                    TimePicker timepicker = (TimePicker) layout.getChildAt(3);
                                    JSONObject date_array = new JSONObject();
                                    int day = datepicker.getDayOfMonth();
                                    int month = datepicker.getMonth() + 1;
                                    int year = datepicker.getYear();
                                    date_array.put("month", month);
                                    date_array.put("day", day);
                                    date_array.put("year", year);
                                    String format;
                                    int hour = 0;
                                    int min = 0;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        hour = timepicker.getHour();
                                        min = timepicker.getMinute();
                                    } else {
                                        hour = timepicker.getCurrentHour();
                                        min = timepicker.getCurrentMinute();
                                    }
                                    if (hour == 0) {
                                        hour += 12;
                                        format = "am";
                                    } else if (hour == 12) {
                                        format = "pm";
                                    } else if (hour > 12) {
                                        hour -= 12;
                                        format = "pm";
                                    } else {
                                        format = "am";
                                    }
                                    date_array.put("hour", hour);
                                    date_array.put("minute", min);
                                    date_array.put("day_part", format);
                                    custom_option_object.put(date_option_id.getText().toString(), date_array);
                                    addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                                }
                            }
                            if (type_string.equals("time")) {
                                TextView date_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    TimePicker timepicker = (TimePicker) layout.getChildAt(2);
                                    JSONObject date_array = new JSONObject();
                                    String format;
                                    int hour = 0;
                                    int min = 0;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        hour = timepicker.getHour();
                                        min = timepicker.getMinute();
                                    } else {
                                        hour = timepicker.getCurrentHour();
                                        min = timepicker.getCurrentMinute();
                                    }
                                    if (hour == 0) {
                                        hour += 12;
                                        format = "am";
                                    } else if (hour == 12) {
                                        format = "pm";
                                    } else if (hour > 12) {
                                        hour -= 12;
                                        format = "pm";
                                    } else {
                                        format = "am";
                                    }
                                    date_array.put("hour", hour);
                                    date_array.put("minute", min);
                                    date_array.put("day_part", format);
                                    custom_option_object.put(date_option_id.getText().toString(), date_array);
                                    addtocart.addProperty("validate_datetime_" + date_option_id.getText().toString(), " ");
                                }
                            }
                            if (type_string.equals("drop_down")) {
                                TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    Spinner spinner = (Spinner) layout.getChildAt(2);
                                    custom_option_object.put(dropdown_option_id.getText().toString(), select_multi_tittle_id_hash.get(spinner.getSelectedItem().toString()));

                                }
                            }
                            if (type_string.equals("radio")) {
                                TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    Spinner spinner = (Spinner) layout.getChildAt(2);
                                    custom_option_object.put(dropdown_option_id.getText().toString(), select_multi_tittle_id_hash.get(spinner.getSelectedItem().toString()));
                                }
                            }
                            if (type_string.equals("checkbox") || type_string.equals("multiple")) {
                                TextView dropdown_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    LinearLayout checkboxlinear = (LinearLayout) layout.getChildAt(2);
                                    JSONArray child = new JSONArray();
                                    if (checkboxlinear.getChildCount() > 0) {
                                        for (int j = 0; j < checkboxlinear.getChildCount(); j++) {
                                            CheckBox checkBox = (CheckBox) checkboxlinear.getChildAt(j);
                                            if (checkBox.isChecked()) {
                                                child.put(select_multi_tittle_id_hash.get(checkBox.getText().toString()));
                                            }
                                        }
                                    }
                                    custom_option_object.put(dropdown_option_id.getText().toString(), child);
                                }
                            }
                            if (type_string.equals("field") || type_string.equals("area")) {
                                TextView text_option_id = (TextView) layout.getChildAt(4);
                                TextView required = (TextView) layout.getChildAt(5);
                                if (required.getText().toString().equals("1")) {
                                    EditText field = (EditText) layout.getChildAt(2);
                                    if (!(field.getText().toString().isEmpty())) {
                                        custom_option_object.put(text_option_id.getText().toString(), field.getText().toString());
                                    }
                                }
                            }
                        }
                        custom_option_final_object.put("options", custom_option_object);
                        addtocart.addProperty("Custom", custom_option_final_object.toString());
                    }
                    try {
                        if (checkingaddtocart) {
                            addtocartfinally(addtocart, atc, addtobagsection, sourceaddtocart);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        /*****************************************Add to cart*********************************************/
    }

    private void addtocartfinally(JsonObject addtocart, TextView atc, RelativeLayout addtobagsection, String source) {

        productViewModel = new ViewModelProvider(this, viewModelFactory).get(ProductViewModel.class);
        productViewModel.addToCart(this, cedSessionManagement.getCurrentStore(), addtocart).observe(this, Ced_NewProductView.this::consumeResponseaddtocart);
        enabletext(atc, addtobagsection, source);
        if (source.equalsIgnoreCase(sourcebuynow)) {
            buynowshow();
        }
    }

    private void consumeResponseaddtocart(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject jsonObject_1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                    if (jsonObject_1.has("cart_id")) {
                        final JSONObject nameArray = jsonObject_1.getJSONObject("cart_id");
                        if (nameArray.getBoolean("success")) {
                            Ced_MainActivity.latestcartcount = String.valueOf(nameArray.getInt("items_count"));
                            setcount(String.valueOf(nameArray.getInt("items_count")));
                            String cart_id = nameArray.getString("cart_id");
                            cedSessionManagement.savecartid(cart_id);
                            /*enabletext(atc,addtobagsection,source);
                            if(source.equalsIgnoreCase(sourcebuynow))
                            {
                                buynowshow();
                            }*/
                            showmsg(nameArray.getString("message"));
                        } else {
                            /*  enabletext(atc,addtobagsection,source);*/
                            showmsg(nameArray.getString("message"));
                        }
                    } else {
                        showmsg(jsonObject_1.getString("message"));
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

    private void enabletext(TextView button, RelativeLayout section, String source) {

        if (source.equalsIgnoreCase(sourceaddtocart)) {
            button.setText(getResources().getString(R.string.addtocart));
            productbinding.MageNativeAtcadding.setVisibility(GONE);
            productbinding.MageNativeAtc.setVisibility(VISIBLE);
            section.setClickable(true);
        } else {
            button.setText(getResources().getString(R.string.buynow));
            section.setClickable(true);
        }
    }

    public void unselectallselectedimages(RecyclerView mRecycler) {
        for (int i = 0; i < mRecycler.getChildCount(); i++) {
            CardView cardView = (CardView) mRecycler.getChildAt(i);
            cardView.setBackground(getResources().getDrawable(R.drawable.cardcorner));
            cardView.setCardElevation(2);
        }
    }

    public void SearchImage2(String search_text, JSONObject index) {
        try {
            if (index.has(search_text)) {
                selectedSpecificationavailable = true;
                JSONObject search_result_object = index.getJSONObject(search_text);
                JSONArray images = search_result_object.getJSONArray("images");
                if (search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0) != null) {
                    normalprice.setVisibility(VISIBLE);
                    normalprice.setPaintFlags(normalprice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    if (search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0).toString().contains(currency_symbol)) {
                        normalprice.setText(search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0).toString());
                    } else {
                        normalprice.setText(currency_symbol + search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0));
                    }
                }

                if (search_result_object.getJSONObject("price").getJSONArray("finalPrice").get(0).toString().equals(search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0).toString())) {
                    specialprice.setVisibility(GONE);
                    normalprice.setVisibility(VISIBLE);
                    // normalprice.setText(currency_symbol + search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0));
                    if (search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0).toString().contains(currency_symbol)) {
                        normalprice.setText(search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0).toString());
                    } else {
                        normalprice.setText(currency_symbol + search_result_object.getJSONObject("price").getJSONArray("oldPrice").get(0));
                    }
                } else {
                    specialprice.setVisibility(VISIBLE);
                    normalprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    // specialprice.setText(currency_symbol + search_result_object.getJSONObject("price").getJSONArray("finalPrice").get(0));
                    if (search_result_object.getJSONObject("price").getJSONArray("finalPrice").get(0).toString().contains(currency_symbol)) {
                        specialprice.setText(search_result_object.getJSONObject("price").getJSONArray("finalPrice").get(0).toString());
                    } else {
                        specialprice.setText(currency_symbol + search_result_object.getJSONObject("price").getJSONArray("finalPrice").get(0));
                    }
                }
                if (images.length() > 0) {
                    if (list1.size() > 0) {
                        list1.clear();
                    }
                }
                for (int k = 0; k < images.length(); k++) {
                    JSONObject images_list = images.getJSONObject(k);
                    list1.add(images_list.getString("img"));
                }

                Ced_ProductViewImagSlider_forswatches productimagesSliderAdapter = new Ced_ProductViewImagSlider_forswatches(getSupportFragmentManager(), getApplicationContext(), list1, list3);
                pager.setAdapter(productimagesSliderAdapter);
                productbinding.MageNativeIndicator.setViewPager(pager);
            } else {
                selectedSpecificationavailable = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void configurabledata(HashMap<String, String> params, JsonObject addtocart) {
        try {
            JSONObject obj = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                obj.put(entry.getKey(), entry.getValue());
            }
            Log.e("PARAMS", "" + obj.toString());
            addtocart.addProperty("super_attribute", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareString = Html.fromHtml(" " + getResources().getString(R.string.hey) + productbinding.MageNativeProductname.getText().toString() + "  " + getResources().getString(R.string.app_name) + "```\n" + frontendurl + "#" + getResources().getString(R.string.app_name).replace(" ", "_").trim()).toString();
                    //  shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(productimage));
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                    startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shareproduct)));
                } else {
                    showmsg(getResources().getString(R.string.permissiondenied));
                }
                break;
        }
    }

    public String getsamplename(String name) {
        String link = "";
        ArrayList<String> list = linkshashmap.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String pricename = (String) iterator.next();
            String[] parts = pricename.split("=");
            if (parts[0].equals("sample_link")) {
                link = parts[1];
            }
        }

        return link;
    }

    public void downloadfile(String file_url, String name) {
        Ced_DownloadFileFromURL cedDownloadFileFromURL = new Ced_DownloadFileFromURL(getApplicationContext(), name);
        cedDownloadFileFromURL.execute(file_url);
    }

    public String selectedprice(String name) {
        String check = "";
        ArrayList<String> list = selectedvaluewithoptions.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String qty = (String) iterator.next();
            String[] parts = qty.split(":");
            if (parts[0].equals("selection_price")) {
                check = parts[1];
            }
        }
        return check;
    }

    public boolean checkusercanchange(String name) {
        boolean check = false;
        ArrayList<String> list = selectedvaluewithoptions.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String qty = (String) iterator.next();
            String[] parts = qty.split(":");
            if (parts[0].equals("user_can_change_qty")) {
                if (parts[1].equals("1")) {
                    check = true;
                } else {
                    check = false;
                }
            }
        }
        return check;
    }

    public String getPriceoflink(String name) {
        String price = "";
        ArrayList<String> list = linkshashmap.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String pricename = (String) iterator.next();
            String[] parts = pricename.split(":");
            if (parts[0].equals("link_price")) {
                price = parts[1];
            }
        }
        return price;
    }

    public String getIdoflink(String name) {
        String id = "";
        ArrayList<String> list = linkshashmap.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String pricename = (String) iterator.next();
            String[] parts = pricename.split(":");
            if (parts[0].equals("link_id")) {
                id = parts[1];
            }
        }
        return id;
    }

    public void setPricefordownloadables() {
        float price = 0;
        float custom_float = pricechangecustomoptions();
        if (pricesoflinks.size() > 0) {
            Iterator iterator = pricesoflinks.iterator();

            while (iterator.hasNext()) {
                String pricename = (String) iterator.next();
                String[] parts = pricename.split("#");
                Float aFloat = Float.valueOf(parts[1]);
                price = price + aFloat;
            }

            if (isspecialset) {
                if (custom_float != 0) {
                    float base = custom_float + price;
                    specialprice.setText(currency_symbol + base);
                } else {
                    Float base = Float.valueOf(baseprice);
                    base = base + price;
                    specialprice.setText(currency_symbol + base);
                }
            } else {
                if (custom_float != 0) {
                    float base = custom_float + price;
                    normalprice.setText(currency_symbol + base);
                } else {
                    Float base = Float.valueOf(baseprice);
                    base = base + price;
                    normalprice.setText(currency_symbol + base);
                }
            }
        } else {
            if (isspecialset) {
                if (custom_float != 0) {
                    specialprice.setText(currency_symbol + custom_float);
                } else {
                    specialprice.setText(currency_symbol + baseprice);
                }
            } else {
                if (custom_float != 0) {
                    normalprice.setText(currency_symbol + custom_float);
                } else {
                    normalprice.setText(currency_symbol + baseprice);
                }
            }
        }
    }

    public void setPricesforbundle() {
        float price = 0;
        float spinnerprice = 0;

        if (pricesforbundle.size() > 0) {
            Iterator iterator = pricesforbundle.iterator();
            while (iterator.hasNext()) {
                String value = (String) iterator.next();
                Float x = Float.valueOf(value);
                price = price + x;
            }
        }

        Iterator iterator1 = pricesforspinners.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator1.next();
            String key = String.valueOf(pair.getKey());
            String value = (String) pair.getValue();
            Float y = Float.valueOf(value);
            spinnerprice = spinnerprice + y;
        }

        float custom_float = pricechangecustomoptions();
        if (custom_float != 0) {
            float base = custom_float + price + spinnerprice;
            normalprice.setText(currency_symbol + base);
        } else {
            Float base = Float.valueOf(baseprice);
            base = base + price + spinnerprice;
            normalprice.setText(currency_symbol + base);
        }
    }

    public String gettype(String key) {
        return bundle_types.get(key);
    }

    public float pricechangecustomoptions() {
        float price = 0;
        if (idsfordatecustomoptions.size() > 0) {
            Iterator iterator = idsfordatecustomoptions.iterator();
            while (iterator.hasNext()) {
                float custom_price = Float.valueOf(Objects.requireNonNull(selectedpricehash.get(iterator.next())));
                price = custom_price + price;
            }
        }

        if (selecthash.size() > 0) {
            Iterator finalvalues = selecthash.entrySet().iterator();
            while (finalvalues.hasNext()) {
                Map.Entry pair = (Map.Entry) finalvalues.next();
                String value = (String) pair.getValue();
                float custom_price = Float.valueOf(value);
                price = custom_price + price;
            }
        }

        if (idsformulticustomoptions.size() > 0) {
            Iterator iterator = idsformulticustomoptions.iterator();
            while (iterator.hasNext()) {
                float custom_price = Float.valueOf(Objects.requireNonNull(selectedoptiontypepricehash.get(iterator.next())));
                price = custom_price + price;
            }
        }

        if (productType.equals("configurable")) {
            if (main_value != 0 && dependentvaluestoadd.size() > 0) {
                Iterator finalvalues = dependentvaluestoadd.entrySet().iterator();
                float finalpricecombo = 0;
                while (finalvalues.hasNext()) {
                    Map.Entry pair = (Map.Entry) finalvalues.next();
                    String value = (String) pair.getValue();
                    Float x = Float.valueOf(value);
                    finalpricecombo = finalpricecombo + x;
                }

                finalpricecombo = main_value + finalpricecombo;
                if (isspecialset) {
                    Float x = Float.valueOf(baseprice);
                    price = (price + x);
                    float temp = price + finalpricecombo;
                    specialprice.setText(currency_symbol + temp);
                } else {
                    Float x = Float.valueOf(baseprice);
                    price = (price + x);
                    float temp = price + finalpricecombo;
                    normalprice.setText(currency_symbol + temp);
                }
            } else {
                if (main_value != 0) {
                    if (isspecialset) {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        float temp = price + main_value;
                        specialprice.setText(currency_symbol + temp);
                    } else {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        float temp = price + main_value;
                        normalprice.setText(currency_symbol + temp);
                    }
                } else {
                    if (isspecialset) {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        specialprice.setText(currency_symbol + price);
                    } else {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        normalprice.setText(currency_symbol + price);
                    }
                }
            }
        } else {
            if (productType.equals("bundle")) {
                float bundleprice = 0;
                float spinnerprice = 0;
                if (pricesforbundle.size() > 0) {
                    Iterator iterator = pricesforbundle.iterator();
                    while (iterator.hasNext()) {
                        String value = (String) iterator.next();
                        Float x = Float.valueOf(value);
                        bundleprice = bundleprice + x;
                    }
                }
                if (pricesforspinners.size() > 0) {
                    Iterator iterator1 = pricesforspinners.entrySet().iterator();
                    while (iterator1.hasNext()) {

                        Map.Entry pair = (Map.Entry) iterator1.next();
                        String value = (String) pair.getValue();
                        Float y = Float.valueOf(value);
                        spinnerprice = spinnerprice + y;
                    }
                }
                Float x = Float.valueOf(baseprice);
                price = (price + x);
                float base = price + bundleprice + spinnerprice;
                normalprice.setText(currency_symbol + base);
            } else {
                if (productType.equals("downloadable")) {
                    float downloadableprice = 0;
                    if (pricesoflinks.size() > 0) {
                        Iterator iterator = pricesoflinks.iterator();
                        while (iterator.hasNext()) {
                            String pricename = (String) iterator.next();
                            String[] parts = pricename.split("#");
                            Float aFloat = Float.valueOf(parts[1]);
                            downloadableprice = downloadableprice + aFloat;
                        }
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        if (isspecialset) {
                            float base = price + downloadableprice;
                            specialprice.setText(currency_symbol + base);
                        } else {
                            float base = price + downloadableprice;
                            normalprice.setText(currency_symbol + base);
                        }
                    }
                } else {
                    if (isspecialset) {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        specialprice.setText(currency_symbol + price);
                    } else {
                        Float x = Float.valueOf(baseprice);
                        price = (price + x);
                        normalprice.setText(currency_symbol + price);
                    }
                }
            }
        }
        return price;
    }

    public String selectedid(String name) {
        String check = "";
        ArrayList<String> list = selectedvaluewithoptions.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String qty = (String) iterator.next();
            String[] parts = qty.split(":");
            if (parts[0].equals("selection_id")) {
                check = parts[1];
            }
        }
        return check;
    }

    public float setBasepriceaccordingoption(String key) {
        float finalbaseprice = 0;
        String type = "";
        String value = "";
        if (hashmappricevalues.containsKey(key)) {
            ArrayList<String> strings = hashmappricevalues.get(key);
            String[] s = Objects.requireNonNull(strings).toArray(new String[strings.size()]);

            for (int data = 0; data < s.length; data++) {
                String[] part = s[data].split(":");
                if (part[0].equals("type")) {
                    type = part[1];
                } else {
                    if (part[0].equals("price")) {
                        value = part[1];
                    }
                }
            }

            if (type.equals("fixed")) {
                Float x = Float.valueOf(value);
                finalbaseprice = finalbaseprice + x;
            } else {
                if (type.equals("percent")) {
                    if (isspecialset) {

                        Float x = Float.valueOf(baseprice);
                        float percent = (x * Float.valueOf(value)) / 100;
                        finalbaseprice = finalbaseprice + percent;
                    } else {

                        Float x = Float.valueOf(baseprice);
                        float percent = (x * Float.valueOf(value)) / 100;
                        finalbaseprice = finalbaseprice + percent;
                    }
                }
            }
        }
        return finalbaseprice;
    }

    public String getdefaultquantity(String name) {
        String check = "";
        ArrayList<String> list = selectedvaluewithoptions.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String qty = (String) iterator.next();
            String[] parts = qty.split(":");
            if (parts[0].equals("default_qty")) {
                check = parts[1];
            }
        }
        return check;
    }

    public void finalpriceset() {
        Iterator finalvalues = dependentvaluestoadd.entrySet().iterator();
        float finalpricecombo = 0;

        while (finalvalues.hasNext()) {
            Map.Entry pair = (Map.Entry) finalvalues.next();
            String value = (String) pair.getValue();
            Float x = Float.valueOf(value);
            finalpricecombo = finalpricecombo + x;
        }

        finalpricecombo = main_value + finalpricecombo;

        if (isspecialset) {
            float custom_float = pricechangecustomoptions();
            if (custom_float != 0) {
                finalpricecombo = finalpricecombo + custom_float;
                specialprice.setText(currency_symbol + finalpricecombo);
            } else {
                Float x = Float.valueOf(baseprice);
                finalpricecombo = finalpricecombo + x;
                specialprice.setText(currency_symbol + finalpricecombo);
            }
        } else {
            float custom_float = pricechangecustomoptions();
            if (custom_float != 0) {
                finalpricecombo = finalpricecombo + custom_float;
                normalprice.setText(currency_symbol + finalpricecombo);
            } else {
                Float x = Float.valueOf(baseprice);
                finalpricecombo = finalpricecombo + x;
                normalprice.setText(currency_symbol + finalpricecombo);
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public String getlinkofproduct(String name) {
        String link = "";
        ArrayList<String> list = linkshashmap.get(name);
        Iterator iterator = Objects.requireNonNull(list).iterator();
        while (iterator.hasNext()) {
            String pricename = (String) iterator.next();
            String[] parts = pricename.split("=");
            if (parts[0].equals("link_url")) {
                link = parts[1];
            }
        }
        return link;
    }

    public void setcount(String count) {
        counter_string = count;
        try {
            invalidateOptionsMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buynowshow() {
        if (session.isLoggedIn()) {
            cedhandlecheckout();
        } else {
            Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }


    /*public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }*/

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        if (getIntent().hasExtra("ean")) {
            Intent intent = new Intent(getApplicationContext(), Ced_Scanner.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}