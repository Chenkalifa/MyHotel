package hyperactive.co.il.mehearthotel;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MyApp extends Application {
    SharedPreferences preferences;
    final String MY_APP_PREFERNCES = "preferences";
    ParseUser currentUser;
    boolean isUserAskedToStayLogged;
    static Typeface FONT_MAIN, FONT_SECONDARY;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "IEYUXkWZvs8am4wYnzqsu1dGTHfcLFUx1tDob06s", "Hb8R5i6KI6v96Mqzyg6vGxgYRNS10O8X18wzWDGF");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        preferences = getSharedPreferences(MY_APP_PREFERNCES, MODE_PRIVATE);
        isUserAskedToStayLogged = preferences.getBoolean("keepLogged", false);
        if (!isUserAskedToStayLogged) {
            currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();
        }
        FONT_MAIN = Typeface.createFromAsset(getAssets(), Fonts.RALEWAY_REGULAR);
        FONT_SECONDARY = Typeface.createFromAsset(getAssets(), Fonts.ACTION_MAN);
    }
}
