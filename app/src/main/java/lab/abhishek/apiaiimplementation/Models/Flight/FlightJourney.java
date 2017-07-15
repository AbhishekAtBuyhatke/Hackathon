package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightJourney implements Parcelable{

    @SerializedName("origin_name")
    private String originName;

    @SerializedName("origin_code")
    private String originCode;

    @SerializedName("destination_name")
    private String destinationName;

    @SerializedName("destination_code")
    private String destinationCode;

    @SerializedName("depart_date")
    private String departDate;

    @SerializedName("arrival_date")
    private String arrivalDate;

    @SerializedName("adults")
    private String adults;

    @SerializedName("children")
    private String children;

    @SerializedName("infants")
    private String infants;

    @SerializedName("flight_options")
    private List<FlightOption> flightOptions ;

/*    @SerializedName("carrier")
    private Carrier carrier; */

    @SerializedName("status")
    private String status;

    public FlightJourney() {

    }
    protected FlightJourney(Parcel in) {
        originName = in.readString();
        originCode = in.readString();
        destinationName = in.readString();
        destinationCode = in.readString();
        departDate = in.readString();
        arrivalDate = in.readString();
        adults = in.readString();
        children = in.readString();
        infants = in.readString();
        flightOptions = in.createTypedArrayList(FlightOption.CREATOR);
        status = in.readString();
    }

    public static final Creator<FlightJourney> CREATOR = new Creator<FlightJourney>() {
        @Override
        public FlightJourney createFromParcel(Parcel in) {
            return new FlightJourney(in);
        }

        @Override
        public FlightJourney[] newArray(int size) {
            return new FlightJourney[size];
        }
    };

    public String getOriginName() {
        return originName;
    }

    public String getOriginCode() {
        return originCode;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public String getDepartDate() {
        return departDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getAdults() {
        return adults;
    }

    public String getChildren() {
        return children;
    }

    public String getInfants() {
        return infants;
    }

    public List<FlightOption> getFlightOptions() {
        return flightOptions;
    }

    public String getStatus() {
        return status;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public void setInfants(String infants) {
        this.infants = infants;
    }

    public void setFlightOptionsData(List<FlightOption> flightOption) {
        flightOptions = flightOption;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originName);
        dest.writeString(originCode);
        dest.writeString(destinationName);
        dest.writeString(destinationCode);
        dest.writeString(departDate);
        dest.writeString(arrivalDate);
        dest.writeString(adults);
        dest.writeString(children);
        dest.writeString(infants);
        dest.writeTypedList(flightOptions);
        dest.writeString(status);
    }

    @Override
    public String toString() {
        return "FlightJourney{" +
                "originName='" + originName + '\'' +
                ", originCode='" + originCode + '\'' +
                ", destinationName='" + destinationName + '\'' +
                ", destinationCode='" + destinationCode + '\'' +
                ", departDate='" + departDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", adults='" + adults + '\'' +
                ", children='" + children + '\'' +
                ", infants='" + infants + '\'' +
                ", flightOptions=" + flightOptions +
                ", status='" + status + '\'' +
                '}';
    }
}
