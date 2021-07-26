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
  package shop.dropapp.base.activity;

  import android.annotation.SuppressLint;
  import android.annotation.TargetApi;
  import android.app.ActivityManager;

  import android.app.DatePickerDialog;
  import android.content.Context;
  import android.content.Intent;
  import android.content.pm.PackageManager;
  import android.content.res.Resources;
  import android.net.Uri;
  import android.os.Build;
  import android.os.Bundle;

  import com.facebook.FacebookSdk;
  import com.facebook.login.LoginManager;
  import com.google.android.gms.auth.api.Auth;
  import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
  import com.google.android.gms.common.ConnectionResult;
  import com.google.android.gms.common.api.GoogleApiClient;
  import com.google.android.material.bottomnavigation.BottomNavigationItemView;
  import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
  import com.google.android.material.bottomnavigation.BottomNavigationView;
  import com.google.android.material.dialog.MaterialAlertDialogBuilder;

  import androidx.annotation.NonNull;
  import androidx.core.view.MenuItemCompat;
  import androidx.databinding.DataBindingUtil;
  import androidx.appcompat.app.AppCompatActivity;
  import androidx.lifecycle.ViewModelProvider;
  import androidx.recyclerview.widget.GridLayoutManager;
  import androidx.recyclerview.widget.RecyclerView;

  import android.os.CountDownTimer;
  import android.provider.Settings;
  import android.text.Html;
  import android.util.Base64;
  import android.util.Log;
  import android.view.Gravity;
  import android.view.LayoutInflater;
  import android.view.Menu;
  import android.view.MenuItem;
  import android.view.View;
  import android.view.ViewGroup;
  import android.view.Window;
  import android.view.WindowManager;
  import android.view.animation.AccelerateInterpolator;
  import android.view.animation.AlphaAnimation;
  import android.view.animation.Animation;
  import android.view.animation.DecelerateInterpolator;
  import android.view.animation.Transformation;
  import android.widget.Button;
  import android.widget.DatePicker;
  import android.widget.EditText;
  import android.widget.LinearLayout;
  import android.widget.RadioButton;
  import android.widget.TextView;
  import android.widget.Toast;

  import com.google.gson.JsonObject;

  import retrofit2.http.Url;
  import shop.dropapp.Ced_MageNative_FontSetting.Ced_FontSetting;
  import shop.dropapp.ui.loginsection.activity.Ced_Login;
  import shop.dropapp.ui.networkhandlea_activities.Ced_ConnectionDetector;
  import shop.dropapp.Ced_MageNative_Scanner.Ced_Scanner;
  import shop.dropapp.ui.profilesection.activity.Ced_AccountDashboard;
  import shop.dropapp.ui.searchsection.activity.Ced_Searchview;
  import shop.dropapp.ui.wishlistsection.activity.Ced_WishListing;
  import shop.dropapp.utils.Ced_Load_Language;
  import shop.dropapp.ui.networkhandlea_activities.Ced_NoInternetconnection;
  import shop.dropapp.ui.cartsection.activity.Ced_CartListing;
  import shop.dropapp.base.fragment.Ced_FragmentDrawer;
  import shop.dropapp.base.viewmodel.NavDrawerViewModel;
  import shop.dropapp.databinding.MagenativeNavMainBinding;
  import shop.dropapp.rest.ApiResponse;
  import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
  import shop.dropapp.ui.notificationactivity.activity.Ced_NotificationList;
  import shop.dropapp.ui.websection.Ced_Weblink;
  import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
  import shop.dropapp.ui.checkoutsection.activity.BillingShippingAddress;

  import shop.dropapp.R;
  import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;

  import com.facebook.network.connectionclass.ConnectionClassManager;
  import com.facebook.network.connectionclass.ConnectionQuality;

  import shop.dropapp.base.fragment.BaseFragment;
  import shop.dropapp.utils.Urls;
  import shop.dropapp.utils.ViewModelFactory;

  import org.json.JSONArray;
  import org.json.JSONException;
  import org.json.JSONObject;

  import java.io.UnsupportedEncodingException;
  import java.net.URLEncoder;
  import java.text.SimpleDateFormat;
  import java.util.ArrayList;
  import java.util.Calendar;
  import java.util.List;
  import java.util.Locale;
  import java.util.Objects;
  import java.util.concurrent.TimeUnit;
  import java.util.regex.Matcher;
  import java.util.regex.Pattern;

  import javax.inject.Inject;

  public class Ced_NavigationActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {
      @Inject
      ViewModelFactory viewModelFactory;
      NavDrawerViewModel drawerViewModel;
      public static String guest_city = "";
      public static String guest_country = "";
      public static String guest_state = "";
      public static String guest_pincode = "";
      public MagenativeNavMainBinding navBinding;
      public Ced_ConnectionDetector cedConnectionDetector;
      TextView bottomcartcount;
      private Ced_FragmentDrawer drawerFragment;
      public Ced_SessionManagement_login session;
      public Ced_SessionManagement cedSessionManagement;
      public Ced_FontSetting cedFontSetting;
      View notifCount;
      private ConnectionClassManager mConnectionClassManager;
      private ConnectionChangedListener mListener;
      private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
      boolean skipshipping = false;
      static public TextView pincode;
      ArrayList<String> storename;
      ArrayList<String> storeid;
      Ced_Load_Language cedLoad_language;
      int Selected = -1;
      public ViewGroup content;
      public GoogleApiClient mGoogleApiClient;
      public int checkbox_visibility;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          navBinding = DataBindingUtil.setContentView(this, R.layout.magenative_nav_main);
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
          setup_bottomnavigation();
          content = navBinding.MageNativeFrameContainer;
          cedLoad_language = new Ced_Load_Language();
          cedConnectionDetector = new Ced_ConnectionDetector(getApplicationContext());
          cedSessionManagement = Ced_SessionManagement.getCed_sessionManagement(getApplicationContext());
          session = Ced_SessionManagement_login.getShredPrefs(getApplicationContext());
          cedFontSetting = new Ced_FontSetting();
          drawerViewModel = new ViewModelProvider(Ced_NavigationActivity.this, viewModelFactory).get(NavDrawerViewModel.class);
          GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                  .requestEmail()
                  .build();
          if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
              mGoogleApiClient.stopAutoManage(Ced_NavigationActivity.this);
              mGoogleApiClient.disconnect();
          }
          mGoogleApiClient = new GoogleApiClient.Builder(this)
                  .enableAutoManage(this, Ced_NavigationActivity.this)
                  .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                  .build();
          checkbox_visibility = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
          if (session.get_iffreshinstall_ondevice()) {
              cleardataandlogout();
              setdevice_withusermail();
          }
          Window window = getWindow();
          window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
          window.getAttributes().flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
          mConnectionClassManager = ConnectionClassManager.getInstance();
          mListener = new ConnectionChangedListener();
          setSupportActionBar(navBinding.MageNativeToolbar);
          Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
          getSupportActionBar().setDisplayShowHomeEnabled(true);
          checkPage();
          navBinding.toolimage.setOnClickListener(v -> {
              try {
                  ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                  List<ActivityManager.RunningTaskInfo> alltasks = Objects.requireNonNull(am).getRunningTasks(1);
                  for (ActivityManager.RunningTaskInfo aTask : alltasks) {
                      if (!(Objects.requireNonNull(aTask.topActivity).getClassName().equals("shop.dropapp.ui.homesection.activity.Ced_New_home_page"))) {
                          Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                          intent.putExtra("exceptfromhome", "true");
                          startActivity(intent);
                          finishAffinity();
                          overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                      }
                  }
              } catch (Exception t) {
                  t.printStackTrace();
              }
          });

          if (mConnectionClassManager.getCurrentBandwidthQuality().toString().equals("POOR")) {
              showmsg(getResources().getString(R.string.lownetwork));
          }

          storename = new ArrayList<>();
          storeid = new ArrayList<>();

          drawerFragment = (Ced_FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.MageNative_fragment_navigation_drawer);
          Objects.requireNonNull(drawerFragment).setUp(R.id.MageNative_fragment_navigation_drawer, navBinding.MageNativeDrawerLayout, navBinding.MageNativeToolbar);
//        drawerFragment.setDrawerListener(this);

          navBinding.MageNativeTawkSupport.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cedcommerce.com/magenativeapp.html?name=MageNativeM2App")));
              }
          });
      }

      private void checkPage() {
          try {
              ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
              List<ActivityManager.RunningTaskInfo> alltasks = Objects.requireNonNull(am).getRunningTasks(1);
              for (ActivityManager.RunningTaskInfo aTask : alltasks) {
                  if (!(Objects.requireNonNull(aTask.topActivity).getClassName().equals("shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme"))) {
                      navBinding.searchIcon.setVisibility(View.GONE);
                  }
              }
          } catch (Exception t) {
              t.printStackTrace();
          }
      }

      private void setup_bottomnavigation() {
          BottomNavigationMenuView bottomNavigationMenuView =
                  (BottomNavigationMenuView) navBinding.bottomNavigation.getChildAt(0);
          View v = bottomNavigationMenuView.getChildAt(1);
          BottomNavigationItemView itemView = (BottomNavigationItemView) v;
          View badge = LayoutInflater.from(this)
                  .inflate(R.layout.cartcount_badge, itemView, true);
          bottomcartcount = badge.findViewById(R.id.cartbadge);
          if (Ced_MainActivity.latestcartcount.equals("no_count")) {
              bottomcartcount.setText("0");
          } else {
              bottomcartcount.setText(Ced_MainActivity.latestcartcount);
          }

          navBinding.searchIcon.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String classname = this.getClass().getSimpleName();
                  if (!((classname.equalsIgnoreCase("Ced_Search") || classname.equalsIgnoreCase("Ced_Searchview")))) {
                      Intent search = new Intent(getApplicationContext(), Ced_Searchview.class);
                      search.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                      startActivity(search);
                      overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                  } else {
                      Toast.makeText(Ced_NavigationActivity.this, getString(R.string.alreadyOnSearchPage), Toast.LENGTH_SHORT).show();
                  }
              }
          });

          navBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @SuppressLint("NonConstantResourceId")
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  switch (item.getItemId()) {
                      case R.id.action_home:
                          Intent home = new Intent(Ced_NavigationActivity.this, Magenative_HomePageNewTheme.class);
                          home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                          home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(home);
                          overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          break;
                  /*  case R.id.action_category:
                            Intent weblink = new Intent(Ced_NavigationActivity.this, Ced_Categories.class);
                        weblink.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(weblink);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            break;*/
                      case R.id.my_cart:
                          Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                          startActivity(intent);
                          overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          break;
                      case R.id.my_wishlist:
                          if (session.isLoggedIn()) {
                              Intent search1 = new Intent(getApplicationContext(), Ced_WishListing.class);
                              search1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              startActivity(search1);
                              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          } else {
                              // showmsg(getResources().getString(R.string.loginfirst));
                              Intent account = new Intent(Ced_NavigationActivity.this, Ced_Login.class);
                              account.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              startActivity(account);
                              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          }
                          break;

                      case R.id.my_account:
                          if (session.isLoggedIn()) {
                              Intent account = new Intent(Ced_NavigationActivity.this, Ced_AccountDashboard.class);
                              account.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              startActivity(account);
                              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          } else {
                              Intent account = new Intent(Ced_NavigationActivity.this, Ced_Login.class);
                              account.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              startActivity(account);
                              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          }
                          break;
                  }
                  return true;
              }
          });
      }

      public void selecttabposition_bottomnav(int bottomtabposition) {
          if (bottomtabposition >= 0 && bottomtabposition < 4)
              navBinding.bottomNavigation.getMenu().getItem(bottomtabposition).setChecked(true);
      }

      public void selecthometab() {
          Ced_MainActivity.bottomtabposition = 0;
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
      }

      public void selectcategorytab() {
          Ced_MainActivity.bottomtabposition = 1;
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
      }

      public void selectcarttab() {
          Ced_MainActivity.bottomtabposition = 2;
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
      }

      public void selectwishtab() {
          Ced_MainActivity.bottomtabposition = 2;
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
      }

      public void selectaccounttab() {
          Ced_MainActivity.bottomtabposition = 3;
          selecttabposition_bottomnav(Ced_MainActivity.bottomtabposition);
      }

      public void getStoresRequest() {
          drawerViewModel.getStoresData(Ced_NavigationActivity.this, Urls.GET_STORES).observe(Ced_NavigationActivity.this, apiResponse -> {
              switch (apiResponse.status) {
                  case SUCCESS:
                      getStoreList(apiResponse.data);
                      break;

                  case ERROR:
                      Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                      showmsg(getResources().getString(R.string.errorString));
                      break;
              }
          });
      }

      private void getStoreList(String response) {
          try {
              JSONObject jsonObj = new JSONObject(response);
              JSONArray jsonArray = jsonObj.getJSONArray("store_data");
              for (int i = 0; i < jsonArray.length(); i++) {
                  JSONObject c = jsonArray.getJSONObject(i);
                  storename.add(c.getString("name"));
                  storeid.add(c.getString("store_id"));
              }
              final CharSequence[] storenames = storename.toArray(new CharSequence[storename.size()]);
              final CharSequence[] storeids = storeid.toArray(new CharSequence[storeid.size()]);
              if (cedSessionManagement.getStoreId() != null) {
                  Selected = storeid.indexOf(cedSessionManagement.getStoreId());
              }
              new MaterialAlertDialogBuilder(Ced_NavigationActivity.this, R.style.SingleChoiceRadioStyle)
                      .setTitle(Html.fromHtml(getResources().getString(R.string.selectwebstore)))
                      .setSingleChoiceItems(storenames, Selected, (dialog, postion) -> {
                          dialog.dismiss();
                          cedSessionManagement.saveStoreId((String) storeids[postion]);
                          // String url = Urls.SET_STORE + storeids[postion];
                          drawerViewModel.setStore(Ced_NavigationActivity.this, (storeids[postion]).toString()).
                                  observe(Ced_NavigationActivity.this, apiResponse -> {
                                      switch (apiResponse.status) {
                                          case SUCCESS:
                                              try {
                                                  JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                                  if (jsonObject.getString("success").equals("true")) {
                                                      String locale = jsonObject.getString("locale_code");
                                                      String[] localecodearray = locale.split("_");
                                                      String localecode = localecodearray[0];
                                                      cedSessionManagement.saveStorelocale(localecode);
                                                      cedLoad_language.setLanguagetoLoad(localecode, Ced_NavigationActivity.this);
                                                      if (localecode.equals("ar"))
                                                          cedSessionManagement.saveCurrentStore("Arabic");
                                                      else if (localecode.equals("fr"))
                                                          cedSessionManagement.saveCurrentStore("fr");
                                                      else
                                                          cedSessionManagement.saveCurrentStore("english_new");

                                                      Intent intent = new Intent(Ced_NavigationActivity.this, Ced_MainActivity.class);
                                                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                      startActivity(intent);
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
                      })
                      .show();

          } catch (JSONException e) {
              e.printStackTrace();
              Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
              main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(main);
              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
          }
      }

    /*public void show()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                andro_aaka.setVisibility(GONE);
            }
        }, 5000);
    }*/

      @Override
      protected void onResume() {
          if (cedConnectionDetector.isConnectingToInternet()) {
              invalidateOptionsMenu();
          } else {
              if (!this.getClass().getSimpleName().equalsIgnoreCase("Magenative_HomePageNewTheme")) {
                  Intent intent = new Intent(getApplicationContext(), Ced_NoInternetconnection.class);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                  startActivity(intent);
                  overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
              }
          }

          mConnectionClassManager.register(mListener);
          super.onResume();
      }


      @Override
      protected void onPause() {
          super.onPause();
      }

      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          if (session.isLoggedIn()) {
              getMenuInflater().inflate(R.menu.magenative_menu_login, menu);
              MenuItem item = menu.findItem(R.id.MageNative_action_cart);

              MenuItemCompat.setActionView(item, R.layout.magenative_feed_update_count);
              notifCount = MenuItemCompat.getActionView(item);
          } else {
              getMenuInflater().inflate(R.menu.magenative_menu_main, menu);
              MenuItem item = menu.findItem(R.id.MageNative_action_cart);
              MenuItemCompat.setActionView(item, R.layout.magenative_feed_update_count);
              notifCount = MenuItemCompat.getActionView(item);
          }

          if (Ced_MainActivity.latestcartcount.equals("no_count")) {
              TextView textView = notifCount.findViewById(R.id.MageNative_hotlist_hot);
              textView.setText("0");
              bottomcartcount.setText("0");
          } else {
              TextView textView = notifCount.findViewById(R.id.MageNative_hotlist_hot);
              textView.setText(Ced_MainActivity.latestcartcount);
              bottomcartcount.setText(Ced_MainActivity.latestcartcount);
          }

          notifCount.setOnClickListener(v -> {
              Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
              startActivity(intent);
              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
          });


          MenuItem item = menu.findItem(R.id.MageNative_action_search);
          String classname = this.getClass().getSimpleName();
          if (((classname.equalsIgnoreCase("Ced_Search")
                  || classname.equalsIgnoreCase("Magenative_HomePageNewTheme")
                  || classname.equalsIgnoreCase("Ced_Searchview")
                  || classname.equalsIgnoreCase("Ced_Categories")) && item != null)) {
              item.setVisible(false);
              navBinding.tooltext.setPadding(0, 0, 100, 0);
          }

          MenuItem item2 = menu.findItem(R.id.MageNative_action_notification);
          String classname2 = this.getClass().getSimpleName();
          if (((classname2.equalsIgnoreCase("Ced_Categories")) && item2 != null)) {
              item2.setVisible(false);
              navBinding.tooltext.setPadding(0, 0, 100, 0);
          }
          return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item.getItemId()) {
              case android.R.id.home:
                  this.finish();
                  return true;

              case R.id.MageNative_action_search:
                  Intent search = new Intent(getApplicationContext(), Ced_Searchview.class);
                  search.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  startActivity(search);
                  overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                  return true;

              case R.id.MageNative_action_cart:
                  Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  startActivity(intent);
                  overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                  return true;

           /* case R.id.MageNative_action_compare:
                Intent compare = new Intent(getApplicationContext(), Ced_CompareList.class);
                compare.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(compare);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                return true;*/


              case R.id.MageNative_action_notification:
                  Intent notification = new Intent(getApplicationContext(), Ced_NotificationList.class);
                  notification.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  startActivity(notification);
                  overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                  return true;


            /*case R.id.MageNative_store_view:
                drawerViewModel.getStoresData(Ced_NavigationActivity.this, Urls.GET_STORES).observe(Ced_NavigationActivity.this, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            getStoreList(apiResponse.data);
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });
                return true;*/
              default:
                  return super.onOptionsItemSelected(item);
          }
      }

    /*@Override
    public void onDrawerItemSelected(View view, int position) {

    }*/

      @Override
      public void onFragmentInteraction(View view) {

      }

      @Override
      public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

      }

      private class ConnectionChangedListener implements ConnectionClassManager.ConnectionClassStateChangeListener {
          @Override
          public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
              mConnectionClass = bandwidthState;
              runOnUiThread(() -> {
                  if (mConnectionClass.toString().equals("POOR")) {
                      showmsg(getResources().getString(R.string.lownetwork));
                  }
              });
          }
      }

      public void openDrawer() {
          drawerFragment.open();
      }

      public void cedhandlecheckout() {
          try {
              String url = null;
              if (session.isLoggedIn()) {
                  url = Urls.WEBCHECKOUTURL + session.getCustomerid();
                  if (cedSessionManagement.getStoreId() != null) {
                      url = url + "/store_id/" + cedSessionManagement.getStoreId();
                  }
              } else {
                  if (cedSessionManagement.getStoreId() != null) {
                      url = Urls.WEBCHECKOUT_GUESTURL + "cart_id/" + cedSessionManagement.getCartId() + "/store_id/" + cedSessionManagement.getStoreId();
                  } else {
                      url = Urls.WEBCHECKOUT_GUESTURL + "cart_id/" + cedSessionManagement.getCartId();
                  }
              }


              if (cedSessionManagement.iswebcheckoutenabled()) {

                  String location = "/city/" + cedSessionManagement.getcity() + "/state/" + cedSessionManagement.getstate() +
                          "/country/" + cedSessionManagement.getcountrycode() + "/zipcode/" + cedSessionManagement.getpostcode() + "/latitude/" + cedSessionManagement.getlatitude()
                          + "/longitude/" + cedSessionManagement.getlongitude() + "/location/" + cedSessionManagement.getAddress();

                  String checkurl = "";
                  checkurl = url + location + "/check/true";
                  Log.i("120120", "cedhandlecheckout: " + checkurl);

                  drawerViewModel.getWebCheckoutData(this, checkurl).observe(this, this::consumeResponse);
              } else {
                  try {
                      JsonObject cartlist = new JsonObject();
                      Ced_SessionManagement_login session = Ced_SessionManagement_login.getShredPrefs(Ced_NavigationActivity.this);
                      if (session.isLoggedIn()) {
                          cartlist.addProperty("customer_id", session.getCustomerid());
                      }
                      if (cedSessionManagement.getStoreId() != null) {
                          cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                      }
                      if (cedSessionManagement.getCartId() != null) {
                          cartlist.addProperty("cart_id", cedSessionManagement.getCartId());
                      }
                      drawerViewModel.getCartData(this, cedSessionManagement.getCurrentStore(), cartlist).observe(this, this::consumeCartResponse);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

      private void consumeResponse(ApiResponse apiResponse) {
          switch (apiResponse.status) {
              case SUCCESS:
                  try {
                      String response = apiResponse.data;
                      JSONObject object = new JSONObject(Objects.requireNonNull(response));
                      if (object.getString("success").equals("false")) {
                          showmsg(object.getString("message"));
                      } else {
                          Intent intent = new Intent(getApplicationContext(), Ced_Weblink.class);
//                            intent.putExtra("link", finalNewurl);
                          intent.putExtra("link", object.getString("message"));
                          intent.putExtra("fromcheckout", "true");
                          startActivity(intent);
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
      }

      private void consumeCartResponse(ApiResponse apiResponse) {
          switch (apiResponse.status) {
              case SUCCESS:
                  try {
                      String Jstring = apiResponse.data;
                      JSONObject object = new JSONObject(Objects.requireNonNull(Jstring));
                      if (object.has("success")) {
                          String status = object.getString("success");
                          if (status.equals("false")) {
                              showmsg(object.getString("message"));
                              Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                              intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                              startActivity(intent);
                              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                          } else {
                              goToAddress(Jstring);
                          }
                      } else {
                          goToAddress(Jstring);
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
      }


      private void goToAddress(String jstring) {
          try {
              JSONObject jsonObj = new JSONObject(jstring);
              JSONArray products = jsonObj.getJSONObject(Ced_CartListing.KEY_ITEM).getJSONArray(Ced_CartListing.KEY_SUB_ITEM);
              int download = 0;
              int virtual = 0;
              for (int i = 0; i < products.length(); i++) {
                  JSONObject c = null;
                  c = products.getJSONObject(i);
                  if (c.getString("product_type").equals("downloadable")) {
                      download = download + 1;
                  }
                  if (c.getString("product_type").equals("virtual")) {
                      virtual = virtual + 1;
                  }
              }
              if (download == products.length()) {
                  skipshipping = true;
              }
              if (virtual == products.length()) {
                  skipshipping = true;
              }
              Log.i("mage-native", "" + download);
              Log.i("mage-native", "" + skipshipping);
              Intent intent = new Intent(getApplicationContext(), BillingShippingAddress.class);
              intent.putExtra("ishavingdownloadableonly", skipshipping);
              startActivity(intent);
              overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

      public static void expand(final View v) {
          v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          final int targetHeight = v.getMeasuredHeight();
          //Older versions of android (pre API 21) cancel animations for views with a height of 0.
          v.getLayoutParams().height = 1;
          v.setVisibility(View.VISIBLE);
          Animation a = new Animation() {
              @Override
              protected void applyTransformation(float interpolatedTime, Transformation t) {
                  v.getLayoutParams().height = interpolatedTime == 1
                          ? LinearLayout.LayoutParams.WRAP_CONTENT
                          : (int) (targetHeight * interpolatedTime);
                  v.requestLayout();
              }

              @Override
              public boolean willChangeBounds() {
                  return true;
              }
          };

          // 1dp/ms
          a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
          v.startAnimation(a);
      }

      public static void collapse(final View v) {
          final int initialHeight = v.getMeasuredHeight();

          Animation a = new Animation() {
              @Override
              protected void applyTransformation(float interpolatedTime, Transformation t) {
                  if (interpolatedTime == 1) {
                      v.setVisibility(View.GONE);
                  } else {
                      v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                      v.requestLayout();
                  }
              }

              @Override
              public boolean willChangeBounds() {
                  return true;
              }
          };

          // 1dp/ms
          a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
          v.startAnimation(a);
      }

      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          switch (requestCode) {
              case 1:
                  Log.d(Urls.TAG, "onRequestPermissionsResult: " + grantResults);
                  if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      Intent searchscanner = new Intent(getApplicationContext(), Ced_Scanner.class);
                      searchscanner.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                      startActivity(searchscanner);
                      overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                      Ced_FragmentDrawer.mDrawerLayout.closeDrawers();
                  } else {
                      showmsg(getResources().getString(R.string.permissiondenied));
                  }
                  break;

              case 0: {
                  // If request is cancelled, the result arrays are empty.
                  if (/*grantResults.length > 0 && */grantResults[0] == PackageManager.PERMISSION_GRANTED /*&& grantResults[1] == PackageManager.PERMISSION_GRANTED*/) {
                      Intent searchscanner = new Intent(getApplicationContext(), Ced_Scanner.class);
                      searchscanner.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                      startActivity(searchscanner);
                      overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                      Ced_FragmentDrawer.mDrawerLayout.closeDrawers();
                  } else {
                      Log.e("REpo", "in onRequestPermissionsResult 3 " + requestCode);
                  }
                  return;
              }
          }
      }

      @TargetApi(Build.VERSION_CODES.GINGERBREAD)
      @SuppressLint("NewApi")
      public class CounterClass extends CountDownTimer {
          TextView counter;

          public CounterClass(long millisInFuture, long countDownInterval, TextView dealcounter) {
              super(millisInFuture, countDownInterval);
              counter = dealcounter;
          }

          @Override
          public void onFinish() {
              counter.setText("00:00:00");
          }

          @SuppressLint("NewApi")
          @TargetApi(Build.VERSION_CODES.GINGERBREAD)
          @Override
          public void onTick(long millisUntilFinished) {
              counter.setText(String.format(Locale.ENGLISH, "%02d D :%02d H :%02d M :%02d S",
                      TimeUnit.MILLISECONDS.toDays(millisUntilFinished),
                      TimeUnit.MILLISECONDS.toHours(millisUntilFinished) -
                              TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
                      TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                              TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                      // The change is in this line
                      TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                              TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
          }
      }

      public void showtootltext(String text) {
          navBinding.MageNativeToolbar.setBackground(getResources().getDrawable(R.drawable.corner_grey_1bottomline));
          navBinding.toolimage.setVisibility(View.GONE);
          navBinding.tooltext.setVisibility(View.VISIBLE);
          navBinding.tooltext.setText(text);
      }

      public void showmsg(String message) {
        /*Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.AppTheme));
        snackbar.show();*/
          Toast toast = new Toast(getApplicationContext());
          toast.setDuration(Toast.LENGTH_SHORT);
          LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          assert inflater != null;
          ViewGroup null_parent = null;
          View view = inflater.inflate(R.layout.toast_layout, null_parent);
          TextView txt = view.findViewById(R.id.btnDefaultToast);
          txt.setText(toTitleCase(message));
          toast.setView(view);
          toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 30);
          toast.show();
      }

      public static String toTitleCase(String str) {

          if (str == null) {
              return null;
          }

          boolean space = true;
          StringBuilder builder = new StringBuilder(str);
          final int len = builder.length();

          for (int i = 0; i < len; ++i) {
              char c = builder.charAt(i);
              if (space) {
                  if (!Character.isWhitespace(c)) {
                      // Convert to title case and switch out of whitespace mode.
                      builder.setCharAt(i, Character.toTitleCase(c));
                      space = false;
                  }
              } else if (Character.isWhitespace(c)) {
                  space = true;
              } else {
                  builder.setCharAt(i, Character.toLowerCase(c));
              }
          }

          return builder.toString();
      }

      public void setdevice_withusermail() {
          try {
              @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
              session.savedeviceid(deviceId);
              JsonObject notification_json = new JsonObject();
              if (session.isLoggedIn()) {
                  notification_json.addProperty("email", session.getUserDetails().get("Email"));
              } else {
                  notification_json.addProperty("email", "Guest user");
              }
              notification_json.addProperty("type", "2");
              notification_json.addProperty("unique_id", session.getdeviceid());
              notification_json.addProperty("Token", session.gettoken());
              drawerViewModel.registerdevicetoserver(this, cedSessionManagement.getCurrentStore(), notification_json).observe(this, apiResponse -> {
                  switch (apiResponse.status) {
                      case SUCCESS:
                          Log.d(Urls.TAG, "" + apiResponse.data);
                          session.save_iffreshinstall_ondevice(false);
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

      public void set_regular_font_fortext(TextView textView) {
          cedFontSetting.setFontforTextviews(textView, "Celias-Regular.ttf", getApplicationContext());
      }

      public void set_regular_font_forButton(Button button) {
          cedFontSetting.setfontforButtons(button, "Celias-Regular.ttf", getApplicationContext());
      }

      public void set_regular_font_forRadio(RadioButton button) {
          cedFontSetting.setfontforRadio(button, "Celias-Regular.ttf", getApplicationContext());
      }

      public void set_regular_font_forEdittext(EditText edittext) {
          cedFontSetting.setfontforEditText(edittext, "Celias-Regular.ttf", getApplicationContext());
      }

      public void set_bold_font_fortext(TextView textView) {
          cedFontSetting.setFontforTextviews(textView, "Celias-Bold.ttf", getApplicationContext());
      }

      public void set_bold_font_forButton(Button button) {
          cedFontSetting.setfontforButtons(button, "Celias-Bold.ttf", getApplicationContext());
      }

      public void set_bold_font_forEdittext(EditText edittext) {
          cedFontSetting.setfontforEditText(edittext, "Celias-Bold.ttf", getApplicationContext());
      }

      public void cleardataandlogout() {
          cedSessionManagement.clearcartId();
          session.clear_data();
          if (mGoogleApiClient.isConnected()) {
              Auth.GoogleSignInApi.signOut(mGoogleApiClient);
              mGoogleApiClient.disconnect();
              mGoogleApiClient.connect();
          }
          Ced_MainActivity.latestcartcount = "0";
          FacebookSdk.sdkInitialize(this);
          LoginManager.getInstance().logOut();
          session.ClearCustomerId();
          session.logoutUser();
      }


      protected void showbackbutton() {
          Objects.requireNonNull(Ced_FragmentDrawer.mDrawerToggle).setDrawerIndicatorEnabled(false);
          Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
          Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_back);
          Objects.requireNonNull(Ced_FragmentDrawer.mDrawerToggle).setToolbarNavigationClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  // Doesn't have to be onBackPressed
                  onBackPressed();
              }
          });
      }

      public void animateRecyclerLayoutChange(final int layoutSpanCount, GridLayoutManager gridLayoutManager, RecyclerView recycler) {
          Animation fadeOut = new AlphaAnimation(1, 0);
          fadeOut.setInterpolator(new DecelerateInterpolator());
          fadeOut.setDuration(500);
          fadeOut.setAnimationListener(new Animation.AnimationListener() {
              @Override
              public void onAnimationStart(Animation animation) {
              }

              @Override
              public void onAnimationRepeat(Animation animation) {
              }

              @Override
              public void onAnimationEnd(Animation animation) {
                  gridLayoutManager.setSpanCount(layoutSpanCount);
                  gridLayoutManager.requestLayout();
                  Animation fadeIn = new AlphaAnimation(0, 1);
                  fadeIn.setInterpolator(new AccelerateInterpolator());
                  fadeIn.setDuration(500);
                  recycler.startAnimation(fadeIn);
              }
          });
          recycler.startAnimation(fadeOut);
      }

      public void showDOBDatePicker(TextView dob) {
          final Calendar myCalendar = Calendar.getInstance();
          final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerButton, new DatePickerDialog.OnDateSetListener() {
              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                  myCalendar.set(Calendar.YEAR, year);
                  myCalendar.set(Calendar.MONTH, monthOfYear);
                  myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                  dob.setText(sdf.format(myCalendar.getTime()));
              }
          }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
          datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
          datePickerDialog.show();
      }


      public boolean isValidEmail(String email) {
          String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                  + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
          Pattern pattern = Pattern.compile(EMAIL_PATTERN);
          Matcher matcher = pattern.matcher(email);
          return matcher.matches();
      }

      public void hideBottomNav() {
          navBinding.bottomNavigation.setVisibility(View.GONE);
      }
  }