package com.komoot.sampl4.ktraker.route_list;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.komoot.sampl4.ktraker.R;
import com.komoot.sampl4.ktraker.route.RouteActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link newRouteDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newRouteDialogFragment extends DialogFragment {


    public newRouteDialogFragment() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("New Route");

        View view=View.inflate(getContext(),R.layout.fragment_new_route_dialog, null);
        RouteNameTxt=(EditText) view.findViewById(R.id.newRouteName);
        adb.setView(view);

        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String routeName = RouteNameTxt.getText().toString();
                        Log.d("route_name","="+routeName.toString());

                        if (RouteNameTxt.equals("")) {
                            Toast.makeText(getContext(),"please Enter name of route",Toast.LENGTH_LONG).show();
                            return;
                        }
                        long routeID = ((MainActivity) getActivity()).db.insertRoutes(routeName);
                        Intent intent = new Intent(getContext(), RouteActivity.class);
                        intent.putExtra("routeID", routeID);
                        startActivity(intent);
                       // getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });


        return adb.create();
    }

    public static newRouteDialogFragment newInstance() {
        newRouteDialogFragment fragment = new newRouteDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText RouteNameTxt=null;





}
