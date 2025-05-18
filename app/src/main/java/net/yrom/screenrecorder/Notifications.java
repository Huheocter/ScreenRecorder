package net.yrom.screenrecorder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Notifications {
    private final Context mContext;

    public Notifications(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Checks if the app has POST_NOTIFICATIONS permission (needed on Android 13+).
     */
    public boolean canPostNotifications(Context context) {
        if (Build.VERSION.SDK_INT < 33) {
            return true; // Permission not needed before Android 13
        }
        return ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS")
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request POST_NOTIFICATIONS permission (to be called from an Activity).
     */
    public static void requestNotificationPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{"android.permission.POST_NOTIFICATIONS"},
                    requestCode);
        }
    }

    /**
     * Show a notification ONLY if permission is granted.
     */
    public void notify(Context context, int id, Notification notification) {
        if (canPostNotifications(context)) {
            getNotificationManager().notify(id, notification);
        }
        // Optionally: else inform user or request permission via UI
    }
}
