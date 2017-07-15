package lab.abhishek.apiaiimplementation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lab.abhishek.apiaiimplementation.Models.Flight.Airport;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightAPI;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightData;
import lab.abhishek.apiaiimplementation.Models.Flight.FlightJourney;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;

import static lab.abhishek.apiaiimplementation.MainActivity.FLIGHT_COUNT;
import static lab.abhishek.apiaiimplementation.MainActivity.FLIGHT_DATE;
import static lab.abhishek.apiaiimplementation.MainActivity.FLIGHT_DEST;
import static lab.abhishek.apiaiimplementation.MainActivity.FLIGHT_SRC;

public class FlightActivity extends AppCompatActivity {

    private FlightData flightData;
    private static Call<FlightJourney> flightCall;
    private static Call<String> flightSessionAPiCall;
    private static Call<List<Airport>> airportCall;
    private SQLiteDatabase database;
    public static final String AIRPORT_TABLE = "airportdata";
    public static final String COLUMN_NAME_CITY_NAME = "city_name";
    public static final String COLUMN_NAME_AIRPORT_NAME = "airport_name";
    public static final String COLUMN_NAME_AIRPORT_CODE = "airport_code";
    public static final String COLUMN_NAME_COUNTRY_NAME = "country";
    public static final String COLUMN_NAME_AIRPORT_ALIAS = "airport_alias";
    private static final String FLIGHT_UPDATE_COMPLETE_STATUS = "UpdatesComplete";
    public static final String _ID = "_id";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private TextView dateNClass, tvSource, tvDest, travellersDetail;
    private SharedPreferences sharedPref;
    private FlightJourney allFlightData;
    private FlightResultAdapter adapter;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        database = openOrCreateDatabase("Database",MODE_PRIVATE,null);
        Intent intent = getIntent();
        sharedPref = getSharedPreferences("sharedPref",MODE_PRIVATE);

        dateNClass = (TextView) findViewById(R.id.tv_flight_date_class);
        tvSource = (TextView) findViewById(R.id.tv_flight_origin);
        tvDest = (TextView) findViewById(R.id.tv_flight_destination);
        travellersDetail = (TextView) findViewById(R.id.tv_travellers_detail);

        flightData = new FlightData();
        retrofit = getRetrofitClient();
        String src = intent.getStringExtra(FLIGHT_SRC);
        String dest = intent.getStringExtra(FLIGHT_DEST);
        flightData.setOriginPlace(src);
        flightData.setOriginCode(getAirportCode(src));
        flightData.setDestinationPlace(dest);
        flightData.setDestinationCode(getAirportCode(dest));
        flightData.setDepartureDate(intent.getStringExtra(FLIGHT_DATE));
        flightData.setReturnDate("");
        flightData.setAdults(""+intent.getIntExtra(FLIGHT_COUNT,1));
        flightData.setChildren("0");
        flightData.setInfants("0");
        flightData.setCabinClass("Economy");
        flightData.setOneWayJourney(true);
        Log.d("Hohoho",flightData.toString());

        setUpRecyclerView();
        setupToolbar(flightData);
        getFlightSession(flightData);
    }

    public void getFlightSession(FlightData flightData){
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
                        allFlightData = journeyDetails;
                        adapter.appendFlightData(allFlightData);
                        findViewById(R.id.flight_progress_bar).setVisibility(View.GONE);
                    } else {
                        allFlightData = journeyDetails;
                        callFlightResultAPIwithDelay();
                        adapter.appendFlightData(allFlightData);
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightJourney> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_flight);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        adapter = new FlightResultAdapter(this);
        rv.setAdapter(adapter);
    }

    private String getPostParams(String originCode, String destinationCode,
                                        String departureDate, String arrivalDate,String adults,
                                        String children, String infants, String cabinclass) {
        String postParams = "&originplace=" + originCode + "&destinationplace=" + destinationCode +
                "&outbounddate=" + departureDate + "&inbounddate=" + arrivalDate + "&adults=" + adults
                + "&children=" + children + "&infants=" + infants + "&cabinclass=" + cabinclass;
        return postParams;
    }

    private void setupToolbar(FlightData flightData) {
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
