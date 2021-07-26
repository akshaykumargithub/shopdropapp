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

public class WriteVendorReviewViewModel extends ViewModel {
    Boolean showloader=true;

    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public WriteVendorReviewViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getrationoption(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableRationOptionData = new MutableLiveData<>();
        Call<Object> call = repository.getratingoption(storekey);
        apiCall.postRequest(call, context, mutableRationOptionData,showloader);
        return mutableRationOptionData;
    }
    public MutableLiveData<ApiResponse> submitdataforvendorreview(Context context, String storekey,JsonObject postData) {
        MutableLiveData<ApiResponse> mutableSaveData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.submitvendorreviewdata(storekey, parameters);
        apiCall.postRequest(call, context, mutableSaveData,showloader);
        return mutableSaveData;
    }

}