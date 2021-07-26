package shop.dropapp.base.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class MainViewModel extends ViewModel {
    Boolean showloader=false;
    private static final String TAG = "MainViewModel";
    private MutableLiveData<ApiResponse> mutableValidityCheck = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> mutableModulesData = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> mutableCategoriesData = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> mutableCartCountData = new MutableLiveData<>();
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> checkValidity(Context context, String url){
        Call<Object> call = repository.checkValidity(url);
        apiCall.postRequest(call, context, mutableValidityCheck,showloader);
        return mutableValidityCheck;
    }

    public MutableLiveData<ApiResponse> getModulesList(Context context, JsonObject postData) {
        parameters.add("parameters", postData);
        Call<Object> call = repository.getModuleList(parameters);
        apiCall.postRequest(call, context, mutableModulesData,showloader);
        return mutableModulesData;
    }

    public MutableLiveData<ApiResponse> getAllCategories(Context context, String storekey,JsonObject postData){
        parameters.add("parameters", postData);
        Call<Object> call = repository.getAllCategories(storekey,parameters);
        apiCall.postRequest(call, context, mutableCategoriesData,showloader);
        return mutableCategoriesData;
    }

    public MutableLiveData<ApiResponse> getCartCount(Context context,String storekey, JsonObject postData){
        parameters.add("parameters", postData);
        Call<Object> call = repository.getCartCount(storekey,parameters);
        apiCall.postRequest(call, context, mutableCartCountData,showloader);
        return mutableCartCountData;
    }

}
