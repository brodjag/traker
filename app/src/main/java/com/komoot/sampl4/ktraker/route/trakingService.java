package com.komoot.sampl4.ktraker.route;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.komoot.sampl4.ktraker.db.DbAdapter;
import com.komoot.sampl4.ktraker.route_list.MainActivity;

import org.json.JSONObject;

public class trakingService extends Service {
    private DbAdapter db;
    public static Long  RequestDistantion=100l;
    Location OldLocation;
    RequestQueue queue;
    Long routeId;
    public trakingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        db=new DbAdapter(getBaseContext());
        queue = Volley.newRequestQueue(this);
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        setLocation();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        try {
            routeId=intent.getLongExtra("routeId",-1);
        }catch (Exception e){}


    }

    @Override
    public void onDestroy() {
        queue.cancelAll("q");
        if(mLocationManager!=null){
            if(mLocationListener!=null) mLocationManager.removeUpdates(mLocationListener);
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        db.onDestroy();
        super.onDestroy();

    }

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private static final long LOCATION_REFRESH_TIME = 1000*3;
    private static final long LOCATION_REFRESH_DISTANCE = 100;
    //******************************
    void setLocation(){
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener= new LocationListener() {
            /**
             * Called when the location has changed.
             * <p/>
             * <p> There are no restrictions on the use of the supplied Location object.
             *
             * @param location The new location, as a Location object.
             */
            @Override
            public void onLocationChanged(Location location) {
                if(OldLocation==null){OldLocation=location;  return;}

                if (location.distanceTo(OldLocation)>=RequestDistantion){
                    imageCall(OldLocation,location);
                    OldLocation=location;
                }
              Toast.makeText(getBaseContext(),"location changed "+location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when the provider status changes. This method is called when
             * a provider is unable to fetch a location or if the provider has recently
             * become available after a period of unavailability.
             *
             * @param provider the name of the location provider associated with this
             *                 update.
             * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
             *                 provider is out of service, and this is not expected to change in the
             *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
             *                 the provider is temporarily unavailable but is expected to be available
             *                 shortly; and {@link LocationProvider#AVAILABLE} if the
             *                 provider is currently available.
             * @param extras   an optional Bundle which will contain provider specific
             *                 status variables.
             *                 <p/>
             *                 <p> A number of common key/value pairs for the extras Bundle are listed
             *                 below. Providers that use any of the keys on this list must
             *                 provide the corresponding value as described below.
             *                 <p/>
             *                 <ul>
             *                 <li> satellites - the number of satellites used to derive the fix
             */
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if(status== LocationProvider.TEMPORARILY_UNAVAILABLE){Toast.makeText(getBaseContext(),"TEMPORARILY_UNAVAILABLE",Toast.LENGTH_SHORT).show();}
                if(status== LocationProvider.OUT_OF_SERVICE){Toast.makeText(getBaseContext(),"OUT_OF_SERVICE :(((",Toast.LENGTH_LONG).show();}
               // if(status== LocationProvider.AVAILABLE){Toast.makeText(getBaseContext(),"AVAILABLE :)",Toast.LENGTH_LONG).show();}

            }

            /**
             * Called when the provider is enabled by the user.
             *
             * @param provider the name of the location provider associated with this
             *                 update.
             */
            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getBaseContext(),"onProviderEnabled",Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when the provider is disabled by the user. If requestLocationUpdates
             * is called on an already disabled provider, this method is called
             * immediately.
             *
             * @param provider the name of the location provider associated with this
             *                 update.
             */
            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getBaseContext(),"location Provider disabled",Toast.LENGTH_SHORT).show();
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
    }


    //image Request
    private void imageCall(final Location oldLocation, final Location location) {
        String urlFormat="http://www.panoramio.com/map/get_panoramas.php?set=public&from=0&to=2&minx=%s&miny=%s&maxx=%s&maxy=%s&size=medium&mapfilter=true";
        double deltaX=(location.getLatitude()-oldLocation.getLatitude())/2d;
        double deltaY=(location.getLongitude()-oldLocation.getLongitude())/2d;
        double delta=Math.sqrt(deltaX*deltaX+deltaY*deltaY);
        String url=String.format(urlFormat, new String[]{""+(location.getLongitude()-delta),"" + (location.getLatitude()-delta),""+(location.getLongitude()+delta), ""+(location.getLatitude()+delta)});
        Log.d("qqq",url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String imageUrl="";
               try {
                   imageUrl=((JSONObject) response.getJSONArray("photos").get(0)).getString("photo_file_url");
               }catch (Exception e){}

            db.insertPoints(""+routeId,location.getLatitude(),location.getLongitude(),imageUrl);
                sendboadcast(imageUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                db.insertPoints(""+routeId,location.getLatitude(),location.getLongitude(),"");
            }
        });

queue.add(jsObjRequest);
    }


   void sendboadcast(String url){
       Intent intent = new Intent(RouteActivity.BROADCAST_ACTION);
       intent.putExtra("url",url);
       sendBroadcast(intent);
   }


}
