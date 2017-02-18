package com.pooja.googlemap;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.*;
import java.util.jar.Manifest;

import static com.pooja.googlemap.R.id.add;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    double latitude, longitude;
    CameraUpdate center;
    CameraUpdate zoom;
    GoogleApiClient mGoogleApiClient;

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_main);
        setupMapifNeeded();
    }

    public void setupMapifNeeded() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(13.0604, 80.2496)).title("chennai").draggable(true));
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        center = CameraUpdateFactory.newLatLng(new LatLng(13.0604, 80.2496));
        zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
                zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addresses=geocoder.getFromLocation(latitude,longitude,1);
                    if(addresses != null && addresses.size() > 0 ){
                        Address address = addresses.get(0);

                        String addressText = String.format("%s,%s,%s,%s",
                                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                address.getSubLocality(),
                                address.getLocality(),
                                address.getAdminArea()
                        );

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(addressText)
                                .snippet(address.getPostalCode()+",\t"+address.getCountryName()));
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        GoogleMap.OnMarkerClickListener onMarkerClickedListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        };
    }

}