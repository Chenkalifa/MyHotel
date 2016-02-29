package hyperactive.co.il.mehearthotel;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationsService extends IntentService {


    private JSONArray resHistory;
    private ArrayList<Object> upcomingResList;
    Resources res;
    static String title, msg, text;
    final DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
    SharedPreferences preferences;
    final String MY_APP_PREFERNCES="preferences";
    boolean isUserAskedToStayLogged;
    ParseUser user;

    public NotificationsService() {
        super("NotificationsService");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        user=ParseUser.getCurrentUser();
        preferences=getSharedPreferences(MY_APP_PREFERNCES, MODE_PRIVATE);
        isUserAskedToStayLogged =preferences.getBoolean("keepLogged", false);
        res = getResources();
        title = res.getString(R.string.noti_title);
        msg = res.getString(R.string.noti_msg);
        text = res.getString(R.string.noti_text);
        if (intent != null&&user!=null) {
            new Thread() {
                @Override
                public void run() {
                    FileInputStream fileInputStream = null;
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + user.getUsername());
                        File resFile = new File(folder, "res.txt");
                        Log.i("myApp", "res file=" + resFile.getAbsolutePath());
                        fileInputStream = new FileInputStream(resFile);
                        int read = -1;
                        while ((read = fileInputStream.read()) != -1) {
                            stringBuffer.append((char) read);
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("myApp", "file error", e);
                    } catch (IOException e) {
                        Log.e("myApp", "I/O error", e);
                    } finally {
                        try {
                            if (fileInputStream != null)
                                fileInputStream.close();
                        } catch (IOException e) {
                            Log.e("myApp", "I/O error", e);
                        }
                    }
                    if (stringBuffer.length() != 0) {
                        try {
                            resHistory = new JSONArray(stringBuffer.toString());
                            upcomingResList = new ArrayList<>();
                            Log.i("myApp", "on MyProfile, resHistory: " + resHistory.toString());
                            for (int i = 0; i < resHistory.length(); i++) {
                                JSONObject res = resHistory.getJSONObject(i);
                                Log.i("myApp", "on MyProfile,current res: " + res.toString());
                                DateTime checkin = new DateTime(res.get("check_in"));
                                int nights = res.getInt("nights");
                                DateTime now = new DateTime();
                                if (checkin.isAfter(now.plusDays(1)) && checkin.isBefore(now.plusDays(2))) {
                                    sendNotification(checkin);
                                    sendEmailRemainder(checkin, nights);
                                }
                            }
                        } catch (JSONException ex) {
                            Log.e("myApp", "json error", ex);
                        }
                    }
                }
            }.start();
        }
    }

    private void sendEmailRemainder(final DateTime checkin, final int nights) {
        new Thread() {
            @Override
            public void run() {
                String hotelEmail = getResources().getString(R.string.hotelEmail);
                String hotelName = getResources().getString(R.string.hotelName);
                Mail mail = new Mail(hotelEmail, "hotellikeme");
                mail.set_from(hotelEmail);
                mail.set_subject(hotelName + ", your visit reminder!");
                String checkIn = dtfOut.print(checkin);
                StringBuilder subject = new StringBuilder();
                subject.append("Hey there, ").append(user.getUsername()).append("!").append("\n")
                        .append("This is ").append(hotelName).append(" staff again\n\n")
                        .append("We'd like to remind you about your next visit,\n")
                        .append(nights).append(" night").append(nights > 1 ? "s " : " ").append("starting at ").append(checkIn).append("\n\n")
                        .append("  Check-In: 10:00 AM local time\n")
                        .append("  Payment upon arrival\n\n")
                        .append("See you soon!");
                String[] to = {user.getEmail()};
                mail.set_body(subject.toString());
                mail.set_to(to);
                try {
                    mail.send();
                    Log.i("myApp", "Succeeded!");
                } catch (Exception e) {
                    Log.e("myApp", "failed", e);
                }
            }
        }.start();
    }

    private void sendNotification(DateTime checkin) {
        PugNotification.with(this)
                .load()
                .smallIcon(R.mipmap.ic_launcher)
                .autoCancel(true)
                .largeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .title(title)
                .message(msg + " " + dtfOut.print(checkin))
                .bigTextStyle(text)
                .simple()
                .build();
    }
}
