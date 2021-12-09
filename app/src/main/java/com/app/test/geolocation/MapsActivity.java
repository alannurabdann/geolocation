package com.app.test.geolocation;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng places = new LatLng(-8.579892, 116.095239);
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        setupAutoCompleteFragment();
    }

    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                places = place.getLatLng();
                mapFragment.getMapAsync(MapsActivity.this);

//                CameraPosition cameraPosition = new CameraPosition.Builder().target(places).zoom(10.0f).build();
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                mMap.moveCamera(cameraUpdate);
            }

            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        mMap = googleMap;

        try {

            addresses = geocoder.getFromLocation(
                    places.latitude,
                    places.longitude,
                    1);

            if (addresses.size() > 0) {
                String ZIP = "";
                String city = "";
                String state = "";
                String address = "";

                Address a = addresses.get(0);


                if (a.getPostalCode() != null) {
                    ZIP = addresses.get(0).getPostalCode();
                    Log.d("ZIP", ZIP);
                }

                if (a.getLocality() != null) {
                    city = a.getLocality();
                    Log.d("city", city);
                }

                if (a.getAdminArea() != null) {
                    state = a.getAdminArea();
                    Log.d("state", state);
                }

                if (a.getAddressLine(0) != null) {
                    address = a.getAddressLine(0);
                    Log.d("address", address);
                }

                mMap.addMarker(new MarkerOptions()
                        .position(places)
                        .title(state)
                        .snippet(address + "\n" + city + " " + ZIP)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(places).zoom(18.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
}