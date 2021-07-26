package shop.dropapp.ui.loginsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class RegisterViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public RegisterViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getFieldsData(Context context,String storekey){
        MutableLiveData<ApiResponse> mutableFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getRegisterFields(storekey);
        apiCall.postRequest(call, context, mutableFieldsData,showloader);
        return mutableFieldsData;
    }

    public MutableLiveData<ApiResponse> getRegisterData(Context context, String storekey,JsonObject postData){
        MutableLiveData<ApiResponse> mutableRegisterData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getRegisterData(storekey, parameters);
        apiCall.postRequest(call, context, mutableRegisterData,showloader);
        return mutableRegisterData;
    }

    public MutableLiveData<ApiResponse> getLoginData(Context context, String storekey,JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getLoginData(storekey,parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

}
