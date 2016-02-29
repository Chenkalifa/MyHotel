package hyperactive.co.il.mehearthotel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Calendar;

public class SpalshScreenActivity extends AppCompatActivity {
    Animation animation ,animation2;
    ImageView logo;
    ImageView white_logo;
    RelativeLayout container;

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(){
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent serviceIntent=new Intent(SpalshScreenActivity.this, NotificationsService.class);
                PendingIntent pendingIntent=PendingIntent.getService(SpalshScreenActivity.this, 100, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        container= (RelativeLayout) findViewById(R.id.splash_container);
        white_logo= (ImageView) findViewById(R.id.logo_white_iv);
        logo= (ImageView) findViewById(R.id.logo_Iv);
        logo.setVisibility(View.GONE);
        animation= AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.removeView(white_logo);
//                white_logo.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2=AnimationUtils.loadAnimation(this, R.anim.dissolve_anim);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i=new Intent(SpalshScreenActivity.this, HomeScreenActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        white_logo.startAnimation(animation);


    }

}
