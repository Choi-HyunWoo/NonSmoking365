package aftercoffee.org.nonsmoking365.activity.main;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import aftercoffee.org.nonsmoking365.R;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;
    ViewPager pager;
    TabsAdapter mAdapter;

    private static final String TAB_TAG = "currentTab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        pager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new TabsAdapter(this, getSupportFragmentManager(), tabHost, pager);

        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator("",getResources().getDrawable(R.drawable.selector_tab_progress)), ProgressFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator("",getResources().getDrawable(R.drawable.selector_tab_count)), CountFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator("",getResources().getDrawable(R.drawable.selector_tab_setting)), OptionsFragment.class, null);
        setTabColor(tabHost);

        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setTabColor(tabHost);
            }
        });

        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
            tabHost.setCurrentTabByTag(savedInstanceState.getString(TAB_TAG));
        }
    }
    public void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorDark)); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorAccent)); // selected
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
        outState.putString(TAB_TAG, tabHost.getCurrentTabTag());
    }

}
