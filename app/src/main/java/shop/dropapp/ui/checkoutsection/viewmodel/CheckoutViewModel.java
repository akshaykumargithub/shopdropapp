package shop.dropapp.ui.checkoutsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CheckoutViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CheckoutViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> saveBillingAddress(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveBillingShipping(storekey,parameters);
        apiCall.postRequest(call, context, mutableSaveAddressData,showloader);
        return mutableSaveAddressData;
    }

    public MutableLiveData<ApiResponse> getShippingPaymentMethods(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getShippingPayment(storekey,parameters);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }

    public MutableLiveData<ApiResponse> getDeliveryDateInfo(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        Call<Object> call = repository.getDeliveryDateInfo  (storekey);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }

    public MutableLiveData<ApiResponse> saveDeliveryDateInfo(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveDeliveryDateInfo(storekey,parameters);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }

    public MutableLiveData<ApiResponse> saveShippingPaymentMethods(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveMethodsData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveShippingPayment(storekey,parameters);
        apiCall.postRequest(call, context, mutableSaveMethodsData,showloader);
        return mutableSaveMethodsData;
    }

    public MutableLiveData<ApiResponse> saveOrder(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveOrderResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveOrder(storekey,parameters);
        apiCall.postRequest(call, context, mutableSaveOrderResponse,showloader);
        return mutableSaveOrderResponse;
    }

    public MutableLiveData<ApiResponse> generateToken(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveOrderResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.generateToken(storekey,parameters);
        apiCall.postRequest(call, context, mutableSaveOrderResponse,showloader);
        return mutableSaveOrderResponse;
    }

    public MutableLiveData<ApiResponse> getAdditionalInfo(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAdditionalInfoResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.additionalInfo(storekey,parameters);
        apiCall.postRequest(call, context, mutableAdditionalInfoResponse,showloader);
        return mutableAdditionalInfoResponse;
    }
}
