package http.chess.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.jingle.firebase.plugin.online.R;
import com.jingle.firebase.plugin.online.util.Config;
import com.jingle.firebase.plugin.online.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import http.chess.MainMenu;
import http.chess.SplashScreen;

public class AlarmReceiver extends BroadcastReceiver {

    int MID = 0;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean(Utility.ID_PRE_COIN,false)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Utility.ID_PRE_COIN,true);
            editor.commit();
        }

        // For our recurring task, we'll just display a message
        Log.e("DEBUG", "" + Utility.isAppIsInBackground(context));
        if(Utility.isAppIsInBackground(context))
        {
//            NotificationManager mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//            Intent intent1 = new Intent(context,SplashScreen.class);
//
//            Notification notification = new Notification(R.drawable.ic_laucher,"This is a test message!", System.currentTimeMillis());
//            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context,0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.setLatestEventInfo(context, "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);
//
//            mManager.notify(0, notification);


            long when = System.currentTimeMillis();

            Intent notificationIntent = new Intent(context, SplashScreen.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            new NotificationUtils(context).showNotificationMessage("Chess Online","You received 300 coins daily reward!!\nLet's Play.",String.valueOf(when),notificationIntent);

        }
    }
    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
