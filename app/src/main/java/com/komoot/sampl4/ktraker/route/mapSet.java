package com.komoot.sampl4.ktraker.route;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.komoot.sampl4.ktraker.R;

/**
 * Created by brodjag on 17.10.15.
 */
public class mapSet {
    RouteActivity con;
    private GoogleMap mMap;

    PolylineOptions polylineOptions;

    public mapSet(RouteActivity c){
        con=c;
        polylineOptions=new PolylineOptions();
        MapFragment mapFragment = (MapFragment) con.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                setPosition(53,48);
                updatePoliline(53.2, 48.3);
                updatePoliline(53.0,48.1);
            }
        });
    }


    public void setPosition(double lan, double lon){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lan, lon))
                .zoom(13)
                .bearing(0)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
    }


public    void updatePoliline(double lan,double lon){
    //todo make call with sql request
       mMap.clear();
       polylineOptions.add(new LatLng(lan, lon));
       mMap.addPolyline(polylineOptions);

       LatLng target =new LatLng(lan,lon);
       CameraPosition current=mMap.getCameraPosition();
       CameraPosition cameraPosition=new CameraPosition(target,current.zoom,current.tilt,current.bearing);
       CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
       mMap.animateCamera(cameraUpdate);
   }



}
