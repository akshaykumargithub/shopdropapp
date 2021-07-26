package shop.dropapp.ui.loginsection.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import org.json.JSONException;

import java.security.MessageDigest;

import javax.inject.Inject;

import retrofit2.Call;

public class LoginViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public LoginViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getLoginData(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getLoginData(storekey,parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

    public MutableLiveData<ApiResponse> validateNumber(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.validateNumber(storekey,parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

    public MutableLiveData<ApiResponse> requestOTP(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.requestOTP(storekey,parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

    public MutableLiveData<ApiResponse> verifyNumber(Context context,String storekey, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.verifyOTP(storekey,parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

    public MutableLiveData<ApiResponse> getSocialLoginData(Context context,String storekey, JsonObject postData) throws JSONException {
        MutableLiveData<ApiResponse> mutableSocialLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSocialLoginData(storekey,parameters);
        apiCall.postRequest(call, context, mutableSocialLoginData,showloader);
        return mutableSocialLoginData;
    }

    public MutableLiveData<ApiResponse> forgotPassword(Context context,String storekey, JsonObject postData/*, String hashkey*/){
        MutableLiveData<ApiResponse> mutableForgotPassData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getForgotPassData(storekey,parameters);
        apiCall.postRequest(call, context, mutableForgotPassData,showloader);
        return mutableForgotPassData;
    }

    public void getFacebookKeyHash(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(context.getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }
}
