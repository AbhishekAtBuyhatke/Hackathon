package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhargav on 15/06/17.
 */

public class ArrivalFlightInfo implements Parcelable{

    @SerializedName("duration")
    private String duration;

    @SerializedName("flight_details")
    private List<FlightDetails> fLightDetails;

    @SerializedName("arrival_date")
    private String arrivalDate;

    @SerializedName("arrival_time")
    private String arrivalTime;

    @SerializedName("depart_date")
    private String departDate;

    @SerializedName("depart_time")
    private String departTime;

    @SerializedName("stops")
    private List<FlightStops> flightStops;

    protected ArrivalFlightInfo(Parcel in) {
        duration = in.readString();
        fLightDetails = in.createTypedArrayList(FlightDetails.CREATOR);
        arrivalDate = in.readString();
        arrivalTime = in.readString();
        departDate = in.readString();
        departTime = in.readString();
        flightStops = in.createTypedArrayList(FlightStops.CREATOR);
    }

    public static final Creator<ArrivalFlightInfo> CREATOR = new Creator<ArrivalFlightInfo>() {
        @Override
        public ArrivalFlightInfo createFromParcel(Parcel in) {
            return new ArrivalFlightInfo(in);
        }

        @Override
        public ArrivalFlightInfo[] newArray(int size) {
            return new ArrivalFlightInfo[size];
        }
    };

    public String getDuration() {
        return duration;
    }

    public List<FlightDetails> getfLightDetails() {
        return fLightDetails;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartDate() {
        return departDate;
    }

    public String getDepartTime() {
        return departTime;
    }

    public List<FlightStops> getFlightStops() {
        return flightStops;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeTypedList(fLightDetails);
        dest.writeString(arrivalDate);
        dest.writeString(arrivalTime);
        dest.writeString(departDate);
        dest.writeString(departTime);
        dest.writeTypedList(flightStops);
    }
}
