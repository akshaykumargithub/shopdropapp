package shop.dropapp.rest;

import com.google.gson.JsonObject;

import retrofit2.http.Headers;
import shop.dropapp.utils.Urls;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<Object> checkValidity(@Url String url);

    @POST(Urls.MODULE_LIST)
    Call<Object> getModuleList(@Body JsonObject postData);

    @POST(Urls.CART_COUNT)
    Call<Object> getCartCount(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.ALl_CATEGORIES)
    Call<Object> getAllCategories(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.LOGIN)
    Call<Object> getLoginData(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.COMPARE_REMOVE)
    Call<Object> removefrom_Compare(@Header("hashkey") String hashkey,
                                    @Path(value = "path") String path,
                                    @Body JsonObject postData);

    @POST(Urls.VALIDATE_NUMBER)
    Call<Object> validateNumber(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.REQUEST_OTP)
    Call<Object> requestOTP(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.VERIFY_OTP)
    Call<Object> verifyOTP(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.SOCIAL_LOGIN)
    Call<Object> getSocialLoginData(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.FORGOT_PASSWORD)
    Call<Object> getForgotPassData(@Path(value = "path") String path, @Body JsonObject postData);

    @POST(Urls.REGISTER)
    Call<Object> getRegisterData(@Path(value = "path") String path, @Body JsonObject postData);

    @GET(Urls.REGISTER_FIELDS)
    Call<Object> getRegisterFields(@Path(value = "path")  String path);

    @POST(Urls.UPDATE_PROFILE)
    Call<Object> getProfileUpdate(@Header("hashkey") String hashkey,
                                  @Path(value = "path")  String path,
                                  @Body JsonObject postData);

    @POST(Urls.HOME_NEWTHEME)
    Call<Object> getnewthemehomepage(@Path(value = "path")  String path,@Body JsonObject postData);

    @POST(Urls.COMPARE_URL)
    Call<Object> getcomparelist(@Header("hashkey") String hashkey,
                                @Path(value = "path")  String path,
                                @Body JsonObject postData);

    @GET("rest/V1/mobiconnect/module/gethomepage/1/{store_id}")
    Call<Object> getBanners(@Path("store_id") String store_id, @Path(value = "path")  String path);

    @GET("rest/V1/mobiconnectdeals/getdealgroup/{store_id}")
    Call<Object> getDeals(@Path("store_id") String store_id,@Path(value = "path")  String path);


    @GET("rest/V1/mobiconnect/home/featured/page/{page}/store/{store_id}")
    Call<Object> getFeatured(@Path("page") String page,
                             @Path("store_id") String store_id,@Path(value = "path")  String path);


    @GET(Urls.GET_COUNTRIES + "{countryid}")
    Call<Object> getState(@Path("countryid") String countryid,@Path(value = "path")  String path);

    @GET(Urls.GET_COUNTRIES)
    Call<Object> getcountry(@Path(value = "path")  String path);

    @GET(Urls.GET__SHOP_RATINGOPTION)
    Call<Object> getratingoption(@Path(value = "path")  String path);

    @GET(Urls.GETPRODUCT_RATINGOPTION)
    Call<Object> getproductratingoption(@Path(value = "path")  String path);

    @POST(Urls.GET_CATEGORIES)
    Call<Object> getCategories(@Body JsonObject postData,@Path(value = "path")  String path);

    /*@GET
    Call<Object> getWebCheckout(@Url String url);*/

    @POST(Urls.ADD_TO_CART)
    Call<Object> addToCart(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.PRODUCTREVIEW_ADD)
    Call<Object> addproductreview(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.PRODUCTREVIEW_LISTING)
    Call<Object> productreviewlisting(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.ADD_TO_COMPARE)
    Call<Object> addToCompare(@Header("hashkey") String hashkey,
                              @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.VIEWIMAGE)
    Call<Object> viewimage(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.VIEW_CART)
    Call<Object> getViewCart(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.DEVICE_REGISTER)
    Call<Object> registerdevicetoserver(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.EMPTY_CART)
    Call<Object> emptyCart(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.DELETE_CART)
    Call<Object> deleteFromCart(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.APPLY_COUPON)
    Call<Object> applycoupon(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.UPDATE_CART)
    Call<Object> updateCart(@Body JsonObject postData,@Path(value = "path")  String path);


    @POST(Urls.SAVE_ADDRESS)
    Call<Object> saveAddress(@Header("hashkey") String hashkey,
                             @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.DELETE_ADDRESS)
    Call<Object> deleteAddress(@Header("hashkey") String hashkey,
                               @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_ADDRESS)
    Call<Object> getAddressList(@Header("hashkey") String hashkey,
                                @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_WISHLIST)
    Call<Object> getWishList(@Header("hashkey") String hashkey,
                             @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.CLEAR_WISHLIST)
    Call<Object> clearWishList(@Header("hashkey") String hashkey,
                               @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.REMOVE_WISHLIST)
    Call<Object> removeWishList(@Header("hashkey") String hashkey,
                                @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.NOTIFICATION_LIST)
    Call<Object> getnotificationlist(@Header("hashkey") String hashkey,
                                     @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.ADD_WISHLIST)
    Call<Object> addToWishList(@Header("hashkey") String hashkey,
                               @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_ORDERS)
    Call<Object> getOrders(@Header("hashkey") String hashkey,
                           @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_ORDERVIEW)
    Call<Object> getOrderView(@Header("hashkey") String hashkey,
                              @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.REORDER)
    Call<Object> getReorder(@Header("hashkey") String hashkey,
                            @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_AUTOCOMPLETE)
    Call<Object> getAutoComplete(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.SEARCH)
    Call<Object> getSearchList(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_DOWNLOADED)
    Call<Object> getDownloadProducts(@Header("hashkey") String hashkey,
                                     @Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.VIEW_PRODUCT)
    Call<Object> getProductView(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.PRODUCT_LIST)
    Call<Object> getProductList(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.SELLER_PRODUCT_LIST)
    Call<Object> getSellerProductList(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_SELLERS_LIST)
    Call<Object> getsellersList(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.SUBMIT_VENDORREVIEW)
    Call<Object> submitvendorreviewdata(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_VENDORREVIEW)
    Call<Object> getVendorReview(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_SELLERS_SHOPS)
    Call<Object> getsellersShops(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_SELLER_CATEGORY)
    Call<Object> getSellerCategories(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.SAVE_BILLING_ADDRESS)
    Call<Object> saveBillingShipping(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GET_METHODS)
    Call<Object> getShippingPayment(@Body JsonObject postData,@Path(value = "path")  String path);

    @GET(Urls.DELIVERY_INFO)
    Call<Object> getDeliveryDateInfo(@Path(value = "path")  String path);

    @POST(Urls.SAVE_DELIVERY_DATE_INFO)
    Call<Object> saveDeliveryDateInfo(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.SAVE_METHODS)
    Call<Object> saveShippingPayment(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.GENERATE_TOKEN)
    Call<Object> generateToken(@Body JsonObject postData/*,@Path(value = "path")  String path*/);

    @POST(Urls.SAVE_ORDER)
    Call<Object> saveOrder(@Body JsonObject postData,@Path(value = "path")  String path);

    @POST(Urls.ADDITIONAL_INFO)
    Call<Object> additionalInfo(@Body JsonObject postData,@Path(value = "path")  String path);

    @GET
    Call<Object> getDataFromUrl(@Url String url);

    @GET(Urls.GETREQUIREDFIELDS)
    Call<Object> getrequiredfields(@Path(value = "path")  String path);

    @GET(Urls.SET_STORE )
    Call<Object> setstore(@Path("store_id") String store_id);

}
