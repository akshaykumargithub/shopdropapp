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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.R;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import shop.dropapp.base.activity.Ced_MainActivity;
import shop.dropapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import shop.dropapp.databinding.MagenativeNewLoginLayoutBinding;
import shop.dropapp.rest.ApiResponse;
import shop.dropapp.ui.loginsection.viewmodel.LoginViewModel;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import shop.dropapp.utils.Status;
import shop.dropapp.utils.Urls;
import shop.dropapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static shop.dropapp.Keys.*;

@AndroidEntryPoint
public class Ced_Login extends Ced_NavigationActivity implements View.OnClickListener {

    MagenativeNewLoginLayoutBinding loginBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel loginViewModel;
    private BottomSheetBehavior sheetBehavior;

    TextView Forgot_Password;
    EditText edt_username;
    EditText edt_password;
    EditText edt_userNumber;
    Button loginNumberBtn;
    JSONObject jsonObj;
    JSONArray response = null;
    Button Login_button;
    String social_url = "";
    String outputstring;
    String status, customer_id, hash, genders, email, firstname, lastname, id, customergroup_id;

    String cart_summary;
    String Url = "";
    String username, password;
    CallbackManager callbackManager;
    LoginButton loginButton;
    LinearLayout social_login_linear;
    LinearLayout social_login_google;
    private Button loginWithEmail;
    private Button loginWithPhone;
    private LinearLayout loginWithPhoneSection;
    private LinearLayout loginWithEmailSection;

    private SignInButton signInButton;
    private int RC_SIGN_IN = 100;
    TextView signupwithustext;
    LinearLayout orsection;
    ImageView MageNative_mage;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectaccounttab();
        loginBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_login_layout, content, true);
        loginViewModel = new ViewModelProvider(Ced_Login.this, viewModelFactory).get(LoginViewModel.class);
        navBinding.MageNativeTawkSupport.setVisibility(View.GONE);
        sheetBehavior = BottomSheetBehavior.from(loginBinding.forgotPassLayout.forgotPassSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("TAG", "STATE_HIDDEN");
                        break;

                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        Log.e("TAG", "STATE_HALF_EXPANDED");
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("TAG", "STATE_DRAGGING");
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("TAG", "STATE_EXPANDED");
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("TAG", "STATE_COLLAPSED");
                        if (loginBinding.view.getVisibility() == View.VISIBLE) {
                            loginBinding.view.setVisibility(View.GONE);
                        }
                        break;

                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("TAG", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                loginBinding.view.setVisibility(View.VISIBLE);
                loginBinding.view.setAlpha(slideOffset);
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        MageNative_mage = loginBinding.headerImage;
        loginButton = loginBinding.fbLoginBtn;
        social_login_linear = loginBinding.layoutFbLogin;
        social_login_google = loginBinding.layoutGoogleLogin;
        orsection = loginBinding.orSection;
        signupwithustext = loginBinding.txtSignUp;
        signInButton = loginBinding.googleSignInBtn;
        edt_username = loginBinding.edtUserName;
        edt_password = loginBinding.edtPassword;
        Forgot_Password = loginBinding.txtForgotPass;
        Login_button = loginBinding.btnLogin;
        loginWithEmail = loginBinding.emailLoginbtn;
        loginWithPhone = loginBinding.phoneLoginbtn;
        loginWithPhoneSection = loginBinding.numLayout;
        loginWithEmailSection = loginBinding.emailLogin;
        edt_userNumber = loginBinding.edtUserNumber;
        loginNumberBtn = loginBinding.btnPhonelogin;

        set_regular_font_fortext(signupwithustext);
        set_regular_font_fortext(edt_password);
        set_regular_font_fortext(edt_username);
        set_regular_font_fortext(Forgot_Password);
        set_regular_font_fortext(Login_button);

        hideBottomNav();
        loginViewModel.getFacebookKeyHash(Ced_Login.this);

        social_login_linear.setOnClickListener(v -> loginButton.performClick());

        social_login_google.setOnClickListener(v -> signIn());

        loginWithEmail.setBackgroundColor(getResources().getColor(R.color.secondory_color));
        loginWithEmail.setTextColor(getResources().getColor(R.color.white));
        loginWithEmailSection.setVisibility(View.VISIBLE);

        loginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmail.setBackgroundColor(getResources().getColor(R.color.secondory_color));
                loginWithPhone.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                loginWithEmail.setTextColor(getResources().getColor(R.color.white));
                loginWithPhone.setTextColor(getResources().getColor(R.color.black));
                loginWithEmailSection.setVisibility(View.VISIBLE);
                loginWithPhoneSection.setVisibility(View.GONE);
            }
        });

        loginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmail.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                loginWithPhone.setBackgroundColor(getResources().getColor(R.color.secondory_color));
                loginWithEmail.setTextColor(getResources().getColor(R.color.black));
                loginWithPhone.setTextColor(getResources().getColor(R.color.white));
                loginWithEmailSection.setVisibility(View.GONE);
                loginWithPhoneSection.setVisibility(View.VISIBLE);
            }
        });

        loginNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_userNumber.getText().toString().isEmpty()) {
                    edt_userNumber.setError(getString(R.string.enterNumber));
                } else if (edt_userNumber.getText().toString().length() < 10 || edt_userNumber.getText().toString().length() > 10) {
                    edt_userNumber.setError(getString(R.string.invalidMobileNumber));
                } else {
                    validateNumber(edt_userNumber.getText().toString());
                }
            }
        });


        MageNative_mage.setOnClickListener(v -> {
            Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
            startActivity(homeintent);
            finishAffinity();
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        loginButton.setReadPermissions("email");
        signInButton.setSize(SignInButton.SIZE_WIDE);
        /* signInButton.setScopes(gso.getScopeArray());*/


        signInButton.setOnClickListener(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                if (accessToken.getCurrentAccessToken().getToken() != null) {
                    checkFacebookLogin();
                }
            }

            @Override
            public void onCancel() {
                showmsg(getResources().getString(R.string.loginattemptcancled));
            }

            @Override
            public void onError(FacebookException exception) {
                showmsg("" + exception.toString());
            }
        });

        signupwithustext.setOnClickListener(v -> {
            Intent RegistrationIntent = new Intent(getApplicationContext(), Ced_Register.class);
            if (getIntent().getStringExtra("Checkout") != null) {
                if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                    RegistrationIntent.putExtra("isfromcheckout", "true");
                }
            }
            startActivity(RegistrationIntent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        Forgot_Password.setOnClickListener(v -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        loginBinding.view.setOnClickListener(view -> {
            if (loginBinding.view.getVisibility() == View.VISIBLE ||
                    sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        loginBinding.forgotPassLayout.MageNativeSend.setOnClickListener(view -> {
            String email = loginBinding.forgotPassLayout.MageNativeGetEmail.getText().toString();
            if (email.isEmpty()) {
                loginBinding.forgotPassLayout.MageNativeGetEmail.requestFocus();
                loginBinding.forgotPassLayout.MageNativeGetEmail.setError("Please fill email address");
            } else {
                if (!isValidEmail(email)) {
                    loginBinding.forgotPassLayout.MageNativeGetEmail.requestFocus();
                    loginBinding.forgotPassLayout.MageNativeGetEmail.setError("Please fill valid email address");
                } else {
                    JsonObject hashMap = new JsonObject();
                    try {
                        hashMap.addProperty("email", email);
                        if (cedSessionManagement.getStoreId() != null) {
                            hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loginViewModel.forgotPassword(Ced_Login.this, cedSessionManagement.getCurrentStore(), hashMap).observe(Ced_Login.this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                applydata(apiResponse.data);
                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                showmsg(getResources().getString(R.string.errorString));
                                break;
                        }
                    });
                }
            }
        });

        Login_button.setOnClickListener(v -> {
            username = edt_username.getText().toString();
            password = edt_password.getText().toString();

            if (username.isEmpty()) {
                edt_username.requestFocus();
                edt_username.setError(getString(R.string.pleaseEnterEmail));
            } else {
                /*if (!isValidEmail(use  rname)) {
                    edt_username.requestFocus();
                    edt_username.setError(getString(R.string.pleaseEnterValidEmail));
                } else {*/
                if (password.isEmpty()) {
                    edt_password.requestFocus();
                    edt_password.setError(getString(R.string.pleaseEnterPassword));
                } else {
                    JsonObject hashMap = new JsonObject();
                    try {
                        hashMap.addProperty(EMAIL, username);
                        hashMap.addProperty(PASSWORD, password);
                        JsonObject extObj = new JsonObject();
                        extObj.addProperty(LOGIN_TYPE, EMAIL);
                        extObj.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
                        hashMap.add(EXTENSION_ATTRIBUTES, extObj);
                        if (cedSessionManagement.getStoreId() != null) {
                            hashMap.addProperty(STORE_ID, cedSessionManagement.getStoreId());
                        }
                        if (cedSessionManagement.getCartId() != null) {
                            hashMap.addProperty(CART_ID, cedSessionManagement.getCartId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginViewModel.getLoginData(Ced_Login.this, cedSessionManagement.getCurrentStore(), hashMap).observe(Ced_Login.this, apiResponse -> {
                        if (apiResponse.status == Status.SUCCESS) {
                            outputstring = apiResponse.data;
                            checklogin();
                        } else {
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                        }
                    });
                }
            }
            /*    }*/

            /*if (!isValidateEmail(username)) {
                edt_username.setError(getResources().getString(R.string.invalidemail));
            } else {


                try {
                    //TODO
                    Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(output -> {

                    }, Ced_Login.this, "POST", hashMap.toString());
                    response.execute(Url);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            }*/
        });
    }

    private void validateNumber(String number) {
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        params.addProperty(TYPE, LOGIN);
        loginViewModel.validateNumber(Ced_Login.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
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


    private void requestForOTP(String response, String number) {
        try {
            JSONObject zerothObj = getZerothObj(response);
            if (zerothObj.getBoolean(STATUS)) {
                JsonObject params = new JsonObject();
                params.addProperty(MOBILE, number);
                params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
                loginViewModel.requestOTP(Ced_Login.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOtpDialog(String number) {
        final Dialog dialog = new Dialog(Ced_Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.otpdialog);

        TextView otpMsg = (TextView) dialog.findViewById(R.id.otpMsg);
        EditText otpValue = (EditText) dialog.findViewById(R.id.otpValue);
        TextView verifyOtp = (TextView) dialog.findViewById(R.id.verifyOtp);
        TextView regenerateOtp = (TextView) dialog.findViewById(R.id.regenerateOtp);
        otpMsg.setText(getString(R.string.pleaseEnterTheOtp) + " " + number);

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
                requestForRegenerateOTP();
            }
        });

        dialog.show();
    }

    private void requestForRegenerateOTP() {
        Log.i(TAG, "requestForRegenerateOTP: Called");
    }

    private void requestOTPVerification(EditText otpValue, String number) {
        Log.i(TAG, "requestOTPVerification: " + otpValue);
        JsonObject params = new JsonObject();
        params.addProperty(MOBILE, number);
        params.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        params.addProperty(OTP, otpValue.getText().toString());
        loginViewModel.verifyNumber(Ced_Login.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = getZerothObj(apiResponse.data);
                        if (object.getString(STATUS).equals("true")) {
                            loginWithOtp(number);
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
        params.addProperty(STORE_ID, cedSessionManagement.getStoreId());
        JsonObject extAttr = new JsonObject();
        extAttr.addProperty(LOGIN_TYPE, OTP);
        extAttr.addProperty(MOBILE, number);
        extAttr.addProperty(COUNTRY_ID, cedSessionManagement.getcountrycode());
        params.add(EXTENSION_ATTRIBUTES, extAttr);
        loginViewModel.getLoginData(Ced_Login.this, cedSessionManagement.getCurrentStore(), params).observe(this, apiResponse -> {
            if (apiResponse.status == Status.SUCCESS) {
                outputstring = apiResponse.data;
                checklogin();
            } else {
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
            }
        });
    }

    private void applydata(String data) {
        try {
            jsonObj = new JSONObject(data);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                response = jsonObj.getJSONObject(DATA).getJSONArray(CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = null;
                    c = response.getJSONObject(i);
                    String status, message = null;
                    status = c.getString(STATUS);
                    if (status.equals("success")) {
                        message = c.getString(MSG);
                        showmsg(message);
                        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            loginBinding.view.setVisibility(View.GONE);
                        }

                        cleardataandlogout();
                        Intent home = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(home);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else if (status.equals("error")) {
                        message = c.getString(MSG);
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signIn();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(Objects.requireNonNull(result));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) throws JSONException {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            JsonObject hashp = new JsonObject();
            try {
                hashp.addProperty("type", "google");
                hashp.addProperty("token", acct.getId());
                hashp.addProperty("email", Objects.requireNonNull(acct).getEmail());
                firstname = acct.getDisplayName();
                lastname = acct.getFamilyName();
                if (firstname.contains(lastname)) {
                    hashp.addProperty("firstname", firstname.replace(lastname, ""));
                } else {
                    hashp.addProperty("firstname", firstname);
                }
                hashp.addProperty("lastname", lastname);
                username = acct.getEmail();
                if (cedSessionManagement.getStoreId() != null) {
                    hashp.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                if (cedSessionManagement.getCartId() != null) {
                    hashp.addProperty("cart_id", cedSessionManagement.getCartId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginViewModel.getSocialLoginData(Ced_Login.this, cedSessionManagement.getCurrentStore(), hashp).observe(Ced_Login.this, this::consumeLoginResponse);
        } else {
            Log.i("socialloginresult", "result " + result.toString());
            showmsg(getResources().getString(R.string.loginfailed));
        }
    }

    private void checkFacebookLogin() {
        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            try {
                if (object.has("email")) {
                    email = object.getString("email");
                    username = email;
                    firstname = object.getString("first_name");
                    lastname = object.getString("last_name");
                    if (object.has("gender")) {
                        genders = object.getString("gender");
                    } else {
                        genders = "none";
                    }

                    JsonObject hashp = new JsonObject();

                    hashp.addProperty("type", "facebook");
                    if (object.has("id")) {
                        Log.e("REpo", "fb unique customer account id == " + object.getString("id"));
                        hashp.addProperty("token", object.getString("id"));
                    }

                    if (firstname.contains(lastname)) {
                        hashp.addProperty("firstname", firstname.replace(lastname, ""));
                    } else {
                        hashp.addProperty("firstname", firstname);
                    }
                    hashp.addProperty("lastname", lastname);
                    hashp.addProperty("email", email);
                    if (genders.equalsIgnoreCase("male")) {
                        hashp.addProperty("gender", "1");
                    } else if (genders.equalsIgnoreCase("female")) {
                        hashp.addProperty("gender", "2");
                    } else {
                        hashp.addProperty("gender", "3");
                    }
                    if (cedSessionManagement.getStoreId() != null) {
                        hashp.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                    if (cedSessionManagement.getCartId() != null) {
                        hashp.addProperty("cart_id", cedSessionManagement.getCartId());
                    }
                    loginViewModel.getSocialLoginData(Ced_Login.this, cedSessionManagement.getCurrentStore(), hashp).observe(Ced_Login.this, this::consumeLoginResponse);
                } else {
                    showmsg(getResources().getString(R.string.noemailfound));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,email,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
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

    private void getFbHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    /**
     * Post login submission
     */
    private void checklogin() {
        try {
            jsonObj = new JSONObject(outputstring);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                if (jsonObj == null) {
                    edt_username.setError("Incorrect");
                    edt_password.setError("Incorrect");
                    showmsg(getResources().getString(R.string.confirmnotmatch));
                } else {
                    JSONObject c = null;
                    response = jsonObj.getJSONObject(DATA).getJSONArray(CUSTOMER);
                    c = response.getJSONObject(0);
                    status = c.getString(STATUS);
                    if (status.equals("success")) {
                        if (c.has("isConfirmationRequired")) {
                            if (c.getString("isConfirmationRequired").equals("NO")) {
                                customer_id = c.getString(CUSTOMER_ID);
                                customergroup_id = c.getString(CUSTOMER_GROUP);
                                hash = c.getString(HASH);
                                session.saveCustomerId(customer_id);
                                session.set_customergroup_id(customergroup_id);
                                cart_summary = String.valueOf(c.getInt(CART_SUMMARY));
                                Ced_MainActivity.latestcartcount = cart_summary;
                                session.savegender(c.getString("gender"));
                                session.savename(c.getString("name"));

                                session.createLoginSession(hash, username);
                                session.saveCustomerId(customer_id);
                                session.set_customergroup_id(customergroup_id);
                                // showmsg(getResources().getString(R.string.succesfullLogin));
                                if (getIntent().getStringExtra("Checkout") != null) {
                                    if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                                        cedhandlecheckout();
                                    }
                                } else {
                                    Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                    startActivity(homeintent);
                                    finishAffinity();
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            } else {
                                showmsg(c.getString("message"));
                            }
                        } else {

                            hash = c.getString(HASH);
                            customer_id = c.getString(CUSTOMER_ID);
                            customergroup_id = c.getString(CUSTOMER_GROUP);
                            cart_summary = String.valueOf(c.getInt(CART_SUMMARY));
                            session.savegender(c.getString("gender"));
                            session.savename(c.getString("name"));
                            Ced_MainActivity.latestcartcount = cart_summary;
                            session.createLoginSession(hash, username);
                            session.saveCustomerId(customer_id);
                            session.set_customergroup_id(customergroup_id);
                            // showmsg(getResources().getString(R.string.succesfullLogin));
                            if (getIntent().getStringExtra("Checkout") != null) {
                                if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                                    cedhandlecheckout();
                                }
                            } else {
                                Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(homeintent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                        setdevice_withusermail();
                    } else if (status.equals("exception")) {
                        showmsg(c.getString(MSG));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }


    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            loginBinding.view.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
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
}