package org.kosmakoff.pocketreckoner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Oleg on 05.01.14.
 */
public final class Utils {

    private Utils() {

    }

    public static boolean isCameraAvailable(Context context) {
        PackageManager pm = context.getPackageManager();

        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
