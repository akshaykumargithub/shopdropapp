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
package shop.dropapp.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import shop.dropapp.base.utilapplication.AnalyticsApplication;
import shop.dropapp.ui.networkhandlea_activities.Ced_ConnectionDetector;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import shop.dropapp.Ced_MageNative_Upgrade.Ced_Upgrade;
import shop.dropapp.R;
import shop.dropapp.base.viewmodel.MainViewModel;
import shop.dropapp.utils.Ced_Load_Language;
import shop.dropapp.ui.networkhandlea_activities.Ced_NoInternetconnection;
import shop.dropapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import shop.dropapp.ui.websection.Ced_Weblink;
import shop.dropapp.databinding.MagenativeActivityMainBinding;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.introsection.activity.Ced_Illustration;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.ui.productsection.activity.Ced_NewProductView;
import shop.dropapp.ui.productsection.activity.Ced_New_Product_Listing;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import shop.dropapp.utils.Status;


@AndroidEntryPoint
public class Ced_MainActivity extends AppCompatActivity {
    public static int bottomtabposition = 0;
    Ced_ConnectionDetector cedConnectionDetector;
    public static boolean enable_Log = true;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Inject
    ViewModelFactory viewModelFactory;
    MainViewModel mainViewModel;
    public static String latestcartcount = "no_count";
    private static int SPLASH_TIME_OUT = 500;
    String Jstring;
    String URL;
    Ced_SessionManagement cedSessionManagement;
    Ced_SessionManagement_login cedSessionManagement_login;
    JsonObject getcartcountdata;
    Ced_Load_Language cedLoad_language;
    boolean paid = true;
    String product_id = "";
    String ID = "";
    String link = "";
    private String NEW = "new";
    private String OLD = "old";
    private String CUSTOMER_ID = "customer_id";
    private String CART_ID = "cart_id";
    private String STORE_ID = "store_id";
    private String ZERO = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cedConnectionDetector = new Ced_ConnectionDetector(this);
        MagenativeActivityMainBinding activityMainBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_main,
                null, false);
        setContentView(activityMainBinding.getRoot());
        cedSessionManagement = Ced_SessionManagement.getCed_sessionManagement(this);
        mainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
        Window window = this.getWindow();
        getfbreleasesha();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cedSessionManagement_login = Ced_SessionManagement_login.getShredPrefs(this);
        cedLoad_language = new Ced_Load_Language();
        if (cedSessionManagement.getStoreLocale() != null) {
            cedLoad_language.setLanguagetoLoad(cedSessionManagement.getStoreLocale(), this);
        }

        if (getIntent().getDataString() != null) {
            String link = getIntent().getDataString();
            String[] datavalue = link.split("/");
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
        processRequests();
    }

    private void processRequests() {
        if (cedConnectionDetector.isConnectingToInternet() || cedSessionManagement.get_getcartcountdata() != null) {
            //getCartCount and Details
            getcartcountdata = new JsonObject();
            if (cedSessionManagement_login.isLoggedIn()) {
                getcartcountdata.addProperty(CUSTOMER_ID, cedSessionManagement_login.getCustomerid());
                request_cartcountdata();
            } else {
                if (cedSessionManagement.getCartId() != null) {
                    getcartcountdata.addProperty(CART_ID, cedSessionManagement.getCartId());
                    if (cedSessionManagement.getStoreId() != null) {
                        getcartcountdata.addProperty(STORE_ID, cedSessionManagement.getStoreId());
                        request_cartcountdata();
                    }
                } else {
                    getcartcountdata.addProperty(CART_ID, ZERO);
                    request_cartcountdata();
                }
            }

        } else {
            setContentView(R.layout.magenative_nointernetconnection);
        }
    }

    private void getfbreleasesha() {
        byte[] sha1 = {
                (byte) 0xE1, 0x28, 0x63, (byte) 0xBB, (byte) 0x7E, (byte) 0xE1, (byte) 0x93, (byte) 0x05, (byte) 0xC0, (byte) 0x34, (byte) 0x3E, (byte) 0xED, (byte) 0xB8, (byte) 0xC7, (byte) 0xE7, (byte) 0xF7, (byte) 0xF5, 0x59, (byte) 0x9A, (byte) 0x3D
        };
        Log.d("REpo", "keyhashGooglePlaySignIn:=>" + Base64.encodeToString(sha1, Base64.NO_WRAP));
    }

    public void showmsg(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.AppTheme));
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void request_cartcountdata() {
        if (cedConnectionDetector.isConnectingToInternet()) {
            mainViewModel.getCartCount(this, cedSessionManagement.getCurrentStore(), getcartcountdata).observe(this, this::getCartCountData);
        } else {
            if (cedSessionManagement.get_getcartcountdata() != null) {
                applydata(cedSessionManagement.get_getcartcountdata());
            } else {
                Intent intent = new Intent(this, Ced_NoInternetconnection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }

        }
    }

    private void getCartCountData(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                cedSessionManagement.save_getcartcountdata(apiResponse.data);
                applydata(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void applydata(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(this, Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else if (jsonObject.has("update") && jsonObject.getString("update").equals("true")) {
                Intent intent = new Intent(this, Ced_Upgrade.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                String success = jsonObject.getString("success");
                if (cedSessionManagement.getStoreId() == null) {
                    if (jsonObject.has("gender")) {
                        cedSessionManagement_login.savegender(jsonObject.getString("gender").toLowerCase());
                        cedSessionManagement_login.savename(jsonObject.getString("name"));
                    }
                    cedSessionManagement.saveStoreId(jsonObject.getString("default_store"));
                    cedSessionManagement.setcurrency(jsonObject.getString("currency_code"));
                    String[] localecodearray = jsonObject.getString("locale").split("_");
                    String localecode = localecodearray[0];
                    cedSessionManagement.saveStorelocale(localecode);
                    cedLoad_language.setLanguagetoLoad(localecode, this);
                }
                if (success.equals("true")) {
                    latestcartcount = String.valueOf(jsonObject.getInt("items_count"));
                    if (jsonObject.has("webview_checkout") && jsonObject.getString("webview_checkout").equalsIgnoreCase("1")) {
                        cedSessionManagement.iswebcheckoutenabled(true);
                    }else{
                        cedSessionManagement.iswebcheckoutenabled(false);
                    }
                }
                //categoriesRequest();
                show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void categoriesRequest() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("theme", "5");
            if (cedSessionManagement.getStoreId() != null) {
                jsonObject.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            // request_getallcat_data(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void request_getallcat_data(JsonObject jsonObject, String storekey) {
        if (cedConnectionDetector.isConnectingToInternet()) {
            mainViewModel.getAllCategories(this, storekey, jsonObject).observe((LifecycleOwner) this, this::getCategories);
        } else {
            if (cedSessionManagement.get_sidedrawercategory_data() != null) {
                getcategories(cedSessionManagement.get_sidedrawercategory_data());
            } else {
                Intent intent = new Intent(this, Ced_NoInternetconnection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        }
    }

    private void getCategories(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                cedSessionManagement.save_sidedrawercategory_data(apiResponse.data);
                getcategories(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void getcategories(String s) {
        try {
            JSONObject object = new JSONObject(s);
            if (object.has("header") && object.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                String status = object.getString("status");
                if (status.equals("success")) {
                    JSONObject data = object.getJSONObject("data");
                    JSONArray categories = data.getJSONArray("categories");
                    cedSessionManagement.savecategories(categories.toString());
                    show();
                } else {
                    show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        new Handler().postDelayed(() -> {
            Intent intent = null;
            if (product_id.isEmpty()) {
                if (ID.isEmpty()) {
                    if (link.isEmpty()) {
                        if (cedSessionManagement.getillustration().equals(NEW)) {
                            cedSessionManagement.illustration(OLD);
                            intent = new Intent(this, Ced_Illustration.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            finish();
                        } else {
                            intent = new Intent(this, Magenative_HomePageNewTheme.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            finish();
                        }

                    } else {
                        intent = new Intent(this, Ced_Weblink.class);
                        intent.putExtra("link", link);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    }
                } else {
                    intent = new Intent(this, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    finish();
                }
            } else {
                intent = new Intent(this, Ced_NewProductView.class);
                intent.putExtra("product_id", product_id);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }


}