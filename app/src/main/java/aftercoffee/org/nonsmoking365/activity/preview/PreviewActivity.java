package aftercoffee.org.nonsmoking365.activity.preview;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import aftercoffee.org.nonsmoking365.activity.BasisInfoActivity;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.R;

public class PreviewActivity extends AppCompatActivity {

    ViewPager pager;
    PreviewPagerAdapter mAdapter;

    public static final String START_MODE = "start_mode";
    public static final String MODE_FIRST = "mode_first";
    public static final String MODE_NOT_FIRST = "mode_not_first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new PreviewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

    }
}
