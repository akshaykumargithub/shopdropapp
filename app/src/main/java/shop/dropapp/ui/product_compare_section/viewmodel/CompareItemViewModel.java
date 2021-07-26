package shop.dropapp.ui.product_compare_section.viewmodel;

/*public class CompareItemViewModel {
}*/

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import shop.dropapp.repository.Repository;
import shop.dropapp.rest.ApiCall;
import shop.dropapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CompareItemViewModel  extends ViewModel
{
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CompareItemViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> removefrom_Compare(Context context, String storekey,JsonObject postData, String hashkey){
        MutableLiveData<ApiResponse> mutableData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.removefrom_Compare(storekey,parameters,hashkey);
        apiCall.postRequest(call, context, mutableData,showloader);
        return mutableData;
    }
  public MutableLiveData<ApiResponse> addtocart(Context context,String storekey, JsonObject postData){
      MutableLiveData<ApiResponse> mutablecartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToCart(storekey,parameters);
        apiCall.postRequest(call, context, mutablecartData,showloader);
        return mutablecartData;
    }


}
