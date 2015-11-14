package aftercoffee.org.nonsmoking365.Centers;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;

import java.net.URI;
import java.util.ArrayList;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

public class CentersActivity extends AppCompatActivity {

    ListView centerListView;
    CentersItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("주변 보건소 및 금연 상담센터");

        centerListView = (ListView)findViewById(R.id.list_centers);

        // Headerview는 Listview에 adapter를 할당하기 전에 설정
        View headerView = getLayoutInflater().inflate(R.layout.view_centers_header, null);
        centerListView.addHeaderView(headerView, null, false);

        mAdapter = new CentersItemAdapter();
        centerListView.setAdapter(mAdapter);

        Button btn = (Button)findViewById(R.id.btn_map);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CentersActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        NetworkManager.getInstance().findPOI(CentersActivity.this, new NetworkManager.OnResultListener<SearchPOIInfo>() {
            @Override
            public void onSuccess(SearchPOIInfo result) {
                for (POI poi : result.pois.poilist) {
                    mAdapter.add(poi);
                }
            }
            @Override
            public void onFail(int code) {
                Toast.makeText(CentersActivity.this, "Network error " + code, Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
