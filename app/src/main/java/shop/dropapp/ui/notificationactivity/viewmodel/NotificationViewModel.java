package shop.dropapp.ui.notificationactivity.viewmodel;

/*
public class NotificationViewModel {
}
*/

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class NotificationViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public NotificationViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getnotificationlist(Context context, JsonObject postData,String storekey,String header) {
        MutableLiveData<ApiResponse> mutableData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getnotificationlist(storekey,parameters,header);
        apiCall.postRequest(call, context, mutableData,showloader);
        return mutableData;
    }
}