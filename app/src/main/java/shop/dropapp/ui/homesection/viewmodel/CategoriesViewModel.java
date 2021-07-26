package shop.dropapp.ui.homesection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CategoriesViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CategoriesViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getCategoriesData(Context context, String storekey,JsonObject postData){
        MutableLiveData<ApiResponse> mutableCategoriesData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getCategories(storekey, parameters);
        apiCall.postRequest(call, context, mutableCategoriesData,showloader);
        return mutableCategoriesData;
    }
}
