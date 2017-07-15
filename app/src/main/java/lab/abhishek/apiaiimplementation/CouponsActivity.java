package lab.abhishek.apiaiimplementation;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import lab.abhishek.apiaiimplementation.Models.Coupons;
import lab.abhishek.apiaiimplementation.Models.CouponsStore;

import static lab.abhishek.apiaiimplementation.MainActivity.COUPON_SITE;

public class CouponsActivity extends AppCompatActivity {

    private static final String COUPON_URL = "https://compare.buyhatke.com/application/couponHome.php?platform=win&app_id=1&app_auth=431394108";
    private CouponsStore[] couponsStores;
    private RequestQueue queue;
    private Coupons[] couponsList;
    private Map<Integer, String> storeHash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        final String site = getIntent().getStringExtra(COUPON_SITE);
        setTitle("Coupons for " + site);
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, COUPON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Gson gson = new Gson();
                couponsStores = gson.fromJson(String.valueOf(response),CouponsStore[].class);
                int pos = getPos(couponsStores, site);
                StringRequest req = new StringRequest(Request.Method.GET, getCouponsApi(pos), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        couponsList = gson.fromJson(String.valueOf(response),Coupons[].class);
                        setupRecyclerView(couponsList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(req);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }


    private void setupRecyclerView(Coupons[] couponsList) {
        RecyclerView rv_coupons = (RecyclerView) findViewById(R.id.rv_coupons);
        rv_coupons.setLayoutManager(new LinearLayoutManager(this));
        rv_coupons.setAdapter(new CouponAdapter(couponsList));
    }

    private int getPos(CouponsStore[] couponsStores, String site) {
        for (int i = 0 ; i < couponsStores.length; i++)
            if (couponsStores[i].getName().toLowerCase().equals(site.toLowerCase()))
                return Integer.parseInt(couponsStores[i].getPos());
        return 0;
    }

    private String getCouponsApi(int pos){
        return "http://coupons.buyhatke.com/PickCoupon/FreshCoupon/siteCpnAPI.php?position="+pos+"&app_id=1&platform=android";
    }
}


