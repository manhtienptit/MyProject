package com.seatgeek.placesautocompletedemo.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.PointF;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.seatgeek.placesautocompletedemo.model.ParkingCar;

import java.util.ArrayList;

public class DBHandler extends SQLiteAssetHelper {

    private static String DATABASE_NAME = "LocationData.sqlite"; // must have file extension
    private static int DATABASE_VERSION = 1;

    public static final String SQLITE_TABLE = "LocationData";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "adress";
    public static final String KEY_DES = "des";
    public static final String KEY_OPEN = "open";
    public static final String KEY_CLOSE = "close";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SCALE = "scale";
    public static final String KEY_PRICE = "price";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";

    private SQLiteDatabase mDb;

    String COL_X = "lat";
    String COL_Y = "lon";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        mDb = this.getReadableDatabase();
    }

    public void close() {
        mDb.close();
    }


    /**
     * Returns a sample string.
     * @return sample - a sample string from your database table
     */
    public ArrayList<ParkingCar> getSample(float x, float y,int radius) {

        PointF center = new PointF(x, y);
        final double mult = 1; // mult = 1.1; is more reliable
        PointF p1 = calculateDerivedPosition(center, mult * radius, 0);
        PointF p2 = calculateDerivedPosition(center, mult * radius, 90);
        PointF p3 = calculateDerivedPosition(center, mult * radius, 180);
        PointF p4 = calculateDerivedPosition(center, mult * radius, 270);

        String strWhere =  "SELECT * FROM LocationData" ;
                //" WHERE "
//                + COL_X + " > " + String.valueOf(p3.x) + " AND "
//                + COL_X + " < " + String.valueOf(p1.x) + " AND "
//                + COL_Y + " < " + String.valueOf(p2.y) + " AND "
//                + COL_Y + " > " + String.valueOf(p4.y);

        return getOrderDetails(strWhere);
    }

    public ArrayList<ParkingCar> getOrderDetails(String querry) throws SQLException {


        ArrayList<ParkingCar> orderDetailList = new ArrayList<ParkingCar>();
        Cursor mCursor = mDb.rawQuery(querry,null);


        if (mCursor.moveToFirst()) {
            do {
                ParkingCar orderDetail = new ParkingCar();
                orderDetail.setIdLocation(Integer.parseInt(mCursor.getString(mCursor.getColumnIndexOrThrow("id"))));
                orderDetail.setLongitude(Float.parseFloat(mCursor.getString(mCursor.getColumnIndexOrThrow("lon"))));
                orderDetail.setLatitude(Float.parseFloat(mCursor.getString(mCursor.getColumnIndexOrThrow("lat"))));
                orderDetail.setAtmName((mCursor.getString(mCursor.getColumnIndexOrThrow("name"))));
                orderDetail.setLoc_Address((mCursor.getString(mCursor.getColumnIndexOrThrow("adress"))));
                orderDetail.setLoc_Des((mCursor.getString(mCursor.getColumnIndexOrThrow("des"))));
                orderDetail.setLoc_Open((mCursor.getString(mCursor.getColumnIndexOrThrow("open"))));
                orderDetail.setLoc_Close((mCursor.getString(mCursor.getColumnIndexOrThrow("close"))));
                orderDetail.setLoc_Phone((mCursor.getString(mCursor.getColumnIndexOrThrow("phone"))));
                orderDetail.setLoc_Type(Integer.parseInt(mCursor.getString(mCursor.getColumnIndexOrThrow("type"))));
                orderDetail.setLoc_Scale(Integer.parseInt(mCursor.getString(mCursor.getColumnIndexOrThrow("scale"))));
                orderDetail.setLoc_Price(Integer.parseInt(mCursor.getString(mCursor.getColumnIndexOrThrow("price"))));
                orderDetailList.add(orderDetail);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return orderDetailList;
    }

    public static PointF calculateDerivedPosition(PointF point,
                                                  double range, double bearing)
    {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        PointF newPoint = new PointF((float) lat, (float) lon);

        return newPoint;

    }

    public static boolean pointIsInCircle(PointF pointForCheck, PointF center,
                                          double radius) {
        if (getDistanceBetweenTwoPoints(pointForCheck, center) <= radius)
            return true;
        else
            return false;
    }

    public static double getDistanceBetweenTwoPoints(PointF p1, PointF p2) {
        double R = 6371000; // m
        double dLat = Math.toRadians(p2.x - p1.x);
        double dLon = Math.toRadians(p2.y - p1.y);
        double lat1 = Math.toRadians(p1.x);
        double lat2 = Math.toRadians(p2.x);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
                * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }
}