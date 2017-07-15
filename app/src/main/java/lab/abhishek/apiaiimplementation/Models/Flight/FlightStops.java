package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightStops implements Parcelable{

    @SerializedName("stop_count")
    private int stopCount;

    protected FlightStops(Parcel in) {
        stopCount = in.readInt();
    }

    public static final Creator<FlightStops> CREATOR = new Creator<FlightStops>() {
        @Override
        public FlightStops createFromParcel(Parcel in) {
            return new FlightStops(in);
        }

        @Override
        public FlightStops[] newArray(int size) {
            return new FlightStops[size];
        }
    };

    public int getStopCount() {
        return stopCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stopCount);
    }
}
