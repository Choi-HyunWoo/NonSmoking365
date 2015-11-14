package aftercoffee.org.nonsmoking365.Centers;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

public class CentersActivity extends AppCompatActivity {

    ListView centerListView;
    ArrayAdapter<POI> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("주변 보건소 및 금연 상담센터");

        centerListView = (ListView)findViewById(R.id.list_centers);
        mAdapter = new ArrayAdapter<POI>(this, android.R.layout.simple_list_item_1);
        centerListView.setAdapter(mAdapter);

        /*

        TMapData data = new TMapData();
        data.findAllPOI("보건소", new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (TMapPOIItem item : arrayList) {
                            POIItem poi = new POIItem();
                            poi.poi = item;
                            mAdapter.add(poi);
                        }
                    }
                });
            }
        });

*/

        String keyword = "보건소";
        NetworkManager.getInstance().findPOI(CentersActivity.this, keyword, new NetworkManager.OnResultListener<SearchPOIInfo>() {
            @Override
            public void onSuccess(SearchPOIInfo result) {
                for (POI poi : result.pois.poilist) {
                    mAdapter.add(poi);
                }
                if (result.pois.poilist.size() > 0) {
                    //moveMap(result.pois.poilist.get(0).getLatitude(), result.pois.poilist.get(0).getLongitude());
                }
            }

            @Override
            public void onFail(int code) {

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
