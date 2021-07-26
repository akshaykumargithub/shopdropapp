package shop.dropapp.Ced_MageNative_Location;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import shop.dropapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import shop.dropapp.R;
import shop.dropapp.base.activity.Ced_NavigationActivity;
import shop.dropapp.ui.homesection.activity.Ced_New_home_page;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Ced_ChangePlace extends Ced_NavigationActivity {
    private static final String TAG = "location";
    private static final int PERMISSIONS_REQUEST = 100;
    String choosenplace;
    String CountryName;
    String PostalCode;
    String city;
    String area;
    String State;
    String capital;
    String lat;
    String lng;
    Ced_SessionManagement management;
    Ced_SessionManagement_login ced_sessionManagement_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.MageNative_frame_container);
        getLayoutInflater().inflate(R.layout.activity_ced__change_place, content, true);

        management = new Ced_SessionManagement(Ced_ChangePlace.this);
        ced_sessionManagement_login = new Ced_SessionManagement_login(Ced_ChangePlace.this);
        pincode.setText(management.gettool_address());

        final Button search = (Button) findViewById(R.id.search);
        Log.i(TAG, "onCreate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: called");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBkjXPO8Vz_TTyfNCdihN2qfOtZJRTO6q4");
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("REpo", "pl getAttributions " + place.getAttributions());
                Log.i("REpo", "pl getAddressComponents" + place.getAddressComponents());
                Log.i("REpo", "pl getAddress" + place.getAddress());
                Log.i("REpo", "pl getName" + place.getName());
                Log.i("REpo", "pl getName" + place.getLatLng());
                ced_sessionManagement_login.savePincode(place.getName());
                choosenplace = place.getName() + "," + place.getAddress();

                Log.d(TAG, "choosenplace: " + place.getName() + "" + place.getAddress());

                LatLng latLng = place.getLatLng();
                Geocoder geocoder = new Geocoder(Ced_ChangePlace.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null) {
                        if (addresses.size() > 0) {
                            Address returnedAddr = addresses.get(0);

                            Log.d(TAG, "returnedAddr: " + returnedAddr.getCountryName());
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getPostalCode());
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getLocality());
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getSubLocality());
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getAddressLine(1));
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getAdminArea());
                            Log.d(TAG, "returnedAddr: " + returnedAddr.getSubAdminArea());

                            CountryName = returnedAddr.getCountryName();
                            PostalCode = returnedAddr.getPostalCode();
                            city = returnedAddr.getLocality();
                            area = returnedAddr.getSubLocality();
                            State = returnedAddr.getAdminArea();
                            capital = returnedAddr.getSubAdminArea();
                            lat = String.valueOf(latLng.latitude);
                            lng = String.valueOf(latLng.longitude);

                            if (choosenplace != null) {
                                search.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Ced_ChangePlace.this);
                                        dialog.setTitle("Alert");
                                        dialog.setMessage("Your cart will get cleared on changing the location..!");
                                        dialog.setCancelable(true);
                                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                pincode.setText(choosenplace);
                                                Log.e("REpo", "1-" + pincode.getText().toString());
                                                management.save_tool_address(choosenplace);
                                                pincode.setText(management.gettool_address());
                                                Log.e("REpo", "2-" + pincode.getText().toString());
                                                management.saveAddress(returnedAddr.toString());
                                                management.save_latitude(lat);
                                                management.save_longitude(lng);
                                                management.save_city(city);
                                                management.save_country(CountryName);
                                                management.save_state(State);
                                                management.save_postcode(PostalCode);
                                                dialog.dismiss();
                                                //clearcart();
                                                Intent home = new Intent(Ced_ChangePlace.this, Ced_New_home_page.class);
                                                startActivity(home);
                                            }
                                        });
                                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();

                                    }
                                });
                            }
                        }
                    } else {
                        Log.w(TAG, "No Address returned!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });
    }
}
