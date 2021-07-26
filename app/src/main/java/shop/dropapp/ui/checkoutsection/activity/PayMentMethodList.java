package shop.dropapp.ui.checkoutsection.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.OkHttpClient;
import shop.dropapp.Ced_MageNative_Location.Constants;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.base.fragment.Ced_CMSPage;
import shop.dropapp.databinding.ActivityPayMentMethodListBinding;
import shop.dropapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.ui.orderssection.activity.Ced_ViewOrder;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import static shop.dropapp.Keys.*;

@AndroidEntryPoint
public class PayMentMethodList extends Ced_NavigationActivity {
    ActivityPayMentMethodListBinding methodListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    private String paymentIntentClientSecret = "";
    private CheckoutViewModel checkoutViewModel;
    private HashMap<String, String> tittle_methodcode;
    private HashMap<String, String> tittle_additional;
    private HashMap<String, String> tittle_post;
    private String payemntmenthods = "";
    private String email = "";
    private String shippingcode = "";
    private String paymentcode = "";
    private boolean load = false;
    private String OrderId = "";
    private String stripeToken = "";
    private boolean custompayment = false;
    private String TAG = getClass().getSimpleName();
    private JsonObject shippingPaymentParam = null;

    //Stripe payment
    private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.paymentmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(PayMentMethodList.this, viewModelFactory).get(CheckoutViewModel.class);
        methodListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pay_ment_method_list, content, true);
        methodListBinding.braintreeradio.setButtonDrawable(checkbox_visibility);
        methodListBinding.payWithStripe.setButtonDrawable(checkbox_visibility);

        shippingcode = getIntent().getStringExtra(SHIPPING_CODE);
        payemntmenthods = getIntent().getStringExtra(PAYMENT_METHODS);
        // methodListBinding.cardInputWidget.setPostalCodeEnabled(false);

        tittle_methodcode = new HashMap<>();
        tittle_additional = new HashMap<>();
        try {
            if (!(payemntmenthods.equals(NOPAYMENT))) {
                createpaymentmethods(payemntmenthods);
            }
            methodListBinding.braintreeradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    //  methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "Braintree";
                    custompayment = true;
                }
            });
            methodListBinding.payWithStripe.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "stripe";
                    custompayment = true;
                }
            });

            methodListBinding.continueshipping.setOnClickListener(v -> {
                if (paymentcode.isEmpty()) {
                    showmsg(getResources().getString(R.string.selectpaymentfirst));
                } else {
                    if (custompayment) {
                        shippingPaymentParam = new JsonObject();
                        shippingPaymentParam.addProperty(CART_ID, cedSessionManagement.getCartId());
                        try {
                            if (session.isLoggedIn()) {
                                shippingPaymentParam.addProperty(ROLE, USER);
                                shippingPaymentParam.addProperty(CUSTOMER_ID, session.getCustomerid());
                            } else {
                                shippingPaymentParam.addProperty(ROLE, GUEST);
                            }
                            shippingPaymentParam.addProperty(PAYMENT_METHOD, APPPAYMENT);
                            shippingPaymentParam.addProperty(SHIPPING_METHOD, shippingcode);
                            postshippingpaymentinfo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        shippingPaymentParam = new JsonObject();
                        try {
                            shippingPaymentParam.addProperty(ROLE, USER);
                            shippingPaymentParam.addProperty(CART_ID, cedSessionManagement.getCartId());
                            shippingPaymentParam.addProperty(CUSTOMER_ID, session.getCustomerid());
                            shippingPaymentParam.addProperty(PAYMENT_METHOD, paymentcode);
                            shippingPaymentParam.addProperty(SHIPPING_METHOD, shippingcode);
                            if (methodListBinding.additionaldata.getChildCount() > 0) {
                                LinearLayout layout = (LinearLayout) methodListBinding.additionaldata.getChildAt(0);
                                if (layout.getChildCount() == 3) {
                                    TextView tag = (TextView) layout.getChildAt(1);
                                    EditText value = (EditText) layout.getChildAt(2);
                                    if (value.getText().toString().isEmpty()) {
                                        value.setError(getResources().getString(R.string.fillsomevaluefirst));
                                        value.requestFocus();
                                    } else {
                                        shippingPaymentParam.addProperty(tag.getText().toString(), value.getText().toString());
                                        postshippingpaymentinfo();
                                    }
                                } else {
                                    postshippingpaymentinfo();
                                }
                            } else {
                                postshippingpaymentinfo();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createpaymentmethods(String methods) throws JSONException {
        JSONObject obj = new JSONObject(methods);
        JSONObject object = obj.getJSONObject("methods");
        if (Objects.requireNonNull(object.names()).length() > 0) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            if (tittle_methodcode.size() > 0) {
                tittle_methodcode.clear();
                tittle_additional.clear();
            }

            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(String.valueOf(Objects.requireNonNull(object.names()).get(i)));
                final RadioButton rdbtn = new RadioButton(this);
                set_regular_font_forRadio(rdbtn);
                rdbtn.setButtonDrawable(checkbox_visibility);
                rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        if (isChecked) {
                            methodListBinding.continueshipping.setText(getResources().getString(R.string.continuee));
                            custompayment = false;
                            paymentcode = tittle_methodcode.get(rdbtn.getText().toString());

                            if (tittle_additional.containsKey(rdbtn.getText().toString())) {
                                if (methodListBinding.additionaldata.getChildCount() > 0) {
                                    methodListBinding.additionaldata.removeAllViews();
                                }
                                String data = tittle_additional.get(rdbtn.getText().toString());
                                if (Objects.requireNonNull(data).startsWith("{")) {
                                    JSONObject object2 = new JSONObject(data);
                                    JSONArray jsonArray = object2.names();
                                    for (int i1 = 0; i1 < Objects.requireNonNull(jsonArray).length(); i1++) {
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(jsonArray.getString(i1) + ":");
                                        heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                        TextView value = new TextView(PayMentMethodList.this);
                                        value.setText(object2.getString(jsonArray.getString(i1)));
                                        layout.addView(heading, 0);
                                        layout.addView(value, 1);
                                        methodListBinding.additionaldata.addView(layout);
                                    }
                                } else {
                                    if (data.startsWith("[")) {
                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(jsonObject.getString("label"));
                                        heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                        TextView value = new TextView(PayMentMethodList.this);
                                        value.setText(jsonObject.getString("name"));
                                        value.setVisibility(View.GONE);
                                        EditText text = new EditText(PayMentMethodList.this);
                                        layout.addView(heading, 0);
                                        layout.addView(value, 1);
                                        layout.addView(text, 2);
                                        methodListBinding.additionaldata.addView(layout);
                                    } else {
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(data);
                                        heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                        layout.addView(heading, 0);
                                        methodListBinding.additionaldata.addView(layout);
                                    }
                                }
                            } else {
                                methodListBinding.additionaldata.removeAllViews();
                            }
                        } else {
                            // continueshipping.setText("Continue");
                            // paymentcode = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (tittle_methodcode.containsKey(object1.getString("label"))) {
                    if (object1.has("additional_data")) {
                        tittle_additional.put(object1.getString("label") + "_new", object1.get("additional_data").toString());
                    }
                    tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                    rdbtn.setText(object1.getString("label") + "_new");
                } else {
                    if (object1.has("additional_data")) {
                        tittle_additional.put(object1.getString("label"), object1.get("additional_data").toString());
                    }
                    tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                    rdbtn.setText(object1.getString("label"));
                }
                methodListBinding.radiogroup.addView(rdbtn);
            }

            Log.i("tittle_additional", "" + tittle_additional);
        }
    }

    public void postshippingpaymentinfo() {
        try {
            checkoutViewModel.saveShippingPaymentMethods(PayMentMethodList.this, cedSessionManagement.getCurrentStore(), shippingPaymentParam).
                    observe(PayMentMethodList.this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                Processdata(apiResponse.data);
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

    private void Processdata(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getBoolean(SUCCESS)) {
                try {
                    Log.i(TAG, "Processdata: " + paymentcode + "\n" + custompayment);
                    if (paymentcode == APPPAYMENT) {
                        Intent intent = new Intent(PayMentMethodList.this, ProcessOrder.class);
                        intent.putExtra("paymentcode", paymentcode);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else {
                        payWthStripe();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showmsg(getResources().getString(R.string.somethingbadhappended));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payWthStripe() {
        final Dialog dialog = new Dialog(PayMentMethodList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.stripe_popup);
        CardMultilineWidget stripe_edittext = (CardMultilineWidget) dialog.findViewById(R.id.card_input_widget);
        TextView pay = (TextView) dialog.findViewById(R.id.pay);
        TextView termsLink = (TextView) dialog.findViewById(R.id.termsLink);
        TextView deliveryInfoLink = (TextView) dialog.findViewById(R.id.deliveryInfoLink);
        CheckBox termcondition = (CheckBox) dialog.findViewById(R.id.termcondition);
        CheckBox deliveryInfo = (CheckBox) dialog.findViewById(R.id.deliveryInfo);

        termsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayMentMethodList.this, Ced_CMSPage.class);
                intent.putExtra("cms", Constants.TERMS_CONDITIONS);
                startActivity(intent);
            }
        });
        deliveryInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayMentMethodList.this, Ced_CMSPage.class);
                intent.putExtra("cms", Constants.DELIVERY);
                startActivity(intent);
            }
        });


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termcondition.isChecked() && deliveryInfo.isChecked()) {
                    pay.setEnabled(false);
                    createStripeToken(stripe_edittext);
                } else {
                    if (!termcondition.isChecked())
                        Toast.makeText(PayMentMethodList.this, getString(R.string.pleaseAgreeOurTermsAndCondition), Toast.LENGTH_SHORT).show();
                    if (!deliveryInfo.isChecked())
                        Toast.makeText(PayMentMethodList.this, getString(R.string.pleaseAgreeOurDelivery), Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveOrder(JsonObject params) {
        try {
            checkoutViewModel.saveOrder(PayMentMethodList.this, cedSessionManagement.getCurrentStore(), params).observe(PayMentMethodList.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        Log.i(TAG, "saveOrder: " + apiResponse.data);
                        try {
                            JSONObject resObj = new JSONObject(apiResponse.data);
                            if (resObj.getBoolean(SUCCESS)) {
                                load = true;
                                OrderId = resObj.getString(ORDER_ID);
                                String grandTotal = resObj.getString(GRANDTOTAL);
                                String currencyCode = resObj.getString(CURRENCY_CODE);
                                JsonObject paramss = new JsonObject();
                                paramss.addProperty(ORDER_ID, OrderId);
                                paramss.addProperty(STRIPE_TOKEN, stripeToken);
                                generateServerToken(paramss);

                                //createStripeCallBack(orderId,grandTotal,currencyCode);
                            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createStripeToken(CardMultilineWidget stripe_edittext) {
        paymentcode = APPPAYMENT;
        if (stripe_edittext.getCard() != null) {
            Card cardToSave = stripe_edittext.getCard();
            if (cardToSave == null) {
                Toast.makeText(PayMentMethodList.this, "Invalid Card Data", Toast.LENGTH_LONG).show();
            } else {
                Stripe stripe = new Stripe(PayMentMethodList.this, STRIPE_TEST_KEY);
                stripe.createToken(cardToSave, new TokenCallback() {
                            public void onSuccess(Token token) {
                                Log.i(TAG, token.getId().toString());
                                stripeToken = token.getId().toString();
                                saveOrder(shippingPaymentParam);
                            }

                            public void onError(Exception error) {
                                Log.i(TAG, error.toString());
                                Toast.makeText(PayMentMethodList.this, "Make sure you have entered correct card details", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
        } else {
            Toast.makeText(PayMentMethodList.this, "Make sure you have entered correct card details", Toast.LENGTH_LONG).show();
        }
    }

    private void generateServerToken(JsonObject params) {
        checkoutViewModel.generateToken(this, cedSessionManagement.getCurrentStore(), params).observe(PayMentMethodList.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    Log.i(TAG, "generateToken: " + apiResponse.data);
                    try {
                        JSONObject resobj = new JSONObject(apiResponse.data.toString());
                        if (resobj.getString("status").equals("processing")) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("failure", FALSE);
                            jsonObject.addProperty(ORDER_ID, OrderId);
                            jsonObject.addProperty("additional_information", stripeToken);
                            setFinalordercheck(jsonObject);
                        } else {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(ORDER_ID, OrderId);
                            jsonObject.addProperty("additional_information", stripeToken);
                            jsonObject.addProperty("failure", TRUE);
                            setFinalordercheck(jsonObject);
                            Toast.makeText(PayMentMethodList.this, "Unable to Place Order", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    public void setFinalordercheck(JsonObject object) {
        try {
            checkoutViewModel.getAdditionalInfo(PayMentMethodList.this,
                    cedSessionManagement.getCurrentStore(), object).observe(PayMentMethodList.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            String success = jsonObject1.getString("success");
                            if (success.equals("true")) {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                changecount();
                                Intent intent = new Intent(PayMentMethodList.this, Ced_ViewOrder.class);
                                intent.putExtra("order_id", object.get(ORDER_ID).toString());
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
                        } catch (Exception e) {
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

    public void changecount() {
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (load) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(ORDER_ID, OrderId);
                jsonObject.addProperty("failure", "true");
                setFinalordercheck(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }
}

