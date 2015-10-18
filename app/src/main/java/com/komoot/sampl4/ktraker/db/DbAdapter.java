package com.komoot.sampl4.ktraker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brodjag on 16.10.15.
 */
public class DbAdapter  {
    private DbHelper DBHelper1;
    //private SQLiteDatabase db;

    public static String TABLE_ROUTES="routes";
    public static String TABLE_POINTS="points";
    public static String TABLE_ROUTES_NAME="name";

    public  DbAdapter (Context context){
        DBHelper1 = new DbHelper(context);
    }


    public long insertRoutes(String namel) {
         SQLiteDatabase db=DBHelper1.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TABLE_ROUTES_NAME, namel);
        long res= db.insert(TABLE_ROUTES, null, initialValues);
        db.close();
        return res;
    }

    public Cursor getRoutes() {
        SQLiteDatabase db=DBHelper1.getReadableDatabase();
        Cursor res=db.rawQuery("select id as _id, name as name from routes", null);
        //db.close();
        return res;
    }

    public long deleteRoutes(String id) {
        SQLiteDatabase db=DBHelper1.getWritableDatabase();
        long res= db.delete("routes", "id=" + id, new String[]{});

        db.close();
        return res;
    }

 public void onDestroy(){
     DBHelper1.close();
 }

    public long insertPoints(String routeId,double lan, double lon, String imageUrl) {
        SQLiteDatabase db=DBHelper1.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("route_id", routeId);
        initialValues.put("langitude", lan);
        initialValues.put("longitude", lon);
        initialValues.put("image_url", imageUrl);
        Log.d("qqq",initialValues.toString());
        long res= db.insert(TABLE_POINTS, null, initialValues);
        db.close();
        return res;
    }

    public List<String> getImages(String route_id) {
        SQLiteDatabase db=DBHelper1.getReadableDatabase();
        String request="select image_url from points where image_url!='' and route_id='"+route_id+"'";
        Cursor cursor=db.rawQuery(request,new String[]{}); //db.query("points",new String[]{"image_url"},"image_url!='' and route_id='%s'",new String[]{route_id},null,null,null);

        List<String> res=new ArrayList<String>();
        if(cursor.getCount()==0){return res;}
        for(int i=0;i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String item=""+cursor.getString(0).toString();
            Log.d("qqq "+i, item);
            res.add(item);
        }
        cursor.close();
        db.close();
        return res;
    }
}
