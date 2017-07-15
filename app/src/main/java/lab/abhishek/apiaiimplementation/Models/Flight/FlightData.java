package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bhargav on 15/06/17.
 */

public class FlightData implements Parcelable{

    private String originPlace;
    private String originCode;
    private String destinationPlace;
    private String destinationCode;
    private String departureDate;
    private String returnDate;
    private String adults;
    private String children;
    private String infants;
    private String cabinClass;
    private boolean isOneWayJourney;


    public FlightData() {

    }

    public FlightData(String originPlace, String orifinCode, String destinationPlace,
                      String destinationCode, String departureDate, String returnDate,
                      String adults, String children, String infants, String cabinClass) {
        this.originPlace = originPlace;
        this.originCode = orifinCode;
        this.destinationPlace = destinationPlace;
        this.destinationCode = destinationCode;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.cabinClass = cabinClass;
    }

    public FlightData(FlightData data){
        originCode = data.getOriginCode();
        originPlace = data.getOriginPlace();
        destinationCode = data.getDestinationCode();
        destinationPlace = data.getDestinationPlace();
        departureDate = data.getDepartureDate();
        returnDate = data.getReturnDate();
        adults= data.getAdults();
        children = data.getChildren();
        infants = data.getInfants();
        cabinClass = data.getCabinClass();
        isOneWayJourney = data.isOneWayJourney();
    }

    protected FlightData(Parcel in) {
        originPlace = in.readString();
        originCode = in.readString();
        destinationPlace = in.readString();
        destinationCode = in.readString();
        departureDate = in.readString();
        returnDate = in.readString();
        adults = in.readString();
        children = in.readString();
        infants = in.readString();
        cabinClass = in.readString();
        isOneWayJourney = in.readByte() != 0;
    }

    public static final Creator<FlightData> CREATOR = new Creator<FlightData>() {
        @Override
        public FlightData createFromParcel(Parcel in) {
            return new FlightData(in);
        }

        @Override
        public FlightData[] newArray(int size) {
            return new FlightData[size];
        }
    };

    public boolean isOneWayJourney() {
        return isOneWayJourney;
    }

    public void setOneWayJourney(boolean oneWayJourney) {
        isOneWayJourney = oneWayJourney;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getInfants() {
        return infants;
    }

    public void setInfants(String infants) {
        this.infants = infants;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originPlace);
        dest.writeString(originCode);
        dest.writeString(destinationPlace);
        dest.writeString(destinationCode);
        dest.writeString(departureDate);
        dest.writeString(returnDate);
        dest.writeString(adults);
        dest.writeString(children);
        dest.writeString(infants);
        dest.writeString(cabinClass);
        dest.writeByte((byte) (isOneWayJourney ? 1 : 0));
    }

    @Override
    public String toString() {
        return "FlightData{" +
                "originPlace='" + originPlace + '\'' +
                ", originCode='" + originCode + '\'' +
                ", destinationPlace='" + destinationPlace + '\'' +
                ", destinationCode='" + destinationCode + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", adults='" + adults + '\'' +
                ", children='" + children + '\'' +
                ", infants='" + infants + '\'' +
                ", cabinClass='" + cabinClass + '\'' +
                ", isOneWayJourney=" + isOneWayJourney +
                '}';
    }
}
