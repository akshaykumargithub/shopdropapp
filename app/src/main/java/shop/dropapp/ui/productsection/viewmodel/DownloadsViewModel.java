package shop.dropapp.ui.productsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class DownloadsViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public DownloadsViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getDownloadProducts(Context context,String storekey, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableDownloadData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getDownloadProducts(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableDownloadData,showloader);
        return mutableDownloadData;
    }
}
