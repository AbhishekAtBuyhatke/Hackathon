package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightOption implements Parcelable{

    @SerializedName("price")
    private String price;

    @SerializedName("inbound")
    private ArrivalFlightInfo arrivalFlightInfo;

    @SerializedName("outbound")
    private ArrivalFlightInfo departureFlightInfo;

    @SerializedName("pricing")
    private List<FlightPricing> flightPricing;

    protected FlightOption(Parcel in) {
        price = in.readString();
        arrivalFlightInfo = in.readParcelable(ArrivalFlightInfo.class.getClassLoader());
        departureFlightInfo = in.readParcelable(ArrivalFlightInfo.class.getClassLoader());
        flightPricing = in.createTypedArrayList(FlightPricing.CREATOR);
    }

    public static final Creator<FlightOption> CREATOR = new Creator<FlightOption>() {
        @Override
        public FlightOption createFromParcel(Parcel in) {
            return new FlightOption(in);
        }

        @Override
        public FlightOption[] newArray(int size) {
            return new FlightOption[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public ArrivalFlightInfo getArrivalFlightInfo() {
        return arrivalFlightInfo;
    }

    public ArrivalFlightInfo getDepartureFlightInfo() {
        return departureFlightInfo;
    }

    public List<FlightPricing> getFlightPricing() {

        return flightPricing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeParcelable(arrivalFlightInfo, flags);
        dest.writeParcelable(departureFlightInfo, flags);
        dest.writeTypedList(flightPricing);
    }
}
