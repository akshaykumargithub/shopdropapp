package shop.dropapp.Ced_MageNative_Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import shop.dropapp.R;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.ui.addresssection.activity.Ced_AddAddress;
import shop.dropapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class Ced_MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private static String TAG = "MAP LOCATION";
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    Ced_SessionManagement_login login;
    String Url, getcountry;
    ArrayList statelabellist, statecodelist;
    EditText optional_text;
    String setdeliverylocation = "";
    private GoogleMap mMap;
    private Context mContext;
    private TextView mLocationMarkerText, txt_location, but_change, or;
    private Button confirm_add, confirm_shipping;
    private LatLng mCenterLatLong;
    private Location currentLocation;
    private AddressResultReceiver mResultReceiver;
    private Ced_SessionManagement ced_sessionManagement;
    private LocationManager locationManager;
    private Boolean fromnavigation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ced_map);
        mContext = this;
        ced_sessionManagement = new Ced_SessionManagement(this);
        login = new Ced_SessionManagement_login(this);
        Url = getResources().getString(R.string.base_url) + "rest/V1/mobiconnect/customer/saveaddress";
        getcountry = getResources().getString(R.string.base_url) + "rest/V1/mobiconnect/module/getcountry/";
        statelabellist = new ArrayList<String>();
        statecodelist = new ArrayList<String>();
        if (getIntent().hasExtra("fromnavigation")) {
            if (getIntent().getStringExtra("fromnavigation").equals("true")) {
                fromnavigation = true;
            }
        }
        mLocationMarkerText = findViewById(R.id.locationMarkertext);
        txt_location = findViewById(R.id.txt_location);
        but_change = findViewById(R.id.but_change);
        optional_text = findViewById(R.id.optional_text);
        confirm_add = findViewById(R.id.confirm_add);
        confirm_shipping = findViewById(R.id.confirm_shipping);
        or = findViewById(R.id.or);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        but_change.setOnClickListener(this);
        confirm_add.setOnClickListener(this);
        confirm_shipping.setOnClickListener(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key)/*"AIzaSyBkjXPO8Vz_TTyfNCdihN2qfOtZJRTO6q4"*/);
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            fetchLocation();
        } else {
            showGPSDisabledAlertToUser();
        }
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, 100);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fetchLocation();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
    }

    private void fetchLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Ced_MapActivity.this);
                    txt_location.setText(getAddress(location));
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.isMyLocationEnabled();
            mCenterLatLong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mCenterLatLong));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCenterLatLong, 18f));
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    mCenterLatLong = mMap.getCameraPosition().target;
                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
                    new Ced_SessionManagement(Ced_MapActivity.this).save_latitude(String.valueOf(mCenterLatLong.latitude));
                    new Ced_SessionManagement(Ced_MapActivity.this).save_longitude(String.valueOf(mCenterLatLong.longitude));
                    startIntentService(mLocation);
                }
            });
        } else {
            Toast.makeText(mContext, "App need location permission to proceed further,please provide the GPS permission.", Toast.LENGTH_SHORT).show();
        }

    }

    protected void startIntentService(Location mLocation) {
        Intent intent = new Intent(this, Ced_FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLocation);
        startService(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.but_change) {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(Ced_MapActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        } else if (view.getId() == R.id.confirm_add) {
            if (!TextUtils.isEmpty(txt_location.getText().toString())) {
                ced_sessionManagement.save_tool_address(txt_location.getText().toString());
                Intent intent = new Intent(Ced_MapActivity.this, Magenative_HomePageNewTheme.class);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                finishAffinity();
            } else {
                Toast.makeText(mContext, "Please set location first", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (view.getId() == R.id.confirm_shipping) {
                if (!TextUtils.isEmpty(txt_location.getText().toString())) {
                    if (login.isLoggedIn()) {
                        ced_sessionManagement.save_tool_address(txt_location.getText().toString());
                        try {
                            saveaddress_as_shippingaddress();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Ced_NavigationActivity.guest_city = ced_sessionManagement.getcity();
                        Ced_NavigationActivity.guest_state = ced_sessionManagement.getstate();
                        Ced_NavigationActivity.guest_country = ced_sessionManagement.getcountry();
                        Ced_NavigationActivity.guest_pincode = ced_sessionManagement.getpostcode();

//                        Intent intent = new Intent(Ced_MapActivity.this, Ced_New_home_page.class);
                        Intent intent = new Intent(Ced_MapActivity.this, Magenative_HomePageNewTheme.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finishAffinity();
                    }

                } else {
                    Toast.makeText(mContext, "Please set location first", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveaddress_as_shippingaddress() throws JSONException {
        Intent NewAddressIntent = new Intent(Ced_MapActivity.this, Ced_AddAddress.class);
        NewAddressIntent.putExtra("frommap_page", true);
        startActivity(NewAddressIntent);
        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    private void getState(String country_code) {
        Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse() {
            @Override
            public void processFinish(Object output) throws JSONException {
                JSONObject object = new JSONObject(output.toString());
                Boolean status = object.getBoolean("success");
                if (status.equals(true)) {
                    JSONArray jsonArray = object.getJSONArray("states");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        statecodelist.add(c.getString("region_id"));
                        statelabellist.add(c.getString("name"));
                    }
                }
            }
        }, Ced_MapActivity.this);
        response.execute(getcountry + country_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                try {
                    if (mMap == null) {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
                        Log.i(TAG, "onActivityResult@@@@@@@@@@@@@: " + place.getAddress());
                        currentLocation = mLocation;
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(Ced_MapActivity.this);
                        startIntentService(mLocation);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                        mResultReceiver = new AddressResultReceiver(new Handler());
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18f));
                        Location mLocation = new Location("");
                        mLocation.setLatitude(place.getLatLng().latitude);
                        mLocation.setLongitude(place.getLatLng().longitude);
                        startIntentService(mLocation);
                        getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == 100) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fetchLocation();
            } else {
                showGPSDisabledAlertToUser();
            }
        }
    }

    private void getPlaceInfo(double lat, double lon) throws IOException {
        Geocoder mGeocoder = new Geocoder(this);
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);


        if (addresses.get(0).getPostalCode() != null) {
            String ZIP = addresses.get(0).getPostalCode();
            Log.d("ZIP CODE", ZIP);
            ced_sessionManagement.save_postcode(ZIP);
        }
        if (addresses.get(0).getAddressLine(0)!= null) {
            String ADDRESSS = addresses.get(0).getAddressLine(0);
            Log.d("ADDRESSS", ADDRESSS);
            ced_sessionManagement.saveAddress(ADDRESSS);
        }

        if (addresses.get(0).getLocality() != null) {
            String city = addresses.get(0).getLocality();
            ced_sessionManagement.save_city(city);
            Log.d("CITY", city);
        }

        if (addresses.get(0).getAdminArea() != null) {
            String state = addresses.get(0).getAdminArea();
            ced_sessionManagement.save_state(state);
            Log.d("STATE", state);
        }

        if (addresses.get(0).getCountryName() != null) {
            String country = addresses.get(0).getCountryName();
            ced_sessionManagement.save_country(country);
            Log.d("COUNTRY", country);
        }

        if (addresses.get(0).getAddressLine(0) != null) {
            String getAddressLine = addresses.get(0).getAddressLine(0).replace(addresses.get(0).getFeatureName(), "");
            setdeliverylocation = addresses.get(0).getAddressLine(0);
            getAddressLine = getAddressLine.replace(ced_sessionManagement.getcountry(), "");
            getAddressLine = getAddressLine.replace(ced_sessionManagement.getstate(), "");
            getAddressLine = getAddressLine.replace(ced_sessionManagement.getpostcode(), "");
            getAddressLine = getAddressLine.replace(ced_sessionManagement.getcity(), "");
            getAddressLine = getAddressLine.replace(",", "");
            ced_sessionManagement.save_shipping_address(getAddressLine + ",\n" + optional_text.getText().toString());
            Log.d("getAddressLine", getAddressLine);
            txt_location.setText(setdeliverylocation.replace(", null", ""));
            ced_sessionManagement.save_tool_address(txt_location.getText().toString());
        }

        if (addresses.get(0).getCountryCode() != null) {
            String CountryCode = addresses.get(0).getCountryCode();
            ced_sessionManagement.save_countrycode(CountryCode);
            Log.d("CountryCode", CountryCode);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (!setdeliverylocation.equals("")) {
                txt_location.setText(setdeliverylocation.replace(", null", ""));
            } else {
                txt_location.setText(resultData.getString("location_result").replace(", null", ""));
            }
            ced_sessionManagement.save_tool_address(txt_location.getText().toString());
            if (resultData.getDouble("latitude") != 0 && resultData.getDouble("longitude") != 0) {
                try {
                    getPlaceInfo(resultData.getDouble("latitude"), resultData.getDouble("longitude"));
                    mResultReceiver = new AddressResultReceiver(new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //  Toast.makeText(mContext, "" + resultData.getString("location_result"), Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddress(Location location) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

