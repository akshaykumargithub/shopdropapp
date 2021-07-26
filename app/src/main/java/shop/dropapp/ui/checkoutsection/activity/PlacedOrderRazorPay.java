package shop.dropapp.ui.checkoutsection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.ui.orderssection.activity.Ced_ViewOrder;
import shop.dropapp.databinding.ActivityPlacedOrderRazorPayBinding;
import shop.dropapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlacedOrderRazorPay extends Ced_NavigationActivity implements PaymentResultListener
{
    ActivityPlacedOrderRazorPayBinding placeOrderBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;

    String saveorderurl="";
    JSONObject jsonObject;
    HashMap<String, HashMap<String,String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String,String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    String email="";
    String paymentcode="";
    String order_id="";
    boolean load=false;
    TextView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        checkoutViewModel = new ViewModelProvider(PlacedOrderRazorPay.this, viewModelFactory).get(CheckoutViewModel.class);
 placeOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_placed_order_razor_pay, content, true);

        Checkout.preload(getApplicationContext());
        //  tooltext_address.setText(cedSessionManagement.gettool_address());
        bundledata=new HashMap<String,ArrayList<String>>();
        configdata=new HashMap<String,String>();
        finalconfigdata=new HashMap<String,HashMap<String,String>>();
        ProductData = new ArrayList<HashMap<String, String>>();
        paymentcode=getIntent().getStringExtra("paymentcode");
        warning= (TextView) findViewById(R.id.warning);
        jsonObject=new JSONObject();
        JsonObject orderpostdata=new JsonObject();
            if (getIntent().hasExtra("partialpayment")){/*******************for wallet******/
                orderpostdata.addProperty("check_wallet","true");
            }
            orderpostdata.addProperty("email",email);
            if(cedSessionManagement.getCartId()!=null)
            {
                orderpostdata.addProperty("cart_id",cedSessionManagement.getCartId());
            }
            if(session.isLoggedIn())
            {
                orderpostdata.addProperty("customer_id",session.getCustomerid());
            }
            if(cedSessionManagement.getStoreId()!=null)
            {
                orderpostdata.addProperty("store_id",cedSessionManagement.getStoreId());
            }
        saveorder(orderpostdata);
    }
    public  void saveorder(JsonObject data)
    {
        try
        {
            try {
                checkoutViewModel.saveOrder(PlacedOrderRazorPay.this,cedSessionManagement.getCurrentStore(), data).observe(PlacedOrderRazorPay.this, apiResponse -> {
                    switch (apiResponse.status){
                        case SUCCESS:
                            try {
                                Processorderdata(apiResponse.data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> Processorderdata(output.toString()), ProcessOrder.this, "POST", data);
            crr.execute(saveorderurl);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse()
            {
                @Override
                public void processFinish(Object output) throws JSONException
                {
                    Processorderdata(output.toString());
                }
            },PlacedOrderRazorPay.this,"POST",data);
            crr.execute(saveorderurl);*/

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void Processorderdata(String s) throws JSONException
    {
        JSONObject object=new JSONObject(s);
        JSONObject jsonObject=object;
        if(jsonObject.getString("success").equals("true"))
        {
            cedSessionManagement.clearcartId();
            load=true;
            warning.setVisibility(View.VISIBLE);
            if(paymentcode.equals("apppayment"))
            {
                order_id=jsonObject.getString("order_id");
                startPayment(jsonObject.getString("currency_code"),jsonObject.getString("grandtotal"));
            }
            else
            {
                Ced_MainActivity.latestcartcount="0";
                cedSessionManagement.clearcartId();
                changecount();
                Intent intent=new Intent(PlacedOrderRazorPay.this, Ced_ViewOrder.class);
                intent.putExtra("order_id",jsonObject.getString("order_id"));
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        }
    }
    @Override
    protected void onResume()
    {

        invalidateOptionsMenu();
        super.onResume();

    }
    public  void changecount()
    {
        invalidateOptionsMenu();
    }
    public void startPayment(String currencycode,String amount)
    {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setImage(R.drawable.placeholder);
        try
        {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Your Total Amount is:");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://k4h6h8w8.stackpathcdn.com/skin/frontend/apps/default/images/app-logo-with-sticky12.png");
            options.put("currency", currencycode);
            float total= Float.parseFloat(amount.replace(",", ""));
            total=total*100;
            options.put("amount", String.valueOf(total));
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");
            options.put("prefill", preFill);
            options.put("theme", new JSONObject("{color: '#011a27'}"));
            co.open(activity, options);
        }
        catch (Exception e)
        {
           showmsg("Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID)
    {
        try
        {
           showmsg("Payment Successful. Processing your order ..." );
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("additional_info", razorpayPaymentID);
            jsonObject.addProperty("order_id", order_id);
            jsonObject.addProperty("failure", "false");
            setFinalordercheck(jsonObject);
        }
        catch (Exception e)
        {
            Log.e("Exception", "Exception in onPaymentSuccess :", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response)
    {
        try
        {

            try
            {
                showmsg("Payment failed::Please try again Thank you.");
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("failure", "true");
                jsonObject.addProperty("order_id", order_id);
                setFinalordercheck(jsonObject);
            }
            catch (Exception e)
            {
                Log.e("Exception", "Exception in onPaymentSuccess :", e);
            }
        }
        catch (Exception e)
        {
            Log.e("Exception", "Exception in onPaymentError :", e);
        }
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        if(load)
        {
            try
            {
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("order_id", order_id);
                jsonObject.addProperty("failure", "true");
                setFinalordercheck(jsonObject);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }
   /* public void setFinalordercheck(JSONObject object)
    {
        try
        {
            Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse()
            {
                @Override
                public void processFinish(Object output) throws JSONException
                {
                    Log.i("FinalData",""+output.toString());
                    JSONObject jsonObject1=new JSONObject(output.toString());
                    String success=jsonObject1.getString("success");
                    if(success.equals("true"))
                    {
                        Ced_MainActivity.latestcartcount="0";
                        cedSessionManagement.clearcartId();
                        changecount();
                        Intent intent=new Intent(PlacedOrderRazorPay.this,Ced_ViewOrder.class);
                        intent.putExtra("order_id", order_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                    else
                    {
                        Ced_MainActivity.latestcartcount="0";
                        cedSessionManagement.clearcartId();
                        changecount();
                        Intent intent1=new Intent(getApplicationContext(), Ced_New_home_page.class);
                        startActivity(intent1);
                        finishAffinity();
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                }
            },PlacedOrderRazorPay.this,"POST",object.toString());
            crr.execute(finalordercheck);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }*/
   public void setFinalordercheck(JsonObject object) {
       try {
           checkoutViewModel.getAdditionalInfo(PlacedOrderRazorPay.this,cedSessionManagement.getCurrentStore(), object).observe(PlacedOrderRazorPay.this, apiResponse -> {
               switch (apiResponse.status){
                   case SUCCESS:
                       try {
                           JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                           String success = jsonObject1.getString("success");
                           if (success.equals("true")) {
                               Ced_MainActivity.latestcartcount = "0";
                               cedSessionManagement.clearcartId();
                               changecount();
                               Intent intent = new Intent(PlacedOrderRazorPay.this, Ced_ViewOrder.class);
                               intent.putExtra("order_id", order_id);
                               startActivity(intent);
                               overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                           } else {
                               Ced_MainActivity.latestcartcount = "0";
                               cedSessionManagement.clearcartId();
                               changecount();
                               Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                               startActivity(intent1);
                               finishAffinity();
                               overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                       break;

                   case ERROR:
                       Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                       showmsg(getResources().getString(R.string.errorString));
                       break;
               }
           });
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
