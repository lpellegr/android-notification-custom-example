package link.pellegrino.customnotificationexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCKSCREEN_NOTIFICATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonCustomView = findViewById(R.id.buttom_custom_view);
        View buttonDefaultView = findViewById(R.id.buttom_default_view);

        buttonCustomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishNotificationWithCustomView();
            }
        });

        buttonDefaultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishNotificationWithDefaultView();
            }
        });
    }

    private void publishNotificationWithCustomView() {
        String title = "Notification Custom View";
        String content = "No ripple effect, no elevation, single tap trigger";
        Context context = getApplicationContext();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(false)
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOngoing(true)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        int notificationLayoutResId = R.layout.lock_screen_notification;

        // using folder layout-vX is having issue with LG devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationLayoutResId = R.layout.lock_screen_notification_android_n;
        }

        RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), notificationLayoutResId);
        remoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.text, content);
        remoteView.setOnClickPendingIntent(
                R.id.container,
                createLockscreenNotificationPendingIntent(context));

        builder.setCustomContentView(remoteView);

        Notification notification = builder.build();
        publishNotification(context, notification, 7);
    }

    private void publishNotificationWithDefaultView() {
        String title = "Notification Default View";
        String content = "Ripple effect, elevation, double tap trigger";
        Context context = getApplicationContext();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(false)
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOngoing(true)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        builder.setContentIntent(NotificationService.createDisplayProfilesPendingIntent(context));

        Notification notification = builder.build();
        publishNotification(context, notification, 8);
    }

    private void publishNotification(Context context, Notification notification, int notificationId) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId,
                notification);
    }

    public static PendingIntent createLockscreenNotificationPendingIntent(Context context) {
        return PendingIntent.getActivity(
                context, REQUEST_CODE_LOCKSCREEN_NOTIFICATION,
                createLockScreenIntent(context, LockScreenActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static Intent createLockScreenIntent(Context context, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
        );

        return intent;
    }

}
