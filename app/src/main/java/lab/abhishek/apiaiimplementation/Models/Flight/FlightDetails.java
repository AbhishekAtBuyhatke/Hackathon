package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightDetails implements Parcelable{

    @SerializedName("flight_no")
    private String flightNo;

    @SerializedName("carrier_name")
    private String flightName;

    @SerializedName("carrier_code")
    private String flightCode;

    @SerializedName("carrier_image")
    private String flightImage;


    protected FlightDetails(Parcel in) {
        flightNo = in.readString();
        flightName = in.readString();
        flightCode = in.readString();
        flightImage = in.readString();
    }

    public static final Creator<FlightDetails> CREATOR = new Creator<FlightDetails>() {
        @Override
        public FlightDetails createFromParcel(Parcel in) {
            return new FlightDetails(in);
        }

        @Override
        public FlightDetails[] newArray(int size) {
            return new FlightDetails[size];
        }
    };

    public String getFlightNo() {
        return flightNo;
    }

    public String getFlightName() {
        return flightName;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public String getFlightImage() {
        return flightImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flightNo);
        dest.writeString(flightName);
        dest.writeString(flightCode);
        dest.writeString(flightImage);
    }
}
