package hyperactive.co.il.mehearthotel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(){
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent serviceIntent=new Intent(context, NotificationsService.class);
                PendingIntent pendingIntent=PendingIntent.getService(context, 100, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }.start();
    }
}
