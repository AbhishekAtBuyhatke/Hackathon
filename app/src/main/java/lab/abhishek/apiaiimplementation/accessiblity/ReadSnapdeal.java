package lab.abhishek.apiaiimplementation.accessiblity;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class ReadSnapdeal {
    private static final String TAG = "ReadSnapdeal";

    public static ProductPageEvent read(AccessibilityNodeInfo node) {

        if (node == null)
            return new ProductPageEvent(false, false, null);

        //com.snapdeal.ui.material.activity.MaterialMainActivity

        //com.snapdeal.main:id/buyButton ---> buy      now button

        //com.snapdeal.main:id/txtNativeCartTitle cart tv name

        //com.snapdeal.main:id/btnPlaceOrder place order button id

        //android.webkit.WebView
        List<AccessibilityNodeInfo> buyNowBtn = node.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/ptitleView");
        traverseTheNode(node, new ArrayList<CharSequence>());
        if (node.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/ptitleView").size() > 0) {
            Log.d(TAG,"\n)))))))))))))))))))))))))))))))))))))))))))))");
            List<AccessibilityNodeInfo> products = node.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/ptitleView");
            String temp = null;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).isVisibleToUser()) {
                    temp = products.get(i).getText().toString();
                }
            }
            if (temp == null) {
                return new ProductPageEvent(true, false, null);
            } else {
                return new ProductPageEvent(true, true, temp);
            }
        }

        return new ProductPageEvent(false, false, null);
    }

    private static void traverseTheNode(AccessibilityNodeInfo node, ArrayList<CharSequence> content) {
        if (node == null)
            return;

        if (node.getChildCount() == 0) {
            CharSequence text = node.getText();
            if (text != null) {
                content.add(text);
                Log.d(TAG, text + "\t" + node.getViewIdResourceName());
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                traverseTheNode(node.getChild(i), content);
            }
        }

    }
}
