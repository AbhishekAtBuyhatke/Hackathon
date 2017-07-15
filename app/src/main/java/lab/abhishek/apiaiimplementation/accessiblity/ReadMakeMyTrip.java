package lab.abhishek.apiaiimplementation.accessiblity;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * Created by Bhargav on 07/06/17.
 */

public class ReadMakeMyTrip {

    private static final String TAG ="ReadMakeMyTrip";
    private static final String FROM_CITY_NAME = "com.makemytrip:id/flight_listing_from_city";
    private static final String TO_CITY_NAME = "com.makemytrip:id/flight_listing_to_city";
    private static final String TRAVELLING_DATE = "com.makemytrip:id/flight_listing_travel_date";
    private static final String NO_OF_TRAVELLERS = "com.makemytrip:id/flight_listing_travellers";

    public static ProductPageEvent read(AccessibilityNodeInfo node) {
        FlightData flightData = findProduct(node);
        if (flightData == null) {
            Log.d(TAG,"flightData NULLLLLL");
            return new ProductPageEvent(null, false);
        } else {
            Log.d(TAG,flightData.toString());
            return new ProductPageEvent(flightData, true);
        }
    }

    private static FlightData findProduct(AccessibilityNodeInfo node) {
        if (node != null) {
            FlightData flightData = new FlightData();
            List<AccessibilityNodeInfo> toCityName = node
                    .findAccessibilityNodeInfosByViewId(FROM_CITY_NAME);

            List<AccessibilityNodeInfo> fromCityName = node
                    .findAccessibilityNodeInfosByViewId(TO_CITY_NAME);


            List<AccessibilityNodeInfo> travellingDate = node
                    .findAccessibilityNodeInfosByViewId(TRAVELLING_DATE);


            List<AccessibilityNodeInfo> noOfTravellers = node
                    .findAccessibilityNodeInfosByViewId(NO_OF_TRAVELLERS);

            String travellingDates = travellingDate.get(0).getText().toString();

            boolean isOneWayJourney = isOneWayJourney(travellingDates);

            if(isOneWayJourney) {
                flightData.setOneWayJourney(true);
                String date = formatFlightDate(travellingDates);
                flightData.setDepartureDate(date);
                flightData.setReturnDate("");
            }
            else {
                flightData.setOneWayJourney(false);
                String[] dates = travellingDates.split("-");
                String departureDate = formatFlightDate(dates[0]);
                String returnDate = formatFlightDate(dates[1]);
                flightData.setDepartureDate(departureDate);
                flightData.setReturnDate(returnDate);
            }

            String[] travellersCount = processTravellers(noOfTravellers.get(0).getText().toString());
            flightData.setOriginPlace(toCityName.get(0).getText().toString());
            flightData.setDestinationPlace(fromCityName.get(0).getText().toString());
            flightData.setAdults(travellersCount[0]);
            flightData.setChildren(travellersCount[1]);
            flightData.setInfants(travellersCount[2]);
            flightData.setCabinClass("Economy");

            if(flightData.getDestinationPlace() != null)
                return flightData;
        }
        return null;
    }

    private static String[] processTravellers(String travellers) {
        String[] travellersCount = new String[3];
        String[] passengers = travellers.split(" ");
        travellersCount[0] = passengers[0];
        if(travellersCount[1] == null)
            travellersCount[1] = "0";
        if(travellersCount[2] == null)
            travellersCount[2] = "0";

        return travellersCount;
    }

    private static String formatFlightDate(String travellingDates) {
        SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
        travellingDates  = travellingDates +" "+ getCurrentYear();
        spf = new SimpleDateFormat("dd MMM yyyy");

        Date newDate = null;
        try {
            newDate = spf.parse(travellingDates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");
        String newDateString = spf.format(newDate);
        return checkYear(newDateString);
    }

    private static String checkYear(String date){
        String checkedDate = date;
        int currMon = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int rawMon = Integer.parseInt(date.substring(5,7));
        if (rawMon < currMon)
            checkedDate = (getCurrentYear()+1) + date.substring(4);
        return checkedDate;
    }


    private static int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }


    private static boolean isOneWayJourney(String travellingDates) {
        String[] dates = travellingDates.split("-");
        if(dates.length == 1)
            return true;
        return false;
    }


}
