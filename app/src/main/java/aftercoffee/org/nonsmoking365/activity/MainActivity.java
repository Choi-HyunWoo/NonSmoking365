package aftercoffee.org.nonsmoking365.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.adapter.TabsAdapter;
import aftercoffee.org.nonsmoking365.fragment.CountFragment;
import aftercoffee.org.nonsmoking365.fragment.OptionsFragment;
import aftercoffee.org.nonsmoking365.fragment.StateFragment;

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

        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator("진행 현황"), StateFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator("금연 카운트"), CountFragment.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator("설정"), OptionsFragment.class, null);
        /*, getResources().getDrawable(R.drawable.tab_chatting_selector) */

        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // ...

            }
        });

        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
            tabHost.setCurrentTabByTag(savedInstanceState.getString(TAB_TAG));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
        outState.putString(TAB_TAG, tabHost.getCurrentTabTag());
    }


}
