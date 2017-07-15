package lab.abhishek.apiaiimplementation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.ui.AIDialog;

public class MainActivity extends AppCompatActivity implements AIDialog.AIDialogListener{

    private static final String CLIENT_ACCESS_TOKEN = "c7c906141fde471d9276a82b8fda2c57";
    private static final String TAG = "SpeechResult";
    private TextView tv_query, tv_action, tv_parameter, tv_context, tv_response;
    private TextToSpeech tts;
    public static final String SEARCH_QUERY = "search_query";
    private AIDataService aiDataService;
    private AIDialog aiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_query = (TextView) findViewById(R.id.tv_resolved_query);
        tv_action = (TextView) findViewById(R.id.tv_action);
        tv_parameter = (TextView) findViewById(R.id.tv_parameters);
        tv_context = (TextView) findViewById(R.id.tv_context);
        tv_response = (TextView) findViewById(R.id.tv_response);

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
            }
        });
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
    public void onResult(AIResponse result) {
        String response = result.getResult().getFulfillment().getSpeech();
        String resolvedQuery = result.getResult().getResolvedQuery();
        final String action = result.getResult().getAction();
        List<AIOutputContext> contexts = result.getResult().getContexts();

        Map<String, JsonElement> paramters = null;
        if (contexts.size() > 0){
            paramters = contexts.get(0).getParameters();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "message_id");
        tts.speak(response, TextToSpeech.QUEUE_FLUSH, map);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                if (!action.contains("_Done")){
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
        if(paramters != null) tv_parameter.setText(paramters.toString());
        tv_response.setText(response);



    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onCancelled() {

    }
}
