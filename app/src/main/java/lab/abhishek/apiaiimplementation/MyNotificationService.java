package lab.abhishek.apiaiimplementation;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static lab.abhishek.apiaiimplementation.MainActivity.NOTIFICATION_RECEIVER;
import static lab.abhishek.apiaiimplementation.MainActivity.NOTIFICATION_TEXT;

/**
 * Created by Abhishek on 15-Jul-17.
 */

public class MyNotificationService extends NotificationListenerService {

    private static final String TRIGGER_TEXT = "abara-ka-dabara";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        String text = extras.getCharSequence("android.text").toString();
        Log.d("MyNotificationService",text);
        if (text.toLowerCase().contains(TRIGGER_TEXT)){
            Intent intent = new Intent(NOTIFICATION_RECEIVER);
            intent.putExtra(NOTIFICATION_TEXT, text);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
