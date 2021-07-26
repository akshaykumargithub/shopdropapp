package shop.dropapp.repository;

import com.google.gson.JsonObject;

import shop.dropapp.rest.ApiInterface;

import javax.inject.Inject;

import retrofit2.Call;

public class Repository {
    private ApiInterface apiInterface;

    @Inject
    public Repository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }
    public Call<Object> checkValidity(String url) {
        return apiInterface.checkValidity(url);
    }

    public Call<Object> getModuleList(JsonObject postData) {
        return apiInterface.getModuleList(postData);
    }

    public Call<Object> getCartCount(String storeKey, JsonObject postData) {
        return apiInterface.getCartCount(storeKey, postData);
    }

    public Call<Object> getAllCategories(String storeKey, JsonObject postData) {
        return apiInterface.getAllCategories(storeKey, postData);
    }

    public Call<Object> getLoginData(String storeKey, JsonObject postData) {
        return apiInterface.getLoginData(storeKey, postData);
    }

    public Call<Object> validateNumber(String storeKey, JsonObject postData) {
        return apiInterface.validateNumber(storeKey, postData);
    }

    public Call<Object> requestOTP(String storeKey, JsonObject postData) {
        return apiInterface.requestOTP(storeKey, postData);
    }

    public Call<Object> verifyOTP(String storeKey, JsonObject postData) {
        return apiInterface.verifyOTP(storeKey, postData);
    }

    public Call<Object> removefrom_Compare(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.removefrom_Compare(hashkey, storeKey, postData);
    }

    public Call<Object> getSocialLoginData(String storeKey, JsonObject postData) {
        return apiInterface.getSocialLoginData(storeKey, postData);
    }

    public Call<Object> getForgotPassData(String storeKey, JsonObject postData/*,String hashkey*/) {
        return apiInterface.getForgotPassData(storeKey, postData);
    }

    public Call<Object> getRegisterData(String storeKey, JsonObject postData) {
        return apiInterface.getRegisterData(storeKey, postData);
    }

    public Call<Object> getRegisterFields(String storeKey) {
        return apiInterface.getRegisterFields(storeKey);
    }

    public Call<Object> getProfileUpdate(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getProfileUpdate(hashkey, storeKey, postData);
    }

    public Call<Object> getBanners(String storeKey, String store_id) {
        return apiInterface.getBanners(store_id, storeKey);
    }

    public Call<Object> getnewthemehomepage(String storeKey, JsonObject postData) {
        return apiInterface.getnewthemehomepage(storeKey, postData);
    }

    public Call<Object> getcomparelist(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getcomparelist(hashkey, storeKey, postData);
    }

    public Call<Object> getDeals(String storeKey, String store_id) {
        return apiInterface.getDeals(store_id, storeKey);
    }

    public Call<Object> getFeatured(String storeKey, String page, String store_id) {
        return apiInterface.getFeatured(page, store_id, storeKey);
    }

    public Call<Object> getStates(String storeKey, String countryid) {
        return apiInterface.getState(countryid, storeKey);
    }

    public Call<Object> getcountry(String storeKey) {
        return apiInterface.getcountry(storeKey);
    }

    public Call<Object> getproductratingoption(String storeKey) {
        return apiInterface.getproductratingoption(storeKey);
    }

    public Call<Object> getratingoption(String storeKey) {
        return apiInterface.getratingoption(storeKey);
    }

    public Call<Object> getCategories(String storeKey, JsonObject postData) {
        return apiInterface.getCategories(postData, storeKey);
    }

    /*public Call<Object> getWebCheckout(String url){
        return apiInterface.getDataFromUrl(url,storeKey);
    }*/

    public Call<Object> addToCart(String storeKey, JsonObject postData) {
        return apiInterface.addToCart(postData, storeKey);
    }

    public Call<Object> addproductreview(String storeKey, JsonObject postData) {
        return apiInterface.addproductreview(postData, storeKey);
    }

    public Call<Object> productreviewlisting(String storeKey, JsonObject postData) {
        return apiInterface.productreviewlisting(postData, storeKey);
    }

    public Call<Object> addToCompare(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.addToCompare(hashkey,postData, storeKey);
    }

    public Call<Object> viewimage(String storeKey, JsonObject postData) {
        return apiInterface.viewimage(postData, storeKey);
    }

    public Call<Object> getViewCart(String storeKey, JsonObject postData) {
        return apiInterface.getViewCart(postData, storeKey);
    }

    public Call<Object> registerdevicetoserver(String storeKey, JsonObject postData) {
        return apiInterface.registerdevicetoserver(postData, storeKey);
    }

    public Call<Object> emptyCart(String storeKey, JsonObject postData) {
        return apiInterface.emptyCart(postData, storeKey);
    }

    public Call<Object> deleteFromCart(String storeKey, JsonObject postData) {
        return apiInterface.deleteFromCart(postData, storeKey);
    }

    public Call<Object> applycoupon(String storeKey, JsonObject postData) {
        return apiInterface.applycoupon(postData, storeKey);
    }

    public Call<Object> updateCart(String storeKey, JsonObject postData) {
        return apiInterface.updateCart(postData, storeKey);
    }

    public Call<Object> saveAddress(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.saveAddress(hashkey, postData, storeKey);
    }

    public Call<Object> deleteAddress(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.deleteAddress(hashkey, postData, storeKey);
    }

    public Call<Object> getAddressList(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getAddressList(hashkey, postData, storeKey);
    }

    public Call<Object> getWishList(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getWishList(hashkey, postData, storeKey);
    }

    public Call<Object> clearWishList(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.clearWishList(hashkey, postData, storeKey);
    }

    public Call<Object> getnotificationlist(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getnotificationlist(hashkey, postData, storeKey);
    }

    public Call<Object> removeWishList(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.removeWishList(hashkey, postData, storeKey);
    }

    public Call<Object> addToWishList(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.addToWishList(hashkey, postData, storeKey);
    }

    public Call<Object> getOrders(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getOrders(hashkey, postData, storeKey);
    }

    public Call<Object> getOrderView(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getOrderView(hashkey, postData, storeKey);
    }

    public Call<Object> getReorder(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getReorder(hashkey, postData, storeKey);
    }

    public Call<Object> getAutoCompleteData(String storeKey, JsonObject postData) {
        return apiInterface.getAutoComplete(postData, storeKey);
    }

    public Call<Object> getSearchList(String storeKey, JsonObject postData) {
        return apiInterface.getSearchList(postData, storeKey);
    }

    public Call<Object> getDownloadProducts(String storeKey, JsonObject postData, String hashkey) {
        return apiInterface.getDownloadProducts(hashkey, postData, storeKey);
    }

    public Call<Object> getProductView(String storeKey, JsonObject postData) {
        return apiInterface.getProductView(postData, storeKey);
    }

    public Call<Object> getProductList(String storeKey, JsonObject postData) {
        return apiInterface.getProductList(postData, storeKey);
    }

    public Call<Object> getSellerProductList(String storeKey, JsonObject postData) {
        return apiInterface.getSellerProductList(postData, storeKey);
    }

    public Call<Object> getSellerList(String storeKey, JsonObject postData) {
        return apiInterface.getsellersList(postData, storeKey);
    }

    public Call<Object> getVendorReview(String storeKey, JsonObject postData) {
        return apiInterface.getVendorReview(postData, storeKey);
    }

    public Call<Object> submitvendorreviewdata(String storeKey, JsonObject postData) {
        return apiInterface.submitvendorreviewdata(postData, storeKey);
    }

    public Call<Object> getSellerShops(String storeKey, JsonObject postData) {
        return apiInterface.getsellersShops(postData, storeKey);
    }

    public Call<Object> getSellerCategories(String storeKey, JsonObject postData) {
        return apiInterface.getSellerCategories(postData, storeKey);
    }

    public Call<Object> saveBillingShipping(String storeKey, JsonObject postData) {
        return apiInterface.saveBillingShipping(postData, storeKey);
    }

    public Call<Object> getShippingPayment(String storeKey, JsonObject postData) {
        return apiInterface.getShippingPayment(postData, storeKey);
    }

    public Call<Object> getDeliveryDateInfo(String storeKey) {
        return apiInterface.getDeliveryDateInfo(storeKey);
    }

    public Call<Object> saveDeliveryDateInfo(String storeKey, JsonObject postData) {
        return apiInterface.saveDeliveryDateInfo(postData, storeKey);
    }

    public Call<Object> saveShippingPayment(String storeKey, JsonObject postData) {
        return apiInterface.saveShippingPayment(postData, storeKey);
    }

    public Call<Object> saveOrder(String storeKey, JsonObject postData) {
        return apiInterface.saveOrder(postData, storeKey);
    }

    public Call<Object> generateToken(String storeKey, JsonObject postData) {
        return apiInterface.generateToken(postData/*, storeKey*/);
    }

    public Call<Object> additionalInfo(String storeKey, JsonObject postData) {
        return apiInterface.additionalInfo(postData, storeKey);
    }

    public Call<Object> getDataFromUrl( String url) {
        return apiInterface.getDataFromUrl(url);
    }

    public Call<Object> setstore(String store_id) {
        return apiInterface.setstore(store_id);
    }

    public Call<Object> getrequiredfields(String storeKey) {
        return apiInterface.getrequiredfields(storeKey);
    }
}
