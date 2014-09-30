package org.kosmakoff.pocketreckoner.app;

import android.app.Application;
import android.content.Context;

public class PocketReckonerApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        PocketReckonerApplication.sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return PocketReckonerApplication.sContext;
    }
}
