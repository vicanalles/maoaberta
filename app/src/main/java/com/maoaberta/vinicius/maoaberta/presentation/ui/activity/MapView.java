package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maoaberta.vinicius.maoaberta.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by vinicius on 28/11/17.
 */

public class MapView extends SupportMapFragment implements GoogleMap.OnMapClickListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private GoogleMap map;
    private boolean hasPosition = false;
    private double latitude;
    private double longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        try{
            map.setMyLocationEnabled(true);
        }catch(SecurityException e){

        }

        setupMap();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 15));
                map.addMarker(new MarkerOptions().position(latLng).title("Selecionar esta Posição")).showInfoWindow();
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("latitude", marker.getPosition().latitude);
                intent.putExtra("longitude", marker.getPosition().longitude);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(20));
        }
    }

    public void checarPermissaoGPS(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    public void verificarStatusGPS(){
        checarPermissaoGPS();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Checks if the GPS service is active
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Seu GPS está Offline, deseja ativá-lo?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), TURN_ON_GPS);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        verificarStatusGPS();
    }
}
