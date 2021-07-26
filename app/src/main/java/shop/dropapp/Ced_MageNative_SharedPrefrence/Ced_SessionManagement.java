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
package shop.dropapp.Ced_MageNative_SharedPrefrence;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Ced_SessionManagement {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int Private_Mode = 0;
    Context con;
    private static final String PREF_NAME = "data";
    public static final String Key_Email = "Email";
    public static final String Key_Name = "Name";
    public static final String KEY_CART_ID = "CARTID";
    public static final String Key_Is_WebCheckout = "iswebcheckoutenabled";
    public static final String Key_Currency = "currency";

    public static final String KEY_Language = "language";
    public static final String KEY_StoreLocale = "storelocale";
    public static final String KEY_STORE_ID = "storeid";
    public static final String KEY_STORED_HOMEPAGEDYNAMIC_DATA = "homepagedata";
    public static final String KEY_STORED_SIDEDRAWER_CAT_DATA = "sidedrawer_cat_data";
    public static final String KEY_STORED_GETCARTCOUNT_DATA = "getcartcountdata";


    public static Ced_SessionManagement cedSessionManagement;

    public static Ced_SessionManagement getCed_sessionManagement(Context context) {
        if (cedSessionManagement == null) {
            cedSessionManagement = new Ced_SessionManagement(context);
        }
        return cedSessionManagement;
    }

    public Ced_SessionManagement(Context context) {
        this.con = context;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
    }

    public Ced_SessionManagement(Application activity) {
        this.con = activity;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
    }

    public void savecartid(String cart_id) {
        editor.putString(KEY_CART_ID, cart_id);
        editor.commit();
    }

    public void save_tool_address(String tool_address) {
        editor.putString("tool_address", tool_address);
        editor.commit();
    }

    public String gettool_address() {
        return pref.getString("tool_address", "");
    }

    public void save_latitude(String lat) {
        editor.putString("lat", lat);
        editor.commit();
    }

    public void save_longitude(String lng) {
        editor.putString("lng", lng);
        editor.commit();
    }

    public void save_postcode(String postcode) {
        editor.putString("postcode", postcode);
        editor.commit();
    }

    public void save_city(String city) {
        editor.putString("city", city);
        editor.commit();
    }

    public void save_country(String country) {
        editor.putString("country", country);
        editor.commit();
    }

    public void save_state(String state) {
        editor.putString("state", state);
        editor.commit();
    }
    public void saveAddress(String address) {
        editor.putString("address", address);
        editor.commit();
    }

    public String getlatitude() {
        return pref.getString("lat", "");
    }
    public String getAddress() {
        return pref.getString("address", "");
    }

    public String getlongitude() {
        return pref.getString("lng", "");
    }

    public String getpostcode() {
        return pref.getString("postcode", "");
    }

    public String getcity() {
        return pref.getString("city", "");
    }

    public String getcountry() {
        return pref.getString("country", "");
    }

    public String getstate() {
        return pref.getString("state", "");
    }

    public void save_countrycode(String countrycode) {
        editor.putString("countrycode", "CA");
        editor.commit();
    }

    public String getcountrycode() {
        return pref.getString("countrycode", "CA");
    }


    public void illustration(String done) {
        editor.putString("illustration", done);
        editor.commit();
    }

    public void save_shipping_address(String shipping) {
        editor.putString("shipping_address", shipping);
        editor.commit();
    }

    public void saveCurrentStore(String currentStore) {
        editor.putString("currentStore", currentStore);
        editor.commit();
    }

    public String getshipping_address() {
        return pref.getString("shipping_address", "");
    }

    public void iswebcheckoutenabled(boolean enable) {
        editor.putBoolean(Key_Is_WebCheckout, enable);
        editor.commit();
    }

    public Boolean iswebcheckoutenabled() {
        return pref.getBoolean(Key_Is_WebCheckout, false);
    }

    public String getcategorytheme() {
        return pref.getString("theme", "1");
    }

    public String getCurrentStore() {
        return pref.getString("currentStore", "english_new");
    }

    public String getdrawertheme() {
        return pref.getString("drawertheme", "white");
    }

    public String getillustration() {
        return pref.getString("illustration", "new");
    }

    public boolean clearcartId() {
        return editor.remove(KEY_CART_ID).commit();
    }

    public String getCartId() {
        return pref.getString(KEY_CART_ID, null);
    }


    public String getvalidity() {
        return pref.getString("valid", "");
    }

    public void savevalidity(String valid) {
        editor.putString("valid", valid);
        editor.commit();
    }

    public void savecategories(String cat) {
        editor.putString("subcategories", cat);
        editor.commit();
    }

    public String getCategories() {
        return pref.getString("subcategories", "");
    }


    public void setcurrency(String currency) {
        editor.putString(Key_Currency, currency);
        editor.commit();
    }

    public String getcurrency() {
        return pref.getString(Key_Currency, "");
    }

    public void saveLanguageToLoad(String lang) {
        editor.putString(KEY_Language, lang);
        editor.commit();
    }

    public String getLanguageToLoad() {
        return pref.getString(KEY_Language, "");
    }

    public void saveStoreId(String store_id) {
        editor.putString(KEY_STORE_ID, store_id);
        editor.commit();
    }

    public String getStoreId() {
        return pref.getString(KEY_STORE_ID, "37");
    }

    public void save_homedata(String data) {
        editor.putString(KEY_STORED_HOMEPAGEDYNAMIC_DATA, data);
        editor.commit();
    }

    public String get_homedata() {
        return pref.getString(KEY_STORED_HOMEPAGEDYNAMIC_DATA, "");
    }

    public void save_getcartcountdata(String data) {
        editor.putString(KEY_STORED_GETCARTCOUNT_DATA, data);
        editor.commit();
    }

    public String get_getcartcountdata() {
        return pref.getString(KEY_STORED_GETCARTCOUNT_DATA, null);
    }

    public void save_sidedrawercategory_data(String data) {
        editor.putString(KEY_STORED_SIDEDRAWER_CAT_DATA, data);
        editor.commit();
    }

    public String get_sidedrawercategory_data() {
        return pref.getString(KEY_STORED_SIDEDRAWER_CAT_DATA, "");
    }

    public void saveStorelocale(String locale) {
        editor.putString(KEY_StoreLocale, locale);
        editor.commit();
    }

    public String getStoreLocale() {
        return pref.getString(KEY_StoreLocale, "");
    }
}
