package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightPricing implements Parcelable{

    @SerializedName("price")
    private double flightPrice;

    @SerializedName("deeplink")
    private String flightDeepLink;

    @SerializedName("agent_name")
    private String agentName;

    @SerializedName("agent_image")
    private String agentImage;

    protected FlightPricing(Parcel in) {
        flightPrice = in.readDouble();
        flightDeepLink = in.readString();
        agentName = in.readString();
        agentImage = in.readString();
    }

    public static final Creator<FlightPricing> CREATOR = new Creator<FlightPricing>() {
        @Override
        public FlightPricing createFromParcel(Parcel in) {
            return new FlightPricing(in);
        }

        @Override
        public FlightPricing[] newArray(int size) {
            return new FlightPricing[size];
        }
    };

    public double getFlightPrice() {
        return flightPrice;
    }

    public String getFlightDeepLink() {
        return flightDeepLink;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentImage() {
        return agentImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(flightPrice);
        dest.writeString(flightDeepLink);
        dest.writeString(agentName);
        dest.writeString(agentImage);
    }
}
