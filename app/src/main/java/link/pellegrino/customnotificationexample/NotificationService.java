package link.pellegrino.customnotificationexample;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }


    public static PendingIntent createDisplayProfilesPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationService.class);

        return PendingIntent.getService(
                context, 9, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        startActivity(
                MainActivity.createLockScreenIntent(getApplicationContext(),
                        LockScreenActivity.class));
    }

}