package shop.dropapp.ui.checkoutsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.R;
import shop.dropapp.databinding.ActivityShippingPaymentMethodBinding;
import shop.dropapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.Keys.*;

@AndroidEntryPoint
public class ShippingPaymentMethod extends Ced_NavigationActivity {
    private ActivityShippingPaymentMethodBinding methodBinding;
    @Inject
    public ViewModelFactory viewModelFactory;
    private CheckoutViewModel checkoutViewModel;
    private JSONObject paymentmethods = null;
    private JSONArray shipping_methods = null;
    private int mYear, mMonth, mDay;
    private String shippingcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.shippingmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(ShippingPaymentMethod.this, viewModelFactory).get(CheckoutViewModel.class);
        methodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_shipping_payment_method, content, true);
        methodBinding.dateValue.setText(getString(R.string.pleaseSelectDate));

        boolean ishavingdownloadableonly = getIntent().getBooleanExtra(ISHAVINGDOWNLOADABLEONLY, false);
        if (ishavingdownloadableonly) {
            Intent paymentMtdIntent = new Intent(ShippingPaymentMethod.this, PayMentMethodList.class);
            paymentMtdIntent.putExtra(SHIPPING_CODE, "");
            paymentMtdIntent.putExtra(PAYMENT_METHODS, NOPAYMENT);
            startActivity(paymentMtdIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        } else {
            try {
                JsonObject getShippingMethodParams = new JsonObject();
                JsonObject location = new JsonObject();
                getShippingMethodParams.addProperty(ROLE, USER);
                getShippingMethodParams.addProperty(CART_ID, cedSessionManagement.getCartId());

                location.addProperty(POSTCODE, cedSessionManagement.getpostcode());
                location.addProperty(LATITUDE, cedSessionManagement.getlatitude());
                location.addProperty(LONGITUDE, cedSessionManagement.getlongitude());
                location.addProperty(STATE, cedSessionManagement.getstate());
                location.addProperty(COUNTRY_ID, cedSessionManagement.getcountry());
                location.addProperty(CITY, cedSessionManagement.getcity());
                location.addProperty(LOCATION, cedSessionManagement.getshipping_address());
                getShippingMethodParams.add(LOCATION,location);
                if (session.isLoggedIn()) {
                    getShippingMethodParams.addProperty(CUSTOMER_ID, session.getCustomerid());
                }
                postshippinginfo(getShippingMethodParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createshipmenthods(String shippingmethods) {
        try {
            JSONObject shippingMtdObj = new JSONObject(shippingmethods);
            if (shippingMtdObj.getBoolean(SUCCESS)) {
                if (!shippingMtdObj.get(PAYMENTS).equals(getString(R.string.noPaymentMtdAvailable))) {
                    paymentmethods = shippingMtdObj.getJSONObject(PAYMENTS);
                }
                if (shippingMtdObj.get(SHIPPING).equals(getString(R.string.noQuotesAvailable))) {
                    showmsg(getString(R.string.noQuotesAvailable));
                } else {
                    JSONObject shipping = shippingMtdObj.getJSONObject(SHIPPING);
                    shipping_methods = shipping.getJSONArray(METHODS);
                    boolean deliveryAddOn = true;
                    if (deliveryAddOn) {
                        methodBinding.radiogroup.setVisibility(View.GONE);
                        methodBinding.deliveryDateAddOnSection.setVisibility(View.VISIBLE);
                        requestForShippingInfo();
                        ArrayList<String> methodNames = new ArrayList<String>();
                        methodNames.add(getString(R.string.selectMethod));
                        for (int i = 0; i < shipping_methods.length(); i++) {
                            JSONObject methodObj = shipping_methods.getJSONObject(i);
                            methodNames.add(methodObj.getString(LABEL));
                            ArrayAdapter<String> spinnerMenu = new ArrayAdapter<String>(ShippingPaymentMethod.this, android.R.layout.simple_list_item_1, methodNames);
                            methodBinding.shippingMethods.setAdapter(spinnerMenu);
                        }
                    } else {
                        if (shipping_methods.length() > 0) {
                            methodBinding.radiogroup.setVisibility(View.VISIBLE);
                            methodBinding.deliveryDateAddOnSection.setVisibility(View.GONE);
                            RadioGroup paymentMtdRadio = new RadioGroup(ShippingPaymentMethod.this);
                            paymentMtdRadio.setOrientation(LinearLayout.VERTICAL);
                            for (int i = 0; i < shipping_methods.length(); i++) {
                                JSONObject obj = shipping_methods.getJSONObject(i);
                                RadioButton rdbtn = new RadioButton(ShippingPaymentMethod.this);
                                set_regular_font_forRadio(rdbtn);
                                rdbtn.setText(obj.getString(LABEL));
                                rdbtn.setTag(obj.getString(VALUE));
                                rdbtn.setButtonDrawable(checkbox_visibility);
                                paymentMtdRadio.addView(rdbtn);
                            }
                            methodBinding.radiogroup.addView(paymentMtdRadio);
                            paymentMtdRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton rb = (RadioButton) findViewById(checkedId);
                                    shippingcode = rb.getTag().toString();
                                }
                            });
                        }

                        methodBinding.continueshipping.setOnClickListener(v -> {
                            if (shippingcode == null) {
                                showmsg(getResources().getString(R.string.selectshippinfmethodfirst));
                            } else {
                                Intent intent = new Intent(ShippingPaymentMethod.this, PayMentMethodList.class);
                                intent.putExtra(SHIPPING_CODE, shippingcode);
                                if (paymentmethods != null)
                                    intent.putExtra(PAYMENT_METHODS, paymentmethods.toString());
                                else
                                    intent.putExtra(PAYMENT_METHODS, NOPAYMENT);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void postshippinginfo(JsonObject data) {
        try {
            checkoutViewModel.getShippingPaymentMethods(ShippingPaymentMethod.this,cedSessionManagement.getCurrentStore(), data).observe(ShippingPaymentMethod.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        createshipmenthods(apiResponse.data);
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

    private void requestForShippingInfo() {
        checkoutViewModel.getDeliveryDateInfo(ShippingPaymentMethod.this,cedSessionManagement.getCurrentStore()).observe(ShippingPaymentMethod.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    addUpDeliveryInfo(apiResponse.data);
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    private void addUpDeliveryInfo(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            if (obj.getBoolean(SUCCESS)) {
                JSONObject dataObj = obj.getJSONObject(DATA);
                int maxDate = Integer.parseInt(dataObj.getString(MAX_DATE));
                methodBinding.dateValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerFragmentDialog datePickerFragmentDialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                                //Toast.makeText(ShippingPaymentMethod.this, "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                                methodBinding.dateValue.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                        datePickerFragmentDialog.show(getSupportFragmentManager(), null);
                        datePickerFragmentDialog.setMinDate(c.getTimeInMillis());
                        c.add(Calendar.DATE, maxDate);
                        datePickerFragmentDialog.setMaxDate(c.getTimeInMillis());
                        datePickerFragmentDialog.setCancelColor(getResources().getColor(R.color.secondory_color));
                        datePickerFragmentDialog.setOkColor(getResources().getColor(R.color.secondory_color));
                        datePickerFragmentDialog.setAccentColor(getResources().getColor(R.color.secondory_color));
                        datePickerFragmentDialog.setOkText(getResources().getString(R.string.ok));
                        datePickerFragmentDialog.setCancelText(getResources().getString(R.string.cancel));
                    }
                });

                methodBinding.resetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        methodBinding.dateValue.setText(getString(R.string.pleaseSelectDate));
                    }
                });
                if (!dataObj.getString(ENABLE_COMMENT).equals("1")) {
                    methodBinding.commentTitle.setVisibility(View.GONE);
                    methodBinding.commentValue.setVisibility(View.GONE);
                    methodBinding.commentValueNote.setVisibility(View.GONE);
                }

                ArrayList<String> timeStampArr = new ArrayList<String>();
                timeStampArr.add(getString(R.string.selectMethod));
                if (dataObj.has(TIME_STAMP) && dataObj.getJSONArray(TIME_STAMP).length() > 0) {
                    for (int i = 0; i < dataObj.getJSONArray(TIME_STAMP).length(); i++) {
                        timeStampArr.add(dataObj.getJSONArray(TIME_STAMP).getString(i));
                        ArrayAdapter<String> spinnerMenu = new ArrayAdapter<String>(ShippingPaymentMethod.this, android.R.layout.simple_list_item_1, timeStampArr);
                        methodBinding.deliveryTime.setAdapter(spinnerMenu);
                    }
                }
                methodBinding.continueshipping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JsonObject params = new JsonObject();
                        params.addProperty(STORE_ID, cedSessionManagement.getStoreId());
                        params.addProperty(CUSTOMER_ID, session.getCustomerid());
                        params.addProperty(CART_ID, cedSessionManagement.getCartId());
                        if (methodBinding.shippingMethods.getSelectedItemPosition() == 0) {
                            Toast.makeText(ShippingPaymentMethod.this, getString(R.string.selectshippinfmethodfirst), Toast.LENGTH_SHORT).show();
                        } else {
                            if (shipping_methods != null) {
                                if (shipping_methods.length() > 0) {
                                    try {
                                        shippingcode = shipping_methods.getJSONObject(methodBinding.shippingMethods.getSelectedItemPosition() - 1).getString(VALUE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (methodBinding.deliveryTime.getSelectedItemPosition() == 0) {
                                Toast.makeText(ShippingPaymentMethod.this, getString(R.string.selectDeliveryTimeFirst), Toast.LENGTH_SHORT).show();
                            } else {
                                params.addProperty(DELIVERY_TIME, methodBinding.deliveryTime.getSelectedItem().toString());
                                if (methodBinding.dateValue.getText().toString().equals(getString(R.string.pleaseSelectDate))) {
                                    Toast.makeText(ShippingPaymentMethod.this, getString(R.string.selectDeliveryDateFirst), Toast.LENGTH_SHORT).show();
                                } else {
                                    params.addProperty(DELIVERY_DATE, methodBinding.dateValue.getText().toString());
                                    if (methodBinding.commentValue.getVisibility() == View.VISIBLE) {
                                        if (methodBinding.commentValue.getText().toString().isEmpty()) {
                                            params.addProperty(COMMENT, "");
                                            saveDeliveryAddOnInfor(params);
                                        } else {
                                            params.addProperty(COMMENT, methodBinding.commentValue.getText().toString());
                                            saveDeliveryAddOnInfor(params);
                                        }
                                    } else {
                                        saveDeliveryAddOnInfor(params);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDeliveryAddOnInfor(JsonObject params) {
        checkoutViewModel.saveDeliveryDateInfo(ShippingPaymentMethod.this,cedSessionManagement.getCurrentStore(), params).observe(ShippingPaymentMethod.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject response = new JSONObject(apiResponse.data);
                        if (response.getBoolean(STATUS)) {
                            Toast.makeText(this, response.getString(MSG), Toast.LENGTH_SHORT).show();
                            if (shippingcode == null) {
                                showmsg(getResources().getString(R.string.selectshippinfmethodfirst));
                            } else {
                                Intent intent = new Intent(ShippingPaymentMethod.this, PayMentMethodList.class);
                                intent.putExtra(SHIPPING_CODE, shippingcode);
                                if (paymentmethods != null)
                                    intent.putExtra(PAYMENT_METHODS, paymentmethods.toString());
                                else
                                    intent.putExtra(PAYMENT_METHODS, NOPAYMENT);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        } else {
                            if (response.has(MSG)) {
                                Toast.makeText(this, response.getString(MSG), Toast.LENGTH_SHORT).show();
                            }
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
}
