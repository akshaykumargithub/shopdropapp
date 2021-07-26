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

public class AddVendorReviewViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public AddVendorReviewViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }


    public MutableLiveData<ApiResponse> getVendorReview(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableProductListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getVendorReview(storekey,parameters);
        apiCall.postRequest(call, context, mutableProductListData,showloader);
        return mutableProductListData;
    }
}