package shop.dropapp.ui.addresssection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class AddressViewModel extends ViewModel {

    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public AddressViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getCountriesData(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableCountriesData = new MutableLiveData<>();
        Call<Object> call = repository.getcountry(storekey);
        apiCall.postRequest(call, context, mutableCountriesData,showloader);
        return mutableCountriesData;
    }

    public MutableLiveData<ApiResponse> getStatesData(Context context,String storekey, String country_code){
        MutableLiveData<ApiResponse> mutableStateData = new MutableLiveData<>();
        Call<Object> call = repository.getStates(storekey,country_code);
        apiCall.postRequest(call, context, mutableStateData,showloader);
        return mutableStateData;
    }

    public MutableLiveData<ApiResponse> saveAddressData(Context context,String storekey, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableSaveAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveAddress(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableSaveAddressData,showloader);
        return mutableSaveAddressData;
    }

    public MutableLiveData<ApiResponse> getRequiredFields(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableAddressFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getrequiredfields(storekey);
        apiCall.postRequest(call, context, mutableAddressFieldsData,showloader);
        return mutableAddressFieldsData;
    }

    public MutableLiveData<ApiResponse> getAddressListData(Context context,String storekey, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableAddressListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getAddressList(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableAddressListData,showloader);
        return mutableAddressListData;
    }

    public MutableLiveData<ApiResponse> deleteAddressData(Context context,String storekey, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableDeleteAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.deleteAddress(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableDeleteAddressData,showloader);
        return mutableDeleteAddressData;
    }
}
