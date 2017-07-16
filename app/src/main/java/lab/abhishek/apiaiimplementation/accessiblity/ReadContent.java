package lab.abhishek.apiaiimplementation.accessiblity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;


public class ReadContent {

    private static final String TAG = "ReadContent";
    private ReadInterface readInterface;
    private ReadContentTask runningTask;
    private boolean isRunning = false;
    private String appName;


    public ReadContent(ReadInterface readInterface) {
        this.readInterface = readInterface;
    }

    public void read(AccessibilityNodeInfo node, String appName) {
        Log.d(TAG,"read() of readContent");
        if (runningTask != null) {
            runningTask.cancel(true);
            runningTask = null;
        }
        this.appName = appName;
        runningTask = new ReadContentTask();
        runningTask.execute(node);
    }

    public interface ReadInterface {
        void onRead(ProductPageEvent event);
    }

    private class ReadContentTask extends AsyncTask<AccessibilityNodeInfo, Void, ProductPageEvent> {


        @Override
        protected ProductPageEvent doInBackground(AccessibilityNodeInfo... params) {
            try {

                Log.d(TAG,"ReadContentTask extends AsyncTask doInBackground()");
                AccessibilityNodeInfo node = params[0];
                isRunning = true;
                ProductPageEvent event = null;
                if(appName.equals("snapDeal"))
                     event = ReadSnapdeal.read(node);
                else if(appName.equals("MakeMyTrip"))
                    event = ReadMakeMyTrip.read(node);
                return event;
            } catch (Exception e) {
                return null;
            }

        }


        protected void onPostExecute(ProductPageEvent event) {
            Log.d(TAG,"ReadContentTask extends AsyncTask onPostExecute()");
            if (event != null && event.product != null && !event.product.equals("null")){
                Log.d(TAG,event.toString() +"\n if con");
                readInterface.onRead(event);
            } else if(event != null && event.flightData != null) {
                Log.d(TAG,event.toString() +"\n if con");
                readInterface.onRead(event);
            } else {
                Log.d(TAG,"Read content not able to find product \n else con") ;
                readInterface.onRead(null);
            }

            isRunning = false;
        }
    }
}
