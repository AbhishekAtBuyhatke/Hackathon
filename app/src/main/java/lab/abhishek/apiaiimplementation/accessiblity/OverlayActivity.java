package lab.abhishek.apiaiimplementation.accessiblity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import lab.abhishek.apiaiimplementation.CouponAdapter;
import lab.abhishek.apiaiimplementation.FlightResultAdapter;
import lab.abhishek.apiaiimplementation.Models.Coupons;
import lab.abhishek.apiaiimplementation.Models.Flight.*;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightData;
import lab.abhishek.apiaiimplementation.Models.SearchResult;
import lab.abhishek.apiaiimplementation.R;
import lab.abhishek.apiaiimplementation.SearchAdapter;
import lab.abhishek.apiaiimplementation.SearchItemActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static lab.abhishek.apiaiimplementation.FlightActivity.AIRPORT_TABLE;
import static lab.abhishek.apiaiimplementation.FlightActivity.COLUMN_NAME_AIRPORT_ALIAS;
import static lab.abhishek.apiaiimplementation.FlightActivity.COLUMN_NAME_AIRPORT_CODE;
import static lab.abhishek.apiaiimplementation.FlightActivity.COLUMN_NAME_AIRPORT_NAME;
import static lab.abhishek.apiaiimplementation.FlightActivity.COLUMN_NAME_CITY_NAME;
import static lab.abhishek.apiaiimplementation.FlightActivity.COLUMN_NAME_COUNTRY_NAME;
import static lab.abhishek.apiaiimplementation.FlightActivity.COMMA_SEP;
import static lab.abhishek.apiaiimplementation.FlightActivity.FLIGHT_UPDATE_COMPLETE_STATUS;
import static lab.abhishek.apiaiimplementation.FlightActivity.TEXT_TYPE;
import static lab.abhishek.apiaiimplementation.FlightActivity._ID;

/**
 * Created by Bhargav on 16/07/17.
 */

public class OverlayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String token;
    String tag = "adapter_flag";
    private TextView dateNClass, tvSource, tvDest, travellersDetail;
    private SharedPreferences sharedPref;
    private FlightJourney allFlightData;
    private FlightResultAdapter adapter;
    private Retrofit retrofit;
    private FlightData flightData;
    private static Call<FlightJourney> flightCall;
    private static Call<String> flightSessionAPiCall;
    private static Call<List<Airport>> airportCall;
    private SQLiteDatabase database;
    private Coupons[] couponList;
    private SearchResult[] searchResults;
    private ProgressDialog pd;
    private RequestQueue queue;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlay_acitivity);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        pd = new ProgressDialog(this);
        queue = Volley.newRequestQueue(this);
        gson = new Gson();

        token = getIntent().getStringExtra(tag);

        switch (token){
            case "COUPONS" :
                pd.setMessage("Loading...");
                pd.show();
                StringRequest req = new StringRequest(Request.Method.GET, getCouponsApi(), new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        couponList = gson.fromJson(String.valueOf(response),Coupons[].class);
                        CouponAdapter adapter = new CouponAdapter(couponList);
                        recyclerView.setAdapter(adapter);
                        pd.dismiss();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                    }
                });
                queue.add(req);
                break;

            case "PRODUCT" :
                pd.setMessage("Loading...");
                pd.show();
                String searchQuery = getIntent().getStringExtra("SearchQuery");
                StringRequest request = new StringRequest(Request.Method.GET, getSearchApi(searchQuery), new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        searchResults = gson.fromJson(String.valueOf(response), SearchResult[].class);
                        SearchAdapter adapter1 = new SearchAdapter(searchResults,OverlayActivity.this);
                        recyclerView.setAdapter(adapter1);
                        pd.dismiss();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OverlayActivity.this, "Error Searching...", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

                queue.add(request);
                break;

            case "FLIGHT" :
                pd.setMessage("Loading...");
                pd.show();
                setUpRecyclerView();
                flightData = getIntent().getParcelableExtra("FLIGHT_DATA");
                setupToolbar(flightData);
                getFlightSession(flightData);
        }

    }

    String getSearchApi(String searchQuery){
        return "https://compare.buyhatke.com/searchEngine2.php?app_id=836312&app_auth=906149708&searchQuery="+searchQuery+"&platform=android&clientId=a08419f51587553b";
    }

    public void getFlightSession(lab.abhishek.apiaiimplementation.Models.Flight.FlightData flightData){
        String postParams  = getPostParams(flightData.getOriginCode(),flightData.getDestinationCode(),flightData.getDepartureDate(),
                flightData.getReturnDate(),flightData.getAdults(),flightData.getChildren(),flightData.getInfants(),flightData.getCabinClass());
        if(postParams.contains("null"))
            postParams = postParams.replaceAll("null","");
        FlightAPI flightAPI = retrofit.create(FlightAPI.class);
        flightSessionAPiCall = flightAPI.getFlightSession(postParams);
        flightSessionAPiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                sharedPref.edit().putString("flightSession",response.body()).apply();
                callFlightResultAPIwithDelay();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void callFlightResultAPIwithDelay() {
        Runnable runnable = new Runnable() {
            public void run() {
                String flightSessionId = sharedPref.getString("flightSession","");
                getFlightDetails(flightSessionId);
            }
        };
        new Handler().postDelayed(runnable, 3 * 1000);
    };

    private void getFlightDetails(String session){
        final FlightAPI flightAPI = retrofit.create(FlightAPI.class);
        flightCall = flightAPI.getFlights(session);
        flightCall.enqueue(new Callback<FlightJourney>() {
            @Override
            public void onResponse(Call<FlightJourney> call, Response<FlightJourney> response) {
                if(response != null && response.body().getOriginName() != null){
                    FlightJourney journeyDetails = response.body();
                    if(journeyDetails == null){
                        callFlightResultAPIwithDelay();
                    } else if(journeyDetails.getStatus().equals(FLIGHT_UPDATE_COMPLETE_STATUS)) {
                        pd.dismiss();
                        allFlightData = journeyDetails;
                        adapter.appendFlightData(allFlightData);
                        findViewById(R.id.flight_progress_bar).setVisibility(View.GONE);
                    } else {
                        allFlightData = journeyDetails;
                        callFlightResultAPIwithDelay();
                        adapter.appendFlightData(allFlightData);
                        //findViewById(R.id.flight_progress_bar).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightJourney> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void setUpRecyclerView() {
        adapter = new FlightResultAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private String getPostParams(String originCode, String destinationCode,
                                 String departureDate, String arrivalDate,String adults,
                                 String children, String infants, String cabinclass) {
        String postParams = "&originplace=" + originCode + "&destinationplace=" + destinationCode +
                "&outbounddate=" + departureDate + "&inbounddate=" + arrivalDate + "&adults=" + adults
                + "&children=" + children + "&infants=" + infants + "&cabinclass=" + cabinclass;
        return postParams;
    }

    public void setupToolbar(lab.abhishek.apiaiimplementation.Models.Flight.FlightData flightData) {
        tvSource.setText(flightData.getOriginPlace());
        tvDest.setText(flightData.getDestinationPlace());
        dateNClass.setText(flightData.getDepartureDate() +" | " + flightData.getCabinClass() );
        travellersDetail.setText(flightData.getAdults() +" Adults," + " 0 Child," + " 0 Infant");
    }

    public static void getAirportCityList(final Context context){
        Retrofit retrofit = getRetrofitClient();
        FlightAPI flightAPI = retrofit.create(FlightAPI.class);
        airportCall = flightAPI.getAllAirportList();
        airportCall.enqueue(new Callback<List<Airport>>() {
            @Override
            public void onResponse(Call<List<Airport>> call, Response<List<Airport>> response) {
                addAirportToSQL(context, response.body());
            }

            @Override
            public void onFailure(Call<List<Airport>> call, Throwable t) {

            }
        });
    }

    public String getAirportCode(String cityName){
        String query = "select " + COLUMN_NAME_AIRPORT_CODE + " from " + AIRPORT_TABLE + " where " +
                COLUMN_NAME_CITY_NAME + " = '"+cityName+"'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 1){
            cursor.moveToNext();
            String cityCode = cursor.getString(0);
            cursor.close();
            return cityCode;
        }
        cursor.close();
        return "";
    }

    private String getCouponsApi(){
        return "http://coupons.buyhatke.com/PickCoupon/FreshCoupon/siteCpnAPI.php?position="+129+"&app_id=1&platform=android";
    }

    public static Retrofit getRetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://compare.buyhatke.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    private static void addAirportToSQL(Context context, List<Airport> airports){
        SQLiteDatabase database = context.openOrCreateDatabase("Database", MODE_PRIVATE, null);
        String SQL_CREATE_AIRPORT_TABLE =
                "CREATE TABLE " + AIRPORT_TABLE + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_AIRPORT_CODE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AIRPORT_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CITY_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COUNTRY_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AIRPORT_ALIAS + TEXT_TYPE + " )";
        database.execSQL(SQL_CREATE_AIRPORT_TABLE);
        database.beginTransaction();
        for (int i =0 ; i < airports.size(); i++){
            Airport airport = airports.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_AIRPORT_CODE, airport.getAirportCode());
            values.put(COLUMN_NAME_AIRPORT_NAME, airport.getAirportName());
            values.put(COLUMN_NAME_CITY_NAME, airport.getAirportCity());
            values.put(COLUMN_NAME_COUNTRY_NAME, airport.getAirportCountry());
            values.put(COLUMN_NAME_AIRPORT_ALIAS, airport.getAirportcityAlias());
            database.insert(AIRPORT_TABLE, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

}
