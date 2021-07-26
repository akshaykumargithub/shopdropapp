package shop.dropapp.ui.orderssection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class OrderViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public OrderViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getOrdersData(Context context,String storekey, JsonObject postData, String hashkey){
        MutableLiveData<ApiResponse> mutableOrdersData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getOrders(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableOrdersData,showloader);
        return mutableOrdersData;
    }

    public MutableLiveData<ApiResponse> getOrderViewData(Context context,String storekey, JsonObject postData, String hashkey)
    {
        MutableLiveData<ApiResponse> mutableOrderViewData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getOrderView(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableOrderViewData,showloader);
        return mutableOrderViewData;
    }
    public MutableLiveData<ApiResponse> getReorder(Context context,String storekey, JsonObject postData,String hashkey)
    {
        MutableLiveData<ApiResponse> mutableReorderData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getReorder(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableReorderData,showloader);
        return mutableReorderData;
    }
}
