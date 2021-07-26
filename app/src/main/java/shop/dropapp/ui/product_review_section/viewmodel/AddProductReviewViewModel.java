package shop.dropapp.ui.product_review_section.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class AddProductReviewViewModel extends ViewModel
{
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public AddProductReviewViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> addproductreview(Context context, String storekey,JsonObject postData){
        MutableLiveData<ApiResponse> mutableData2 = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addproductreview(storekey,parameters);
        apiCall.postRequest(call, context, mutableData2,showloader);
        return mutableData2;
    }
    /*  public MutableLiveData<ApiResponse> productreviewlisting(Context context, JsonObject postData, String hashkey){
        parameters.add("parameters", postData);
        Call<Object> call = repository.productreviewlisting(parameters,hashkey);
        apiCall.postRequest(call, context, mutableData);
        return mutableData;
    }*/
    public MutableLiveData<ApiResponse> getproductratingoption(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableData = new MutableLiveData<>();
        Call<Object> call = repository.getproductratingoption(storekey);
        apiCall.postRequest(call, context, mutableData,showloader);
        return mutableData;
    }



}