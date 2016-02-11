package com.komoot.sampl4.ktraker.route;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.komoot.sampl4.ktraker.R;
import com.komoot.sampl4.ktraker.route_list.MainActivity;

import java.util.List;

/**

 */
public class RouteInfoFragmentDialog extends DialogFragment {


    public RouteInfoFragmentDialog() { }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Route info")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });


        String message=getMesage();
        adb.setMessage(message);
        return adb.create();
    }


    public String getMesage() {
        String message="";
        List<LatLng> latLngList=((RouteActivity) getActivity()).db.getPoints(""+((RouteActivity) getActivity()).routeID);
        Float distance=0f;

        for(int i=0; i<(latLngList.size()-1); i++){
          Location location0=new Location("blah");
            location0.setLatitude(latLngList.get(i).latitude);
            location0.setLongitude(latLngList.get(i).longitude);
            Location location1=new Location("blah");
            location1.setLatitude(latLngList.get(i+1).latitude);
            location1.setLongitude(latLngList.get(i + 1).longitude);
            distance=distance+location0.distanceTo(location1);

        }
        message=message+"distance = "+distance;
        return message;
    }
}
