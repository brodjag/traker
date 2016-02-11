package com.komoot.sampl4.ktraker.route_list;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.komoot.sampl4.ktraker.db.DbAdapter;
import com.komoot.sampl4.ktraker.R;
import com.komoot.sampl4.ktraker.route.RouteActivity;

public class MainActivity extends AppCompatActivity {
    public DbAdapter db;
    newRouteDialogFragment routeDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DbAdapter(this);
        setContentView(R.layout.activity_main);
        setRecykler();

        routeDialog=newRouteDialogFragment.newInstance();
    }

    private void setRecykler() {
        ListView listView=(ListView) findViewById(R.id.rv);
        Cursor cursor =db.getRoutes();
        //startManagingCursor(cursor);

        String[] fields=new String[]{cursor.getColumnName(1)};//
        int[] vidgetId=new int[]{R.id.text_id};//
        //CursorAdapter cursorAdapter=new SimpleCursorAdapter(getBaseContext(),R.layout.route_item,db.getRoutes(), fields,vidgetId);
        CursorAdapter cursorAdapter=new SimpleCursorAdapter(getBaseContext(),R.layout.route_item,cursor,fields,vidgetId,SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //look Route
                Toast.makeText(getBaseContext(), "id " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), RouteActivity.class);
                intent.putExtra("routeID", id);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            routeDialog.show(getSupportFragmentManager(),"new route dlg");
/*
            long routeID= db.insertRoutes("route #4");
            Intent intent= new Intent(getBaseContext(),RouteActivity.class);
            intent.putExtra("routeID",routeID);
            startActivity(intent);
            finish();
            */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDestroy(){
        db.onDestroy();
        super.onDestroy();
    }
}
