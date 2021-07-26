/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package shop.dropapp.ui.loginsection.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.ui.loginsection.viewmodel.LoginViewModel;
import shop.dropapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import shop.dropapp.databinding.MagenativeNewRegistrationLayoutBinding;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.loginsection.viewmodel.RegisterViewModel;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.utils.Status;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.Keys.*;


@AndroidEntryPoint
public class Ced_Register extends Ced_NavigationActivity {
    @Inject
    ViewModelFactory viewModelFactory;
    private RegisterViewModel registerViewModel;
    private LoginViewModel loginViewModel;
    private MagenativeNewRegistrationLayoutBinding registerBinding;
    private JsonObject register_param = null;
    private EditText edt_firstname, edt_lastname, edt_email, edt_phonenumber, edt_password, edt_cnf_password;
    private ImageView mr, mis;
    private Button register_button;
    private CheckBox newsletter;
    private String status, cart_summary, customer_id, hash, outputstring, isConfirmationRequired, message, firstname,
            lastname, email, password, cnf_password, phonenumber, customergroup_id = "";
    private TextView account;
    private LinearLayout prefixsection, suffixsection, dobsection;
    private TextInputLayout middlenamesection, taxvatsection;
    private TextView prefixlabel, suffixlabel;
    private RadioGroup prefix;
    private RadioGroup suffix;
    private TextView prefixname, suffixname, prefixoptions, suffixoptions;
    private String prefixvalue = "";
    private String suffixvalue = "";
    private TextView dob;
    private EditText MageNative_midllename;
    private EditText MageNative_taxvat;

    private boolean male = false;
    private boolean female = false;
    private boolean prefixflag = false;
    private boolean suffixflag = false;
    private boolean dobflag = false;
    private boolean taxvatflag = false;
    private boolean middlenamevatflag = false;
    private boolean flag = true;
    private boolean isfromcheckout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_registration_layout, content, true);
        registerViewModel = new ViewModelProvider(Ced_Register.this, viewModelFactory).get(RegisterViewModel.class);
        loginViewModel = new ViewModelProvider(Ced_Register.this, viewModelFactory).get(LoginViewModel.class);
        Objects.requireNonNull(getSupportActionBar()).hide();
        navBinding.MageNativeTawkSupport.setVisibility(View.GONE);

        if (getIntent().getStringExtra(ISFROMCHECKOUT) != null) {
            isfromcheckout = true;
        }

        edt_firstname = registerBinding.edtFirstName;
        edt_lastname = registerBinding.edtLastName;
        edt_email = registerBinding.edtEmail;
        edt_phonenumber = registerBinding.edtPhonenumber;
        account = registerBinding.txtAccount;
        newsletter = registerBinding.chkNewsLetter;
        mr = registerBinding.male;
        mis = registerBinding.female;
        prefixsection = registerBinding.prefixsection;
        middlenamesection = registerBinding.middlenamesection;
        suffixsection = registerBinding.suffixsection;
        dobsection = registerBinding.dobsection;
        taxvatsection = registerBinding.taxvatsection;
        prefixlabel = registerBinding.prefixlabel;
        suffixlabel = registerBinding.suffixlabel;
      /*  middlenamelabel = registerBinding.middlenamelabel;
        taxvatlabel = registerBinding.taxvatlabel;*/
        prefix = registerBinding.prefix;
        suffix = registerBinding.suffix;
        prefixname = registerBinding.prefixname;
        suffixname = registerBinding.suffixname;
      /*  taxvatname = registerBinding.taxvatname;
        middlename = registerBinding.midllenamename;*/
        prefixoptions = registerBinding.prefixoptions;
        suffixoptions = registerBinding.suffixoptions;
        dob = registerBinding.dob;
        MageNative_midllename = registerBinding.MageNativeMidllename;
        MageNative_taxvat = registerBinding.MageNativeTaxvat;
        edt_password = registerBinding.edtPassword;
        edt_cnf_password = registerBinding.edtConfirmPass;
        register_button = registerBinding.btnRegister;

        mr.setOnClickListener(v -> {
            mr.setBackground(getResources().getDrawable(R.drawable.selected));
            male = true;
            female = false;
            mis.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDOBDatePicker(dob);
            }
        });
        mis.setOnClickListener(v -> {
            mis.setBackground(getResources().getDrawable(R.drawable.selected));
            male = false;
            female = true;
            mr.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });

        register_param = new JsonObject();
        register_button.setOnClickListener(v -> {
            try {
                firstname = edt_firstname.getText().toString();
                lastname = edt_lastname.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                cnf_password = edt_cnf_password.getText().toString();
                phonenumber = edt_phonenumber.getText().toString();

                if (firstname.isEmpty()) {
                    edt_firstname.requestFocus();
                    edt_firstname.setError(getResources().getString(R.string.empty));
                } else {
                    if (lastname.isEmpty()) {
                        edt_lastname.requestFocus();
                        edt_lastname.setError(getResources().getString(R.string.empty));
                    } else {
                        if (phonenumber.isEmpty()) {
                            edt_phonenumber.requestFocus();
                            edt_phonenumber.setError(getResources().getString(R.string.empty));
                        } else {
                            if (phonenumber.length() < 8 || phonenumber.length() > 11) {
                                edt_phonenumber.requestFocus();
                                edt_phonenumber.setError(getResources().getString(R.string.invalidMobileNumber));
                            } else {
                                if (email.isEmpty()) {
                                    edt_email.requestFocus();
                                    edt_email.setError(getResources().getString(R.string.empty));
                                } else {
                                    if (!isValidEmail(email)) {
                                        edt_email.requestFocus();
                                        edt_email.setError(getResources().getString(R.string.invalidemail));
                                    } else {
                                        if (password.isEmpty() || password.length() < 8) {
                                            edt_password.requestFocus();
                                            edt_password.setError(getResources().getString(R.string.minimum_8_character));
                                        } else {
                                            if (cnf_password.isEmpty() || !cnf_password.equals(password)) {
                                                edt_cnf_password.requestFocus();
                                                edt_cnf_password.setError(getResources().getString(R.string.confirmnotmatch));
                                            } else {
                                                if (prefixflag) {
                                                    if (prefixvalue.isEmpty()) {
                                                        flag = false;
                                                        showmsg(getResources().getString(R.string.selectsomeprefixvalue));
                                                    } else {
                                                        flag = true;
                                                        register_param.addProperty(prefixname.getText().toString(), prefixvalue);
                                                    }
                                                }
                                                if (middlenamevatflag) {
                                                    if (flag) {
                                                        if (MageNative_midllename.getText().toString().isEmpty()) {
                                                            flag = false;
                                                            MageNative_midllename.setError(getResources().getString(R.string.empty));
                                                            MageNative_midllename.requestFocus();
                                                        } else {
                                                            flag = true;
                                                            register_param.addProperty(MageNative_midllename.getTag().toString(), MageNative_midllename.getText().toString());
                                                        }
                                                    }
                                                }
                                                if (suffixflag) {
                                                    if (flag) {
                                                        if (suffixvalue.isEmpty()) {
                                                            flag = false;
                                                            showmsg(getResources().getString(R.string.selectsomesuffixvalue));
                                                        } else {
                                                            flag = true;
                                                            register_param.addProperty(suffixname.getText().toString(), suffixvalue);
                                                        }
                                                    }
                                                }
                                                if (dobflag) {
                                                    if (dob.getText().toString().isEmpty()) {
                                                        flag = false;
                                                        dob.setError(getResources().getString(R.string.selectdob));
                                                        dob.requestFocus();
                                                    } else {
                                                        dob.setError(null);
                                                        flag = true;
                                                        String[] parts = dob.getText().toString().split("/");
                                                        String year = String.valueOf(Integer.parseInt(parts[2]));
                                                        String month = String.valueOf(Integer.parseInt(parts[1]));
                                                        String day = String.valueOf(Integer.parseInt(parts[0]));
                                                        if (month.length() < 2) {
                                                            month = ZERO + month;
                                                        }
                                                        if (day.length() < 2) {
                                                            day = ZERO + day;
                                                        }
                                                        register_param.addProperty(dob.getTag().toString(), month + "/" + day + "/" + year);
                                                    }
                                                }
                                                if (taxvatflag) {
                                                    if (MageNative_taxvat.getText().toString().isEmpty()) {
                                                        flag = false;
                                                        MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                                        MageNative_taxvat.requestFocus();
                                                    } else {
                                                        MageNative_taxvat.setError(null);
                                                        flag = true;
                                                        register_param.addProperty(MageNative_taxvat.getTag().toString(), MageNative_taxvat.getText().toString());
                                                    }
                                                }
                                                register_param.addProperty(FIRTNAME, firstname);
                                                register_param.addProperty(LASTNAME, lastname);
                                                register_param.addProperty(EMAIL, email);
                                                register_param.addProperty(PASSWORD, password);
                                                if (cedSessionManagement.getStoreId() != null) {
                                                    register_param.addProperty(STORE_ID, cedSessionManagement.getStoreId());
                                                }
                                                if (male) {
                                                    register_param.addProperty(GENDER, ONE);
                                                    session.savegender(MALE);
                                                } else {
                                                    register_param.addProperty(GENDER, THREE);
                                                    session.savegender(FEMALE);
                                                }
                                                if (female) {
                                                    register_param.addProperty(GENDER, TWO);
                                                } else {
                                                    register_param.addProperty(GENDER, THREE);
                                                }

                                                if (newsletter.isChecked()) {
                                                    register_param.addProperty(IS_SUBSCRIBED, ONE);
                                                } else {
                                                    register_param.addProperty(IS_SUBSCRIBED, ZERO);
                                                }
                                                JsonObject extArrt = new JsonObject();
                                                extArrt.addProperty(MOBILE, phonenumber);
                                                register_param.add(EXTENSION_ATTRIBUTES, extArrt);

                                                if (flag) {
                                                    validateNumber(phonenumber);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        account.setOnClickListener(v -> {
            Intent Loginpage = new Intent(getApplicationContext(), Ced_Login.class);
            startActivity(Loginpage);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });
        get_the_fields();
    }

    private void validateNumber(String number) {
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        params.addProperty(TYPE, RGISTER);
        loginViewModel.validateNumber(Ced_Register.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject zerothObj = getZerothObj(apiResponse.data);
                        if (zerothObj.getBoolean(STATUS)) {
                            requestForOTP(apiResponse.data, number);
                        } else {
                            Toast.makeText(this, zerothObj.getString(MSG), Toast.LENGTH_SHORT).show();
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


    private void consumeRegisterResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                register(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void get_the_fields() {
        registerViewModel.getFieldsData(Ced_Register.this,cedSessionManagement.getCurrentStore()).observe(Ced_Register.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        String success = object.getString(SUCCESS);
                        if (success.equals(TRUE)) {
                            JSONArray data = object.getJSONArray(DATA);
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);
                                    if (jsonObject.has(PREFIX)) {
                                        if (jsonObject.getString(PREFIX).equals(TRUE)) {
                                            prefixflag = true;
                                            prefixsection.setVisibility(View.VISIBLE);
                                            prefixlabel.setText(jsonObject.getString(LABEL));
                                            prefixname.setText(jsonObject.getString(NAME));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            if (jsonObject.get(PREFIX_OPTIONS) instanceof JSONObject) {
                                                JSONObject prefix_options = jsonObject.getJSONObject(PREFIX_OPTIONS);
                                                prefixoptions.setText(prefix_options.toString());
                                                if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                    for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                        final RadioButton rdbtn = new RadioButton(Ced_Register.this);
                                                        rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                            if (isChecked) {
                                                                try {
                                                                    JSONObject object1 = new JSONObject(prefixoptions.getText().toString());
                                                                    prefixvalue = object1.getString(rdbtn.getText().toString());
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        rdbtn.setText((CharSequence) Objects.requireNonNull(prefix_options.names()).get(j));
                                                        ll.addView(rdbtn);
                                                    }
                                                }
                                            }
                                            prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has(DOB)) {
                                        if (jsonObject.getString(DOB).equals(TRUE)) {
                                            dobflag = true;
                                            dobsection.setVisibility(View.VISIBLE);
                                            dob.setHint(jsonObject.getString(LABEL) + "*");
                                            dob.setTag(jsonObject.getString(NAME));
                                        }
                                    }
                                    if (jsonObject.has(TAXVAT)) {
                                        if (jsonObject.getString(TAXVAT).equals(TRUE)) {
                                            taxvatflag = true;
                                            taxvatsection.setVisibility(View.VISIBLE);
                                            MageNative_taxvat.setHint(jsonObject.getString(LABEL) + "*");
                                            MageNative_taxvat.setTag(jsonObject.getString(NAME));
                                        }
                                    }
                                    if (jsonObject.has(MIDDLENAME)) {
                                        if (jsonObject.getString(MIDDLENAME).equals(TRUE)) {
                                            middlenamevatflag = true;
                                            middlenamesection.setVisibility(View.VISIBLE);
                                            MageNative_midllename.setHint(jsonObject.getString(LABEL) + "*");
                                            MageNative_midllename.setTag(jsonObject.getString(NAME));
                                        }
                                    }
                                    if (jsonObject.has(SUFFIX)) {
                                        if (jsonObject.getString(SUFFIX).equals(TRUE)) {
                                            suffixflag = true;
                                            suffixsection.setVisibility(View.VISIBLE);
                                            suffixlabel.setText(jsonObject.getString(LABEL));
                                            suffixname.setText(jsonObject.getString(NAME));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject(SUFFIX_OPTIONS);
                                            suffixoptions.setText(suffixx_options.toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_Register.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(suffixoptions.getText().toString());
                                                                suffixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(suffixx_options.names()).get(j));
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            suffix.addView(ll);
                                        }
                                    }
                                }
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

        /*//TODO
        Ced_ClientRequestResponseRest requestResponse = new Ced_ClientRequestResponseRest(output -> {

        }, Ced_Register.this);
        requestResponse.execute(requiredfieldurl);*/
    }

    private void register(String output) {
        try {
            JSONObject jsonObj = new JSONObject(output);
            if (jsonObj.has(HEADER) && jsonObj.getString(HEADER).equals(FALSE)) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                JSONArray response = jsonObj.getJSONObject(DATA).getJSONArray(CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    status = c.getString(STATUS);
                    message = c.getString(MSG);
                    if (status.equals(SUCCESS)) {
                        isConfirmationRequired = c.getString(ISCONFIRMATIONREQUIRED);
                        //showmsg(message);
                        if (isConfirmationRequired.equals(NO)) {
                            JsonObject hashMap = new JsonObject();
                            hashMap.addProperty(EMAIL, email);
                            hashMap.addProperty(PASSWORD, password);
                            JsonObject extArrt = new JsonObject();
                            extArrt.addProperty(LOGIN_TYPE, EMAIL);
                            hashMap.add(EXTENSION_ATTRIBUTES, extArrt);

                            if (cedSessionManagement.getCartId() != null) {
                                hashMap.addProperty(CART_ID, cedSessionManagement.getCartId());
                            }
                            try {
                                registerViewModel.getLoginData(Ced_Register.this,cedSessionManagement.getCurrentStore(), hashMap)
                                        .observe(Ced_Register.this, this::consumeLoginResponse);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        } else {
                            Intent popActivity = new Intent(getApplicationContext(), Ced_Login.class);
                            popActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            popActivity.putExtra(ISHAVINGDOWNLOADABLE, TRUE);
                            popActivity.putExtra(CHECKOUT, CHECKOUTMODULE);
                            startActivity(popActivity);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    } else {
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void consumeLoginResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                outputstring = apiResponse.data;
                checklogin();
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void checklogin() {
        try {
            JSONObject jsonObj = new JSONObject(outputstring);
            if (jsonObj.has(HEADER) && jsonObj.getString(HEADER).equals(FALSE)) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                JSONArray response = jsonObj.getJSONObject(DATA).getJSONArray(CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = null;
                    c = response.getJSONObject(i);
                    status = c.getString(STATUS);
                    if (status.equals(SUCCESS)) {
                        customer_id = c.getString(CUSTOMER_ID);
                        customergroup_id = c.getString(CUSTOMER_GROUP);
                        hash = c.getString(HASH);
                        session.savegender(c.getString(GENDER));
                        session.savename(c.getString(NAME));
                        cart_summary = String.valueOf(c.getInt(CART_SUMMARY));
                        Ced_MainActivity.latestcartcount = cart_summary;
                        showmsg(getResources().getString(R.string.succesfullLogin));
                        session.createLoginSession(hash, email);
                        session.saveCustomerId(customer_id);
                        session.set_customergroup_id(customergroup_id);
                        if (isfromcheckout) {
                            cedhandlecheckout();
                        } else {
                            Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeintent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                        setdevice_withusermail();
                    } else if (status.equals(EXCEPTION)) {
                        message = c.getString(MSG);
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        super.onBackPressed();
    }

    private JSONObject getZerothObj(String obj) {
        JSONObject zerothObj = null;
        try {
            JSONObject responseObj = new JSONObject(obj);
            JSONObject dataObj = responseObj.getJSONObject(DATA);
            JSONArray customerArray = dataObj.getJSONArray(CUSTOMER);
            zerothObj = customerArray.getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zerothObj;
    }

    private void requestForOTP(String response, String number) {
        try {
            JSONObject zerothObj = getZerothObj(response);
            if (zerothObj.getBoolean(STATUS)) {
                getOTP(number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOTP(String number) {
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        loginViewModel.requestOTP(Ced_Register.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject zeroth = getZerothObj(apiResponse.data);
                        if (zeroth.getBoolean(STATUS)) {
                            Toast.makeText(this, zeroth.getString(MSG), Toast.LENGTH_SHORT).show();
                            createOtpDialog(number);
                        } else {
                            Toast.makeText(this, zeroth.getString(MSG), Toast.LENGTH_SHORT).show();
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

    private void createOtpDialog(String number) {
        try {
            final Dialog dialog = new Dialog(Ced_Register.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.otpdialog);

            TextView otpMsg = (TextView) dialog.findViewById(R.id.otpMsg);
            EditText otpValue = (EditText) dialog.findViewById(R.id.otpValue);
            TextView verifyOtp = (TextView) dialog.findViewById(R.id.verifyOtp);
            TextView regenerateOtp = (TextView) dialog.findViewById(R.id.regenerateOtp);
            String txt = getResources().getString(R.string.pleaseEnterTheOtp) + " " + number;
            otpMsg.setText(txt);

            verifyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (otpValue.getText().toString().isEmpty()) {
                        otpValue.setError(getResources().getString(R.string.pleaseEnterTheOTP));
                    } else if (otpValue.getText().toString().length() != 4) {
                        otpValue.setError(getResources().getString(R.string.invalidOTP));
                    } else {
                        requestOTPVerification(otpValue, number);
                    }
                }
            });

            regenerateOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOTP(number);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestOTPVerification(EditText otpValue, String number) {
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        params.addProperty(OTP, otpValue.getText().toString());
        loginViewModel.verifyNumber(Ced_Register.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = getZerothObj(apiResponse.data);
                        if (object.getBoolean(STATUS)) {
                            // loginWithOtp(number);
                            registerViewModel.getRegisterData(Ced_Register.this,cedSessionManagement.getCurrentStore(), register_param)
                                    .observe(Ced_Register.this, Ced_Register.this::consumeRegisterResponse);
                            Toast.makeText(this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                        } else {
                            otpValue.setError(getString(R.string.invalidOTP));
                            Toast.makeText(this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    break;
            }
        });
    }

    private void loginWithOtp(String number) {
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(STORE_ID, cedSessionManagement.getStoreId());
        params.addProperty(LOGIN_TYPE, OTP);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        loginViewModel.getLoginData(Ced_Register.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            if (apiResponse.status == Status.SUCCESS) {
                outputstring = apiResponse.data;
                JSONObject object = getZerothObj(apiResponse.data);
                try {
                    if (object.getBoolean(STATUS)) {
                        loginWithOtp(number);
                        Toast.makeText(this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                        registerViewModel.getRegisterData(Ced_Register.this,cedSessionManagement.getCurrentStore(), params)
                                .observe(Ced_Register.this, Ced_Register.this::consumeRegisterResponse);
                    } else {
                        Toast.makeText(this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //checklogin();
            } else {
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
            }
        });
    }
}
