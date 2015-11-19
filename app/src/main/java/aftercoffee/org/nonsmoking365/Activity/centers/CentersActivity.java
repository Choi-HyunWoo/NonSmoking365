package aftercoffee.org.nonsmoking365.Activity.centers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aftercoffee.org.nonsmoking365.R;

public class CentersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("주변 보건소 및 금연 상담센터");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new CenterListFragment()).commit();
        }
    }

    public void pushMapFragment(double latitude, double longtitude) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, MapFragment.newInstance(latitude,longtitude)).addToBackStack(null).commit();
    }

    public void popMapFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
