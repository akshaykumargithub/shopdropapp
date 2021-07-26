package shop.dropapp.ui.sellersection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class SellerListViewModel extends ViewModel {

    Boolean showloader=true;
    private static final String TAG = "SellerListViewModel";
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public SellerListViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getSellerListData(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableProductListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSellerList(storekey,parameters);
        apiCall.postRequest(call, context, mutableProductListData,showloader);
        return mutableProductListData;
    }
    public MutableLiveData<ApiResponse> getCountriesData(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableCountriesData = new MutableLiveData<>();
        Call<Object> call = repository.getcountry(storekey);
        apiCall.postRequest(call, context, mutableCountriesData,showloader);
        return mutableCountriesData;
    }
    public MutableLiveData<ApiResponse> getStates(Context context,String storekey, String country_code){
        MutableLiveData<ApiResponse> mutableStateData = new MutableLiveData<>();
        Call<Object> call = repository.getStates(country_code,storekey);
        apiCall.postRequest(call, context, mutableStateData,showloader);
        return mutableStateData;
    }
}