package lab.abhishek.apiaiimplementation.accessiblity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import lab.abhishek.apiaiimplementation.R;

/**
 * Created by Bhargav on 16/07/17.
 */

public class OverlayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
