package lab.abhishek.apiaiimplementation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.ui.AIDialog;

import static lab.abhishek.apiaiimplementation.FlightActivity.getAirportCityList;

public class MainActivity extends AppCompatActivity implements AIDialog.AIDialogListener{

    public static final String CLIENT_ACCESS_TOKEN = "c7c906141fde471d9276a82b8fda2c57";
    private static final String TAG = "SpeechResult";
    public static final String FLIGHT_SRC = "FlightSrc";
    public static final String FLIGHT_DEST = "FlightDest";
    public static final String FLIGHT_DATE = "FlightDate";
    public static final String FLIGHT_COUNT = "FlightCount";
    public static final String SEARCH_SITE = "search_site";
    private static final String E_COMMERCE = "E_Commerce_websites";
    public static final String COUPON_SITE = "coupons_site";
    public static final String NOTIFICATION_TEXT = "notification_text";
    public static final String NOTIFICATION_RECEIVER = "notification_receiver";
    private static final String[] welcome = new String[]{"Hey!","Welcome!","How Can I help you today?"};

    /*
    "url": "https://stream.watsonplatform.net/text-to-speech/api",
  "username": "0f95da2d-34c5-4787-b8ff-5b2ad23eaf64",
  "password": "EZd8mTesn6LM"
     */

    private TextView tv_query, tv_action, tv_parameter, tv_context, tv_response;
    //private TextToSpeech tts;
    public static final String SEARCH_QUERY = "search_query";
    private AIDataService aiDataService;
    private AIDialog aiDialog;
    private boolean continueListening;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(NOTIFICATION_TEXT);
            Toast.makeText(context, "Show dialog", Toast.LENGTH_SHORT).show();
        }
    };

    private SharedPreferences sharedPreferences;
    private android.speech.tts.TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        askPermission();

        sharedPreferences = getSharedPreferences("sharedPref",MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(NOTIFICATION_RECEIVER));
        tv_query = (TextView) findViewById(R.id.tv_resolved_query);
        tv_action = (TextView) findViewById(R.id.tv_action);
        tv_parameter = (TextView) findViewById(R.id.tv_parameters);
        tv_context = (TextView) findViewById(R.id.tv_context);
        tv_response = (TextView) findViewById(R.id.tv_response);

        if (sharedPreferences.getBoolean("firstTime",true)){
            getAirportCityList(this);
            sharedPreferences.edit().putBoolean("firstTime",false).apply();
        }

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDialog = new AIDialog(MainActivity.this, config);
        aiDataService = new AIDataService(this, config);
        aiDialog.setResultsListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiDialog.showAndListen();
                continueListening = true;
            }
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.RECORD_AUDIO},101);
        }
    }

    private void sendRequest() {

        //final String queryString = !eventSpinner.isEnabled() ? String.valueOf(queryEditText.getText()) : null;
        //final String eventString = eventSpinner.isEnabled() ? String.valueOf(String.valueOf(eventSpinner.getSelectedItem())) : null;
        //final String contextString = String.valueOf(contextEditText.getText());

        final String queryString = "Hi";
        final String eventString = "en";

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError("Error"));
            return;
        }

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];
                String event = params[1];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                if (!TextUtils.isEmpty(event))
                    request.setEvent(new AIEvent(event));
                final String contextString = params[2];
                RequestExtras requestExtras = null;
                if (!TextUtils.isEmpty(contextString)) {
                    final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
                    requestExtras = new RequestExtras(contexts, null);
                }

                try {
                    return aiDataService.request(request, requestExtras);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };

        task.execute(queryString, eventString, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(final AIResponse result) {
        final String response = result.getResult().getFulfillment().getSpeech();
        String resolvedQuery = result.getResult().getResolvedQuery();
        final String action = result.getResult().getAction();
        List<AIOutputContext> contexts = result.getResult().getContexts();
        Map<String, JsonElement> parameters = result.getResult().getParameters();
        Map<String, JsonElement> contextParamters = null;
        if (contexts.size() > 0){
            contextParamters = contexts.get(0).getParameters();
        }

        HashMap<String, String> map = new HashMap<>();
        //new WatsonTask().execute(response);
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "message_id");
        tts.speak(response, TextToSpeech.QUEUE_FLUSH, map);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if (continueListening){
                    aiDialog.showAndListen();
                }

            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        tv_query.setText(resolvedQuery);
        tv_action.setText(action);
        tv_context.setText(contexts.toString());
        if(contextParamters != null) tv_parameter.setText(contextParamters.toString());
        tv_response.setText(response);

        Intent intent;

        if (action.equals("FlightSearch.FlightSearch-custom.FlightSearch-SourceDestination-custom.FlightSearch-Date-custom_Done") &&
                contextParamters.get("geo-city") != null &&
                contextParamters.get("geo-city1") != null &&
                contextParamters.get("date") != null){
            continueListening = false;
            intent = new Intent(this,FlightActivity.class);
            intent.putExtra(FLIGHT_SRC, contextParamters.get("geo-city").getAsString());
            intent.putExtra(FLIGHT_DEST, contextParamters.get("geo-city1").getAsString());
            intent.putExtra(FLIGHT_DATE, contextParamters.get("date").getAsString());
            if (contextParamters.get("number-integer") != null)
                intent.putExtra(FLIGHT_COUNT, contextParamters.get("number-integer").getAsInt());
            else
                intent.putExtra(FLIGHT_COUNT, 1);
            startActivity(intent);
            result.cleanup();
        } else if (action.contains("searchitem_Done") && parameters.get("any") != null){
            continueListening = false;
            intent = new Intent(this, SearchItemActivity.class);
            intent.putExtra(SEARCH_QUERY, parameters.get("any").getAsString());
            if (parameters.get("E_Commerce_websites") != null){
                intent.putExtra(SEARCH_SITE, parameters.get("E_Commerce_websites").getAsString());
            }
            startActivity(intent);
            result.cleanup();
        } else if (action.equals("coupons_Done") && parameters.get(E_COMMERCE) != null){
            continueListening = false;
            intent = new Intent(this, CouponsActivity.class);
            intent.putExtra(COUPON_SITE,parameters.get(E_COMMERCE).getAsString());
            startActivity(intent);
            result.cleanup();
        } else if (action.toLowerCase().equals("flightsearchoneliner_Done") &&
                parameters.get("date") != null &&
                parameters.get("geo-city") != null &&
                parameters.get("geo-city1") != null &&
                parameters.get("number-integer") != null){

            continueListening = false;
            intent = new Intent(this,FlightActivity.class);
            intent.putExtra(FLIGHT_SRC, parameters.get("geo-city").getAsString());
            intent.putExtra(FLIGHT_DEST, parameters.get("geo-city").getAsString());
            intent.putExtra(FLIGHT_DATE, parameters.get("date").getAsString());
            intent.putExtra(FLIGHT_COUNT, parameters.get("number-integer").getAsInt());

            startActivity(intent);
            result.cleanup();

        }

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onCancelled() {

    }

    /*private class WatsonTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            TextToSpeech textToSpeech = initTextToSpeech();
            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(params[0],Voice.EN_LISA).execute());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (continueListening){
                aiDialog.showAndListen();
            }
        }
    } */
}
