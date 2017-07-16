package lab.abhishek.apiaiimplementation;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import lab.abhishek.apiaiimplementation.accessiblity.OverlayActivity;

import static lab.abhishek.apiaiimplementation.MainActivity.NOTIFICATION_RECEIVER;
import static lab.abhishek.apiaiimplementation.MainActivity.NOTIFICATION_TEXT;

/**
 * Created by Abhishek on 15-Jul-17.
 */

public class MyNotificationService extends NotificationListenerService implements View.OnClickListener{

    private static final String TRIGGER_TEXT = "iphone";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        String text = extras.getCharSequence("android.text").toString();
        Log.d("MyNotificationService",text);
        if (text.toLowerCase().contains(TRIGGER_TEXT)){
           // Intent intent = new Intent(NOTIFICATION_RECEIVER);
           // intent.putExtra(NOTIFICATION_TEXT, text);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    maybeShowFloatingButton();
                }
            });
            //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    private void maybeShowFloatingButton() {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Point szWindow = new Point();
            Display display = windowManager.getDefaultDisplay();
            display.getSize(szWindow);
            ImageView assistant = new ImageView(this);
            assistant.setImageResource(R.drawable.bh_ic_assistant);
            assistant.setOnClickListener(this);
            int imageSize = 150;
            WindowManager.LayoutParams assistantLayoutParams = new WindowManager.LayoutParams(imageSize, imageSize,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            assistantLayoutParams.gravity = Gravity.CENTER | Gravity.LEFT;
            windowManager.addView(assistant, assistantLayoutParams);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onClick(View v) {
        enableShoppingAssistant();
    }

    private void enableShoppingAssistant() {
        Intent intent = new Intent(this, OverlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra("adapter_flag", "PRODUCT");
        intent.putExtra("SearchQuery","iphone");
        startActivity(intent);
    }

}
