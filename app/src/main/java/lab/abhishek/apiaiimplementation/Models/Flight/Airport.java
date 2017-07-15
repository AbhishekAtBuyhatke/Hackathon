package lab.abhishek.apiaiimplementation.Models.Flight;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhargav on 05/07/17.
 */

public class Airport implements Parcelable {

    @SerializedName("code")
    @Expose
    public String airportCode;

    @SerializedName("country")
    @Expose
    public String airportCountry;

    @SerializedName("city")
    @Expose
    public String airportCity;

    @SerializedName("city_alias")
    @Expose
    public String airportcityAlias;

    @SerializedName("name")
    @Expose
    public String airportName;

    public Airport() {
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public String getAirportcityAlias() {
        return airportcityAlias;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public void setAirportCountry(String airportCountry) {
        this.airportCountry = airportCountry;
    }

    public void setAirportCity(String airportCity) {
        this.airportCity = airportCity;
    }

    public void setAirportcityAlias(String airportcityAlias) {
        this.airportcityAlias = airportcityAlias;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    protected Airport(Parcel in) {
        airportCode = in.readString();
        airportCountry = in.readString();
        airportCity = in.readString();
        airportcityAlias = in.readString();
        airportName = in.readString();
    }

    public static final Creator<Airport> CREATOR = new Creator<Airport>() {
        @Override
        public Airport createFromParcel(Parcel in) {
            return new Airport(in);
        }

        @Override
        public Airport[] newArray(int size) {
            return new Airport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airportCode);
        dest.writeString(airportCountry);
        dest.writeString(airportCity);
        dest.writeString(airportcityAlias);
        dest.writeString(airportName);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportCode='" + airportCode + '\'' +
                ", airportCountry='" + airportCountry + '\'' +
                ", airportCity='" + airportCity + '\'' +
                ", airportcityAlias='" + airportcityAlias + '\'' +
                ", airportName='" + airportName + '\'' +
                '}';
    }
}
