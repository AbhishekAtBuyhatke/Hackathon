package lab.abhishek.apiaiimplementation.accessiblity;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.ui.AIDialog;
import lab.abhishek.apiaiimplementation.MainActivity;
import lab.abhishek.apiaiimplementation.R;

import static lab.abhishek.apiaiimplementation.MainActivity.CLIENT_ACCESS_TOKEN;

/**
 * Created by Bhargav on 15/07/17.
 */

public class CustomAccessibilityService extends AccessibilityService implements ReadContent.ReadInterface, View.OnTouchListener, AIDialog.AIDialogListener {

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
    private AIDialog aiDialog;
    private TextToSpeech t1;;

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
        t1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Log.d(TAG, "onAccessibilityEvent() -->" + event.getEventType());

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

        //Log.d(TAG, event.getContentDescription()+"-------------->");
        if (packageName.equals("com.snapdeal.main")) {
            currentAppName = "snapDeal";
            readContent.read(getRootInActiveWindow(),"snapDeal");
        } else if(packageName.equals("com.makemytrip")){
            //maybeShowFloatingButton();
            currentAppName = "MAkeMyTrip";
            readContent.read(getRootInActiveWindow(),"MakeMyTrip");
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
            } else {
                productPageEvent.appType = COUPONS;
            }
            maybeShowFloatingButton();
            Log.d(TAG, event.toString());
        } else if(event == null && currentAppName.equals("snapDeal")){
            productPageEvent = new ProductPageEvent();
            productPageEvent.appType = COUPONS;
            maybeShowFloatingButton();
        }
        else{
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
        if (productPageEvent != null && productPageEvent.flightData != null) {
            Intent intent = new Intent(this, OverlayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.putExtra(APP_NAME, currentAppName);
            intent.putExtra("FLIGHT_DATA", productPageEvent.flightData);
            intent.putExtra("adapter_flag","FLIGHT");
            startActivity(intent);
        } else {
            Log.d(TAG, "wont identify flight");
        }
    }


    private void maybeHideFloatingButton() {//TODO dont call this method for unsupportd app
        Log.d(TAG, "maybeHideFloatingButton()");
        if (assistantVisible) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.removeViewImmediate(assistant);
            assistantVisible = false;
            assistant = null;
        }
    }

    private void enableShoppingAssistant() {
        Log.d(TAG, "enableShoppingAssistant()");
        maybeHideFloatingButton();
        if (productPageEvent != null && productPageEvent.product != null) {
            Intent intent = new Intent(this, OverlayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            //intent.putExtra(APP_NAME, currentAppName);
            intent.putExtra("SearchQuery", productPageEvent.product);
            intent.putExtra("adapter_flag","PRODUCT");
            startActivity(intent);
        } else {
            Log.d(TAG, "wont identify Product");
        }
    }

    private void enableCouponAssistant() {
        maybeHideFloatingButton();
        Intent intent = new Intent(this, OverlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra("adapter_flag","COUPONS");
        intent.putExtra(APP_NAME, currentAppName);
        startActivity(intent);
    }


    private void maybeShowFloatingButton() {
            if (!assistantVisible) {
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                szWindow = new Point();
                display = windowManager.getDefaultDisplay();
                display.getSize(szWindow);
                assistant = new ImageView(CustomAccessibilityService.this);
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


                speakTts();


            }

    }

    private void speakTts() {
        String toSpeak = "Hi";
        switch (productPageEvent.appType) {
            case COUPONS:
                toSpeak = toSpeak + "We have SnapDeal coupons       click to see coupons";
                break;

            case SHOPPING:
                toSpeak = toSpeak + "you are browsing " + productPageEvent.product + "click to see chippest price";
                enableShoppingAssistant();
                break;

            case FLIGHT:
                toSpeak = toSpeak + "you are searching flight click to see chippesr price";
                enableFlightAssistant();
                break;
        }

        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
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

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onCancelled() {

    }
}
