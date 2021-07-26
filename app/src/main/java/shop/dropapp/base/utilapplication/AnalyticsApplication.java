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
package shop.dropapp.base.utilapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.HiltAndroidApp;
import shop.dropapp.ui.notificationactivity.MyApplication;

@HiltAndroidApp
public class AnalyticsApplication extends Application {
    private static AnalyticsApplication mInstance;
    public static HashMap<String, ArrayList<String>> main_filters;
    public static HashMap<String, ArrayList<String>> seller_main_filters;
    public static String dealResponse = "";
    public static String categoryresponse = "";
    public static String productresponse = "";
    public static boolean loadeddeal = false;
    public static boolean loadedproducts = false;
    public String storeId = "";
    public Ced_SessionManagement ced_sessionManagement;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        main_filters = new HashMap<>();
        seller_main_filters = new HashMap<>();
        MultiDex.install(this);
    }

    public static synchronized AnalyticsApplication getInstance() {
        if (mInstance == null) {
            mInstance = new AnalyticsApplication();
        }
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /*public String getBaseUrl() {
        String baseUrl = "";
        if (storeId.equals("37"))
            baseUrl = "https://shopdrop.ca/rest/english_new/V1/";
        if (storeId.equals(("1")))
            baseUrl = "https://shopdrop.ca/rest/fr/V1/";
        if (storeId.equals(("4")))
            baseUrl = "https://shopdrop.ca/rest/Arabic/V1/";
        else
            baseUrl = "https://shopdrop.ca/rest/english_new/V1/";
        return baseUrl;

    }*/
}
