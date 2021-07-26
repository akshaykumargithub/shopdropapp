package shop.dropapp.utils;

import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.R;

public class Urls {

    //put your base url here
    public static final String BASE_URL = "https://shopdrop.ca/";
    public static final String TAG = "REpo";
    public static final String HEADER = "c9f7f8f114089b7c0b4f396913cad6";

    //MainActivity
    public static final String MODULE_LIST = "rest/{path}/V1/mobiconnectadvcart/getmodulelist";
    public static final String CART_COUNT = "rest/{path}/V1/mobiconnect/checkout/getcartcount";
    public static final String ALl_CATEGORIES = "rest/{path}/V1/mobiconnectadvcart/category/getallcategories/";

    //Login & RegistrationSection
    public static final String LOGIN = "rest/{path}/V1/mobiconnect/customer/login";
    public static final String SOCIAL_LOGIN = "rest/{path}/V1/mobiconnect/sociallogin/create";
    public static final String FORGOT_PASSWORD = "rest/{path}/V1/mobiconnect/customer/forgotpassword/";
    public static final String REGISTER = "rest/{path}/V1/mobiconnect/customer/register";
    public static final String REGISTER_FIELDS = "rest/{path}/V1/mobiconnect/customer/getRequiredFields";

    //HomeSection
    public static final String HOMEPAGE = "rest/{path}/V1/mobiconnect/module/gethomepage/1/";
    public static final String DEALS = "rest/{path}/V1/mobiconnectdeals/getdealgroup/";
    public static final String FEATURED = "rest/{path}/V1/mobiconnect/home/featured/page/";
    public static final String GET_CATEGORIES = "rest/{path}/V1/mobiconnect/catalog/subcategories";
    public static final String HOME_NEWTHEME = "rest/{path}/V1/getNewHomepage";

    //UserProfileSection
    public static final String UPDATE_PROFILE = "rest/{path}/V1/mobiconnect/customer/update";
    public static final String GET_DOWNLOADED = "rest/{path}/V1/mobiconnectadvcart/customer/download";

    //AddressSection
    public static final String GET_COUNTRIES = "rest/{path}/V1/mobiconnect/module/getcountry/";
    public static final String SAVE_ADDRESS = "rest/{path}/V1/mobiconnect/customer/saveaddress";
    public static final String GET_ADDRESS = "rest/{path}/V1/mobiconnect/customer/address";
    public static final String DELETE_ADDRESS = "rest/{path}/V1/mobiconnect/customer/deleteaddress/";
    public static final String GETREQUIREDFIELDS = "rest/{path}/V1/mobiconnect/customer/getRequiredFields/";

    //WishListSection
    public static final String GET_WISHLIST = "rest/{path}/V1/mobiconnect/wishlist/getwishlist/";
    public static final String CLEAR_WISHLIST = "rest/{path}/V1/mobiconnect/wishlist/clear";
    public static final String REMOVE_WISHLIST = "rest/{path}/V1/mobiconnect/wishlist/remove";
    public static final String ADD_WISHLIST = "rest/{path}/V1/mobiconnect/wishlist/add/";

    //ProductSection
    public static final String VIEW_PRODUCT = "rest/{path}/V1/mobiconnect/catalog/view/";
    public static final String PRODUCT_LIST = "rest/{path}/V1/mobiconnect/catalog/products/";
    public static final String SELLER_PRODUCT_LIST = "rest/{path}/V1/seller/category/getproducts";
    public static final String ADD_TO_CART = "rest/{path}/V1/mobiconnect/checkout/add/";
    public static final String VIEWIMAGE = "rest/{path}/V1/mobiconnect/catalog/viewimage/";

    //Productreview
    public static final String PRODUCTREVIEW_LISTING = "rest/{path}/V1/mobiconnect/review/product";
    public static final String PRODUCTREVIEW_ADD = "rest/{path}/V1/mobiconnect/review/add";
    public static final String GETPRODUCT_RATINGOPTION = "rest/{path}/V1/mobiconnect/review/ratingoption";

    //Compare Section
    public static final String ADD_TO_COMPARE = "rest/{path}/V1/mobiconnect/addtocompare";
    public static final String COMPARE_URL = "rest/{path}/V1/mobiconnect/listcompare";
    public static final String COMPARE_REMOVE = "rest/{path}/V1/mobiconnect/remove";
    public static final String COMPARE_ADD = "rest/{path}/V1/mobiconnect/checkout/add/";

    //OTP Request Section
    public static final String VALIDATE_NUMBER = "rest/{path}/V1/mobiconnect/customer/validateNumber";
    public static final String REQUEST_OTP = "rest/{path}/V1/mobiconnect/customer/sendOtp";
    public static final String VERIFY_OTP = "rest/{path}/V1/mobiconnect/customer/verifyOtp";

    //Notification Section
    public static final String NOTIFICATION_LIST = "rest/{path}/V1/mobinotifications/listNotification";
    public static final String DEVICE_REGISTER = "rest/{path}/V1/mobinotifications/setdevice";

    //OrderSection
    public static final String GET_ORDERS = "rest/{path}/V1/mobiconnect/customer/order/";
    public static final String GET_ORDERVIEW = "rest/{path}/V1/mobiconnect/customer/orderview/";
    public static final String REORDER = "rest/{path}/V1/mobiconnect/customer/reorder";

    //SearchSection
    public static final String GET_AUTOCOMPLETE = "rest/{path}/V1/mobiconnectadvcart/search/autocomplete";
    public static final String SEARCH = "rest/{path}/V1/mobiconnect/catalog/search";

    //StoreSection
    public static final String GET_STORES = "rest/V1/mobiconnectstore/getlist";
    public static final String SET_STORE = "rest/V1/mobiconnectstore/setstore/{store_id}";

    //CartSection
    public static final String VIEW_CART = "rest/{path}/V1/mobiconnect/checkout/viewcart";
    public static final String EMPTY_CART = "rest/{path}/V1/mobiconnect/checkout/emptycart";
    public static final String DELETE_CART = "rest/{path}/V1/mobiconnect/checkout/delete";
    public static final String UPDATE_CART = "rest/{path}/V1/mobiconnect/checkout/updateqty";
    public static final String APPLY_COUPON = "rest/{path}/V1/mobiconnect/checkout/coupon";

    //CheckoutSection
    public static final String SAVE_BILLING_ADDRESS = "rest/{path}/V1/mobiconnect/checkout/savebillingshipping";
    public static final String GET_METHODS = "rest/{path}/V1/mobiconnect/checkout/getshippingpayament";
    public static final String SAVE_METHODS = "rest/{path}/V1/mobiconnect/checkout/saveshippingpayament";
    public static final String SAVE_ORDER = "rest/{path}/V1/mobiconnect/checkout/saveorder";
    public static final String ADDITIONAL_INFO = "rest/{path}/V1/mobiconnect/checkout/additionalinfo";

    public static final String WEBCHECKOUTURL = "mobiconnectcheckout/onepage/index/customer_id/";
    public static final String WEBCHECKOUT_GUESTURL = "mobiconnectcheckout/onepage/index/method/guest/";

    //SellerSection
    public static final String GET_SELLERS_LIST = "rest/{path}/V1/vendorapi/index/item";
    public static final String GET_SELLERS_SHOPS = "rest/{path}/V1/rest//V1/vendorapi/vproducts/vshops";
    public static final String GET_SELLER_CATEGORY = "rest/{path}/V1/seller/products/getcategories";
    public static final String GET__SHOP_RATINGOPTION = "rest/{path}/V1/vreviewapi/getRatingOption";
    public static final String SUBMIT_VENDORREVIEW = "rest/{path}/V1/vreviewapi/addReview";
    public static final String GET_VENDORREVIEW = "rest/{path}/V1/vreviewapi/getVendorReview";

    //DeliveryDateAddon
    public static final String DELIVERY_INFO = "rest/{path}/V1/mobideliverydate/deliverydate/info/";
    public static final String SAVE_DELIVERY_DATE_INFO = "rest/{path}/V1/mobideliverydate/deliverydate/saveDeliveryDateInfo";

    //Stripe
    public static final String GENERATE_TOKEN = "rest/V1/mobistripe/generatetoken";
}