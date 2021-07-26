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

public class HomeViewModel extends ViewModel {
    Boolean showloader = true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public HomeViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getnewthemehomepage(Context context, String storekey, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableNewThemeHomePage = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getnewthemehomepage(storekey, parameters);
        apiCall.postRequest(call, context, mutableNewThemeHomePage, showloader);
        return mutableNewThemeHomePage;
    }

    public MutableLiveData<ApiResponse> getBannersData(Context context, String storekey, String store_id) {
        MutableLiveData<ApiResponse> mutableBannersData = new MutableLiveData<>();
        Call<Object> call = repository.getBanners(storekey, store_id);
        apiCall.postRequest(call, context, mutableBannersData, showloader);
        return mutableBannersData;
    }

    public MutableLiveData<ApiResponse> getDealsData(Context context,String storekey, String store_id) {
        MutableLiveData<ApiResponse> mutableDealsData = new MutableLiveData<>();
        Call<Object> call = repository.getDeals(storekey,store_id);
        apiCall.postRequest(call, context, mutableDealsData, showloader);
        return mutableDealsData;
    }

    public MutableLiveData<ApiResponse> getFeaturedData(Context context, String storekey,String page, String store_id) {
        MutableLiveData<ApiResponse> mutableFeaturedData = new MutableLiveData<>();
        Call<Object> call = repository.getFeatured(storekey,page, store_id);
        apiCall.postRequest(call, context, mutableFeaturedData, showloader);
        return mutableFeaturedData;
    }

}
