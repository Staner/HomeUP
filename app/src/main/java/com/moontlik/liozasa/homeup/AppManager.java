package com.moontlik.liozasa.homeup;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by liozasa on 8/5/15.
 */
public class AppManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);
    }

    private void initParse() {
        // Enable Local DataBase in parse server .
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "CK9iiNqcgXGAyjOqPZTbXWR8MVeNpBQnrfpPHEIG", "Z4KAw1c8bYiMLAB7uQSk6be73M04qlSvZdx4v794");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
