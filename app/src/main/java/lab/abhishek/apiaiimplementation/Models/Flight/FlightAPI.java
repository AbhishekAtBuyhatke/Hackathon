package lab.abhishek.apiaiimplementation.Models.Flight;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Bhargav on 14/06/17.
 */

public interface FlightAPI {

    @FormUrlEncoded
    @POST("/flights/getSession.php?apikey=bu862466354479549285577482220942")
    Call<String> getFlightSession(@Field("postFields") String postFields);

    @FormUrlEncoded
    @POST("/flights/getFlightDetailsApp.php")
    Call<FlightJourney> getFlights(@Field("code") String flightSessionId);

    @GET("/flights/flights.json")
    Call<List<Airport>> getAllAirportList();

}
