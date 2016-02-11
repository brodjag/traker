package com.komoot.sampl4.ktraker.route;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.komoot.sampl4.ktraker.R;
import com.komoot.sampl4.ktraker.db.DbAdapter;
import com.komoot.sampl4.ktraker.route_list.MainActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see
 */
public class RouteActivity  extends AppCompatActivity {
    public static String BROADCAST_ACTION="com.komoot.sampl4.ktraker.route#newUrl";

    long routeID;
    public DbAdapter db;
    Intent serviceIntent;
    RecyclerSet recyclerSet;
    mapSet MymapSet;

    RouteInfoFragmentDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceIntent=new Intent(getBaseContext(),trakingService.class);

        db=new DbAdapter(this);
        setContentView(R.layout.activity_route);
        routeID=getIntent().getLongExtra("routeID",-1);
        serviceIntent.putExtra("routeId",routeID);
        recyclerSet=new RecyclerSet(this);

        setBroadcast();
        MymapSet= new mapSet(this);

        setTitle(db.getRouteName(""+routeID));
        infoDialog=new RouteInfoFragmentDialog();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_menu, menu);


        return true;
    }

    MenuItem menu_start,menu_stop;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
         menu_start = menu.findItem(R.id.action_tracking_start);
         menu_stop = menu.findItem(R.id.action_tracking_stop);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove) {
            db.deleteRoutes(""+routeID);
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
            return true;
        }

        if (id == R.id.action_tracking_start) {
            startService(serviceIntent);
            item.setVisible(false);
            menu_stop.setVisible(true);
            return true;
        }

        if (id == R.id.action_tracking_stop) {
            stopService(serviceIntent);
            item.setVisible(false);
            menu_start.setVisible(true);
            return true;
        }

        if (id == R.id.action_info) {
            infoDialog.show(getFragmentManager(),"infoDLG");
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(),MainActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        stopService(serviceIntent);
        db.onDestroy();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    BroadcastReceiver broadcastReceiver;

   void setBroadcast(){
       broadcastReceiver=new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {
               String url=intent.getStringExtra("url");
               if(!url.equals("")) recyclerSet.addItem(url);

               double lan=intent.getDoubleExtra("lan",0);
               double lon=intent.getDoubleExtra("lon",0);
               Log.d("qqq","lan="+lan);
               MymapSet.updatePoliline(lan,lon);
           }
       };
       IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
       registerReceiver(broadcastReceiver, intFilt);
   }
}
