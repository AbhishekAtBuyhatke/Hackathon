package lab.abhishek.apiaiimplementation.accessiblity;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import lab.abhishek.apiaiimplementation.MainActivity;
import lab.abhishek.apiaiimplementation.R;

/**
 * Created by Bhargav on 15/07/17.
 */

public class CustomAccessibilityService extends AccessibilityService implements ReadContent.ReadInterface, View.OnTouchListener {

    private static final String TAG = "CustomAccessibility";
    private static final String APP_NAME = "app_name";
    private static final String PRODUCT_NAME = "product_name";
    private WindowManager windowManager;
    private ReadContent readContent;
    private Point szWindow;
    private Display display;
    private ImageView assistant;
    private WindowManager.LayoutParams assistantLayoutParams;
    private boolean assistantVisible;
    private ProductPageEvent productPageEvent;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    private final int COUPONS = 21;
    private final int SHOPPING = 1;
    private final int FLIGHT = 31;
    private String currentAppName = "";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes =
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                        | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                        | AccessibilityEvent.TYPE_VIEW_CLICKED
                        | AccessibilityEvent.TYPE_VIEW_FOCUSED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        config.flags |= 1;
        config.flags |= 16;
        config.flags |= 2;
        //config.packageNames = new String[]{"com.snapdeal.main"};
        Log.d(TAG, "onServiceConnected()");
        readContent = new ReadContent(this);
        setServiceInfo(config);
        init();
    }

    private void init() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Log.d(TAG, "onAccessibilityEvent() -->" + event.getEventType());
       /* switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "BHARGAV\n" + "TYPE_VIEW_CLICKED || CONTENT_CHANGE_TYPE_SUBTREE");
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.d(TAG, "BHARGAV\n" +  "TYPE_NOTIFICATION_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                Log.d(TAG, "BHARGAV\n" +  "TYPE_VIEW_CONTEXT_CLICKED");
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                Log.d(TAG, "BHARGAV\n  " +  "TYPE_WINDOWS_CHANGED");
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT:
                Log.d(TAG, "BHARGAV\n" +  "CONTENT_CHANGE_TYPE_TEXT");
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION:
                Log.d(TAG, "BHARGAV\n" +  "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d(TAG, "BHARGAV\n" + "TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG, "BHARGAV\n" + "TYPE_WINDOW_STATE_CHANGED");
                break;
        }*/

        int type = -1;
        CharSequence className = "", source = "", packageName = "";
        type = event.getEventType();
        className = event.getClassName();
        source = event.getSource().getViewIdResourceName();
        packageName = event.getPackageName();

        Log.d(TAG, "className:--> " + className + "\n source--> " + source + "\npackageName--> " + packageName);
        if(packageName.equals("lab.abhishek.apiaiimplementation"))
            return;
        String eventText = null;
        productPageEvent = new ProductPageEvent();

        Log.d(TAG, event.getContentDescription()+"-------------->");
        if (packageName.equals("com.snapdeal.main")) {
            currentAppName = "snapDeal";
            readContent.read(getRootInActiveWindow());
        } else if(packageName.equals("com.makemytrip")){
            currentAppName = "MAkeMyTrip";
            readContent.read(getRootInActiveWindow());
        } else {
            maybeHideFloatingButton();
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onRead(ProductPageEvent event) {
        if (event != null) {
            if (event.isProductPage) {
                if (event.isProductVisible) {
                    productPageEvent.appType = SHOPPING;
                    productPageEvent.isProductVisible = true;
                    productPageEvent.product = event.product;
                }
            } else if(event.isFlightApp) {
                productPageEvent.appType = FLIGHT;
            }
            productPageEvent.appType = COUPONS;
            maybeShowFloatingButton();
            Log.d(TAG, event.toString());
        }else{
            maybeHideFloatingButton();
        }

    }

    private void onAssistantButtonClick() {
        switch (productPageEvent.appType) {
            case COUPONS:
                enableCouponAssistant();
                break;

            case SHOPPING:
                enableShoppingAssistant();
                break;

            case FLIGHT:
                enableFlightAssistant();
                break;
        }
    }

    private void enableFlightAssistant() {
        maybeHideFloatingButton();

    }


    private void maybeHideFloatingButton() {//TODO dont call this method for unsupportd app
        Log.d(TAG, "maybeHideFloatingButton()");
        if (assistantVisible) {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.removeViewImmediate(assistant);
            assistantVisible = false;
            assistant = null;
        }
    }

    private void enableShoppingAssistant() {
        Log.d(TAG, "enableShoppingAssistant()");
        maybeHideFloatingButton();
        if (productPageEvent != null && productPageEvent.product != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.putExtra(APP_NAME, currentAppName);
            intent.putExtra(PRODUCT_NAME, productPageEvent.product);
            startActivity(intent);
        } else {
            Log.d(TAG, "wont identify Product");
        }
    }

    private void enableCouponAssistant() {
        maybeHideFloatingButton();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(APP_NAME, currentAppName);
        startActivity(intent);
    }


    private void maybeShowFloatingButton() {
            if (!assistantVisible) {
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                szWindow = new Point();
                display = windowManager.getDefaultDisplay();
                display.getSize(szWindow);
                assistant = new ImageView(this);
                assistant.setImageResource(R.drawable.bh_ic_assistant);
                assistant.setOnTouchListener(this);
                int imageSize = 150;
                assistantLayoutParams = new WindowManager.LayoutParams(imageSize, imageSize,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                assistantLayoutParams.gravity = Gravity.CENTER | Gravity.LEFT;
                windowManager.addView(assistant, assistantLayoutParams);
                assistantVisible = true;
            }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                if (isClick()) {
                    onAssistantButtonClick();
                    return true;
                }
                return true;
        }
        return false;
    }

    private boolean isClick() {
        int moveThreshHold = Math.abs(assistantLayoutParams.x - initialX) + Math.abs(assistantLayoutParams.y - initialY);
        return moveThreshHold < 10;
    }

}
