package shop.dropapp.ui.cartsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CartViewModel extends ViewModel {
    Boolean showloader = true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CartViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> viewCart(Context context,String storekey, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableCartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getViewCart(storekey,parameters);
        apiCall.postRequest(call, context, mutableCartData, showloader);
        return mutableCartData;
    }

    public MutableLiveData<ApiResponse> emptyCart(Context context, String storekey, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableEmptyCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.emptyCart(storekey, parameters);
        apiCall.postRequest(call, context, mutableEmptyCartResponse, showloader);
        return mutableEmptyCartResponse;
    }

    public MutableLiveData<ApiResponse> applycoupon(Context context,String storekey, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableapplycouponResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.applycoupon(storekey,parameters);
        apiCall.postRequest(call, context, mutableapplycouponResponse, showloader);
        return mutableapplycouponResponse;
    }


    public MutableLiveData<ApiResponse> deleteFromCart(Context context,String storekey, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableDeleteCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.deleteFromCart(storekey,parameters);
        apiCall.postRequest(call, context, mutableDeleteCartResponse, showloader);
        return mutableDeleteCartResponse;
    }

    public MutableLiveData<ApiResponse> updateCart(Context context, String storekey,JsonObject postData) {
        MutableLiveData<ApiResponse> mutableUpdateCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.updateCart(storekey,parameters);
        apiCall.postRequest(call, context, mutableUpdateCartResponse, showloader);
        return mutableUpdateCartResponse;
    }
}
