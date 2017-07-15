package lab.abhishek.apiaiimplementation;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import lab.abhishek.apiaiimplementation.Models.SearchResult;

import static lab.abhishek.apiaiimplementation.MainActivity.SEARCH_QUERY;

public class ActivityRecyclerView extends AppCompatActivity {

    private RequestQueue queue;
    private SearchResult[] searchResults;
    private RecyclerView recyclerView;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setTitle("Loading");
        pd.show();

        String searchQuery = getIntent().getStringExtra(SEARCH_QUERY);
        setTitle(searchQuery);
        searchQuery = searchQuery.replace(" ","%20");
        StringRequest request = new StringRequest(Request.Method.GET, getSearchApi(searchQuery), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                searchResults = gson.fromJson(String.valueOf(response), SearchResult[].class);
                setupRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityRecyclerView.this, "Error Searching...", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchAdapter(searchResults, this));
        pd.dismiss();
    }

    String getSearchApi(String searchQuery){
        return "https://compare.buyhatke.com/searchEngine2.php?app_id=836312&app_auth=906149708&searchQuery="+searchQuery+"&platform=android&clientId=a08419f51587553b";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
